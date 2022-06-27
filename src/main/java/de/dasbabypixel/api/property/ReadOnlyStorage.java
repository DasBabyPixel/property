package de.dasbabypixel.api.property;

/**
 * A ReadOnly {@link ExternalStorage} implementation
 * 
 * @author DasBabyPixel
 * @param <T>
 */
public interface ReadOnlyStorage<T> extends ExternalStorage<T> {
	@Override
	default void write(final T value) {
	}
}
