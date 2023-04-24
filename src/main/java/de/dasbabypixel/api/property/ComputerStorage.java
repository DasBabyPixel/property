package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

import java.util.function.Supplier;

class ComputerStorage<T> implements ReadOnlyExternalStorage<T> {
	private final Supplier<T> computer;

	public ComputerStorage(Supplier<T> computer) {
		this.computer = computer;
	}

	@Api
	@Override
	public T read() {
		return computer.get();
	}
}
