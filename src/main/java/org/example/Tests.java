package org.example;

public class Tests {

	@BeforeSuite
	static void beforeTests() {
		System.out.println("beforeTests");
	}

	@Test
	@CsvSource("10, Java, 20, true")
	void test1(int a, String b, int c, boolean d) {

		String messageString = String.format(
				"test1: a = %s, b = %s, c = %s, d = %s",
				a,
				b,
				c,
				d
		);

		System.out.println(messageString);

	}

	void test2() {
		System.out.println("test2");
	}

	@Test(priority = 3)
	void test3() {
		System.out.println("test3");
	}

	@AfterSuite
	static void afterTests() {
		System.out.println("afterTests");
	}

}
