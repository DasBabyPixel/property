package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 * @param <T>
 */
public interface Storage<T> {
	/**
	 * @return the current value stored
	 */
	T read();

	/**
	 * Writes a value to this {@link Storage}
	 * 
	 * @param value
	 */
	void write(final T value);

	/**
	 * @return true if this 
	 */
	boolean checkForChanges();
}
