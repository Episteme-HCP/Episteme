/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.mathematics.function;

/**
 * Differentiable function.
 * <p>
 * A function that has a derivative at every point in its domain.
 * </p>
 * 
 * @param <D> the domain type
 * @param <R> the range type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface DifferentiableFunction<D, R> extends ContinuousFunction<D, R> {

    /**
     * Computes the derivative at a point.
     * 
     * @param x the point
     * @return the derivative value
     */
    R derivative(D x);

    /**
     * Returns the derivative function.
     * 
     * @return the derivative as a function
     */
    default Function<D, R> getDerivative() {
        return this::derivative;
    }
}
