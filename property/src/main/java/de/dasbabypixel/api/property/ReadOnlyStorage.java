package de.dasbabypixel.api.property;

public interface ReadOnlyStorage<T> extends ExternalStorage<T> {

	@Override
	default void write(T value) {
	}
}
