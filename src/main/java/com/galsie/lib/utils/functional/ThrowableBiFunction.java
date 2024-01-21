package com.galsie.lib.utils.functional;

/**
 *
 * @param <T1> the first input type
 * @param <T2> the second input type
 * @param <R> the result type
 */
@FunctionalInterface
public interface ThrowableBiFunction<T1, T2, R> {

    R apply(T1 arg1, T2 arg2) throws Exception;
}
