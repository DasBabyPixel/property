package de.dasbabypixel.api.property;

public interface Storage<T> {

	T read();

	void write(T value);
	
	boolean checkForChanges();

}
