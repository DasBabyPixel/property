package de.dasbabypixel.api.property;

import de.dasbabypixel.api.property.implementation.NumberProperty;

public interface NumberValue extends Property<Number> {

	void addListener(NumberInvalidationListener listener);

	void removeListener(NumberInvalidationListener listener);

	void addListener(NumberChangeListener listener);

	void removeListener(NumberChangeListener listener);

	Number getNumber();

	void setNumber(Number number);

	default long longValue() {
		return getNumber().longValue();
	}

	default double doubleValue() {
		return getNumber().doubleValue();
	}

	default int intValue() {
		return getNumber().intValue();
	}

	default float floatValue() {
		return getNumber().floatValue();
	}

	NumberValue negate();

	NumberValue multiply(Property<Number> other);

	NumberValue divide(Property<Number> other);

	NumberValue add(Property<Number> other);

	NumberValue subtract(Property<Number> other);

	NumberValue multiply(Number other);

	NumberValue divide(Number other);

	NumberValue add(Number other);

	NumberValue subtract(Number other);

	public static NumberValue constant(Number number) {
		return NumberProperty.constant(number);
	}
	
	public static NumberValue withValue(Number number) {
		return NumberProperty.withNumber(number);
	}

	public static NumberValue withStorage(Storage<Number> storage) {
		return NumberProperty.withNumberStorage(storage);
	}

	public static NumberValue zero() {
		return NumberProperty.zero();
	}
}
