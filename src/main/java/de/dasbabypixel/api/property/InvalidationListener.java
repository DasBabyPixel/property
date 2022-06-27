package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 */
public interface InvalidationListener {

	/**
	 * Called when the {@link Property} is invalidated
	 * 
	 * @param property
	 */
	void invalidated(final Property<?> property);
}
