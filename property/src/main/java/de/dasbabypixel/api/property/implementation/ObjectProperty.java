package de.dasbabypixel.api.property.implementation;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import de.dasbabypixel.api.property.BindingRedirectListener;
import de.dasbabypixel.api.property.BooleanMapFunction;
import de.dasbabypixel.api.property.BooleanValue;
import de.dasbabypixel.api.property.ChangeListener;
import de.dasbabypixel.api.property.InvalidationListener;
import de.dasbabypixel.api.property.NumberMapFunction;
import de.dasbabypixel.api.property.NumberValue;
import de.dasbabypixel.api.property.Property;
import de.dasbabypixel.api.property.Storage;

public class ObjectProperty<T> implements Property<T> {

	protected final AtomicBoolean valid = new AtomicBoolean(false);
	protected final AtomicBoolean bound = new AtomicBoolean(false);
	protected final AtomicBoolean computor = new AtomicBoolean(false);
	// Always false, may only be true in one method, has to be made false at method
	// end, synchronize with "bound"
	protected final AtomicBoolean bindingBidirectional = new AtomicBoolean(false);
	protected final AtomicBoolean boundBidirectional = new AtomicBoolean(false);
	protected final AtomicReference<Property<T>> boundTo = new AtomicReference<>(null);
	protected final WeakReferenceObserver weakReferenceObserver = new WeakReferenceObserver(this);
	protected final Collection<InvalidationListener> invalidationListeners = ConcurrentHashMap.newKeySet();
	protected final Collection<ChangeListener<? super T>> changeListeners = ConcurrentHashMap.newKeySet();
	protected final AtomicReference<T> currentValue = new AtomicReference<>(null);
	protected final Storage<T> storage;
	protected final BindingRedirectListener<T> bindingRedirectListener = new BindingRedirectListener<>();

	public static <T> ObjectProperty<T> withStorage(Storage<T> storage) {
		ObjectProperty<T> prop = new ObjectProperty<>(storage);
		prop.getValue(); // Validate
		return prop;
	}

	public static <T> ObjectProperty<T> withValue(T value) {
		ObjectProperty<T> prop = new ObjectProperty<>();
		prop.setValue(value);
		return prop;
	}

	public static <T> ObjectProperty<T> empty() {
		return withValue(null);
	}

	protected ObjectProperty() {
		this(new AtomicStorage<>());
	}

	protected ObjectProperty(Storage<T> storage) {
		this.storage = storage;
	}

	protected T computeValue() {
		return isBound() ? getBoundTo().getValue() : storage.read();
	}

	@Override
	public T getValue() {
		if (valid.compareAndSet(false, true)) {
			T oldValue = currentValue.get();
			T newValue = computeValue();
			if (!equals(oldValue, newValue)) {
				currentValue.set(newValue);
				if (!isBound() && !computor.get()) {
					storage.write(newValue);
				}
				fireChangeListeners(oldValue, newValue);
			}
			return newValue;
		}
		if (isBound() && !isBoundBidirectional()) {
			return getBoundTo().getValue();
		}
		if (storage.checkForChanges()) {
			T newValue = storage.read();
			T oldValue = currentValue.get();
			if (!equals(oldValue, newValue)) {
				currentValue.set(newValue);
				fireInvalidationListeners();
				fireChangeListeners(oldValue, newValue);
			}
			return newValue;
		}
		return currentValue.get();
	}

	@Override
	public void setValue(T value) {
		if (isBound() && !isBoundBidirectional()) {
			throw new UnsupportedOperationException("Can't change the value of a bound property!");
		}
		if (computor.get()) {
			throw new UnsupportedOperationException("Can't change the value of a computor property!");
		}
		T oldValue = currentValue.get();
		if (!equals(oldValue, value)) {
			currentValue.set(value);
			storage.write(value);
			valid.set(true);
			fireInvalidationListeners();
			fireChangeListeners(oldValue, value);
		}
	}

	@Override
	public void invalidate() {
		valid.set(false);
		fireInvalidationListeners();
	}

	@Override
	public String toString() {
		return String.format("%s", getValue());
	}

	@Override
	public void bind(Property<T> other) {
		if (computor.get()) {
			throw new UnsupportedOperationException("Can't bind a computor property!");
		}
		synchronized (bound) {
			if (bound.compareAndSet(false, true)) {
				boundTo.set(other);
				T oldValue = currentValue.get();
				T newValue = other.getValue();
				valid.set(true);
				addDependencies(other);
				fireInvalidationListeners();
				if (!equals(oldValue, newValue)) {
					currentValue.set(newValue);
					fireChangeListeners(oldValue, newValue);
				}
			} else {
				throw new UnsupportedOperationException("Already bound to another property!");
			}
		}
	}

	@Override
	public void bindBidirectional(Property<T> other) {
		if (computor.get()) {
			throw new UnsupportedOperationException("Can't bind a computor property!");
		}
		synchronized (bound) {
			if (bound.compareAndSet(false, true)) {
				boundTo.set(other);
				bindingBidirectional.set(true);
				boundBidirectional.set(true);
				if (other.isBound()) {
					if (other.isBoundBidirectional() && other.getBoundTo() == this) {
						bindingBidirectional.set(false);
					} else {
						bound.set(false);
						boundTo.set(null);
						boundBidirectional.set(false);
						bindingBidirectional.set(false);
						throw new UnsupportedOperationException("Other property already bound to another property!");
					}
				} else {
					// Set value of this to other value
					T oldValue = currentValue.get();
					T newValue = other.getValue();
					if (!equals(oldValue, newValue)) {
						currentValue.set(newValue);
						valid.set(true);
						fireInvalidationListeners();
						fireChangeListeners(oldValue, newValue);
					}

					BidirectionalBindingObserver<T> obs = new BidirectionalBindingObserver.Typed<T>(this,
							(ObjectProperty<T>) other);
					obs.register(this);
					obs.register(other);
				}
				other.bindBidirectional(this);
			} else if (bindingBidirectional.compareAndSet(true, false)) {
				// Nothing, skip this to end recursion
			} else {
				throw new UnsupportedOperationException("Already bound to another property!");
			}
		}
	}

	@Override
	public void unbind() {
		if (computor.get()) {
			throw new UnsupportedOperationException("Can't unbind a computor property!");
		}
		synchronized (bound) {
			if (bound.compareAndSet(true, false)) {
				if (boundBidirectional.compareAndSet(true, false)) {
					bindingBidirectional.set(true);
					Property<T> other = boundTo.get();
					if (other.isBound() && other.isBoundBidirectional()) {
						bound.set(true);
					} else if (other.isBound()) {
						boundTo.set(null);
					}
					other.unbind();
					bindingBidirectional.set(false);
				} else if (bindingBidirectional.get()) {
					Property<T> other = boundTo.getAndSet(null);
					BidirectionalBindingObserver<?> obs = new BidirectionalBindingObserver.Untyped(other, this);
					obs.unregister(other);
					obs.unregister(this);
				} else {
					Property<T> other = boundTo.getAndSet(null);
					removeDependencies(other);
					T oldValue = currentValue.get();
					T newValue = other.getValue();
					storage.write(newValue);
					if (!equals(oldValue, newValue)) {
						currentValue.set(newValue);
						fireChangeListeners(oldValue, newValue);
					}
					valid.set(true);
				}
			}
		}
	}

	@Override
	public <V> ObjectProperty<V> map(Function<T, V> function) {
		ObjectProperty<V> o = new ObjectProperty<V>() {
			@Override
			protected V computeValue() {
				return function.apply(ObjectProperty.this.getValue());
			}
		};
		o.addDependencies(this);
		o.computor.set(true);
		return o;
	}

	@Override
	public BooleanValue mapToBoolean() {
		if (this instanceof BooleanValue) {
			return (BooleanValue) this;
		}
		T value = getValue();
		if (value instanceof Boolean) {
			return mapToBoolean(n -> (Boolean) n);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public BooleanValue mapToBoolean(BooleanMapFunction<T> function) {
		BooleanProperty o = new BooleanProperty() {
			@Override
			protected Boolean computeValue() {
				return function.apply(ObjectProperty.this.getValue());
			}
		};
		o.addDependencies(this);
		o.computor.set(true);
		return o;
	}

	@Override
	public NumberValue mapToNumber() {
		if (this instanceof NumberValue) {
			return (NumberValue) this;
		}
		T value = getValue();
		if (value instanceof Number) {
			return mapToNumber(n -> (Number) n);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public NumberValue mapToNumber(NumberMapFunction<T> function) {
		NumberProperty o = new NumberProperty() {
			@Override
			protected Number computeValue() {
				return function.apply(ObjectProperty.this.getValue());
			}
		};
		o.addDependencies(this);
		o.computor.set(true);
		return o;
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean hasValue(T value) {
		return equals(getValue(), value);
	}

	@Override
	public Storage<T> getStorage() {
		return storage;
	}

	@Override
	public BindingRedirectListener<T> getBindingRedirectListener() {
		return bindingRedirectListener;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ObjectProperty<T> addDependencies(Property<?>... dependencies) {
		for (final Property<?> dep : dependencies) {
			dep.getBindingRedirectListener().redirectInvalidation.add(weakReferenceObserver);
		}
		return this;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ObjectProperty<T> removeDependencies(Property<?>... dependencies) {
		for (final Property<?> dep : dependencies) {
			dep.getBindingRedirectListener().redirectInvalidation.remove(weakReferenceObserver);
		}
		return this;
	}

	@Override
	public void addListener(ChangeListener<? super T> listener) {
		changeListeners.add(listener);
	}

	@Override
	public void removeListener(ChangeListener<? super T> listener) {
		changeListeners.remove(listener);
	}

	@Override
	public void addListener(InvalidationListener listener) {
		invalidationListeners.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		invalidationListeners.remove(listener);
	}

	@Override
	public boolean isValid() {
		return valid.get();
	}

	@Override
	public boolean isBoundBidirectional() {
		return boundBidirectional.get();
	}

	@Override
	public boolean isBound() {
		return bound.get();
	}

	@Override
	public Property<T> getBoundTo() {
		return boundTo.get();
	}

	@Override
	public Collection<InvalidationListener> getInvalidationListeners() {
		return invalidationListeners;
	}

	@Override
	public Collection<ChangeListener<? super T>> getChangeListeners() {
		return changeListeners;
	}

	protected void fireInvalidationListeners() {
		for (InvalidationListener listener : invalidationListeners) {
			listener.invalidated(this);
		}
		bindingRedirectListener.invalidated(this);
	}

	protected void fireChangeListeners(T oldValue, T newValue) {
		for (ChangeListener<? super T> listener : changeListeners) {
			listener.handleChange(this, oldValue, newValue);
		}
		bindingRedirectListener.handleChange(this, oldValue, newValue);
	}

	public static boolean equals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 != null && o1.equals(o2)) {
			return true;
		}
		if (o2 != null && o2.equals(o1)) {
			return true;
		}
		return false;
	}
}
