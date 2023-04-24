package de.dasbabypixel.api.property;

public class BasicStorage<T> implements Storage<T> {

	private T value;

	public BasicStorage(T value) {
		this.value = value;
	}

	@Override
	public T read() {
		return this.value;
	}

	@Override
	public void write(final T value) {
		this.value = value;
	}

	@Override
	public boolean checkForChanges() {
		return false;
	}

	@Override
	public boolean writable() {
		return true;
	}
}
