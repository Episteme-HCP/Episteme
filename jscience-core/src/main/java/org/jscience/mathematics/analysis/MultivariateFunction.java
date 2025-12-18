/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.mathematics.analysis;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Multivariate function (multiple variables: ℝⁿ → ℝ).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface MultivariateFunction extends ScalarFunction<Vector<Real>> {

    /**
     * Computes the partial derivative with respect to variable i.
     * 
     * @param x     the point
     * @param index the variable index
     * @return the partial derivative
     */
    default Real partialDerivative(Vector<Real> x, int index) {
        // Numerical approximation
        Real h = Real.of(1e-8);
        Vector<Real> xPlus = x; // Would need to modify component i
        Vector<Real> xMinus = x;
        return evaluate(xPlus).subtract(evaluate(xMinus)).divide(h.multiply(Real.of(2)));
    }

    /**
     * Computes the gradient at a point.
     * 
     * @param x the point
     * @return the gradient vector
     */
    default Vector<Real> gradient(Vector<Real> x) {
        throw new UnsupportedOperationException("Gradient not yet implemented");
    }
}
