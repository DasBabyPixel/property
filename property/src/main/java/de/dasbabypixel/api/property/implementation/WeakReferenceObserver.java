package de.dasbabypixel.api.property.implementation;

import java.lang.ref.WeakReference;

import de.dasbabypixel.api.property.InvalidationListener;
import de.dasbabypixel.api.property.Property;

public class WeakReferenceObserver implements InvalidationListener {

	private final WeakReference<Property<?>> ref;

	public WeakReferenceObserver(Property<?> binding) {
		if (binding == null) {
			throw new NullPointerException("Binding has to be specified.");
		}
		ref = new WeakReference<>(binding);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void invalidated(Property<?> observable) {
		final Property<?> binding = ref.get();
		if (binding == null) {
			observable.getBindingRedirectListener().redirectInvalidation.remove(this);
		} else {
			binding.invalidate();
		}
	}
}
