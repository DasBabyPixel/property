package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

import java.util.function.Supplier;

/**
 * @author DasBabyPixel
 */
@Api
public interface BooleanValue extends Property<Boolean> {

	static BooleanValue computing(Supplier<Boolean> supplier) {
		return withStorage(new ComputerStorage<>(supplier));
	}

	/**
	 * @return false property
	 */
	@Api
	static BooleanValue falseValue() {
		return BooleanProperty.falseProperty();
	}

	/**
	 * @return true property
	 */
	@Api
	static BooleanValue trueValue() {
		return BooleanProperty.trueProperty();
	}

	/**
	 * @return property with custom storage
	 */
	@Api
	static BooleanValue withStorage(final Storage<Boolean> storage) {
		return BooleanProperty.withBooleanStorage(storage);
	}

	/**
	 * @return property with initial value
	 */
	@Api
	static BooleanValue withValue(final boolean value) {
		return BooleanProperty.withValue(value);
	}

	/**
	 * @return the boolean value
	 */
	@Api
	boolean booleanValue();

	/**
	 * @return property (this {@literal &&} other)
	 */
	@Api
	BooleanValue and(final Property<? extends Boolean> value);

	/**
	 * @return property (this || other)
	 */
	@Api
	BooleanValue or(final Property<? extends Boolean> value);

	/**
	 * @return property (this ^ other)
	 */
	@Api
	BooleanValue xor(final Property<? extends Boolean> value);

	/**
	 * @return property (this ^ other)
	 */
	@Api
	BooleanValue xor(final boolean value);

	@Override
	BooleanValue addDependencies(final Property<?>... dependencies);

	@Override
	BooleanValue removeDependencies(final Property<?>... dependencies);

	/**
	 * @return !this
	 */
	@Api
	BooleanValue negate();
}
