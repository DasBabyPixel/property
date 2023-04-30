package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

public class FloatHolder extends AbstractNumberHolder {
    private final FloatHolder partner;
    private final FloatStorage storage;
    private float current;

    FloatHolder() {
        this(0);
    }

    FloatHolder(float number) {
        this.storage = new FloatStorage.Simple();
        write(number);
        this.partner = new FloatHolder(this);
    }

    FloatHolder(FloatStorage storage) {
        this.storage = storage;
        this.partner = new FloatHolder(this, storage);
    }


    private FloatHolder(FloatHolder partner, FloatStorage storage) {
        this.partner = partner;
        this.storage = storage;
    }

    private FloatHolder(FloatHolder partner) {
        this.partner = partner;
        this.storage = new FloatStorage.Simple();
        write(partner.floatValue());
    }

    private void write(float number) {
        storage.write(number);
        current = storage.read();
    }

    private float get() {
        if (storage.checkForChanges()) return current = storage.read();
        return current;
    }

    @Override
    public int intValue() {
        return (int) get();
    }

    @Override
    public long longValue() {
        return (long) get();
    }

    @Override
    public float floatValue() {
        return get();
    }

    @Override
    public double doubleValue() {
        return get();
    }

    @Override
    public int hashCode() {
        return Float.hashCode(get());
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
        if (storage.writable()) write(partner.floatValue());
        else current = partner.get();
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

    @Override
    public boolean writable() {
        return storage.writable();
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
}
