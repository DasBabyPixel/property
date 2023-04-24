package de.dasbabypixel.api.property;

public interface IntegerStorage {
	int read();

	void write(int value);

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
	}
}
