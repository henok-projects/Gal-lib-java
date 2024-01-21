package com.galsie.lib.utils.functional;

/**
 *
 * @param <T> The input type
 * @param <R> The result type
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> {

     R apply(T arg1) throws Exception;
}
