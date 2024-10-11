package org.example;

import java.lang.reflect.Method;
import java.util.Comparator;

public class TestMethodComparator implements Comparator<Method> {

	@Override
	public int compare(
			Method o1,
			Method o2
	) {

		Test test1 = o1.getAnnotation(Test.class);

		Test test2 = o2.getAnnotation(Test.class);

		if (test1 != null && test2 != null)
			if (test1.priority() == test2.priority())
				return 0;
			else
				return test1.priority() < test2.priority() ? 1 : -1;
		else
			return 0;

	}

}
