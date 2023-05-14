package de.dasbabypixel.api.property;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("UnusedAssignment")
public class NumberUtils {

    public static AbstractNumberHolder createHolder(Number number) {
        if (isByte(number) || isShort(number) || isInteger(number))
            return new IntegerHolder(number.intValue());
        else if (isLong(number))
            return new LongHolder(number.longValue());
        else if (isFloat(number))
            return new FloatHolder(number.floatValue());
        else if (isDouble(number))
            return new DoubleHolder(number.doubleValue());
        return new NumberHolder(number);
    }

    public static HolderPair<NNHAlgorithm> add(Number n1, Number n2) {
        boolean n1fit;
        boolean n2fit;
        if ((n1fit = (isByte(n1) || isShort(n1) || isInteger(n1))) & (n2fit = (isByte(n2) || isShort(n2) || isInteger(n2)))) {
            return new HolderPair<>(new IntegerHolder(), (n11, n21, storage) -> storage.set(n11.intValue() + n21.intValue()));
        } else if ((n1fit = (n1fit || isLong(n1))) & (n2fit = (n2fit || isLong(n2)))) {
            return new HolderPair<>(new LongHolder(), (n11, n21, storage) -> storage.set(n11.longValue() + n21.longValue()));
        } else if ((n1fit = (n1fit || isFloat(n1))) & (n2fit = (n2fit || isFloat(n2)))) {
            return new HolderPair<>(new FloatHolder(), (n11, n21, storage) -> storage.set(n11.floatValue() + n21.floatValue()));
        } else if ((n1fit = (n1fit || isDouble(n1))) & (n2fit = (n2fit || isDouble(n2)))) {
            return new HolderPair<>(new DoubleHolder(), (n11, n21, storage) -> storage.set(n11.doubleValue() + n21.doubleValue()));
        }
        throw new UnsupportedOperationException("Adding " + n1.getClass() + " and " + n2.getClass() + " not supported!");
    }

    public static HolderPair<NNHAlgorithm> subtract(Number n1, Number n2) {
        boolean n1fit;
        boolean n2fit;
        if ((n1fit = (isByte(n1) || isShort(n1) || isInteger(n1))) & (n2fit = (isByte(n2) || isShort(n2) || isInteger(n2)))) {
            return new HolderPair<>(new IntegerHolder(), (n11, n21, storage) -> storage.set(n11.intValue() - n21.intValue()));
        } else if ((n1fit = (n1fit || isLong(n1))) & (n2fit = (n2fit || isLong(n2)))) {
            return new HolderPair<>(new LongHolder(), (n11, n21, storage) -> storage.set(n11.longValue() - n21.longValue()));
        } else if ((n1fit = (n1fit || isFloat(n1))) & (n2fit = (n2fit || isFloat(n2)))) {
            return new HolderPair<>(new FloatHolder(), (n11, n21, storage) -> storage.set(n11.floatValue() - n21.floatValue()));
        } else if ((n1fit = (n1fit || isDouble(n1))) & (n2fit = (n2fit || isDouble(n2)))) {
            return new HolderPair<>(new DoubleHolder(), (n11, n21, storage) -> storage.set(n11.doubleValue() - n21.doubleValue()));
        }
        throw new UnsupportedOperationException("Subtracting " + n1.getClass() + " and " + n2.getClass() + " not supported!");
    }

    public static HolderPair<NNHAlgorithm> multiply(Number n1, Number n2) {
        boolean n1fit;
        boolean n2fit;
        if ((n1fit = (isByte(n1) || isShort(n1) || isInteger(n1))) & (n2fit = (isByte(n2) || isShort(n2) || isInteger(n2)))) {
            return new HolderPair<>(new IntegerHolder(), (n11, n21, storage) -> storage.set(n11.intValue() * n21.intValue()));
        } else if ((n1fit = (n1fit || isLong(n1))) & (n2fit = (n2fit || isLong(n2)))) {
            return new HolderPair<>(new LongHolder(), (n11, n21, storage) -> storage.set(n11.longValue() * n21.longValue()));
        } else if ((n1fit = (n1fit || isFloat(n1))) & (n2fit = (n2fit || isFloat(n2)))) {
            return new HolderPair<>(new FloatHolder(), (n11, n21, storage) -> storage.set(n11.floatValue() * n21.floatValue()));
        } else if ((n1fit = (n1fit || isDouble(n1))) & (n2fit = (n2fit || isDouble(n2)))) {
            return new HolderPair<>(new DoubleHolder(), (n11, n21, storage) -> storage.set(n11.doubleValue() * n21.doubleValue()));
        }
        throw new UnsupportedOperationException("Multiplying " + n1.getClass() + " and " + n2.getClass() + " not supported!");
    }

    public static HolderPair<NNHAlgorithm> divide(Number n1, Number n2) {
        boolean n1fit;
        boolean n2fit;
        if ((n1fit = (isByte(n1) || isShort(n1) || isInteger(n1))) & (n2fit = (isByte(n2) || isShort(n2) || isInteger(n2)))) {
            return new HolderPair<>(new IntegerHolder(), (n11, n21, storage) -> storage.set(n11.intValue() / n21.intValue()));
        } else if ((n1fit = (n1fit || isLong(n1))) & (n2fit = (n2fit || isLong(n2)))) {
            return new HolderPair<>(new LongHolder(), (n11, n21, storage) -> storage.set(n11.longValue() / n21.longValue()));
        } else if ((n1fit = (n1fit || isFloat(n1))) & (n2fit = (n2fit || isFloat(n2)))) {
            return new HolderPair<>(new FloatHolder(), (n11, n21, storage) -> storage.set(n11.floatValue() / n21.floatValue()));
        } else if ((n1fit = (n1fit || isDouble(n1))) & (n2fit = (n2fit || isDouble(n2)))) {
            return new HolderPair<>(new DoubleHolder(), (n11, n21, storage) -> storage.set(n11.doubleValue() / n21.doubleValue()));
        }
        throw new UnsupportedOperationException("Dividing " + n1.getClass() + " and " + n2.getClass() + " not supported!");
    }

    public static HolderPair<NNHAlgorithm> max(Number n1, Number n2) {
        boolean n1fit;
        boolean n2fit;
        if ((n1fit = (isByte(n1) || isShort(n1) || isInteger(n1))) & (n2fit = (isByte(n2) || isShort(n2) || isInteger(n2)))) {
            return new HolderPair<>(new IntegerHolder(), (n11, n21, storage) -> storage.set(Math.max(n11.intValue(), n21.intValue())));
        } else if ((n1fit = (n1fit || isLong(n1))) & (n2fit = (n2fit || isLong(n2)))) {
            return new HolderPair<>(new LongHolder(), (n11, n21, storage) -> storage.set(Math.max(n11.longValue(), n21.longValue())));
        } else if ((n1fit = (n1fit || isFloat(n1))) & (n2fit = (n2fit || isFloat(n2)))) {
            return new HolderPair<>(new FloatHolder(), (n11, n21, storage) -> storage.set(Math.max(n11.floatValue(), n21.floatValue())));
        } else if ((n1fit = (n1fit || isDouble(n1))) & (n2fit = (n2fit || isDouble(n2)))) {
            return new HolderPair<>(new DoubleHolder(), (n11, n21, storage) -> storage.set(Math.max(n11.doubleValue(), n21.doubleValue())));
        }
        throw new UnsupportedOperationException("Max " + n1.getClass() + " and " + n2.getClass() + " not supported!");
    }

    public static HolderPair<NNHAlgorithm> min(Number n1, Number n2) {
        boolean n1fit;
        boolean n2fit;
        if ((n1fit = (isByte(n1) || isShort(n1) || isInteger(n1))) & (n2fit = (isByte(n2) || isShort(n2) || isInteger(n2)))) {
            return new HolderPair<>(new IntegerHolder(), (n11, n21, storage) -> storage.set(Math.min(n11.intValue(), n21.intValue())));
        } else if ((n1fit = (n1fit || isLong(n1))) & (n2fit = (n2fit || isLong(n2)))) {
            return new HolderPair<>(new LongHolder(), (n11, n21, storage) -> storage.set(Math.min(n11.longValue(), n21.longValue())));
        } else if ((n1fit = (n1fit || isFloat(n1))) & (n2fit = (n2fit || isFloat(n2)))) {
            return new HolderPair<>(new FloatHolder(), (n11, n21, storage) -> storage.set(Math.min(n11.floatValue(), n21.floatValue())));
        } else if ((n1fit = (n1fit || isDouble(n1))) & (n2fit = (n2fit || isDouble(n2)))) {
            return new HolderPair<>(new DoubleHolder(), (n11, n21, storage) -> storage.set(Math.min(n11.doubleValue(), n21.doubleValue())));
        }
        throw new UnsupportedOperationException("Max " + n1.getClass() + " and " + n2.getClass() + " not supported!");
    }

    public static HolderPair<NNHAlgorithm> pow(Number n1, Number n2) {
        boolean n1fit;
        boolean n2fit;
        if ((n1fit = (isByte(n1) || isShort(n1) || isInteger(n1))) & (n2fit = (isByte(n2) || isShort(n2) || isInteger(n2)))) {
            return new HolderPair<>(new IntegerHolder(), (n11, n21, storage) -> storage.set(Math.pow(n11.intValue(), n21.intValue())));
        } else if ((n1fit = (n1fit || isLong(n1))) & (n2fit = (n2fit || isLong(n2)))) {
            return new HolderPair<>(new LongHolder(), (n11, n21, storage) -> storage.set(Math.pow(n11.longValue(), n21.longValue())));
        } else if ((n1fit = (n1fit || isFloat(n1))) & (n2fit = (n2fit || isFloat(n2)))) {
            return new HolderPair<>(new FloatHolder(), (n11, n21, storage) -> storage.set(Math.pow(n11.floatValue(), n21.floatValue())));
        } else if ((n1fit = (n1fit || isDouble(n1))) & (n2fit = (n2fit || isDouble(n2)))) {
            return new HolderPair<>(new DoubleHolder(), (n11, n21, storage) -> storage.set(Math.pow(n11.doubleValue(), n21.doubleValue())));
        }
        throw new UnsupportedOperationException("Max " + n1.getClass() + " and " + n2.getClass() + " not supported!");
    }

    public static HolderPair<NHAlgorithm> negate(Number n) {
        if (isByte(n) || isShort(n) || isInteger(n)) {
            return new HolderPair<>(new IntegerHolder(), (n1, storage) -> storage.set(-n1.intValue()));
        } else if (isLong(n)) {
            return new HolderPair<>(new LongHolder(), (n1, storage) -> storage.set(-n1.longValue()));
        } else if (isFloat(n)) {
            return new HolderPair<>(new FloatHolder(), (n1, storage) -> storage.set(-n1.floatValue()));
        } else if (isDouble(n)) {
            return new HolderPair<>(new DoubleHolder(), (n1, storage) -> storage.set(-n1.doubleValue()));
        }
        throw new UnsupportedOperationException("Negating " + n.getClass() + " not supported");
    }

    public static HolderPair<NHAlgorithm> round(Number n) {
        if (isByte(n) || isShort(n) || isInteger(n)) {
            return new HolderPair<>(new IntegerHolder(), (n1, storage) -> storage.set(n1.intValue()));
        } else if (isLong(n)) {
            return new HolderPair<>(new LongHolder(), (n1, storage) -> storage.set(n1.longValue()));
        } else if (isFloat(n)) {
            return new HolderPair<>(new FloatHolder(), (n1, storage) -> storage.set((float) Math.round(n1.floatValue())));
        } else if (isDouble(n)) {
            return new HolderPair<>(new DoubleHolder(), (n1, storage) -> storage.set((double) Math.round(n1.doubleValue())));
        }
        throw new UnsupportedOperationException("Negating " + n.getClass() + " not supported");
    }

    public static HolderPair<NHAlgorithm> floor(Number n) {
        if (isByte(n) || isShort(n) || isInteger(n)) {
            return new HolderPair<>(new IntegerHolder(), (n1, storage) -> storage.set(n1.intValue()));
        } else if (isLong(n)) {
            return new HolderPair<>(new LongHolder(), (n1, storage) -> storage.set(n1.longValue()));
        } else if (isFloat(n)) {
            return new HolderPair<>(new FloatHolder(), (n1, storage) -> storage.set((float) Math.floor(n1.floatValue())));
        } else if (isDouble(n)) {
            return new HolderPair<>(new DoubleHolder(), (n1, storage) -> storage.set(Math.floor(n1.doubleValue())));
        }
        throw new UnsupportedOperationException("Negating " + n.getClass() + " not supported");
    }

    public static HolderPair<NHAlgorithm> ceil(Number n) {
        if (isByte(n) || isShort(n) || isInteger(n)) {
            return new HolderPair<>(new IntegerHolder(), (n1, storage) -> storage.set(n1.intValue()));
        } else if (isLong(n)) {
            return new HolderPair<>(new LongHolder(), (n1, storage) -> storage.set(n1.longValue()));
        } else if (isFloat(n)) {
            return new HolderPair<>(new FloatHolder(), (n1, storage) -> storage.set((float) Math.ceil(n1.floatValue())));
        } else if (isDouble(n)) {
            return new HolderPair<>(new DoubleHolder(), (n1, storage) -> storage.set(Math.ceil(n1.doubleValue())));
        }
        throw new UnsupportedOperationException("Negating " + n.getClass() + " not supported");
    }

    public static boolean isInteger(Number number) {
        return number instanceof Integer || number instanceof AtomicInteger || number instanceof IntegerHolder;
    }

    public static boolean isLong(Number number) {
        return number instanceof Long || number instanceof AtomicLong || number instanceof LongHolder;
    }

    public static boolean isFloat(Number number) {
        return number instanceof Float || number instanceof FloatHolder;
    }

    public static boolean isDouble(Number number) {
        return number instanceof Double || number instanceof DoubleHolder;
    }

    public static boolean isByte(Number number) {
        return number instanceof Byte;
    }

    public static boolean isShort(Number number) {
        return number instanceof Short;
    }

    public interface NNHAlgorithm {
        void calculate(Number n1, Number n2, AbstractNumberHolder storage);
    }


    public interface NHAlgorithm {
        void calculate(Number n, AbstractNumberHolder storage);
    }


    public static final class HolderPair<T> {
        private final AbstractNumberHolder holder;
        private final T algorithm;

        public HolderPair(AbstractNumberHolder holder, T algorithm) {
            this.holder = holder;
            this.algorithm = algorithm;
        }

        public AbstractNumberHolder holder() {
            return holder;
        }

        public T algorithm() {
            return algorithm;
        }
    }
}
