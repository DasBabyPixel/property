package de.dasbabypixel.api.property;

public interface DoubleStorage {
	double read();

	void write(double value);

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
	}
}
