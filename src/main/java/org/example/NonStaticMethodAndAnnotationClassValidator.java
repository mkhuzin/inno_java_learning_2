package org.example;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;

public class NonStaticMethodAndAnnotationClassValidator implements BiConsumer<Method, Class<? extends Annotation>> {

	@Override
	public void accept(
			Method method,
			Class<? extends Annotation> annotationClass
	) {

		if (Modifier.isStatic(method.getModifiers())) {

			String exceptionMessage = String.format(
					"Метод %s с аннотацией @%s является статическим. Эта аннотация применима только к non-static методам",
					method.getName(),
					annotationClass.getSimpleName()
			);

			throw new RuntimeException(exceptionMessage);

		}

	}

}
