package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A {@link NumberValue} {@link Property}
 *
 * @author DasBabyPixel
 */
@Api
public interface NumberValue extends Property<AbstractNumberHolder> {

	@Api
	static NumberValue computing(Supplier<Number> supplier) {
		return withStorage(new ComputerStorage<>(supplier));
	}

	@Api
	static NumberValue computing(IntegerSupplier supplier) {
		return NumberProperty.computing(supplier);
	}

	@Api
	static NumberValue computing(LongSupplier supplier) {
		return NumberProperty.computing(supplier);
	}

	@Api
	static NumberValue computing(DoubleSupplier supplier) {
		return NumberProperty.computing(supplier);
	}

	@Api
	static NumberValue computing(FloatSupplier supplier) {
		return NumberProperty.computing(supplier);
	}

	/**
	 * @return an unmodifiable property with the given {@link Number}
	 */
	@Api
	static NumberValue constant(final Number number) {
		return NumberProperty.constant(number);
	}

	/**
	 * @return a {@link NumberValue} with the given {@link Number} as initial value
	 */
	@Api
	static NumberValue withValue(final Number number) {
		return NumberProperty.withValue(number);
	}

	@Api
	static NumberValue withStorage(Storage<Number> storage) {
		return NumberProperty.withStorage(storage);
	}

	/**
	 * Adds a {@link NumberInvalidationListener} to this property
	 */
	@Api
	void addListener(final NumberInvalidationListener listener);

	/**
	 * Removes a {@link NumberInvalidationListener} from this property
	 */
	@Api
	void removeListener(final NumberInvalidationListener listener);

	/**
	 * Adds a {@link NumberChangeListener} to this property
	 */
	@Api
	void addListener(final NumberChangeListener listener);

	/**
	 * Removes a {@link NumberChangeListener} from this property
	 */
	@Api
	void removeListener(final NumberChangeListener listener);

	/**
	 * @return the {@link AbstractNumberHolder} of this {@link NumberValue}. This {@link AbstractNumberHolder} is also of type {@link Number} and should be used as such where possible
	 */
	@Api
	AbstractNumberHolder numberHolder();

	/**
	 * Sets the {@link Number} of this {@link NumberValue}
	 */
	@Api
	void number(final Number number);

	/**
	 * Sets the {@link Number} of this {@link NumberValue}
	 */
	@Api
	void number(final double number);

	/**
	 * Sets the {@link Number} of this {@link NumberValue}
	 */
	@Api
	void number(final long number);

	/**
	 * Sets the {@link Number} of this {@link NumberValue}
	 */
	@Api
	void number(final float number);

	/**
	 * Sets the {@link Number} of this {@link NumberValue}
	 */
	@Api
	void number(final int number);

	/**
	 * @return the long value of this {@link NumberValue}
	 */
	@Api
	default long longValue() {
		return this.numberHolder().longValue();
	}

	/**
	 * @return the double value of this {@link NumberValue}
	 */
	@Api
	default double doubleValue() {
		return this.numberHolder().doubleValue();
	}

	/**
	 * @return the int value of this {@link NumberValue}
	 */
	@Api
	default int intValue() {
		return this.numberHolder().intValue();
	}

	/**
	 * @return the float value of this {@link NumberValue}
	 */
	@Api
	default float floatValue() {
		return this.numberHolder().floatValue();
	}

	/**
	 * @return the negated {@link NumberValue}
	 */
	@Api
	NumberValue negate();

	/**
	 * @return this {@link NumberValue} multiplied with the other {@link Property NumberProperty}
	 */
	@Api
	NumberValue multiply(final Property<? extends Number> value);

	/**
	 * @return this {@link NumberValue} divided by the other {@link Property NumberProperty}
	 */
	@Api
	NumberValue divide(final Property<? extends Number> value);

	/**
	 * @return adds the given {@link Property NumberProperty} to this {@link NumberValue}
	 */
	@Api
	NumberValue add(final Property<? extends Number> value);

	/**
	 * @return subtracts the given {@link Property NumberProperty} from this {@link NumberValue}
	 */
	@Api
	NumberValue subtract(final Property<? extends Number> value);

	/**
	 * @return a property with the larger number
	 */
	@Api
	NumberValue max(final Property<? extends Number> value);

	/**
	 * @return a property with the smaller number
	 */
	@Api
	NumberValue min(final Property<? extends Number> value);

	/**
	 * @return a property with {@link Math#pow(double, double) Math.pow(this, value)}
	 */
	@Api
	NumberValue pow(final Property<? extends Number> value);

	/**
	 * @return multiplies this by the given {@link Number}
	 */
	@Api
	NumberValue multiply(final Number number);

	/**
	 * @return divides this by the given {@link Number}
	 */
	@Api
	NumberValue divide(final Number number);

	/**
	 * @return adds the given {@link Number} to this
	 */
	@Api
	NumberValue add(final Number number);

	/**
	 * @return subtracts the given {@link Number} from this
	 */
	@Api
	NumberValue subtract(final Number number);

	/**
	 * @return a property with the larger number
	 */
	@Api
	NumberValue max(final Number number);

	/**
	 * @return a property with the smaller number
	 */
	@Api
	NumberValue min(final Number number);

	/**
	 * @return a property with {@link Math#pow(double, double) Math.pow(this, value)}
	 */
	@Api
	NumberValue pow(final Number value);

	@Override
	NumberValue addDependencies(Property<?>... dependencies);

	@Override
	NumberValue removeDependencies(Property<?>... dependencies);

	/**
	 * {@inheritDoc}<br>
	 *
	 * <b>The operation MUST modify the given object. It MUST NOT return another {@link AbstractNumberHolder}!</b>
	 */
	@Override
	void atomicOperation(Function<AbstractNumberHolder, AbstractNumberHolder> operation);
}
