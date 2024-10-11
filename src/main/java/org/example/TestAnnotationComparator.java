package org.example;

import java.util.Comparator;

public class TestAnnotationComparator implements Comparator<Test> {

	@Override
	public int compare(
			Test testAnnotation1,
			Test testAnnotation2
	) {
		if (testAnnotation1 != null && testAnnotation2 != null)
			if (testAnnotation1.priority() == testAnnotation2.priority())
				return 0;
			else
				return testAnnotation1.priority() < testAnnotation2.priority() ? 1 : -1;
		else
			return 0;
	}

}
