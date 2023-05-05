package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

import java.util.Objects;

@Api
public class NumberHolder extends AbstractNumberHolder {
    private final NumberHolder partner;
    private Number number;

    NumberHolder(Number number) {
        this.number = number;
        this.partner = new NumberHolder(this);
    }

    private NumberHolder(NumberHolder partner) {
        this.partner = partner;
        this.number = partner.number;
    }

    @Api
    public Number get() {
        return number;
    }

    @Override
    public int intValue() {
        return number.intValue();
    }

    @Override
    public long longValue() {
        return number.longValue();
    }

    @Override
    public float floatValue() {
        return number.floatValue();
    }

    @Override
    public double doubleValue() {
        return number.doubleValue();
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NumberHolder that = (NumberHolder) o;
        return Objects.equals(number, that.number);
    }

    @Override
    void pollFromStorage() {
        get();
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    NumberHolder partner() {
        return partner;
    }

    @Override
    void pollFromPartner() {
        number = partner.number;
    }

    @Api
    public void set(Number number) {
        this.number = number;
    }

    @Override
    public void set(double number) {
        if (this.number instanceof DoubleHolder)
            ((DoubleHolder) this.number).set(number);
        else
            this.number = new DoubleHolder(number);
    }

    @Override
    public void set(float number) {
        if (this.number instanceof FloatHolder)
            ((FloatHolder) this.number).set(number);
        else
            this.number = new FloatHolder(number);
    }

    @Override
    public void set(long number) {
        if (this.number instanceof LongHolder)
            ((LongHolder) this.number).set(number);
        else
            this.number = new LongHolder(number);
    }

    @Override
    public void set(int number) {
        if (this.number instanceof IntegerHolder)
            ((IntegerHolder) this.number).set(number);
        else
            this.number = new IntegerHolder(number);
    }

    @Override
    public boolean writable() {
        return true;
    }
}
