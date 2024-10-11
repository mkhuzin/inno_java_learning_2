package org.example;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SingleMethodAnnotationClassValidator implements BiConsumer<Class<?>, Class<? extends Annotation>> {

	@Override
	public void accept(
			Class<?> c,
			Class<? extends Annotation> annotationClass
	) {

		Method[] methodArray = c.getDeclaredMethods();

		List<Method> annotatedMethodList = new ArrayList<>();

		for (Method method : methodArray)
			if (method.getAnnotation(annotationClass) != null)
				annotatedMethodList.add(method);

		if (annotatedMethodList.size() > 1) {

			String exceptionMessage = String.format(
					"Допускается не более одного метода с аннотацией @%s. Фактическое количество: %s",
					annotationClass.getSimpleName(),
					annotatedMethodList.size()
			);

			throw new RuntimeException(exceptionMessage);

		}

	}

}
