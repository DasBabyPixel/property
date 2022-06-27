package de.dasbabypixel.api.property.implementation;

import java.util.concurrent.atomic.AtomicReference;

import de.dasbabypixel.api.property.Storage;

public class AtomicStorage<T> implements Storage<T> {

	private final AtomicReference<T> value = new AtomicReference<>();
	
	@Override
	public T read() {
		return value.get();
	}

	@Override
	public void write(T value) {
		this.value.set(value);
	}

	@Override
	public boolean checkForChanges() {
		return false;
	}
}
