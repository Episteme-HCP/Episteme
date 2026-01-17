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

/**
 * Interface for Monte Carlo Pi Estimation providers.
 * 
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Metropolis, N., et al. (1953). Equation of State Calculations by Fast Computing Machines. <i>The Journal of Chemical Physics</i>, 21(6), 1087.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MonteCarloPiProvider {

    /**
     * Estimates Pi by counting points inside a unit circle.
     * 
     * @param numSamples Number of samples to generate
     * @return Number of points inside the unit circle
     */
    long countPointsInside(long numSamples);

    /**
     * Returns the name of this provider.
     * 
     * @return provider name
     */
    String getName();
}
