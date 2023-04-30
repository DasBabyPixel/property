package de.dasbabypixel.api.property;

public interface DoubleStorage {
    double read();

    void write(double value);

    boolean writable();

    boolean checkForChanges();

    interface External extends DoubleStorage {
        @Override
        default boolean checkForChanges() {
            return true;
        }
    }

    interface ReadOnlyExternal extends External {
        @Override
        default boolean writable() {
            return false;
        }

        @Override
        default void write(double value) {
            throw new UnsupportedOperationException();
        }
    }

    class Simple implements DoubleStorage {
        private double value;

        @Override
        public double read() {
            return value;
        }

        @Override
        public void write(double value) {
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
