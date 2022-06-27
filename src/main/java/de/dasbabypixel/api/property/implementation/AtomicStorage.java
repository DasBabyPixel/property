package de.dasbabypixel.api.property.implementation;

import java.util.concurrent.atomic.AtomicReference;

import de.dasbabypixel.api.property.Storage;

@SuppressWarnings("javadoc")
public class AtomicStorage<T> implements Storage<T> {
	
	private final AtomicReference<T> value;

	public AtomicStorage() {
		this.value = new AtomicReference<T>();
	}

	@Override
	public T read() {
		return this.value.get();
	}

	@Override
	public void write(final T value) {
		this.value.set(value);
	}

	@Override
	public boolean checkForChanges() {
		return false;
	}
}
