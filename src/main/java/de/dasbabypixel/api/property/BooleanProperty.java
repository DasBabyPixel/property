package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

class BooleanProperty extends AbstractProperty<Boolean> implements BooleanValue {

	public BooleanProperty(Storage<Boolean> storage) {
		super(storage);
	}

	public static BooleanValue withValue(boolean value) {
		return new BooleanProperty(new BasicStorage<>(value));
	}

	public static BooleanValue withBooleanStorage(Storage<Boolean> storage) {
		return new BooleanProperty(storage);
	}

	public static BooleanValue trueProperty() {
		return withValue(true);
	}

	public static BooleanValue falseProperty() {
		return withValue(false);
	}

	@Api
	@Override
	public boolean booleanValue() {
		return value();
	}

	@Api
	@Override
	public BooleanValue and(Property<? extends Boolean> value) {
		return mapToBoolean(b -> b & value.value()).addDependencies(value);
	}

	@Api
	@Override
	public BooleanValue or(Property<? extends Boolean> value) {
		return mapToBoolean(b -> b | value.value()).addDependencies(value);
	}

	@Api
	@Override
	public BooleanValue xor(Property<? extends Boolean> value) {
		return mapToBoolean(b -> b ^ value.value()).addDependencies(value);
	}

	@Api
	@Override
	public BooleanValue xor(boolean value) {
		return mapToBoolean(b -> b ^ value);
	}

	@Api
	@Override
	public BooleanValue negate() {
		return mapToBoolean(b -> !b);
	}

	@Override
	public BooleanProperty addDependencies(Property<?>... dependencies) {
		super.addDependencies(dependencies);
		return this;
	}

	@Override
	public BooleanProperty removeDependencies(Property<?>... dependencies) {
		super.removeDependencies(dependencies);
		return this;
	}

	@Api
	@Override
	public BooleanValue mapToBoolean() {
		return this;
	}
}
