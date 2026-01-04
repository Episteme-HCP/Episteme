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

package org.jscience.technical.backend.algorithms;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for Wave Equation providers using High-Precision Real numbers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface WaveProvider {

    /**
     * Solves one time step of the 2D Wave Equation.
     * 
     * @param u       Current wave height state (Real[][])
     * @param uPrev   Previous wave height state (Real[][])
     * @param width   Width of grid
     * @param height  Height of grid
     * @param c       Wave speed
     * @param damping Damping factor
     */
    void solve(Real[][] u, Real[][] uPrev, int width, int height, Real c, Real damping);

    /**
     * Solves one time step using double primitives.
     * 
     * @param u       Current wave height state
     * @param uPrev   Previous wave height state
     * @param width   Width of grid
     * @param height  Height of grid
     * @param c       Wave speed
     * @param damping Damping factor
     */
    void solve(double[][] u, double[][] uPrev, int width, int height, double c, double damping);

    /**
     * Returns the name of this provider.
     * 
     * @return provider name
     */
    String getName();
}
