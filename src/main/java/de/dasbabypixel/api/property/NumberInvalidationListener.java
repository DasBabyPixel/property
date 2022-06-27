package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 */
public interface NumberInvalidationListener {

	/**
	 * Called when a {@link NumberValue} is invalidated
	 * 
	 * @param property
	 */
	void invalidated(final NumberValue property);
}
