/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.mathematics.analysis;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Univariate function (single variable: ℝ → ℝ).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface UnivariateFunction extends ScalarFunction<Real>, DifferentiableFunction<Real, Real> {

    /**
     * Computes the integral from a to b.
     * 
     * @param a lower bound
     * @param b upper bound
     * @return the definite integral
     */
    default Real integrate(Real a, Real b) {
        // Simplified - would use numerical integration
        throw new UnsupportedOperationException("Integration not yet implemented");
    }
}
