package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 * @param <T>
 */
public interface ChangeListener<T> {

	/**
	 * Called when a property value is changed
	 * 
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
	void handleChange(final Property<? extends T> property, final T oldValue, final T newValue);
}
