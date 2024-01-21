package com.galsie.lib.utils.functional;

/**
 *
 * @param <R> the return value of the method, ie the supplied value
 */
@FunctionalInterface
public interface ThrowableSupplier<R> {

    R supply() throws Exception;
}
