package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

@Api
public final class DoubleHolder extends AbstractNumberHolder {

    private final DoubleHolder partner;
    private final DoubleStorage storage;
    private double current;

    DoubleHolder() {
        this(0);
    }

    DoubleHolder(double number) {
        this.storage = new DoubleStorage.Simple();
        write(number);
        this.partner = new DoubleHolder(this);
    }

    DoubleHolder(DoubleStorage storage) {
        this.storage = storage;
        this.partner = new DoubleHolder(this, storage);
    }

    private DoubleHolder(DoubleHolder partner, DoubleStorage storage) {
        this.partner = partner;
        this.storage = storage;
    }


    private DoubleHolder(DoubleHolder partner) {
        this.partner = partner;
        this.storage = new DoubleStorage.Simple();
        write(partner.doubleValue());
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

    private void write(double number) {
        storage.write(number);
        current = storage.read();
    }

    private double get() {
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
        return (float) get();
    }

    @Override
    public double doubleValue() {
        return get();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(get());
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
        if (storage.writable()) write(partner.doubleValue());
        else current = partner.get();
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

    @Override
    public boolean writable() {
        return storage.writable();
    }
}
