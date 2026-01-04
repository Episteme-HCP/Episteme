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

/**
 * Interface for Navier-Stokes Fluid Dynamics providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface NavierStokesProvider {

    /**
     * Solves one time step of the Navier-Stokes equations used detailed arrays.
     * All arrays are flattened 1D arrays of size width*height*depth.
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
    void solve(double[] density, double[] u, double[] v, double[] w, double dt, double viscosity, int width, int height,
            int depth);

    /**
     * Returns the name of this provider.
     * 
     * @return provider name
     */
    String getName();
}
