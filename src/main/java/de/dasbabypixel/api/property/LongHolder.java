package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;
import de.dasbabypixel.api.property.LongStorage.Simple;

public class LongHolder extends AbstractNumberHolder {
	private final LongHolder partner;
	private final LongStorage storage;

	public LongHolder() {
		this(0);
	}

	LongHolder(LongStorage storage) {
		this.storage = storage;
		this.partner = new LongHolder(this, storage);
	}

	LongHolder(LongHolder partner, LongStorage storage) {
		this.partner = partner;
		this.storage = storage;
	}

	LongHolder(LongHolder partner) {
		this.partner = partner;
		this.storage = new Simple();
		write(partner.longValue());
	}

	public LongHolder(long number) {
		this.storage = new Simple();
		write(number);
		this.partner = new LongHolder(this);
	}

	protected void write(long number) {
		storage.write(number);
	}

	protected long read() {
		return storage.read();
	}

	@Override
	public int intValue() {
		return (int) read();
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
		return Long.hashCode(read());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		LongHolder that = (LongHolder) o;
		return that.longValue() == longValue();
	}

	@Override
	public String toString() {
		return Long.toString(longValue());
	}

	@Override
	LongHolder partner() {
		return partner;
	}

	@Override
	void pollFromPartner() {
		write(partner.longValue());
	}

	@Override
	public void set(Number number) {
		write(number.intValue());
	}

	@Override
	public void set(double number) {
		write((long) number);
	}

	@Override
	public void set(float number) {
		write((long) number);
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
