package de.dasbabypixel.api.property;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class BindingRedirectListener<T> implements InvalidationListener, ChangeListener<T> {

	public final Collection<InvalidationListener> redirectInvalidation = ConcurrentHashMap.newKeySet();
	public final Collection<ChangeListener<T>> redirectChange = ConcurrentHashMap.newKeySet();

	@Override
	public void handleChange(Property<? extends T> observable, T oldValue, T newValue) {
		for (ChangeListener<T> l : redirectChange) {
			l.handleChange(observable, oldValue, newValue);
		}
	}

	@Override
	public void invalidated(Property<?> property) {
		for (InvalidationListener l : redirectInvalidation) {
			l.invalidated(property);
		}
	}
}
