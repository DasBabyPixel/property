package de.dasbabypixel.api.property;

public class ConstantStorage<T> implements ReadOnlyStorage<T> {
	private final T value;

	public ConstantStorage(T value) {
		this.value = value;
	}

	@Override
	public T read() {
		return value;
	}

	@Override
	public boolean checkForChanges() {
		return false;
	}
}
