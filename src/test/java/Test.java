import de.dasbabypixel.api.property.InvalidationListener;
import de.dasbabypixel.api.property.NumberChangeListener;
import de.dasbabypixel.api.property.NumberValue;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    public Test() {
        NumberValue v1 = NumberValue.withValue(0);
        NumberValue v2 = NumberValue.withValue(0);
        NumberValue v3 = NumberValue.withValue(0);
        v1.number(3);
        v2.number(2);
        v3.number(7);
        NumberValue v4 = v1.add(6).add(v2).add(v3);
        check(v4.intValue(), 3 + 2 + 7 + 6);
        v1.number(7);
        check(v4.intValue(), 7 + 2 + 7 + 6);
        v1.number(9);
        check(v4.intValue(), 9 + 2 + 7 + 6);

        v2.bind(v1);
        v3.bind(v2);
        v1.number(10);
        check(v4.intValue(), 36);
        NumberValue v5 = NumberValue.withValue(0);
        check(v5.intValue(), 0);
        v5.bind(v4);
        check(v5.intValue(), 36);
        v1.number(15);
        check(v5.intValue(), 51);
        NumberValue v6 = v5.add(40);
        check(v6.intValue(), 91);

        AtomicInteger i2 = new AtomicInteger(4);
        NumberValue v7 = NumberValue.computing(i2::get);
        NumberValue v8 = NumberValue.withValue(0);
        NumberValue v9 = NumberValue.withValue(0);
        NumberValue v10 = v9.add(1);
        v8.bind(v7);
        v9.bind(v8);
        check(v8.intValue(), 4);
        check(v9.intValue(), 4);
        i2.set(9);
        check(v8.intValue(), 4); // This will not update until we poll v7 again
        check(v9.intValue(), 4);
        i2.set(10);
        check(v9.intValue(), 4); // Still 9 because we didn't invalidateAll before and v7 is computing
        check(v10.intValue(), 5);
        i2.set(11);
        v7.addListener((NumberChangeListener) (value, oldNumber, newNumber) -> System.out.println(oldNumber + "->" + newNumber));
        v8.addListener((InvalidationListener) property -> System.out.println("v8 invalidated"));
        check(v7.intValue(), 11);
        check(v8.intValue(), 11);
        check(v9.intValue(), 11);
        check(v10.intValue(), 12);

        NumberValue n1 = NumberValue.computing(i2::get).add(5).add(7);
        check(n1.intValue(), 11 + 5 + 7);
        i2.set(15);
        n1.invalidateAll();
        check(n1.intValue(), 15 + 5 + 7);

        NumberValue n3 = n1.mapToDouble(h -> h.intValue() + 1).add(10).add(5).add(5).divide((double) 3);
        check(n3.doubleValue(), (15 + 5 + 7 + 1 + 10 + 5 + 5) / 3D);
        i2.set(17);
        n1.invalidateAll();
        check(n3.doubleValue(), (17 + 5 + 7 + 1 + 10 + 5 + 5) / 3D);
    }

    public static void check(Object o1, Object expect) {
        if (!Objects.equals(o1, expect)) throw new RuntimeException(o1 + " is not " + expect);
    }

    public static void main(String[] args) {
        //noinspection InstantiationOfUtilityClass
        new Test();
    }
}
