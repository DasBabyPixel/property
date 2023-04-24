package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 * @param <T>
 */
public interface BooleanMapFunction<T>
{
    /**
     * @return the boolean from this {@link BooleanMapFunction}
     */
    boolean apply(final T value);
}
