package de.dasbabypixel.api.property;

import de.dasbabypixel.api.property.implementation.NumberProperty;

/**
 * A {@link NumberValue} {@link Property}
 * 
 * @author DasBabyPixel
 */
public interface NumberValue extends Property<Number> {

	/**
	 * Adds a {@link NumberInvalidationListener} to this property
	 * 
	 * @param listener
	 */
	void addListener(final NumberInvalidationListener listener);

	/**
	 * Removes a {@link NumberInvalidationListener} from this property
	 * 
	 * @param listener
	 */
	void removeListener(final NumberInvalidationListener listener);

	/**
	 * Adds a {@link NumberChangeListener} to this property
	 * 
	 * @param listener
	 */
	void addListener(final NumberChangeListener listener);

	/**
	 * Removes a {@link NumberChangeListener} from this property
	 * 
	 * @param listener
	 */
	void removeListener(final NumberChangeListener listener);

	/**
	 * @return the {@link Number} of this {@link NumberValue}
	 */
	Number getNumber();

	/**
	 * Sets the {@link Number} of this {@link NumberValue}
	 * 
	 * @param number
	 */
	void setNumber(final Number number);

	/**
	 * @return the long value of this {@link NumberValue}
	 */
	default long longValue() {
		return this.getNumber().longValue();
	}

	/**
	 * @return the double value of this {@link NumberValue}
	 */
	default double doubleValue() {
		return this.getNumber().doubleValue();
	}

	/**
	 * @return the int value of this {@link NumberValue}
	 */
	default int intValue() {
		return this.getNumber().intValue();
	}

	/**
	 * @return the float value of this {@link NumberValue}
	 */
	default float floatValue() {
		return this.getNumber().floatValue();
	}

	/**
	 * @return the negated {@link NumberValue}
	 */
	NumberValue negate();

	/**
	 * @param value
	 * @return this {@link NumberValue} multiplied with the other {@link Property
	 *         NumberProperty}
	 */
	NumberValue multiply(final Property<Number> value);

	/**
	 * @param value
	 * @return this {@link NumberValue} divided by the other {@link Property
	 *         NumberProperty}
	 */
	NumberValue divide(final Property<Number> value);

	/**
	 * @param value
	 * @return adds the given {@link Property NumberProperty} to this
	 *         {@link NumberValue}
	 */
	NumberValue add(final Property<Number> value);

	/**
	 * @param value
	 * @return subtracts the given {@link Property NumberProperty} from this
	 *         {@link NumberValue}
	 */
	NumberValue subtract(final Property<Number> value);

	/**
	 * @param value
	 * @return a property with the larger number
	 */
	NumberValue max(final Property<Number> value);

	/**
	 * @param value
	 * @return a property with the smaller number
	 */
	NumberValue min(final Property<Number> value);

	/**
	 * @param number
	 * @return multiplies this by the given {@link Number}
	 */
	NumberValue multiply(final Number number);

	/**
	 * @param number
	 * @return divides this by the given {@link Number}
	 */
	NumberValue divide(final Number number);

	/**
	 * @param number
	 * @return adds the given {@link Number} to this
	 */
	NumberValue add(final Number number);

	/**
	 * @param number
	 * @return subtracts the given {@link Number} from this
	 */
	NumberValue subtract(final Number number);

	/**
	 * @param number
	 * @return a property with the larger number
	 */
	NumberValue max(final Number number);

	/**
	 * @param number
	 * @return a property with the smaller number
	 */
	NumberValue min(final Number number);

	@Override
	NumberValue addDependencies(Property<?>... dependencies);

	@Override
	NumberValue removeDependencies(Property<?>... dependencies);

	/**
	 * @param number
	 * @return an unmodifiable property with the given {@link Number}
	 */
	static NumberValue constant(final Number number) {
		return NumberProperty.constant(number);
	}

	/**
	 * @param number
	 * @return a {@link NumberValue} with the given {@link Number} as initial value
	 */
	static NumberValue withValue(final Number number) {
		return NumberProperty.withNumber(number);
	}

	/**
	 * @param storage
	 * @return a {@link NumberValue} with the given {@link Storage}
	 */
	static NumberValue withStorage(final Storage<Number> storage) {
		return NumberProperty.withNumberStorage(storage);
	}

	/**
	 * @return a {@link NumberValue} with 0 as initial value
	 */
	static NumberValue zero() {
		return NumberProperty.zero();
	}

}
