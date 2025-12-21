/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

/**
 * Represents a real-valued function of a real variable (R -> R).
 * <p>
 * Provides capabilities for numerical analysis, such as differentiation
 * and integration.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface RealFunction extends DifferentiableFunction<Real, Real>, IntegrableFunction<Real, Real> {

    /**
     * Returns the derivative of this function.
     * 
     * @return the derivative function f'
     */
    default RealFunction derivative() {
        // Default to numerical differentiation using Ridders' method
        return x -> Differentiation.derivativeAt(this, x);
    }

    @Override
    default Function<Real, Real> differentiate() {
        return derivative();
    }

    /**
     * Returns the integral of this function (antiderivative) with zero constant.
     * 
     * @return the antiderivative function F such that F' = f
     * @throws UnsupportedOperationException if symbolic integration is not
     *                                       supported
     */
    @Override
    default RealFunction integrate() {
        // Return numerical antiderivative F(x) = ∫[0,x] f(t) dt
        return x -> integrate(Real.ZERO, x);
    }

    /**
     * Integrates this function over a definite interval [a, b].
     * 
     * @param a the lower bound
     * @param b the upper bound
     * @return the definite integral value
     */
    @Override
    default Real integrate(Real a, Real b) {
        // Default to Adaptive Simpson's method
        return Integration.integrate(this, a, b);
    }

    static RealFunction identity() {
        return x -> x;
    }
}
