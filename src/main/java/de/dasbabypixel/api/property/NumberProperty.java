package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;
import de.dasbabypixel.api.property.NumberUtils.HolderPair;
import de.dasbabypixel.api.property.NumberUtils.NHAlgorithm;
import de.dasbabypixel.api.property.NumberUtils.NNHAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

class NumberProperty extends AbstractProperty<AbstractNumberHolder> implements NumberValue {

    private final NEvents nevents = new NEvents();
    private boolean computer = false;

    private NumberProperty(AbstractNumberHolder value) {
        super(null);
        this.value = value;
        this.events = nevents;
    }

    public static NumberProperty withValue(Number number) {
        return new NumberProperty(NumberUtils.createHolder(number));
    }

    public static NumberProperty constant(Number number) {
        NumberProperty property = new NumberProperty(NumberUtils.createHolder(number));
        property.computer = true;
        return property;
    }

    public static NumberProperty withStorage(Storage<Number> storage) {
        return new NumberProperty(new NumberStorageHolder(storage));
    }

    public static NumberProperty withStorage(IntegerStorage storage) {
        return new NumberProperty(new IntegerHolder(storage));
    }

    public static NumberProperty withStorage(DoubleStorage storage) {
        return new NumberProperty(new DoubleHolder(storage));
    }

    public static NumberProperty withStorage(FloatStorage storage) {
        return new NumberProperty(new FloatHolder(storage));
    }

    public static NumberProperty withStorage(LongStorage storage) {
        return new NumberProperty(new LongHolder(storage));
    }

    public static NumberProperty computing(FloatSupplier supplier) {
        return withStorage((FloatStorage.ReadOnlyExternal) supplier::get);
    }

    public static NumberProperty computing(DoubleSupplier supplier) {
        return withStorage((DoubleStorage.ReadOnlyExternal) supplier::get);
    }

    public static NumberProperty computing(IntegerSupplier supplier) {
        return withStorage((IntegerStorage.ReadOnlyExternal) supplier::get);
    }

    public static NumberProperty computing(LongSupplier supplier) {
        return withStorage((LongStorage.ReadOnlyExternal) supplier::get);
    }

    @Api
    @Override
    protected boolean equals(Object o1, Object o2) {
        return o1 == o2 || o1.equals(o2);
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
            if (!value.writable() || computer)
                throw new UnsupportedOperationException("Cannot write to this property because it's storage does not support writing!");
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

    @Override
    public AbstractNumberHolder value() {
        if (!valid || value.checkForChanges()) {
            while (true) {
                try {
                    lock.writeLock().lock();
                    bound:
                    if (boundTo != null) {
                        if (!boundTo.lock.writeLock().tryLock()) continue;
                        try {
                            if (boundTo.boundTo == this)
                                break bound;
                            AbstractNumberHolder newValue = value.partner();
                            newValue.set(boundTo.value());
                            AbstractNumberHolder oldValue = value;
                            if (!equals(oldValue, newValue)) {
                                value = newValue;
                                if (valid) events.invalidate();
                                events.change(oldValue, newValue);
                            }
                            valid = true;
                            return value;
                        } finally {
                            boundTo.lock.writeLock().unlock();
                        }
                    }
                    AbstractNumberHolder oldValue = value;
                    value = computeValue();
                    if (!equals(oldValue, value)) {
                        if (valid) events.invalidate();
                        events.change(oldValue, value);
                    }
                    valid = true;
                } finally {
                    lock.writeLock().unlock();
                }
                break;
            }
        }
        return value;
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

    @SuppressWarnings("deprecation")
    @Api
    @Override
    @Deprecated
    public NumberValue mapToNumber() {
        return this;
    }

    @Api
    @Override
    protected AbstractNumberHolder computeValue() {
        value.partner().pollFromPartner();
        value.partner().pollFromStorage();
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
            if (!value.writable() || computer)
                throw new UnsupportedOperationException("Cannot write to this property because it's storage does not support writing!");
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
            if (!value.writable() || computer)
                throw new UnsupportedOperationException("Cannot write to this property because it's storage does not support writing!");
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
            if (!value.writable() || computer)
                throw new UnsupportedOperationException("Cannot write to this property because it's storage does not support writing!");
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
            if (!value.writable() || computer)
                throw new UnsupportedOperationException("Cannot write to this property because it's storage does not support writing!");
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
            if (!value.writable() || computer)
                throw new UnsupportedOperationException("Cannot write to this property because it's storage does not support writing!");
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
        return calculate(val -> pair.algorithm().calculate(value(), value.value(), val), pair.holder()).addDependencies(value);
    }

    private NumberValue mapToNumberNNH(HolderPair<NNHAlgorithm> pair, Number value) {
        return calculate(val -> pair.algorithm().calculate(value(), value, val), pair.holder());
    }

    private NumberValue mapToNumberNH(HolderPair<NHAlgorithm> pair) {
        return calculate(val -> pair.algorithm().calculate(value(), val), pair.holder());
    }

    private NumberValue calculate(Consumer<AbstractNumberHolder> modifier, AbstractNumberHolder holder) {
        NumberProperty val = new NumberProperty(holder) {
            @Override
            protected AbstractNumberHolder computeValue() {
                AbstractNumberHolder val = super.computeValue();
                modifier.accept(val);
                return val;
            }
        };
        val.computer = true;
//        NumberProperty val = new NumberProperty()
        val.invalidate();
        val.addDependencies(this);
        return val;
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
