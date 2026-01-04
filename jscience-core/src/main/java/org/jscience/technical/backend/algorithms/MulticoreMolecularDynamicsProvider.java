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
 * Multicore implementation of MolecularDynamicsProvider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreMolecularDynamicsProvider implements MolecularDynamicsProvider {

    @Override
    public void integrate(Real[] positions, Real[] velocities, Real[] forces, Real[] masses, Real dt, Real damping) {
        int numAtoms = masses.length;
        double dtVal = dt.doubleValue();
        double dampVal = damping.doubleValue();

        // Simple loop (could be parallelized but overhead might be high for simple
        // integration)
        // Parallelizing for large N
        java.util.stream.IntStream.range(0, numAtoms).parallel().forEach(i -> {
            int idx = i * 3;
            double m = masses[i].doubleValue();
            if (m == 0)
                m = 1e-27; // Safety

            double ax = forces[idx].doubleValue() / m;
            double ay = forces[idx + 1].doubleValue() / m;
            double az = forces[idx + 2].doubleValue() / m;

            double vx = velocities[idx].doubleValue() + ax * dtVal;
            double vy = velocities[idx + 1].doubleValue() + ay * dtVal;
            double vz = velocities[idx + 2].doubleValue() + az * dtVal;

            vx *= dampVal;
            vy *= dampVal;
            vz *= dampVal;

            velocities[idx] = Real.of(vx);
            velocities[idx + 1] = Real.of(vy);
            velocities[idx + 2] = Real.of(vz);

            double x = positions[idx].doubleValue() + vx * dtVal;
            double y = positions[idx + 1].doubleValue() + vy * dtVal;
            double z = positions[idx + 2].doubleValue() + vz * dtVal;

            positions[idx] = Real.of(x);
            positions[idx + 1] = Real.of(y);
            positions[idx + 2] = Real.of(z);
        });
    }

    @Override
    public void calculateBondForces(Real[] positions, Real[] forces, int[] bondIndices, Real[] bondLengths,
            Real[] bondConstants) {
        int numBonds = bondIndices.length / 2;
        for (int i = 0; i < numBonds; i++) {
            int idx1 = bondIndices[i * 2] * 3;
            int idx2 = bondIndices[i * 2 + 1] * 3;

            double x1 = positions[idx1].doubleValue();
            double y1 = positions[idx1 + 1].doubleValue();
            double z1 = positions[idx1 + 2].doubleValue();

            double x2 = positions[idx2].doubleValue();
            double y2 = positions[idx2 + 1].doubleValue();
            double z2 = positions[idx2 + 2].doubleValue();

            double dx = x2 - x1;
            double dy = y2 - y1;
            double dz = z2 - z1;

            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double r0 = bondLengths[i].doubleValue();
            double k = bondConstants[i].doubleValue();

            if (dist > 1e-12) {
                double forceMag = k * (dist - r0);
                double fx = forceMag * (dx / dist);
                double fy = forceMag * (dy / dist);
                double fz = forceMag * (dz / dist);

                forces[idx1] = forces[idx1].add(Real.of(fx));
                forces[idx1 + 1] = forces[idx1 + 1].add(Real.of(fy));
                forces[idx1 + 2] = forces[idx1 + 2].add(Real.of(fz));

                forces[idx2] = forces[idx2].subtract(Real.of(fx));
                forces[idx2 + 1] = forces[idx2 + 1].subtract(Real.of(fy));
                forces[idx2 + 2] = forces[idx2 + 2].subtract(Real.of(fz));
            }
        }
    }

    @Override
    public String getName() {
        return "Multicore Molecular Dynamics";
    }
}
