package de.dasbabypixel.api.property;

/**
 * A ReadOnly {@link ExternalStorage} implementation
 *
 * @param <T>
 *
 * @author DasBabyPixel
 */
public interface ReadOnlyExternalStorage<T> extends ExternalStorage<T> {
	@Override
	default void write(final T value) {
		throw new UnsupportedOperationException();
	}

	@Override
	default boolean writable() {
		return false;
	}
}
