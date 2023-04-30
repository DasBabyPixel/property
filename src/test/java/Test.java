import de.dasbabypixel.api.property.NumberValue;

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
        System.out.println(v4.intValue());
        v1.number(7);
        System.out.println(v4.intValue());
        v1.number(9);
        System.out.println(v4.intValue());

        v2.bind(v1);
        v3.bind(v2);
        v1.number(10);
        System.out.println(v4.intValue());
        NumberValue v5 = NumberValue.withValue(0);
        System.out.println(v5);
        v5.bind(v4);
        System.out.println(v5);
        v1.number(15);
        System.out.println(v5);
        NumberValue v6 = v5.add(40);
        System.out.println(v6);


        AtomicInteger i2 = new AtomicInteger(4);
        NumberValue v7 = NumberValue.computing(i2::get);
        NumberValue v8 = NumberValue.withValue(0);
        NumberValue v9 = NumberValue.withValue(0);
        NumberValue v10 = v9.add(1);
        v8.bind(v7);
        v9.bind(v8);
        System.out.println(v8);
        System.out.println(v9);
        i2.set(9);
        System.out.println(v8);
        System.out.println(v9);
        i2.set(10);
        System.out.println(v9);
        System.out.println(v10);
        i2.set(11);
        System.out.println(v7);
        System.out.println(v8);
        System.out.println(v9);
        System.out.println(v10);

        NumberValue n1 = NumberValue.computing(i2::get).add(5).add(7);
        System.out.println(n1);
        i2.set(15);
        n1.invalidateAll();
        System.out.println(n1);

        NumberValue n3 = n1.mapToDouble(h -> h.intValue() + 1).add(10).add(5).add(5).divide((double) 3);
        System.out.println(n3);
        i2.set(17);
        n1.invalidateAll();
        System.out.println(n3);

    }

    public static void main(String[] args) {
        //noinspection InstantiationOfUtilityClass
        new Test();
    }
}
