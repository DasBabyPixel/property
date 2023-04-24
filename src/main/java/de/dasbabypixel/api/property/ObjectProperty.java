package de.dasbabypixel.api.property;

class ObjectProperty<T> extends AbstractProperty<T> {

	public ObjectProperty(T value) {
		super(new BasicStorage<>(value));
	}

	public ObjectProperty(Storage<T> storage) {
		super(storage);
	}
}
