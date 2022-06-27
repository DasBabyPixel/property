package de.dasbabypixel.api.property.implementation;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import de.dasbabypixel.api.property.NumberChangeListener;
import de.dasbabypixel.api.property.NumberInvalidationListener;
import de.dasbabypixel.api.property.NumberValue;
import de.dasbabypixel.api.property.Property;
import de.dasbabypixel.api.property.Storage;

public class NumberProperty extends ObjectProperty<Number> implements NumberValue {

	protected final Collection<NumberInvalidationListener> numberInvalidationListeners = ConcurrentHashMap.newKeySet();
	protected final Collection<NumberChangeListener> numberChangeListeners = ConcurrentHashMap.newKeySet();

	public static NumberProperty withNumber(Number value) {
		NumberProperty prop = new NumberProperty();
		prop.setNumber(value);
		return prop;
	}

	public static NumberProperty withNumberStorage(Storage<Number> storage) {
		NumberProperty prop = new NumberProperty(storage);
		prop.getNumber();
		return prop;
	}

	public static NumberProperty constant(Number number) {
		NumberProperty prop = new NumberProperty();
		prop.setNumber(number);
		prop.computor.set(true);
		return prop;
	}

	@Override
	protected void fireChangeListeners(Number oldValue, Number newValue) {
		numberChangeListeners.forEach(n -> n.handleChange(this, oldValue, newValue));
		super.fireChangeListeners(oldValue, newValue);
	}

	@Override
	protected void fireInvalidationListeners() {
		numberInvalidationListeners.forEach(n -> n.invalidated(this));
		super.fireInvalidationListeners();
	}

	public static NumberProperty zero() {
		return withNumber(0);
	}

	protected NumberProperty() {
		super();
	}

	protected NumberProperty(Storage<Number> storage) {
		super(storage);
	}

	@Override
	public NumberValue negate() {
		return mapToNumber(n -> -n.doubleValue());
	}

	@Override
	public NumberValue multiply(Property<Number> other) {
		return mapToNumber(n -> n.doubleValue() * other.getValue().doubleValue()).addDependencies(other).mapToNumber();
	}

	@Override
	public NumberValue divide(Property<Number> other) {
		return mapToNumber(n -> n.doubleValue() / other.getValue().doubleValue()).addDependencies(other).mapToNumber();
	}

	@Override
	public NumberValue add(Property<Number> other) {
		return mapToNumber(n -> n.doubleValue() + other.getValue().doubleValue()).addDependencies(other).mapToNumber();
	}

	@Override
	public NumberValue subtract(Property<Number> other) {
		return mapToNumber(n -> n.doubleValue() - other.getValue().doubleValue()).addDependencies(other).mapToNumber();
	}

	@Override
	public NumberValue multiply(Number other) {
		return mapToNumber(n -> n.doubleValue() * other.doubleValue());
	}

	@Override
	public NumberValue divide(Number other) {
		return mapToNumber(n -> n.doubleValue() / other.doubleValue());
	}

	@Override
	public NumberValue add(Number other) {
		return mapToNumber(n -> n.doubleValue() + other.doubleValue());
	}

	@Override
	public NumberValue subtract(Number other) {
		return mapToNumber(n -> n.doubleValue() - other.doubleValue());
	}

	@Override
	public void addListener(NumberInvalidationListener listener) {
		numberInvalidationListeners.add(listener);
	}

	@Override
	public void removeListener(NumberInvalidationListener listener) {
		numberInvalidationListeners.remove(listener);
	}

	@Override
	public void addListener(NumberChangeListener listener) {
		numberChangeListeners.add(listener);
	}

	@Override
	public void removeListener(NumberChangeListener listener) {
		numberChangeListeners.remove(listener);
	}

	@Override
	public Number getNumber() {
		return getValue();
	}

	@Override
	public void setNumber(Number number) {
		setValue(number);
	}

}
