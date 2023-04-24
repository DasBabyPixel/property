package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 */
public interface NumberMapFunction<T> {
	/**
	 * @return the number mapped from the value
	 */
	Number apply(final T value);
}
