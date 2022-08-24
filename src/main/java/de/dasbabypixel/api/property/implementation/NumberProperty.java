package de.dasbabypixel.api.property.implementation;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import de.dasbabypixel.api.property.NumberChangeListener;
import de.dasbabypixel.api.property.NumberInvalidationListener;
import de.dasbabypixel.api.property.NumberValue;
import de.dasbabypixel.api.property.Property;
import de.dasbabypixel.api.property.Storage;

@SuppressWarnings("javadoc")
public class NumberProperty extends ObjectProperty<Number> implements NumberValue {

	protected final Collection<NumberInvalidationListener> numberInvalidationListeners;

	protected final Collection<NumberChangeListener> numberChangeListeners;

	public static NumberProperty withNumber(final Number value) {
		final NumberProperty prop = new NumberProperty();
		prop.setNumber(value);
		return prop;
	}

	public static NumberProperty withNumberStorage(final Storage<Number> storage) {
		final NumberProperty prop = new NumberProperty(storage);
		prop.getNumber();
		return prop;
	}

	public static NumberProperty constant(final Number number) {
		final NumberProperty prop = new NumberProperty();
		prop.setNumber(number);
		prop.computor.set(true);
		return prop;
	}

	@Override
	protected void fireChangeListeners(final Number oldValue, final Number newValue) {
		this.numberChangeListeners.forEach(n -> n.handleChange(this, oldValue, newValue));
		super.fireChangeListeners(oldValue, newValue);
	}

	@Override
	protected void fireInvalidationListeners() {
		this.numberInvalidationListeners.forEach(n -> n.invalidated(this));
		super.fireInvalidationListeners();
	}

	public static NumberProperty zero() {
		return withNumber(0);
	}

	protected NumberProperty() {
		this.numberInvalidationListeners = ConcurrentHashMap.newKeySet();
		this.numberChangeListeners = ConcurrentHashMap.newKeySet();
	}

	protected NumberProperty(final Storage<Number> storage) {
		super(storage);
		this.numberInvalidationListeners = ConcurrentHashMap.newKeySet();
		this.numberChangeListeners = ConcurrentHashMap.newKeySet();
	}

	@Override
	public NumberValue negate() {
		return this.mapToNumber(n -> -n.doubleValue());
	}

	@Override
	public NumberValue multiply(final Property<Number> other) {
		return this.mapToNumber(n -> n.doubleValue() * other.getValue().doubleValue())
				.addDependencies(other)
				.mapToNumber();
	}

	@Override
	public NumberValue divide(final Property<Number> other) {
		return this.mapToNumber(n -> n.doubleValue() / other.getValue().doubleValue())
				.addDependencies(other)
				.mapToNumber();
	}

	@Override
	public NumberValue add(final Property<Number> other) {
		return this.mapToNumber(n -> n.doubleValue() + other.getValue().doubleValue())
				.addDependencies(other)
				.mapToNumber();
	}

	@Override
	public NumberValue subtract(final Property<Number> other) {
		return this.mapToNumber(n -> n.doubleValue() - other.getValue().doubleValue())
				.addDependencies(other)
				.mapToNumber();
	}

	@Override
	public NumberValue multiply(final Number other) {
		return this.mapToNumber(n -> n.doubleValue() * other.doubleValue());
	}

	@Override
	public NumberValue divide(final Number other) {
		return this.mapToNumber(n -> n.doubleValue() / other.doubleValue());
	}

	@Override
	public NumberValue add(final Number other) {
		return this.mapToNumber(n -> n.doubleValue() + other.doubleValue());
	}

	@Override
	public NumberValue subtract(final Number other) {
		return this.mapToNumber(n -> n.doubleValue() - other.doubleValue());
	}

	@Override
	public NumberValue max(final Number other) {
		return this.mapToNumber(n -> Math.max(n.doubleValue(), other.doubleValue()));
	}

	@Override
	public NumberValue min(final Number other) {
		return this.mapToNumber(n -> Math.min(n.doubleValue(), other.doubleValue()));
	}

	@Override
	public NumberValue max(final Property<Number> value) {
		return this.mapToNumber(n -> Math.max(n.doubleValue(), value.getValue().doubleValue()))
				.addDependencies(value)
				.mapToNumber();
	}

	@Override
	public NumberValue min(final Property<Number> value) {
		return this.mapToNumber(n -> Math.min(n.doubleValue(), value.getValue().doubleValue()))
				.addDependencies(value)
				.mapToNumber();
	}

	@Override
	public void addListener(final NumberInvalidationListener listener) {
		this.numberInvalidationListeners.add(listener);
	}

	@Override
	public void removeListener(final NumberInvalidationListener listener) {
		this.numberInvalidationListeners.remove(listener);
	}

	@Override
	public void addListener(final NumberChangeListener listener) {
		this.numberChangeListeners.add(listener);
	}

	@Override
	public void removeListener(final NumberChangeListener listener) {
		this.numberChangeListeners.remove(listener);
	}

	@Override
	public Number getNumber() {
		return this.getValue();
	}

	@Override
	public void setNumber(final Number number) {
		this.setValue(number);
	}

}
