package org.example;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;

public class StaticMethodAndAnnotationClassValidator implements BiConsumer<Method, Class<? extends Annotation>> {

	@Override
	public void accept(
			Method method,
			Class<? extends Annotation> annotationClass
	) {

		if (!Modifier.isStatic(method.getModifiers())) {

			String exceptionMessage = String.format(
					"Метод %s с аннотацией @%s не является статическим. Эта аннотация применима только к статическим методам",
					method.getName(),
					annotationClass.getSimpleName()
			);

			throw new RuntimeException(exceptionMessage);

		}

	}

}
