package org.mql.test;

public interface InterfaceDeTest extends I1 {

}

class Test implements InterfaceDeTest {

	@Override
	public void sayHelloTo(String name) {
	}

}

interface I1 extends I2 {
	int i = 1;

	default void sayHello() {
		System.out.println("Hello");
	}

	static String returnHello() {
		return "Hello";
	}

	void sayHelloTo(String name);
}

interface I2 {
	int i = 2;
}