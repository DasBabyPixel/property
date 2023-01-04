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

	protected final AtomicBoolean valid;

	protected final AtomicBoolean bound;

	protected final AtomicBoolean computer;

	protected final AtomicBoolean bindingBidirectional;

	protected final AtomicBoolean boundBidirectional;

	protected final AtomicReference<Property<T>> boundTo;

	protected final WeakReferenceObserver weakReferenceObserver;

	protected final Collection<InvalidationListener> invalidationListeners;

	protected final Collection<ChangeListener<? super T>> changeListeners;

	protected final AtomicReference<T> currentValue;

	protected final Storage<T> storage;

	protected final BindingRedirectListener<T> bindingRedirectListener;

	protected boolean invalidating = false;

	public static <T> ObjectProperty<T> withObjectStorage(final Storage<T> storage) {
		final ObjectProperty<T> prop = new ObjectProperty<T>(storage);
		prop.getValue();
		return prop;
	}

	public static <T> ObjectProperty<T> withValue(final T value) {
		final ObjectProperty<T> prop = new ObjectProperty<T>();
		prop.setValue(value);
		return prop;
	}

	public static <T> ObjectProperty<T> empty() {
		return withValue((T) null);
	}

	protected ObjectProperty() {
		this(new AtomicStorage<>());
	}

	protected ObjectProperty(final Storage<T> storage) {
		this.valid = new AtomicBoolean(false);
		this.bound = new AtomicBoolean(false);
		this.computer = new AtomicBoolean(false);
		this.bindingBidirectional = new AtomicBoolean(false);
		this.boundBidirectional = new AtomicBoolean(false);
		this.boundTo = new AtomicReference<Property<T>>(null);
		this.weakReferenceObserver = new WeakReferenceObserver(this);
		this.invalidationListeners = ConcurrentHashMap.newKeySet();
		this.changeListeners = ConcurrentHashMap.newKeySet();
		this.currentValue = new AtomicReference<T>(null);
		this.bindingRedirectListener = new BindingRedirectListener<T>();
		this.storage = storage;
	}

	protected T computeValue() {
		return this.isBound() ? this.getBoundTo().getValue() : this.storage.read();
	}

	@Override
	public T getValue() {
		if (this.valid.compareAndSet(false, true)) {
			final T oldValue = this.currentValue.get();
			final T newValue = this.computeValue();
			if (!equals(oldValue, newValue)) {
				this.currentValue.set(newValue);
				if (!this.isBound() && !this.computer.get()) {
					this.storage.write(newValue);
				}
				this.fireChangeListeners(oldValue, newValue);
			}
			return newValue;
		}
		if (this.isBound() && !this.isBoundBidirectional()) {
			return this.getBoundTo().getValue();
		}
		if (this.storage.checkForChanges()) {
			final T newValue2 = this.storage.read();
			final T oldValue2 = this.currentValue.get();
			if (!equals(oldValue2, newValue2)) {
				this.currentValue.set(newValue2);
				this.fireInvalidationListeners();
				this.fireChangeListeners(oldValue2, newValue2);
			}
			return newValue2;
		}
		return this.currentValue.get();
	}

	@Override
	public void setValue(final T value) {
		if (this.isBound() && !this.isBoundBidirectional()) {
			throw new UnsupportedOperationException("Can't change the value of a bound property!");
		}
		if (this.computer.get()) {
			throw new UnsupportedOperationException("Can't change the value of a computor property!");
		}
		final T oldValue = this.currentValue.get();
		if (!equals(oldValue, value)) {
			this.currentValue.set(value);
			this.storage.write(value);
			this.valid.set(true);
			this.fireInvalidationListeners();
			this.fireChangeListeners(oldValue, value);
		}
	}

	@Override
	public void invalidate() {
		this.valid.set(false);
		if (!invalidating) {
			invalidating = true;
			this.fireInvalidationListeners();
			invalidating = false;
		}
	}

	@Override
	public String toString() {
		return String.format("%s", this.getValue());
	}

	@Override
	public void bind(final Property<T> other) {
		if (this.computer.get()) {
			throw new UnsupportedOperationException("Can't bind a computor property!");
		}
		synchronized (this.bound) {
			if (!this.bound.compareAndSet(false, true)) {
				throw new UnsupportedOperationException("Already bound to another property!");
			}
			this.boundTo.set(other);
			final T oldValue = this.currentValue.get();
			final T newValue = other.getValue();
			this.valid.set(true);
			this.addDependencies(other);
			this.fireInvalidationListeners();
			if (!equals(oldValue, newValue)) {
				this.currentValue.set(newValue);
				this.fireChangeListeners(oldValue, newValue);
			}
		}
		// monitorexit(this.bound)
	}

	@Override
	public void bindBidirectional(final Property<T> other) {
		if (this.computer.get()) {
			throw new UnsupportedOperationException("Can't bind a computor property!");
		}
		synchronized (this.bound) {
			if (this.bound.compareAndSet(false, true)) {
				this.boundTo.set(other);
				this.bindingBidirectional.set(true);
				this.boundBidirectional.set(true);
				if (other.isBound()) {
					if (!other.isBoundBidirectional() || other.getBoundTo() != this) {
						this.bound.set(false);
						this.boundTo.set(null);
						this.boundBidirectional.set(false);
						this.bindingBidirectional.set(false);
						throw new UnsupportedOperationException("Other property already bound to another property!");
					}
					this.bindingBidirectional.set(false);
				} else {
					final T oldValue = this.currentValue.get();
					final T newValue = other.getValue();
					if (!equals(oldValue, newValue)) {
						this.currentValue.set(newValue);
						this.valid.set(true);
						this.fireInvalidationListeners();
						this.fireChangeListeners(oldValue, newValue);
					}
					final BidirectionalBindingObserver<T> obs = new BidirectionalBindingObserver.Typed<T>(this,
							(ObjectProperty<T>) other);
					obs.register(this);
					obs.register(other);
				}
				other.bindBidirectional(this);
			} else if (!this.bindingBidirectional.compareAndSet(true, false)) {
				throw new UnsupportedOperationException("Already bound to another property!");
			}
		}
		// monitorexit(this.bound)
	}

	@Override
	public void unbind() {
		if (this.computer.get()) {
			throw new UnsupportedOperationException("Can't unbind a computor property!");
		}
		synchronized (this.bound) {
			if (this.bound.compareAndSet(true, false)) {
				if (this.boundBidirectional.compareAndSet(true, false)) {
					this.bindingBidirectional.set(true);
					final Property<T> other = this.boundTo.get();
					if (other.isBound() && other.isBoundBidirectional()) {
						this.bound.set(true);
					} else if (other.isBound()) {
						this.boundTo.set(null);
					}
					other.unbind();
					this.bindingBidirectional.set(false);
				} else if (this.bindingBidirectional.get()) {
					final Property<T> other = this.boundTo.getAndSet(null);
					final BidirectionalBindingObserver<?> obs = new BidirectionalBindingObserver.Untyped(other, this);
					obs.unregister(other);
					obs.unregister(this);
				} else {
					final Property<T> other = this.boundTo.getAndSet(null);
					this.removeDependencies(other);
					final T oldValue = this.currentValue.get();
					final T newValue = other.getValue();
					this.storage.write(newValue);
					if (!equals(oldValue, newValue)) {
						this.currentValue.set(newValue);
						this.fireChangeListeners(oldValue, newValue);
					}
					this.valid.set(true);
				}
			}
		}
		// monitorexit(this.bound)
	}

	@Override
	public <V> ObjectProperty<V> map(final Function<T, V> function) {
		final ObjectProperty<V> o = new ObjectProperty<V>() {

			@Override
			protected V computeValue() {
				return function.apply(ObjectProperty.this.getValue());
			}

		};
		o.addDependencies(this);
		o.computer.set(true);
		return o;
	}

	@Override
	public BooleanValue mapToBoolean() {
		if (this instanceof BooleanValue) {
			return (BooleanValue) this;
		}
		final T value = this.getValue();
		if (value instanceof Boolean) {
			return this.mapToBoolean(Boolean.class::cast);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public BooleanValue mapToBoolean(final BooleanMapFunction<T> function) {
		final BooleanProperty o = new BooleanProperty() {

			@Override
			protected Boolean computeValue() {
				return function.apply(ObjectProperty.this.getValue());
			}

		};
		o.addDependencies(this);
		o.computer.set(true);
		return o;
	}

	@Override
	public NumberValue mapToNumber() {
		if (this instanceof NumberValue) {
			return (NumberValue) this;
		}
		final T value = this.getValue();
		if (value instanceof Number) {
			return this.mapToNumber(Number.class::cast);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public NumberValue mapToNumber(final NumberMapFunction<T> function) {
		final NumberProperty o = new NumberProperty() {

			@Override
			protected Number computeValue() {
				return function.apply(ObjectProperty.this.getValue());
			}

		};
		o.addDependencies(this);
		o.computer.set(true);
		return o;
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean hasValue(final T value) {
		return equals(this.getValue(), value);
	}

	@Override
	public Storage<T> getStorage() {
		return this.storage;
	}

	@Override
	public BindingRedirectListener<T> getBindingRedirectListener() {
		return this.bindingRedirectListener;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ObjectProperty<T> addDependencies(final Property<?>... dependencies) {
		for (final Property<?> dep : dependencies) {
			dep.getBindingRedirectListener().redirectInvalidation.add(this.weakReferenceObserver);
		}
		return this;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ObjectProperty<T> removeDependencies(final Property<?>... dependencies) {
		for (final Property<?> dep : dependencies) {
			dep.getBindingRedirectListener().redirectInvalidation.remove(this.weakReferenceObserver);
		}
		return this;
	}

	@Override
	public void addListener(final ChangeListener<? super T> listener) {
		this.changeListeners.add(listener);
	}

	@Override
	public void removeListener(final ChangeListener<? super T> listener) {
		this.changeListeners.remove(listener);
	}

	@Override
	public void addListener(final InvalidationListener listener) {
		this.invalidationListeners.add(listener);
	}

	@Override
	public void removeListener(final InvalidationListener listener) {
		this.invalidationListeners.remove(listener);
	}

	@Override
	public boolean isValid() {
		return this.valid.get();
	}

	@Override
	public boolean isBoundBidirectional() {
		return this.boundBidirectional.get();
	}

	@Override
	public boolean isBound() {
		return this.bound.get();
	}

	@Override
	public Property<T> getBoundTo() {
		return this.boundTo.get();
	}

	@Override
	public Collection<InvalidationListener> getInvalidationListeners() {
		return this.invalidationListeners;
	}

	@Override
	public Collection<ChangeListener<? super T>> getChangeListeners() {
		return this.changeListeners;
	}

	protected void fireInvalidationListeners() {
		for (final InvalidationListener listener : this.invalidationListeners) {
			listener.invalidated(this);
		}
		this.bindingRedirectListener.invalidated(this);
	}

	protected void fireChangeListeners(final T oldValue, final T newValue) {
		for (final ChangeListener<? super T> listener : this.changeListeners) {
			listener.handleChange(this, oldValue, newValue);
		}
		this.bindingRedirectListener.handleChange(this, oldValue, newValue);
	}

	public static boolean equals(final Object o1, final Object o2) {
		return o1 == o2 || (o1 != null && o1.equals(o2)) || (o2 != null && o2.equals(o1));
	}

}
