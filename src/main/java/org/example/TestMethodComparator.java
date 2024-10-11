package org.example;

import java.lang.reflect.Method;
import java.util.Comparator;

public class TestMethodComparator implements Comparator<Method> {

	@Override
	public int compare(
			Method method1,
			Method method2
	) {
		return new TestAnnotationComparator().compare(
				method1.getAnnotation(Test.class),
				method2.getAnnotation(Test.class)
		);
	}

}
