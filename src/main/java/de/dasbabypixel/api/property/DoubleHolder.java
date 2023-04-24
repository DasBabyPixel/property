package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;
import de.dasbabypixel.api.property.DoubleStorage.Simple;

@Api
public final class DoubleHolder extends AbstractNumberHolder {
	private final DoubleHolder partner;
	private final DoubleStorage storage;

	public DoubleHolder() {
		this(0);
	}

	DoubleHolder(DoubleStorage storage) {
		this.storage = storage;
		this.partner = new DoubleHolder(this, storage);
	}

	DoubleHolder(DoubleHolder partner, DoubleStorage storage) {
		this.partner = partner;
		this.storage = storage;
	}

	DoubleHolder(DoubleHolder partner) {
		this.partner = partner;
		this.storage = new Simple();
		write(partner.doubleValue());
	}

	public DoubleHolder(double number) {
		this.storage = new Simple();
		write(number);
		this.partner = new DoubleHolder(this);
	}

	private void write(double number) {
		storage.write(number);
	}

	private double read() {
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
		return (float) read();
	}

	@Override
	public double doubleValue() {
		return read();
	}

	@Override
	public int hashCode() {
		return Double.hashCode(read());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DoubleHolder that = (DoubleHolder) o;
		return Double.compare(that.doubleValue(), this.doubleValue()) == 0;
	}

	@Override
	public String toString() {
		return Double.toString(doubleValue());
	}

	@Override
	DoubleHolder partner() {
		return partner;
	}

	@Override
	void pollFromPartner() {
		write(partner.doubleValue());
	}

	@Override
	public void set(Number number) {
		write(number.doubleValue());
	}

	@Override
	public void set(double number) {
		write(number);
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
