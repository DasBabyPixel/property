package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;
import de.dasbabypixel.api.property.IntegerStorage.Simple;

public class IntegerHolder extends AbstractNumberHolder {
    private final IntegerHolder partner;
    private final IntegerStorage storage;
    private int current;

    IntegerHolder() {
        this(0);
    }

    IntegerHolder(int number) {
        this.storage = new Simple();
        write(number);
        this.partner = new IntegerHolder(this);
    }

    IntegerHolder(IntegerStorage storage) {
        this.storage = storage;
        this.partner = new IntegerHolder(this, storage);
    }


    private IntegerHolder(IntegerHolder partner, IntegerStorage storage) {
        this.partner = partner;
        this.storage = storage;
    }

    private IntegerHolder(IntegerHolder partner) {
        this.partner = partner;
        this.storage = new Simple();
        write(partner.intValue());
    }

    private void write(int number) {
        storage.write(number);
        current = storage.read();
    }

    private int get() {
        if (storage.checkForChanges()) return current = storage.read();
        return current;
    }

    @Override
    public int intValue() {
        return get();
    }

    @Override
    public long longValue() {
        return get();
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
        return Integer.hashCode(get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
        if (storage.writable()) write(partner.intValue());
        else current = partner.get();
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

    @Override
    public boolean writable() {
        return storage.writable();
    }
}
