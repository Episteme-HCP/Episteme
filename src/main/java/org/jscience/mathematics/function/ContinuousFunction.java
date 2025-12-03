/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.mathematics.function;

/**
 * Continuous function.
 * <p>
 * A function f is continuous if small changes in input produce small changes in
 * output.
 * Formally: lim(x→a) f(x) = f(a) for all a in the domain.
 * </p>
 * 
 * @param <D> the domain type
 * @param <R> the range type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface ContinuousFunction<D, R> extends Function<D, R> {

    /**
     * Checks if the function is continuous at a point.
     * Default implementation returns true (assumes continuity).
     * 
     * @param x the point
     * @return true if continuous at x
     */
    default boolean isContinuousAt(D x) {
        return true;
    }
}
