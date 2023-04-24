package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

/**
 * @param <T>
 *
 * @author DasBabyPixel
 */
public interface Storage<T> {
	/**
	 * @return the current value stored
	 */
	@Api
	T read();

	/**
	 * Writes a value to this {@link Storage}
	 */
	@Api
	void write(final T value);

	/**
	 * @return true if we should check for changes on every read because we don't know if the
	 * underlying value has changed
	 */
	@Api
	boolean checkForChanges();

	/**
	 * @return whether this storage is writable
	 */
	@Api
	boolean writable();
}
