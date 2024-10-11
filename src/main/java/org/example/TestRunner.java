package org.example;

import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

public class TestRunner {

	private static List<Method> getAnnotatedMethods(
			Class<?> c,
			Class<? extends Annotation> annotationClass,
			BiConsumer<Class<?>, Class<? extends Annotation>> classAndAnnotationClassValidator,
			BiConsumer<Method, Class<? extends Annotation>> methodAndAnnotationClassValidator,
			Comparator<Method> comparator
	) {

		Method[] methodArray = c.getDeclaredMethods();

		List<Method> annotatedMethodList = new ArrayList<>();

		for (Method method : methodArray)

			if (method.getAnnotation(annotationClass) != null) {

				if (methodAndAnnotationClassValidator != null)
					methodAndAnnotationClassValidator.accept(
							method,
							annotationClass
					);

				annotatedMethodList.add(method);

			}

		if (classAndAnnotationClassValidator != null)
			classAndAnnotationClassValidator.accept(
					c,
					annotationClass
			);

		if (comparator != null)
			annotatedMethodList.sort(comparator);

		return annotatedMethodList;

	}

	@SneakyThrows
	private static Object getWrappedPrimitiveFromString(
			Class<?> c,
			String string
	) {

		if (c.equals(int.class))
			return Integer.parseInt(string);

		if (c.equals(boolean.class))
			return Boolean.parseBoolean(string);

		if (c.equals(byte.class))
			return Byte.parseByte(string);

		if (c.equals(char.class))
			return string.charAt(0);

		if (c.equals(short.class))
			return Short.parseShort(string);

		if (c.equals(long.class))
			return Long.parseLong(string);

		if (c.equals(float.class))
			return Float.parseFloat(string);

		if (c.equals(double.class))
			return Double.parseDouble(string);

		String exceptionMessage = String.format(
				"Неизвестный примитивный тип %s для получения \"обёртки\"",
				c.getName()
		);

		throw new RuntimeException(exceptionMessage);

	}

	@SneakyThrows
	private static Object[] getMethodArgArr(Method method) {

		CsvSource csvSourceAnnotation = method.getAnnotation(CsvSource.class);

		String[] csvSourceArgArr;

		if (csvSourceAnnotation == null)
			csvSourceArgArr = new String[0];
		else
			csvSourceArgArr = csvSourceAnnotation.value().split("\\s*,\\s*");

		Class<?>[] methodArgsClasses = method.getParameterTypes();

		if (csvSourceArgArr.length != methodArgsClasses.length) {

			String exceptionMessage = String.format(
					"Количество аргументов в аннотации @CsvSource (%s) не совпадает с количеством аргументов метода %s (%s)",
					csvSourceArgArr.length,
					method.getName(),
					methodArgsClasses.length
			);

			throw new RuntimeException(exceptionMessage);

		}

		Object[] methodArgObjectArr = new Object[methodArgsClasses.length];

		if (methodArgObjectArr.length == 0)
			return methodArgObjectArr;

		for (int i = 0; i < methodArgsClasses.length; i++)
			if (methodArgsClasses[i].isPrimitive())
				methodArgObjectArr[i] = getWrappedPrimitiveFromString(
						methodArgsClasses[i],
						csvSourceArgArr[i]
				);
			else
				methodArgObjectArr[i] = methodArgsClasses[i]
						.getConstructor(String.class)
						.newInstance(csvSourceArgArr[i]);

		return methodArgObjectArr;

	}

	@SneakyThrows
	private static void executeMethod(
			Object object,
			Method method
	) {

		Object[] methodArgArr = getMethodArgArr(method);

		method.invoke(object, methodArgArr);

	}

	@SneakyThrows
	private static void executeMethodList(
			Class<?> c,
			List<Method> methodList
	) {

		if (methodList.isEmpty())
			return;

		Object object = c.getConstructor().newInstance();

		methodList.forEach(method -> executeMethod(object, method));

	}

	@SneakyThrows
	public static void runTests(Class<?> c) {

		List<Method> methodList = getAnnotatedMethods(
				c,
				BeforeSuite.class,
				new SingleMethodAnnotationClassValidator(),
				new StaticMethodAndAnnotationClassValidator(),
				null
		);

		executeMethodList(c, methodList);

		methodList = getAnnotatedMethods(
				c,
				Test.class,
				null,
				new NonStaticMethodAndAnnotationClassValidator(),
				new TestMethodComparator()
		);

		executeMethodList(c, methodList);

		methodList = getAnnotatedMethods(
				c,
				AfterSuite.class,
				new SingleMethodAnnotationClassValidator(),
				new StaticMethodAndAnnotationClassValidator(),
				null
		);

		executeMethodList(c, methodList);

	}

}
