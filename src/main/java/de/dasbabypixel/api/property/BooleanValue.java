package de.dasbabypixel.api.property;

import de.dasbabypixel.api.property.implementation.BooleanProperty;

/**
 * @author DasBabyPixel
 */
public interface BooleanValue extends Property<Boolean> {

	/**
	 * @return the boolean value
	 */
	boolean booleanValue();

	/**
	 * @param value
	 * @return property (this || other)
	 */
	BooleanValue and(final BooleanValue value);

	/**
	 * @param value
	 * @return property (this || other)
	 */
	BooleanValue or(final BooleanValue value);

	/**
	 * @return !this
	 */
	BooleanValue negate();

	/**
	 * @return false property
	 */
	static BooleanValue falseValue() {
		return BooleanProperty.falseProperty();
	}

	/**
	 * @return true property
	 */
	static BooleanValue trueValue() {
		return BooleanProperty.trueProperty();
	}

	/**
	 * @param storage
	 * @return property with custom storage
	 */
	static BooleanValue withStorage(final Storage<Boolean> storage) {
		return BooleanProperty.withBooleanStorage(storage);
	}

	/**
	 * @param value
	 * @return property with initial value
	 */
	static BooleanValue withValue(final boolean value) {
		return BooleanProperty.withValue(value);
	}
}
