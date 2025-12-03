/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.mathematics.function;

import org.jscience.mathematics.vector.Vector;

/**
 * Vector-valued function (returns a vector).
 * 
 * @param <D> the domain type
 * @param <R> the range element type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface VectorFunction<D, R> extends Function<D, Vector<R>> {

    /**
     * Returns the dimension of the output vector.
     * 
     * @return the output dimension
     */
    int getOutputDimension();

    /**
     * Evaluates a single component of the vector function.
     * 
     * @param x     the input
     * @param index the component index
     * @return the component value
     */
    default R evaluateComponent(D x, int index) {
        return evaluate(x).get(index);
    }
}
