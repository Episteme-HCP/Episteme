/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.analysis.integration;

import org.jscience.mathematics.analysis.RealFunction;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for numerical integration strategies.
 * <p>
 * Allows pluggable integration algorithms for computing definite integrals.
 * </p>
 * <p>
 * <b>Usage Example:</b>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Integrator {

    /**
     * Computes the definite integral of a function over [a, b].
     *
     * @param f the function to integrate
     * @param a the lower bound
     * @param b the upper bound
     * @return the approximate value of the integral
     */
    Real integrate(RealFunction f, Real a, Real b);

    /**
     * Computes the definite integral with specified tolerance.
     *
     * @param f         the function to integrate
     * @param a         the lower bound
     * @param b         the upper bound
     * @param tolerance the desired accuracy
     * @return the approximate value of the integral
     */
    Real integrate(RealFunction f, Real a, Real b, Real tolerance);
}


