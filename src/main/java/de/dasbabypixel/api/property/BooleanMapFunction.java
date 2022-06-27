package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 * @param <T>
 */
public interface BooleanMapFunction<T>
{
    /**
     * @param value
     * @return the boolean from this {@link BooleanMapFunction}
     */
    boolean apply(final T value);
}
