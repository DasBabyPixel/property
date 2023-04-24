package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;
import de.dasbabypixel.api.property.NumberUtils.HolderPair;
import de.dasbabypixel.api.property.NumberUtils.NHAlgorithm;
import de.dasbabypixel.api.property.NumberUtils.NNHAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class NumberProperty extends AbstractProperty<AbstractNumberHolder> implements NumberValue {
	private final NEvents nevents = new NEvents();

	private NumberProperty(AbstractNumberHolder value) {
		super(null);
		this.value = value;
		this.events = nevents;
	}

	private NumberProperty(Storage<AbstractNumberHolder> storage) {
		super(storage);
		this.events = nevents;
	}

	public static NumberProperty zero() {
		return withValue(0);
	}

	public static NumberProperty withValue(Number number) {
		return new NumberProperty(NumberUtils.createHolder(number));
	}

	public static NumberProperty constant(Number number) {
		return new NumberProperty(new ConstantStorage<>(NumberUtils.createHolder(number)));
	}

	public static NumberProperty withStorage(Storage<Number> storage) {
		return new NumberProperty(new NumberStorageHolder(storage));
	}

	public static NumberProperty computing(FloatSupplier supplier) {
		return new NumberProperty(new FloatHolder(new FloatStorage() {
			@Override
			public float read() {
				return supplier.get();
			}

			@Override
			public void write(float value) {
				throw new ComputerException();
			}
		}));
	}

	public static NumberProperty computing(DoubleSupplier supplier) {
		return new NumberProperty(new DoubleHolder(new DoubleStorage() {
			@Override
			public double read() {
				return supplier.get();
			}

			@Override
			public void write(double value) {
				throw new ComputerException();
			}
		}));
	}

	public static NumberProperty computing(IntegerSupplier supplier) {
		return new NumberProperty(new IntegerHolder(new IntegerStorage() {
			@Override
			public int read() {
				return supplier.get();
			}

			@Override
			public void write(int value) {
				throw new ComputerException();
			}
		}));
	}

	public static NumberProperty computing(LongSupplier supplier) {
		return new NumberProperty(new LongHolder(new LongStorage() {
			@Override
			public long read() {
				return supplier.get();
			}

			@Override
			public void write(long value) {
				throw new ComputerException();
			}
		}));
	}

	@Api
	@Override
	protected boolean equals(Object o1, Object o2) {
		return o1 == o2;
	}

	@Api
	@Override
	public NumberProperty addDependencies(Property<?>... dependencies) {
		super.addDependencies(dependencies);
		return this;
	}

	@Api
	@Override
	public NumberProperty removeDependencies(Property<?>... dependencies) {
		super.removeDependencies(dependencies);
		return this;
	}

	@Api
	@Override
	public void atomicOperation(Function<AbstractNumberHolder, AbstractNumberHolder> operation) {
		boolean statusChanged = false;
		try {
			lock.writeLock().lock();
			boolean bidirectional = boundTo != null && boundTo.boundTo == this;
			if (boundTo != null && !bidirectional)
				throw new IllegalStateException("This property is bound and a number can not be set");
			AbstractNumberHolder newVal = value.partner();
			newVal.pollFromPartner();
			AbstractNumberHolder ret = operation.apply(newVal);
			if (!equals(newVal, ret))
				throw new UnsupportedOperationException("You did not return the same AbstractNumberHolder you were given!");
			if (equals(value, ret))
				return; // Did not modify anything
			if (updateStatus == 1)
				return;
			else if (updateStatus != 0)
				throw new IllegalStateException();
			statusChanged = true;
			updateStatus = 1;
			AbstractNumberHolder oldVal = value;
			value = newVal;
			valid = true;
			events.invalidate();
			events.change(oldVal, newVal);
		} finally {
			if (statusChanged)
				updateStatus = 0;
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void value(AbstractNumberHolder value) {
		throw new UnsupportedOperationException();
	}

	@Api
	@Override
	public Storage<AbstractNumberHolder> storage() {
		throw new UnsupportedOperationException();
	}

	@Api
	@Override
	public BooleanValue mapToBoolean() {
		throw new UnsupportedOperationException();
	}

	@Api
	@Override
	public NumberValue mapToNumber() {
		return this;
	}

	@Api
	@Override
	protected AbstractNumberHolder computeValue() {
		return value.partner();
	}

	@Api
	@Override
	public void addListener(NumberInvalidationListener listener) {
		try {
			lock.writeLock().lock();
			nevents.numberInvalidationListeners.add(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void removeListener(NumberInvalidationListener listener) {
		try {
			lock.writeLock().lock();
			nevents.numberInvalidationListeners.remove(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void addListener(NumberChangeListener listener) {
		try {
			lock.writeLock().lock();
			nevents.numberChangeListeners.add(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void removeListener(NumberChangeListener listener) {
		try {
			lock.writeLock().lock();
			nevents.numberChangeListeners.remove(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public AbstractNumberHolder numberHolder() {
		return value();
	}

	@Api
	@Override
	public void number(Number number) {
		boolean statusChanged = false;
		try {
			lock.writeLock().lock();
			boolean bidirectional = boundTo != null && boundTo.boundTo == this;
			if (boundTo != null && !bidirectional)
				throw new IllegalStateException("This property is bound and a number can not be set");
			if (updateStatus == 1)
				return;
			else if (updateStatus != 0)
				throw new IllegalStateException();
			statusChanged = true;
			updateStatus = 1;
			AbstractNumberHolder old = this.value;
			AbstractNumberHolder newVal = old.partner();
			newVal.set(number);
			if (equals(old, newVal))
				return;
			this.value = newVal;
			valid = true;
			events.invalidate();
			events.change(old, this.value);
		} finally {
			if (statusChanged)
				updateStatus = 0;
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void number(double number) {
		boolean statusChanged = false;
		try {
			lock.writeLock().lock();
			boolean bidirectional = boundTo != null && boundTo.boundTo == this;
			if (boundTo != null && !bidirectional)
				throw new IllegalStateException("This property is bound and a number can not be set");
			if (updateStatus == 1)
				return;
			else if (updateStatus != 0)
				throw new IllegalStateException();
			statusChanged = true;
			updateStatus = 1;
			AbstractNumberHolder old = this.value;
			AbstractNumberHolder newVal = old.partner();
			newVal.set(number);
			if (equals(old, newVal))
				return;
			this.value = newVal;
			valid = true;
			events.invalidate();
			events.change(old, this.value);
		} finally {
			if (statusChanged)
				updateStatus = 0;
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void number(long number) {
		boolean statusChanged = false;
		try {
			lock.writeLock().lock();
			boolean bidirectional = boundTo != null && boundTo.boundTo == this;
			if (boundTo != null && !bidirectional)
				throw new IllegalStateException("This property is bound and a number can not be set");
			if (updateStatus == 1)
				return;
			else if (updateStatus != 0)
				throw new IllegalStateException();
			statusChanged = true;
			updateStatus = 1;
			AbstractNumberHolder old = this.value;
			AbstractNumberHolder newVal = old.partner();
			newVal.set(number);
			if (equals(old, newVal))
				return;
			this.value = newVal;
			valid = true;
			events.invalidate();
			events.change(old, this.value);
		} finally {
			if (statusChanged)
				updateStatus = 0;
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void number(float number) {
		boolean statusChanged = false;
		try {
			lock.writeLock().lock();
			boolean bidirectional = boundTo != null && boundTo.boundTo == this;
			if (boundTo != null && !bidirectional)
				throw new IllegalStateException("This property is bound and a number can not be set");
			if (updateStatus == 1)
				return;
			else if (updateStatus != 0)
				throw new IllegalStateException();
			statusChanged = true;
			updateStatus = 1;
			AbstractNumberHolder old = this.value;
			AbstractNumberHolder newVal = old.partner();
			newVal.set(number);
			if (equals(old, newVal))
				return;
			this.value = newVal;
			valid = true;
			events.invalidate();
			events.change(old, this.value);
		} finally {
			if (statusChanged)
				updateStatus = 0;
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public void number(int number) {
		boolean statusChanged = false;
		try {
			lock.writeLock().lock();
			boolean bidirectional = boundTo != null && boundTo.boundTo == this;
			if (boundTo != null && !bidirectional)
				throw new IllegalStateException("This property is bound and a number can not be set");
			if (updateStatus == 1)
				return;
			else if (updateStatus != 0)
				throw new IllegalStateException();
			statusChanged = true;
			updateStatus = 1;
			AbstractNumberHolder old = this.value;
			AbstractNumberHolder newVal = old.partner();
			newVal.set(number);
			if (equals(old, newVal))
				return;
			this.value = newVal;
			valid = true;
			events.invalidate();
			events.change(old, this.value);
		} finally {
			if (statusChanged)
				updateStatus = 0;
			lock.writeLock().unlock();
		}
	}

	@Api
	@Override
	public NumberValue negate() {
		return mapToNumberNH(NumberUtils.negate(this.value()));
	}

	@Api
	@Override
	public NumberValue multiply(Property<? extends Number> value) {
		return mapToNumberNNH(NumberUtils.multiply(this.value(), value.value()), value);
	}

	@Api
	@Override
	public NumberValue divide(Property<? extends Number> value) {
		return mapToNumberNNH(NumberUtils.divide(this.value(), value.value()), value);
	}

	@Api
	@Override
	public NumberValue add(Property<? extends Number> value) {
		return mapToNumberNNH(NumberUtils.add(this.value(), value.value()), value);
	}

	@Api
	@Override
	public NumberValue subtract(Property<? extends Number> value) {
		return mapToNumberNNH(NumberUtils.subtract(this.value(), value.value()), value);
	}

	@Api
	@Override
	public NumberValue max(Property<? extends Number> value) {
		return mapToNumberNNH(NumberUtils.max(this.value(), value.value()), value);
	}

	@Api
	@Override
	public NumberValue min(Property<? extends Number> value) {
		return mapToNumberNNH(NumberUtils.min(this.value(), value.value()), value);
	}

	@Api
	@Override
	public NumberValue pow(Property<? extends Number> value) {
		return mapToNumberNNH(NumberUtils.pow(this.value(), value.value()), value);
	}

	@Api
	@Override
	public NumberValue multiply(Number number) {
		return mapToNumberNNH(NumberUtils.multiply(this.value(), number), number);
	}

	@Api
	@Override
	public NumberValue divide(Number number) {
		return mapToNumberNNH(NumberUtils.divide(this.value(), number), number);
	}

	@Api
	@Override
	public NumberValue add(Number number) {
		return mapToNumberNNH(NumberUtils.add(this.value(), number), number);
	}

	@Api
	@Override
	public NumberValue subtract(Number number) {
		return mapToNumberNNH(NumberUtils.subtract(this.value(), number), number);
	}

	@Api
	@Override
	public NumberValue max(Number number) {
		return mapToNumberNNH(NumberUtils.max(this.value(), number), number);
	}

	@Api
	@Override
	public NumberValue min(Number number) {
		return mapToNumberNNH(NumberUtils.min(this.value(), number), number);
	}

	@Api
	@Override
	public NumberValue pow(Number value) {
		return mapToNumberNNH(NumberUtils.pow(this.value(), value), value);
	}

	private NumberValue mapToNumberNNH(HolderPair<NNHAlgorithm> pair, Property<? extends Number> value) {
		return mapToNumber((NumberMapFunction<AbstractNumberHolder>) val -> {
			pair.algorithm().calculate(value(), value.value(), pair.holder());
			return pair.holder();
		});
	}

	private NumberValue mapToNumberNNH(HolderPair<NNHAlgorithm> pair, Number value) {
		return mapToNumber((NumberMapFunction<AbstractNumberHolder>) val -> {
			pair.algorithm().calculate(value(), value, pair.holder());
			return pair.holder();
		});
	}

	private NumberValue mapToNumberNH(HolderPair<NHAlgorithm> pair) {
		return mapToNumber((NumberMapFunction<AbstractNumberHolder>) val -> {
			pair.algorithm().calculate(value(), pair.holder());
			return pair.holder();
		});
	}

	private class NEvents extends Events<AbstractNumberHolder> {
		private final List<NumberInvalidationListener> numberInvalidationListeners = new ArrayList<>();
		private final List<NumberChangeListener> numberChangeListeners = new ArrayList<>();

		public NEvents() {
			super(NumberProperty.this);
		}

		@Override
		public void invalidate() {
			super.invalidate();
			for (int i = 0; i < numberInvalidationListeners.size(); i++)
				numberInvalidationListeners.get(i).invalidated(NumberProperty.this);
		}

		@Override
		public void change(AbstractNumberHolder oldValue, AbstractNumberHolder newValue) {
			super.change(oldValue, newValue);
			for (int i = 0; i < numberChangeListeners.size(); i++)
				numberChangeListeners.get(i).handleChange(NumberProperty.this, oldValue, newValue);
		}
	}
}
