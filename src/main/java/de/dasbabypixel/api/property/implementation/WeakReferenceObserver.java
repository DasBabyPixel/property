package de.dasbabypixel.api.property.implementation;

import java.lang.ref.WeakReference;

import de.dasbabypixel.api.property.InvalidationListener;
import de.dasbabypixel.api.property.Property;

@SuppressWarnings("javadoc")
public class WeakReferenceObserver implements InvalidationListener {
	private final WeakReference<Property<?>> ref;

	public WeakReferenceObserver(final Property<?> binding) {
		if (binding == null) {
			throw new NullPointerException("Binding has to be specified.");
		}
		this.ref = new WeakReference<Property<?>>(binding);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void invalidated(final Property<?> observable) {
		final Property<?> binding = this.ref.get();
		if (binding == null) {
			observable.getBindingRedirectListener().redirectInvalidation.remove(this);
		} else {
			binding.invalidate();
		}
	}
}
