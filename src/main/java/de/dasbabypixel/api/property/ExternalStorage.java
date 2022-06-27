package de.dasbabypixel.api.property;

/**
 * @author DasBabyPixel
 * @param <T>
 */
public interface ExternalStorage<T> extends Storage<T> {
	
	@Override
	default boolean checkForChanges() {
		return true;
	}
}
