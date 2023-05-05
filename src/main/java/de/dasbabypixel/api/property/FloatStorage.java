package de.dasbabypixel.api.property;

public interface FloatStorage {
	float read();

	void write(float value);

	boolean writable();

	boolean checkForChanges();

	interface External extends FloatStorage {
		@Override
		default boolean checkForChanges() {
			return true;
		}
	}

	interface ReadOnlyExternal extends FloatStorage.External {
		@Override
		default boolean writable() {
			return false;
		}

		@Override
		default void write(float value) {
			throw new UnsupportedOperationException();
		}
	}
	class Simple implements FloatStorage {
		private float value;

		@Override
		public float read() {
			return value;
		}

		@Override
		public void write(float value) {
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
