package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

@Api
public interface ReadOnlyStorage<T> extends Storage<T>{
	@Override
	default void write(final T value) {
		throw new UnsupportedOperationException();
	}

	@Override
	default boolean writable() {
		return false;
	}
}
