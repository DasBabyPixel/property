package de.dasbabypixel.api.property;

import de.dasbabypixel.api.property.implementation.BooleanProperty;

public interface BooleanValue extends Property<Boolean> {

	boolean booleanValue();
	
	BooleanValue and(BooleanValue other);
	
	BooleanValue or(BooleanValue other);
	
	BooleanValue negate();
	
	static BooleanValue falseValue() {
		return BooleanProperty.falseProperty();
	}
	
	static BooleanValue trueValue() {
		return BooleanProperty.trueProperty();
	}
	
	static BooleanValue withStorage(Storage<Boolean> storage) {
		return BooleanProperty.withBooleanStorage(storage);
	}
	
	static BooleanValue withValue(boolean value) {
		return BooleanProperty.withValue(value);
	}
	
}
