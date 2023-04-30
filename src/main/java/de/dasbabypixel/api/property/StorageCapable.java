package de.dasbabypixel.api.property;

public interface StorageCapable {
    boolean writable();

    boolean checkForChanges();
}
