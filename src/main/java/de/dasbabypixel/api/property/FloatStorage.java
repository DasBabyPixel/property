package de.dasbabypixel.api.property;

public interface FloatStorage {
	float read();

	void write(float value);

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
	}
}
