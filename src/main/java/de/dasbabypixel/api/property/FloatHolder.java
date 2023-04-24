package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;
import de.dasbabypixel.api.property.FloatStorage.Simple;

public class FloatHolder extends AbstractNumberHolder {
	private final FloatHolder partner;
	private final FloatStorage storage;

	public FloatHolder() {
		this(0);
	}

	FloatHolder(FloatStorage storage) {
		this.storage = storage;
		this.partner = new FloatHolder(this, storage);
	}

	FloatHolder(FloatHolder partner, FloatStorage storage) {
		this.partner = partner;
		this.storage = storage;
	}

	FloatHolder(FloatHolder partner) {
		this.partner = partner;
		this.storage = new Simple();
		write(partner.floatValue());
	}

	public FloatHolder(float number) {
		this.storage = new Simple();
		write(number);
		this.partner = new FloatHolder(this);
	}

	protected void write(float number) {
		storage.write(number);
	}

	protected float read() {
		return storage.read();
	}

	@Override
	public int intValue() {
		return (int) read();
	}

	@Override
	public long longValue() {
		return (long) read();
	}

	@Override
	public float floatValue() {
		return read();
	}

	@Override
	public double doubleValue() {
		return read();
	}

	@Override
	public int hashCode() {
		return Float.hashCode(read());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		FloatHolder that = (FloatHolder) o;
		return Float.compare(that.floatValue(), this.floatValue()) == 0;
	}

	@Override
	public String toString() {
		return Float.toString(floatValue());
	}

	@Override
	FloatHolder partner() {
		return partner;
	}

	@Override
	void pollFromPartner() {
		write(partner.floatValue());
	}

	@Override
	public void set(Number number) {
		write(number.floatValue());
	}

	@Override
	public void set(double number) {
		write((float) number);
	}

	@Override
	public void set(float number) {
		write(number);
	}

	@Override
	public void set(long number) {
		write(number);
	}

	@Api
	public void set(int number) {
		write(number);
	}
}
