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
 * Interface for SPH (Smoothed Particle Hydrodynamics) fluid providers.
 * Allows switching between CPU (Multicore) and GPU implementations.
 */
public interface SPHFluidProvider {

    /**
     * Performs one simulation step (Density/Pressure, Forces, Integration).
     * 
     * @param positions       Particle positions [x, y, z, x, y, z...]
     * @param velocities      Particle velocities [vx, vy, vz...]
     * @param densities       Particle densities
     * @param pressures       Particle pressures
     * @param forces          Particle forces [fx, fy, fz...]
     * @param numParticles    Number of particles
     * @param dt              Time step
     * @param mass            Particle mass
     * @param restDensity     Rest density
     * @param stiffness       Stiffness constant
     * @param viscosity       Viscosity coefficient
     * @param smoothingRadius Smoothing radius (h)
     * @param gravity         Gravity vector [gx, gy, gz]
     */
    void step(double[] positions, double[] velocities, double[] densities, double[] pressures, double[] forces,
            int numParticles, double dt,
            double mass, double restDensity, double stiffness, double viscosity, double smoothingRadius,
            double[] gravity);

    /**
     * Returns the name of the provider.
     */
    String getName();
}
