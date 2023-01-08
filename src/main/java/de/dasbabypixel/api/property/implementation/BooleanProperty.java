package de.dasbabypixel.api.property.implementation;

import de.dasbabypixel.api.property.BooleanValue;
import de.dasbabypixel.api.property.Storage;

/**
 * @author DasBabyPixel
 */
public class BooleanProperty extends ObjectProperty<Boolean> implements BooleanValue {
	protected BooleanProperty() {
	}

	protected BooleanProperty(final Storage<Boolean> storage) {
		super(storage);
	}

	@Override
	public boolean booleanValue() {
		return this.getValue();
	}

	/**
	 * @return property with initial value false
	 */
	public static BooleanProperty falseProperty() {
		return withValue(Boolean.FALSE);
	}

	/**
	 * @return property with initial value true
	 */
	public static BooleanProperty trueProperty() {
		return withValue(Boolean.TRUE);
	}

	/**
	 * @param storage the storage
	 *
	 * @return property with custom storage
	 */
	public static BooleanProperty withBooleanStorage(final Storage<Boolean> storage) {
		final BooleanProperty prop = new BooleanProperty(storage);
		prop.booleanValue();
		return prop;
	}

	/**
	 * @param value the initial value
	 *
	 * @return property with initial value
	 */
	public static BooleanProperty withValue(final Boolean value) {
		final BooleanProperty p = new BooleanProperty();
		p.setValue(value);
		return p;
	}

	@Override
	public BooleanValue negate() {
		return this.mapToBoolean(b -> !b);
	}

	@Override
	public BooleanValue and(final BooleanValue other) {
		return this.mapToBoolean(b -> b && other.booleanValue()).addDependencies(other)
				.mapToBoolean();
	}

	@Override
	public BooleanValue or(final BooleanValue other) {
		return this.mapToBoolean(b -> b || other.booleanValue()).addDependencies(other)
				.mapToBoolean();
	}
}
