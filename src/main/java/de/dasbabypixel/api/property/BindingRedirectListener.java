package de.dasbabypixel.api.property;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DasBabyPixel
 * @param <T>
 */
public class BindingRedirectListener<T> implements InvalidationListener, ChangeListener<T> {

	@SuppressWarnings("javadoc")
	public final Collection<InvalidationListener> redirectInvalidation;
	@SuppressWarnings("javadoc")
	public final Collection<ChangeListener<T>> redirectChange;

	/**
	 * 
	 */
	public BindingRedirectListener() {
		this.redirectInvalidation = ConcurrentHashMap.newKeySet();
		this.redirectChange = ConcurrentHashMap.newKeySet();
	}

	@Override
	public void handleChange(final Property<? extends T> observable, final T oldValue, final T newValue) {
		for (final ChangeListener<T> l : this.redirectChange) {
			l.handleChange(observable, oldValue, newValue);
		}
	}

	@Override
	public void invalidated(final Property<?> property) {
		for (final InvalidationListener l : this.redirectInvalidation) {
			l.invalidated(property);
		}
	}
}
