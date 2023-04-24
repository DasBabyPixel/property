package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 */
public interface NumberChangeListener {

	/**
	 * Called when the value of the {@link NumberValue} is changed
	 */
	void handleChange(final NumberValue value, Number oldNumber, Number newNumber);
}
