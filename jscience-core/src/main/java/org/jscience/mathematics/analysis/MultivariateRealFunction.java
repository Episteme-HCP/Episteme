/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.analysis;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Represents a real-valued function of multiple real variables (R^n -> R).
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MultivariateRealFunction extends Function<Vector<Real>, Real> {

    /**
     * Evaluates this function at the given point.
     * 
     * @param point the input vector (x1, x2, ..., xn)
     * @return the function value f(x)
     */
    @Override
    Real evaluate(Vector<Real> point);

    /**
     * Computes the gradient of the function at the given point.
     * <p>
     * The gradient is the vector of partial derivatives:
     * ∇f = (∂f/∂x1, ∂f/∂x2, ..., ∂f/∂xn)
     * </p>
     * 
     * @param point the point at which to compute the gradient
     * @return the gradient vector
     */
    default Vector<Real> gradient(Vector<Real> point) {
        int n = point.dimension();
        java.util.List<Real> grad = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            grad.add(partialDerivative(point, i));
        }
        return org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(grad,
                org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Computes the partial derivative with respect to the i-th variable at the
     * given point.
     * 
     * @param point         the point at which to compute the derivative
     * @param variableIndex the index of the variable (0-based)
     * @return the partial derivative ∂f/∂xi
     */
    default Real partialDerivative(Vector<Real> point, int variableIndex) {
        // Central difference approximation: (f(x+h) - f(x-h)) / 2h
        double h = 1e-8;
        Real hReal = Real.of(h);

        // Create x+h vector
        java.util.List<Real> plusHCoords = new java.util.ArrayList<>();
        for (int i = 0; i < point.dimension(); i++) {
            plusHCoords.add(point.get(i));
        }
        plusHCoords.set(variableIndex, plusHCoords.get(variableIndex).add(hReal));
        Vector<Real> pointPlusH = org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(plusHCoords,
                org.jscience.mathematics.sets.Reals.getInstance());

        // Create x-h vector
        java.util.List<Real> minusHCoords = new java.util.ArrayList<>();
        for (int i = 0; i < point.dimension(); i++) {
            minusHCoords.add(point.get(i));
        }
        minusHCoords.set(variableIndex, minusHCoords.get(variableIndex).subtract(hReal));
        Vector<Real> pointMinusH = org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(minusHCoords,
                org.jscience.mathematics.sets.Reals.getInstance());

        Real fPlus = evaluate(pointPlusH);
        Real fMinus = evaluate(pointMinusH);

        return fPlus.subtract(fMinus).divide(hReal.multiply(Real.of(2.0)));
    }

    @Override
    default String getDomain() {
        return "R^n";
    }

    @Override
    default String getCodomain() {
        return "R";
    }
}