package de.dasbabypixel.api.property;

public interface IntegerStorage {
    int read();

    void write(int value);

    boolean writable();

    boolean checkForChanges();

    interface External extends IntegerStorage {
        @Override
        default boolean checkForChanges() {
            return true;
        }
    }

    interface ReadOnlyExternal extends IntegerStorage.External {
        @Override
        default boolean writable() {
            return false;
        }

        @Override
        default void write(int value) {
            throw new UnsupportedOperationException();
        }
    }

    class Simple implements IntegerStorage {
        private int value;

        @Override
        public int read() {
            return value;
        }

        @Override
        public void write(int value) {
            this.value = value;
        }

        @Override
        public boolean writable() {
            return true;
        }

        @Override
        public boolean checkForChanges() {
            return false;
        }
    }
}
