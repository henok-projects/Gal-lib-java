package com.galsie.lib.utils.builder;

public abstract class InternalBuilder<T> {
    /**
     * The builder which contains this InternalBuilder instance
     */
    T outerBuilder;

    /**
     * Initializes the Internal Builder for an External Builder
     * - The outerBuilder is expected for convenient chaining operations
     * @param outerBuilder The Outer Builder holding this Internal Builder
     */
    public InternalBuilder(T outerBuilder){
        this.outerBuilder = outerBuilder;
    }
    /**
     * Return back to the outer builder
     * @return The Outer Builder
     */
    public T done(){
        return this.outerBuilder;
    }
}
