package de.dasbabypixel.api.property.implementation;

import java.lang.ref.WeakReference;

import de.dasbabypixel.api.property.BindingRedirectListener;
import de.dasbabypixel.api.property.ChangeListener;
import de.dasbabypixel.api.property.InvalidationListener;
import de.dasbabypixel.api.property.Property;

public abstract class BidirectionalBindingObserver<T> implements InvalidationListener, ChangeListener<T> {
	private final int hashCode;

	public BidirectionalBindingObserver(final Object p1, final Object p2) {
		this.hashCode = p1.hashCode() * p2.hashCode();
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BidirectionalBindingObserver)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final BidirectionalBindingObserver<T> other = (BidirectionalBindingObserver<T>) obj;
		return this.hashCode == other.hashCode;
	}

	@SuppressWarnings("deprecation")
	public void register(final Property<T> property) {
		final BindingRedirectListener<T> r = property.getBindingRedirectListener();
		r.redirectChange.add(this);
		r.redirectInvalidation.add(this);
	}

	@SuppressWarnings("deprecation")
	public void unregister(final Property<?> property) {
		final BindingRedirectListener<?> r = property.getBindingRedirectListener();
		r.redirectChange.remove(this);
		r.redirectInvalidation.remove(this);
	}

	protected abstract Object getProperty1();

	protected abstract Object getProperty2();

	public static class Typed<T> extends BidirectionalBindingObserver<T> {
		private final WeakReference<ObjectProperty<T>> ref1;
		private final WeakReference<ObjectProperty<T>> ref2;
		private boolean updatingChange;
		private boolean updatingInvalidated;

		public Typed(final ObjectProperty<T> property1, final ObjectProperty<T> property2) {
			super(property1, property2);
			this.updatingChange = false;
			this.updatingInvalidated = false;
			this.ref1 = new WeakReference<ObjectProperty<T>>(property1);
			this.ref2 = new WeakReference<ObjectProperty<T>>(property2);
		}

		private boolean isAReferenceNullAndIfSoRemoveListeners(final ObjectProperty<T> p1, final ObjectProperty<T> p2) {
			if (p1 == null || p2 == null) {
				if (p1 != null) {
					this.unregister(p1);
				}
				if (p2 != null) {
					this.unregister(p2);
				}
				return true;
			}
			return false;
		}

		@Override
		public void handleChange(final Property<? extends T> source, final T oldValue, final T newValue) {
			final ObjectProperty<T> p1 = this.ref1.get();
			final ObjectProperty<T> p2 = this.ref2.get();
			if (this.isAReferenceNullAndIfSoRemoveListeners(p1, p2)) {
				return;
			}
			if (!this.updatingChange) {
				this.updatingChange = true;
				final ObjectProperty<T> v = (source == p1) ? p2 : p1;
				v.currentValue.set(newValue);
				v.valid.set(true);
				v.fireChangeListeners(oldValue, newValue);
				this.updatingChange = false;
			}
		}

		@Override
		public void invalidated(final Property<?> source) {
			final ObjectProperty<T> p1 = this.ref1.get();
			final ObjectProperty<T> p2 = this.ref2.get();
			if (this.isAReferenceNullAndIfSoRemoveListeners(p1, p2)) {
				return;
			}
			if (!this.updatingInvalidated) {
				this.updatingInvalidated = true;
				final ObjectProperty<T> v = (source == p1) ? p2 : p1;
				v.valid.set(false);
				v.fireInvalidationListeners();
				this.updatingInvalidated = false;
			}
		}

		@Override
		protected Object getProperty1() {
			return this.ref1.get();
		}

		@Override
		protected Object getProperty2() {
			return this.ref2.get();
		}
	}

	public static class Untyped extends BidirectionalBindingObserver<Object> {
		private final Object property1;
		private final Object property2;

		public Untyped(final Object property1, final Object property2) {
			super(property1, property2);
			this.property1 = property1;
			this.property2 = property2;
		}

		@Override
		protected Object getProperty1() {
			return this.property1;
		}

		@Override
		protected Object getProperty2() {
			return this.property2;
		}

		@Override
		public void invalidated(final Property<?> property) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void handleChange(final Property<?> observable, final Object oldValue, final Object newValue) {
			throw new UnsupportedOperationException();
		}
	}
}
