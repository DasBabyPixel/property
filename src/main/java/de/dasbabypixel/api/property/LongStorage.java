package de.dasbabypixel.api.property;

public interface LongStorage {
    long read();

    void write(long value);

    boolean writable();

    boolean checkForChanges();

    interface External extends LongStorage {
        @Override
        default boolean checkForChanges() {
            return true;
        }
    }

    interface ReadOnlyExternal extends LongStorage.External {
        @Override
        default boolean writable() {
            return false;
        }

        @Override
        default void write(long value) {
            throw new UnsupportedOperationException();
        }
    }

    class Simple implements LongStorage {
        private long value;

        @Override
        public long read() {
            return value;
        }

        @Override
        public void write(long value) {
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
