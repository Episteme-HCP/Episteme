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
 * Interface for Navier-Stokes Fluid Dynamics providers using High-Precision
 * Real numbers.
 * 
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Navier, C. L. M. H. (1822). Mémoire sur les lois du mouvement des fluides. <i>Mémoires de l'Académie Royale des Sciences de l'Institut de France</i>, 6, 389-440.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface NavierStokesProvider {

    /**
     * Solves one time step of the Navier-Stokes equations using Real arrays.
     * 
     * @param density   Density field (read/write)
     * @param u         Velocity X field (read/write)
     * @param v         Velocity Y field (read/write)
     * @param w         Velocity Z field (read/write)
     * @param dt        Time step
     * @param viscosity Viscosity
     * @param width     Grid width
     * @param height    Grid height
     * @param depth     Grid depth
     */
    void solve(Real[] density, Real[] u, Real[] v, Real[] w, Real dt, Real viscosity, int width, int height,
            int depth);

    /**
     * Solves one time step using double primitives.
     * 
     * @param density   Density field
     * @param u         Velocity X field
     * @param v         Velocity Y field
     * @param w         Velocity Z field
     * @param dt        Time step
     * @param viscosity Viscosity
     * @param width     Grid width
     * @param height    Grid height
     * @param depth     Grid depth
     */
    void solve(double[] density, double[] u, double[] v, double[] w, double dt, double viscosity, int width, int height,
            int depth);

    /**
     * Returns the name of this provider.
     * 
     * @return provider name
     */
    String getName();
}
