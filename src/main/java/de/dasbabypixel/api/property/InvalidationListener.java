package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

/**
 * @author DasBabyPixel
 */
public interface InvalidationListener {

	/**
	 * Called when the {@link Property} is invalidated
	 */
	@Api
	void invalidated(final Property<?> property);
}
