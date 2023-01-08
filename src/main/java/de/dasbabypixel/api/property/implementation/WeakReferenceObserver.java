package de.dasbabypixel.api.property.implementation;

import java.lang.ref.WeakReference;

import de.dasbabypixel.api.property.InvalidationListener;
import de.dasbabypixel.api.property.Property;

class WeakReferenceObserver implements InvalidationListener {
	private final WeakReference<ObjectProperty<?>> ref;
	volatile ObjectProperty<?> raw;

	public WeakReferenceObserver(final ObjectProperty<?> binding) {
		if (binding == null) {
			throw new NullPointerException("Binding has to be specified.");
		}
		this.ref = new WeakReference<>(binding);
	}

	@Override
	public void invalidated(final Property<?> observable) {
		final ObjectProperty<?> binding = this.ref.get();
		if (binding == null) {
			observable.getBindingRedirectListener().redirectInvalidation.remove(this);
		} else {
			binding.invalidate();
		}
	}
}
