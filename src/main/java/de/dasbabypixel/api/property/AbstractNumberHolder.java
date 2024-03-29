package de.dasbabypixel.api.property;

import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class AbstractNumberHolder extends Number implements StorageCapable {

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract String toString();

    @Internal
    AbstractNumberHolder partner() {
        return null;
    }

    @Override
    public boolean checkForChanges() {
        return false;
    }

    @Internal
    void pollFromStorage() {
    }

    @Internal
    void pollFromPartner() {
    }

    public abstract void set(Number number);

    public abstract void set(double number);

    public abstract void set(float number);

    public abstract void set(long number);

    public abstract void set(int number);
}
