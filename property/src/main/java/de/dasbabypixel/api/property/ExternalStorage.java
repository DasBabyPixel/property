package de.dasbabypixel.api.property;

public interface ExternalStorage<T> extends Storage<T> {

	@Override
	default boolean checkForChanges() {
		return true;
	}
}
