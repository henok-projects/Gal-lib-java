package com.galsie.lib.utils.functional;

@FunctionalInterface
public interface ThrowableRunnable {

    void run() throws Exception;
}
