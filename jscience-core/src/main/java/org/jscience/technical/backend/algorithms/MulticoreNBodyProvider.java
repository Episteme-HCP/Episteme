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
import java.util.stream.IntStream;

/**
 * Multicore implementation of N-Body simulation using Java Streams.
 * 
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Aarseth, S. J. (2003). <i>Gravitational N-Body Simulations</i>. Cambridge University Press.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreNBodyProvider implements NBodyProvider {

    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
        int n = masses.length;

        // Reset forces
        for (int i = 0; i < forces.length; i++) {
            forces[i] = Real.ZERO;
        }

        // Cache Real values to doubles for performance in inner loop
        double[] posD = new double[positions.length];
        double[] massD = new double[masses.length];
        double gVal = G.doubleValue();
        double softVal = softening.doubleValue();

        for (int i = 0; i < positions.length; i++)
            posD[i] = positions[i].doubleValue();
        for (int i = 0; i < masses.length; i++)
            massD[i] = masses[i].doubleValue();

        // Parallel calculation
        IntStream.range(0, n).parallel().forEach(i -> {
            double px = posD[i * 3];
            double py = posD[i * 3 + 1];
            double pz = posD[i * 3 + 2];
            double mi = massD[i];

            double fx = 0;
            double fy = 0;
            double fz = 0;

            for (int j = 0; j < n; j++) {
                if (i == j)
                    continue;

                double dx = posD[j * 3] - px;
                double dy = posD[j * 3 + 1] - py;
                double dz = posD[j * 3 + 2] - pz;

                double distSq = dx * dx + dy * dy + dz * dz + softVal * softVal;
                double dist = Math.sqrt(distSq);
                double f = gVal * mi * massD[j] / (distSq * dist);

                fx += f * dx;
                fy += f * dy;
                fz += f * dz;
            }

            // Write back to forces array (thread-safe because distinct indices)
            // Note: Since we need to write back Real objects, we might encounter some
            // overhead
            // creating objects inside the parallel stream or we can collect double results
            // and write back later. But the interface requires writing to the shared array.
            // Writing to shared Real[] array is safe if indices are distinct.

            forces[i * 3] = Real.of(fx);
            forces[i * 3 + 1] = Real.of(fy);
            forces[i * 3 + 2] = Real.of(fz);
        });
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int n = masses.length;
        double soft2 = softening * softening;

        IntStream.range(0, n).parallel().forEach(i -> {
            double fx = 0, fy = 0, fz = 0;
            double xi = positions[i * 3];
            double yi = positions[i * 3 + 1];
            double zi = positions[i * 3 + 2];
            double mi = masses[i];

            for (int j = 0; j < n; j++) {
                if (i == j)
                    continue;

                double dx = positions[j * 3] - xi;
                double dy = positions[j * 3 + 1] - yi;
                double dz = positions[j * 3 + 2] - zi;

                double dist2 = dx * dx + dy * dy + dz * dz + soft2;
                double dist = Math.sqrt(dist2);
                double f = G * mi * masses[j] / (dist2 * dist);

                fx += f * dx;
                fy += f * dy;
                fz += f * dz;
            }

            forces[i * 3] = fx;
            forces[i * 3 + 1] = fy;
            forces[i * 3 + 2] = fz;
        });
    }

    @Override
    public String getName() {
        return "Multicore N-Body (CPU)";
    }
}
