/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.mathematics.function;

import org.jscience.mathematics.number.Real;

/**
 * Scalar-valued function (returns a single real number).
 * 
 * @param <D> the domain type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface ScalarFunction<D> extends Function<D, Real> {
    // Marker interface for scalar-valued functions
}
