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

/**
 * Interface for N-Body simulation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface NBodyProvider {

    /**
     * Computes gravitational forces between particles.
     * 
     * @param positions Flattened array of positions [x0, y0, z0, x1, y1, z1, ...]
     * @param masses    Array of masses [m0, m1, ...]
     * @param forces    Output flattened array of forces [fx0, fy0, fz0, ...]
     * @param G         Gravitational constant
     * @param softening Softening parameter to avoid singularities
     */
    void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening);

    /**
     * Computes gravitational forces using double primitives.
     */
    void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening);

    /**
     * Returns the name of this provider.
     * 
     * @return provider name
     */
    String getName();
}
