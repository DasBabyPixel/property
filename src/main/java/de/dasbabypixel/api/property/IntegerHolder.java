package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;
import de.dasbabypixel.api.property.IntegerStorage.Simple;

public class IntegerHolder extends AbstractNumberHolder {
	private final IntegerHolder partner;
	private final IntegerStorage storage;

	public IntegerHolder() {
		this(0);
	}

	IntegerHolder(IntegerStorage storage) {
		this.storage = storage;
		this.partner = new IntegerHolder(this, storage);
	}

	IntegerHolder(IntegerHolder partner, IntegerStorage storage) {
		this.partner = partner;
		this.storage = storage;
	}

	IntegerHolder(IntegerHolder partner) {
		this.partner = partner;
		this.storage = new Simple();
		write(partner.intValue());
	}

	public IntegerHolder(int number) {
		this.storage = new Simple();
		write(number);
		this.partner = new IntegerHolder(this);
	}

	protected void write(int number) {
		storage.write(number);
	}

	protected int read() {
		return storage.read();
	}

	@Override
	public int intValue() {
		return read();
	}

	@Override
	public long longValue() {
		return read();
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
		return Integer.hashCode(read());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		IntegerHolder that = (IntegerHolder) o;
		return that.intValue() == intValue();
	}

	@Override
	public String toString() {
		return Integer.toString(intValue());
	}

	@Override
	IntegerHolder partner() {
		return partner;
	}

	@Override
	void pollFromPartner() {
		write(partner.intValue());
	}

	@Override
	public void set(Number number) {
		write(number.intValue());
	}

	@Override
	public void set(double number) {
		write((int) number);
	}

	@Override
	public void set(float number) {
		write((int) number);
	}

	@Override
	public void set(long number) {
		write((int) number);
	}

	@Api
	public void set(int number) {
		write(number);
	}
}
