package de.dasbabypixel.api.property;

public interface ChangeListener<T> {

	void handleChange(Property<? extends T> observable, T oldValue, T newValue);

}
