package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 */
public interface NumberChangeListener {

	/**
	 * Called when the value of the {@link NumberValue} is changed
	 * 
	 * @param value
	 * @param oldValue
	 * @param newValue
	 */
	void handleChange(final NumberValue value, final Number oldValue, final Number newValue);
}
