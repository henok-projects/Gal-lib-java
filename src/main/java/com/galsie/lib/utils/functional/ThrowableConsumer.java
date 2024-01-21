package com.galsie.lib.utils.functional;

@FunctionalInterface
public interface ThrowableConsumer<T>{

     void consume(T arg) throws Exception;
}
