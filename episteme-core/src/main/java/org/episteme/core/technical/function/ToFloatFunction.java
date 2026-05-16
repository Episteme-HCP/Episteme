/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.function;

/**
 * Represents a function that produces a float-valued result.
 * This is a functional interface whose functional method is {@link #applyAsFloat(Object)}.
 *
 * @param <T> the type of the input to the function
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@FunctionalInterface
public interface ToFloatFunction<T> {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    float applyAsFloat(T value);
}
