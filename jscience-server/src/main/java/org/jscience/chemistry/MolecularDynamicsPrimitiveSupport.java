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

package org.jscience.chemistry;

/**
 * Primitive-based support for Molecular Dynamics simulation.
 * Optimized for high-performance double-precision computation using flat
 * arrays.
 */
public class MolecularDynamicsPrimitiveSupport {

    /**
     * Calculates non-bonded forces (Lennard-Jones) using double primitives.
     * 
     * @param positions Flat array of position coordinates [x1, y1, z1, x2, y2, z2,
     *                  ...]
     * @param forces    Flat array of force components [fx1, fy1, fz1, fx2, fy2,
     *                  fz2, ...]
     * @param epsilon   L-J depth of the potential well
     * @param sigma     L-J distance at which the potential is zero
     * @param cutoff    Cutoff distance for interactions
     * @param numAtoms  Number of atoms
     */
    public void calculateNonBondedForces(double[] positions, double[] forces,
            double epsilon, double sigma, double cutoff, int numAtoms) {
        double sigma6 = Math.pow(sigma, 6);
        double sigma12 = sigma6 * sigma6;
        double cutoff2 = cutoff * cutoff;

        for (int i = 0; i < numAtoms; i++) {
            for (int j = i + 1; j < numAtoms; j++) {
                double dx = positions[j * 3] - positions[i * 3];
                double dy = positions[j * 3 + 1] - positions[i * 3 + 1];
                double dz = positions[j * 3 + 2] - positions[i * 3 + 2];

                double r2 = dx * dx + dy * dy + dz * dz;
                if (r2 < cutoff2) {
                    double invR2 = 1.0 / r2;
                    double invR6 = invR2 * invR2 * invR2;
                    double forceMag = 24.0 * epsilon * invR6 * (2.0 * sigma12 * invR6 - sigma6) * invR2;

                    forces[i * 3] -= forceMag * dx;
                    forces[i * 3 + 1] -= forceMag * dy;
                    forces[i * 3 + 2] -= forceMag * dz;
                    forces[j * 3] += forceMag * dx;
                    forces[j * 3 + 1] += forceMag * dy;
                    forces[j * 3 + 2] += forceMag * dz;
                }
            }
        }
    }

    /**
     * Integrates positions and velocities using Velocity Verlet algorithm.
     * 
     * @param positions  Flat array of position coordinates
     * @param velocities Flat array of velocity coordinates
     * @param forces     Flat array of force components
     * @param masses     Array of atom masses
     * @param dt         Time step
     * @param damping    Damping factor (optional, set to 1.0 for none)
     * @param numAtoms   Number of atoms
     */
    public void integrate(double[] positions, double[] velocities, double[] forces,
            double[] masses, double dt, double damping, int numAtoms) {
        for (int i = 0; i < numAtoms; i++) {
            double invMass = 1.0 / masses[i];

            // v = (v + (f/m)*dt) * damping
            velocities[i * 3] = (velocities[i * 3] + forces[i * 3] * invMass * dt) * damping;
            velocities[i * 3 + 1] = (velocities[i * 3 + 1] + forces[i * 3 + 1] * invMass * dt) * damping;
            velocities[i * 3 + 2] = (velocities[i * 3 + 2] + forces[i * 3 + 2] * invMass * dt) * damping;

            // x = x + v*dt
            positions[i * 3] += velocities[i * 3] * dt;
            positions[i * 3 + 1] += velocities[i * 3 + 1] * dt;
            positions[i * 3 + 2] += velocities[i * 3 + 2] * dt;
        }
    }
}
