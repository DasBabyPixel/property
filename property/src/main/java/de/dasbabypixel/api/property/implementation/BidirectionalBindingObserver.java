package de.dasbabypixel.api.property.implementation;

import java.lang.ref.WeakReference;

import de.dasbabypixel.api.property.BindingRedirectListener;
import de.dasbabypixel.api.property.ChangeListener;
import de.dasbabypixel.api.property.InvalidationListener;
import de.dasbabypixel.api.property.Property;

public abstract class BidirectionalBindingObserver<T> implements InvalidationListener, ChangeListener<T> {

	private final int hashCode;

	public BidirectionalBindingObserver(Object p1, Object p2) {
		hashCode = p1.hashCode() * p2.hashCode();
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BidirectionalBindingObserver)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		BidirectionalBindingObserver<T> other = (BidirectionalBindingObserver<T>) obj;
		return hashCode == other.hashCode;
	}

	public void register(Property<T> property) {
		@SuppressWarnings("deprecation")
		BindingRedirectListener<T> r = property.getBindingRedirectListener();
		r.redirectChange.add(this);
		r.redirectInvalidation.add(this);
	}

	public void unregister(Property<?> property) {
		@SuppressWarnings("deprecation")
		BindingRedirectListener<?> r = property.getBindingRedirectListener();
		r.redirectChange.remove(this);
		r.redirectInvalidation.remove(this);
	}

	protected abstract Object getProperty1();

	protected abstract Object getProperty2();

	public static class Typed<T> extends BidirectionalBindingObserver<T> {

		private final WeakReference<ObjectProperty<T>> ref1;
		private final WeakReference<ObjectProperty<T>> ref2;
		private boolean updatingChange = false;
		private boolean updatingInvalidated = false;

		public Typed(ObjectProperty<T> property1, ObjectProperty<T> property2) {
			super(property1, property2);
			ref1 = new WeakReference<ObjectProperty<T>>(property1);
			ref2 = new WeakReference<ObjectProperty<T>>(property2);
		}

		private boolean isAReferenceNullAndIfSoRemoveListeners(ObjectProperty<T> p1, ObjectProperty<T> p2) {
			if (p1 == null || p2 == null) {
				if (p1 != null) {
					unregister(p1);
				}
				if (p2 != null) {
					unregister(p2);
				}
				return true;
			}
			return false;
		}

		@Override
		public void handleChange(Property<? extends T> source, T oldValue, T newValue) {
			ObjectProperty<T> p1 = ref1.get();
			ObjectProperty<T> p2 = ref2.get();
			if (isAReferenceNullAndIfSoRemoveListeners(p1, p2)) {
				return;
			}
			if (!updatingChange) {
				updatingChange = true;
				ObjectProperty<T> v = source == p1 ? p2 : p1;
				v.currentValue.set(newValue);
				v.valid.set(true);
				v.fireChangeListeners(oldValue, newValue);
				updatingChange = false;
			}
		}

		@Override
		public void invalidated(Property<?> source) {
			ObjectProperty<T> p1 = ref1.get();
			ObjectProperty<T> p2 = ref2.get();
			if (isAReferenceNullAndIfSoRemoveListeners(p1, p2)) {
				return;
			}
			if (!updatingInvalidated) {
				updatingInvalidated = true;
				ObjectProperty<T> v = source == p1 ? p2 : p1;
				v.valid.set(false);
				v.fireInvalidationListeners();
				updatingInvalidated = false;
			}
		}

		@Override
		protected Object getProperty1() {
			return ref1.get();
		}

		@Override
		protected Object getProperty2() {
			return ref2.get();
		}

	}

	public static class Untyped extends BidirectionalBindingObserver<Object> {

		private final Object property1;
		private final Object property2;

		public Untyped(Object property1, Object property2) {
			super(property1, property2);
			this.property1 = property1;
			this.property2 = property2;
		}

		@Override
		protected Object getProperty1() {
			return property1;
		}

		@Override
		protected Object getProperty2() {
			return property2;
		}

		@Override
		public void invalidated(Property<?> property) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void handleChange(Property<? extends Object> observable, Object oldValue, Object newValue) {
			throw new UnsupportedOperationException();
		}
	}
}
