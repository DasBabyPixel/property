package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

abstract class AbstractProperty<T> implements Property<T> {
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();
	protected final List<WeakReference<Property<?>>> dependants = new ArrayList<>();
	protected final WeakReference<Property<?>> reference = new WeakReference<>(this);
	protected final Storage<T> storage;
	protected Events<T> events = new Events<>(this);
	protected volatile T value;
	protected volatile boolean valid;
	protected AbstractProperty<T> boundTo;
	protected boolean invalidating = false;
	protected final InvalidationListener dependencyListener = property -> invalidate();
	protected int updateStatus = 0;

	public AbstractProperty(Storage<T> storage) {
		this.storage = storage;
	}

	protected boolean equals(Object o1, Object o2) {
		return Objects.equals(o1, o2);
	}

	@Api
	@Override
	public void addListener(ChangeListener<? super T> listener) {
		lock.writeLock().lock();
		events.changeListeners.add(listener);
		lock.writeLock().unlock();
	}

	@Api
	@Override
	public void removeListener(ChangeListener<? super T> listener) {
		lock.writeLock().lock();
		events.changeListeners.remove(listener);
		lock.writeLock().unlock();
	}

	@Override
	public void addListener(InvalidationListener listener) {
		lock.writeLock().lock();
		events.invalidationListeners.add(listener);
		lock.writeLock().unlock();
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		lock.writeLock().lock();
		events.invalidationListeners.remove(listener);
		lock.writeLock().unlock();
	}

	@Api
	@Override
	public AbstractProperty<T> addDependencies(Property<?>... dependencies) {
		for (Property<?> dependency : dependencies) {
			dependency.dependants(); // Try clean up unused dependants for more memory-friendly environment
			synchronized (((AbstractProperty<?>) dependency).dependants) {
				((AbstractProperty<?>) dependency).dependants.add(reference);
			}
		}
		return this;
	}

	@Api
	@Override
	public AbstractProperty<T> removeDependencies(Property<?>... dependencies) {
		for (Property<?> dependency : dependencies) {
			synchronized (((AbstractProperty<?>) dependency).dependants) {
				Iterator<WeakReference<Property<?>>> it = ((AbstractProperty<?>) dependency).dependants.iterator();
				while (it.hasNext()) {
					WeakReference<Property<?>> ref = it.next();
					Property<?> property = ref.get();
					if (property == null) {
						it.remove();
						continue;
					}
					if (property == this) {
						it.remove();
					}
				}
			}
		}
		return this;
	}

	@Api
	@Override
	public void atomicOperation(Function<T, T> operation) {
		try {
			lock.writeLock().lock();
			T val = value();
			T newVal = operation.apply(val);
			value(newVal);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public Collection<Property<?>> dependants() {
		synchronized (dependants) {
			Set<Property<?>> res = new HashSet<>();
			Iterator<WeakReference<Property<?>>> it = dependants.iterator();
			while (it.hasNext()) {
				WeakReference<Property<?>> ref = it.next();
				Property<?> p = ref.get();
				if (p == null) {
					it.remove();
					continue;
				}
				res.add(p);
			}
			return res;
		}
	}

	@Override
	public T value() {
		if (!valid) {
			try {
				lock.writeLock().lock();
				if (valid)
					return value;
				T oldValue = value;
				value = computeValue();
				if (!equals(oldValue, value)) {
					if (storage != null && storage.writable())
						storage.write(value);
					events.change(oldValue, value);
				}
				valid = true;
			} finally {
				lock.writeLock().unlock();
			}
		} else if (storage != null && storage.checkForChanges()) {
			try {
				lock.readLock().lock();
				T oldValue = value;
				T newValue = computeValue();
				if (equals(oldValue, newValue))
					return value;
			} finally {
				lock.readLock().unlock();
			}
			try {
				lock.writeLock().lock();
				T oldValue = value;
				T newValue = computeValue();
				if (equals(oldValue, newValue))
					return value;
				value = newValue;
				storage.write(value);
			} finally {
				lock.writeLock().unlock();
			}
		}
		return value;
	}

	@Override
	public void value(T value) {
		if (!storage.writable())
			throw new UnsupportedOperationException("Cannot write to this property because it's storage does not support writing! Typically means that this is a property that was created by the #map function");
		boolean statusChanged = false;
		try {
			lock.writeLock().lock();
			boolean bidirectional = boundTo != null && boundTo.boundTo == this;
			if (boundTo != null && !bidirectional)
				throw new IllegalStateException("This property is bound and a value can not be set");
			if (updateStatus == 1)
				return;
			else if (updateStatus != 0)
				throw new IllegalStateException();
			statusChanged = true;
			updateStatus = 1;
			T old = this.value;
			if (equals(old, value))
				return;
			this.value = value;
			storage.write(value);
			valid = true;
			if (bidirectional)
				boundTo.value(value);
			events.invalidate();
			events.change(old, this.value);
		} finally {
			if (statusChanged)
				updateStatus = 0;
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public boolean hasValue(T value) {
		return this.value == value;
	}

	@Api
	@Override
	public boolean valid() {
		return valid;
	}

	@Override
	public void invalidate() {
		try {
			lock.writeLock().lock();
			if (!invalidating) {
				invalidating = true;
				valid = false;
				events.invalidate();
				invalidating = false;
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void bind(Property<T> other) {
		try {
			lock.readLock().lock();
			if (boundTo != null)
				throw new IllegalStateException("Property already bound!");
		} finally {
			lock.readLock().unlock();
		}
		try {
			lock.writeLock().lock();
			if (boundTo != null)
				throw new IllegalStateException("Property already bound!");
			boundTo = (AbstractProperty<T>) other;
			boundTo.addListener(dependencyListener);
			valid = false;
			events.invalidate();
			value();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void unbind() {
		while (true) {
			try {
				lock.readLock().lock();
				if (boundTo == null)
					return;
			} finally {
				lock.readLock().unlock();
			}
			try {
				lock.writeLock().lock();
				if (boundTo == null)
					return;
				if (!boundTo.lock.writeLock().tryLock())
					continue;

				boolean bidirectional = boundTo.boundTo == this;

				if (bidirectional) {
					boundTo.boundTo = null;
					boundTo.lock.writeLock().unlock();
				} else {
					boundTo.removeListener(dependencyListener);
				}
				boundTo = null;
				return;
			} finally {
				lock.writeLock().unlock();
			}
		}
	}

	@Api
	@Override
	public void bindBidirectional(Property<T> other) {
		AbstractProperty<T> property = (AbstractProperty<T>) other;
		while (true) {
			try {
				lock.readLock().lock();
				if (boundTo != null)
					throw new IllegalStateException("Property already bound!");
			} finally {
				lock.readLock().unlock();
			}
			try {
				lock.writeLock().lock();
				if (!property.lock.writeLock().tryLock())
					continue;
				if (property.boundTo != null)
					throw new IllegalStateException("Bidirectional binding partner already bound!");
				boundTo = property;
				property.boundTo = this;
				T oldValue = value;
				T newValue = other.value();
				if (equals(oldValue, newValue))
					return;
				value = newValue;
				storage.write(value);
				valid = true;
				events.invalidate();
				events.change(oldValue, value);
				property.lock.writeLock().unlock();
				return;
			} finally {
				lock.writeLock().unlock();
			}
		}
	}

	@Api
	@Override
	public boolean bound() {
		try {
			lock.readLock().lock();
			return boundTo != null;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Api
	@Override
	public boolean boundBidirectional() {
		while (true) {
			try {
				lock.readLock().lock();
				if (boundTo == null)
					return false;
				if (!boundTo.lock.readLock().tryLock())
					continue;
				try {
					return boundTo.boundTo == this;
				} finally {
					boundTo.lock.readLock().unlock();
				}
			} finally {
				lock.readLock().unlock();
			}
		}
	}

	@Api
	@Override
	public Property<T> boundTo() {
		try {
			lock.readLock().lock();
			return boundTo;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Api
	@Override
	public Storage<T> storage() {
		try {
			lock.readLock().lock();
			return storage;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Api
	@Override
	public <V> Property<V> map(Function<T, V> function) {
		Property<V> property = Property.withStorage(new ComputerStorage<>(() -> function.apply(value())));
		property.addDependencies(this);
		return property;
	}

	@Api
	@Override
	public BooleanValue mapToBoolean() {
		return mapToBoolean(Boolean.class::cast);
	}

	@Api
	@Override
	public BooleanValue mapToBoolean(BooleanMapFunction<T> function) {
		return BooleanValue.computing(() -> function.apply(value())).addDependencies(this);
	}

	@Api
	@Override
	public NumberValue mapToNumber() {
		return mapToNumber(Number.class::cast);
	}

	@Api
	@Override
	public NumberValue mapToNumber(NumberMapFunction<T> function) {
		return NumberValue.computing(() -> function.apply(value())).addDependencies(this);
	}

	@Api
	@Override
	public NumberValue mapToNumber(DoubleMapFunction<T> function) {
		return NumberValue.computing(() -> function.apply(value())).addDependencies(this);
	}

	@Api
	@Override
	public NumberValue mapToNumber(FloatMapFunction<T> function) {
		return NumberValue.computing(() -> function.apply(value())).addDependencies(this);
	}

	@Api
	@Override
	public NumberValue mapToNumber(IntegerMapFunction<T> function) {
		return NumberValue.computing(() -> function.apply(value())).addDependencies(this);
	}

	@Api
	@Override
	public NumberValue mapToNumber(LongMapFunction<T> function) {
		return NumberValue.computing(() -> function.apply(value())).addDependencies(this);
	}

	protected T computeValue() {
		return defaultComputeValue();
	}

	private T defaultComputeValue() {
		return storage.read();
	}

	@Override
	public String toString() {
		return "Property{" + value() + "}";
	}
}
