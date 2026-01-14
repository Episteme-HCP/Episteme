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

package org.jscience.technical.backend.algorithms;

import org.jscience.mathematics.numbers.real.Real;
import java.util.function.Function;

/**
 * Interface for Monte Carlo simulation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MonteCarloProvider {

    /**
     * Estimates the integral of a function over a hypercube [0,1]^d.
     *
     * @param f          the function to integrate, takes double[] and returns
     *                   double
     * @param dimensions number of dimensions
     * @param samples    total number of samples
     * @return estimated integral value
     */
    double integrate(Function<double[], Double> f, int dimensions, long samples);

    /**
     * Estimates the integral using Real API for arbitrary precision.
     *
     * @param f          the function to integrate
     * @param dimensions number of dimensions
     * @param samples    total number of samples
     * @return estimated integral value as Real
     */
    Real integrateReal(Function<Real[], Real> f, int dimensions, long samples);

    /**
     * Returns the name of this provider.
     * 
     * @return provider name
     */
    String getName();
}
