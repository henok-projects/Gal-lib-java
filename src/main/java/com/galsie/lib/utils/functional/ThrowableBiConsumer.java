package com.galsie.lib.utils.functional;

/**
 *
 * @param <T1> The first input type
 * @param <T2> the second input type
 */
@FunctionalInterface
public interface ThrowableBiConsumer<T1, T2> {

    void consume(T1 arg1, T2 arg2) throws Exception;
}
