package de.dasbabypixel.api.property.implementation;

import de.dasbabypixel.api.property.BooleanValue;
import de.dasbabypixel.api.property.Storage;

public class BooleanProperty extends ObjectProperty<Boolean> implements BooleanValue {

	protected BooleanProperty() {
	}

	protected BooleanProperty(Storage<Boolean> storage) {
		super(storage);
	}

	@Override
	public boolean booleanValue() {
		return getValue().booleanValue();
	}

	public static BooleanProperty falseProperty() {
		return withValue(false);
	}

	public static BooleanProperty trueProperty() {
		return withValue(true);
	}

	public static BooleanProperty withBooleanStorage(Storage<Boolean> storage) {
		BooleanProperty prop = new BooleanProperty(storage);
		prop.booleanValue();
		return prop;
	}

	public static BooleanProperty withValue(boolean value) {
		BooleanProperty p = new BooleanProperty();
		p.setValue(value);
		return p;
	}

	@Override
	public BooleanValue negate() {
		return mapToBoolean(b -> !b);
	}

	@Override
	public BooleanValue and(BooleanValue other) {
		return mapToBoolean(b -> b && other.booleanValue()).addDependencies(other).mapToBoolean();
	}

	@Override
	public BooleanValue or(BooleanValue other) {
		return mapToBoolean(b -> b || other.booleanValue()).addDependencies(other).mapToBoolean();
	}
}
