import de.dasbabypixel.api.property.BooleanValue;

public class Test {


	public Test() {
		BooleanValue v1 = BooleanValue.trueValue();
		BooleanValue neg = v1.negate();
		BooleanValue xor = v1.xor(neg);
		BooleanValue xor2 = neg.xor(true);

		System.out.println(v1);
		System.out.println(neg);
		System.out.println(xor);
		System.out.println(xor2);
		v1.value(false);
		System.out.println(v1);
		System.out.println(neg);
		System.out.println(xor);
		System.out.println(xor2);
	}

	public static void main(String[] args) {
		//noinspection InstantiationOfUtilityClass
		new Test();
	}
}
