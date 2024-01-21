package com.galsie.lib.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReflectionUtils {

    public static <T extends Annotation> List<Method> getAnnotatedMethods(Class<?> forClass, Class<T> annotation) {
        return Arrays.stream(forClass.getMethods()).filter(method -> method.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }


    public static <T extends Annotation> List<Method> getAnnotatedMethods(Class<?> forClass, Class<T> annotation, Class<?>... parameterTypes) {
        return Arrays.stream(forClass.getMethods())
                .filter(method -> method.getParameterCount() == parameterTypes.length)
                .filter(method -> method.isAnnotationPresent(annotation))
                .filter(method -> areParametersAssignableFrom(method.getParameterTypes(), parameterTypes))
                .collect(Collectors.toList());
    }

    public static boolean areParametersAssignableFrom(Class<?>[] parameters, Class<?>[] fromParameters) {
        return IntStream.range(0, parameters.length)
                .allMatch(i -> fromParameters[i].isAssignableFrom(parameters[i]));
    }

}
