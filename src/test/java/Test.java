import de.dasbabypixel.api.property.NumberValue;

public class Test {

	public Test() {
		NumberValue v1 = NumberValue.withValue(0);
		NumberValue v2 = NumberValue.withValue(0);
		NumberValue v3 = NumberValue.withValue(0);
		v1.number(3);
		v2.number(2);
		v3.number(7);
		NumberValue v4 = v1.add(v2).add(v3);
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
	}

	public static void main(String[] args) {
		//noinspection InstantiationOfUtilityClass
		new Test();
	}
}
