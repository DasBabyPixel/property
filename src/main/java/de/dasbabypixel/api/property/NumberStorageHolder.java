package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

import java.util.Objects;

@Api
public class NumberStorageHolder extends AbstractNumberHolder {
    private final NumberStorageHolder partner;
    private final Storage<Number> storage;

    @Api
    public NumberStorageHolder(Storage<Number> storage) {
        this.storage = storage;
        this.partner = new NumberStorageHolder(this);
    }

    NumberStorageHolder(NumberStorageHolder partner) {
        this.partner = partner;
        this.storage = partner.storage;
    }

    @Api
    public Storage<Number> storage() {
        return storage;
    }

    @Override
    public int hashCode() {
        return storage.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NumberStorageHolder that = (NumberStorageHolder) o;
        return Objects.equals(storage, that.storage);
    }

    @Override
    public String toString() {
        return storage.read().toString();
    }

    @Override
    NumberStorageHolder partner() {
        return partner;
    }

    @Override
    public void set(Number number) {
        storage.write(number);
    }

    @Override
    public void set(double number) {
        storage.write(number);
    }

    @Override
    public void set(float number) {
        storage.write(number);
    }

    @Override
    public void set(long number) {
        storage.write(number);
    }

    @Override
    public void set(int number) {
        storage.write(number);
    }

    @Override
    public int intValue() {
        return storage.read().intValue();
    }

    @Override
    public long longValue() {
        return storage.read().longValue();
    }

    @Override
    public float floatValue() {
        return storage.read().floatValue();
    }

    @Override
    public double doubleValue() {
        return storage.read().doubleValue();
    }

    @Override
    public boolean writable() {
        return storage.writable();
    }
}
