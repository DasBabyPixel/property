package de.dasbabypixel.api.property;

public interface LongStorage {
	long read();

	void write(long value);

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
	}
}
