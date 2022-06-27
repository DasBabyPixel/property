package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 * @param <T>
 */
public interface NumberMapFunction<T> {
	/**
	 * @param value
	 * @return the number mapped from the value
	 */
	Number apply(final T value);
}
