package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

public class LongHolder extends AbstractNumberHolder {
    private final LongHolder partner;
    private final LongStorage storage;
    private long current;

    LongHolder() {
        this(0);
    }

    LongHolder(long number) {
        this.storage = new LongStorage.Simple();
        write(number);
        this.partner = new LongHolder(this);
    }

    LongHolder(LongStorage storage) {
        this.storage = storage;
        this.partner = new LongHolder(this, storage);
    }


    private LongHolder(LongHolder partner, LongStorage storage) {
        this.partner = partner;
        this.storage = storage;
    }

    private LongHolder(LongHolder partner) {
        this.partner = partner;
        this.storage = new LongStorage.Simple();
        write(partner.longValue());
    }

    private void write(long number) {
        storage.write(number);
        current = storage.read();
    }

    private long get() {
        if (storage.checkForChanges()) return current = storage.read();
        return current;
    }

    @Override
    public int intValue() {
        return (int) get();
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
        return Long.hashCode(get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
        if (storage.writable()) write(partner.longValue());
        else current = partner.get();
    }

    @Override
    public void set(Number number) {
        write(number.longValue());
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

    @Override
    public boolean writable() {
        return storage.writable();
    }
}
