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

import java.util.stream.IntStream;
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
    public void integrate(double[] positions, double[] velocities, double[] forces, double[] masses, double dt,
            double damping) {
        int n = masses.length;
        IntStream.range(0, n).parallel().forEach(i -> {
            int idx = i * 3;
            double m = masses[i];
            for (int d = 0; d < 3; d++) {
                double a = forces[idx + d] / m;
                velocities[idx + d] = (velocities[idx + d] + a * dt) * (1.0 - damping);
                positions[idx + d] += velocities[idx + d] * dt;
                forces[idx + d] = 0; // Reset for next step
            }
        });
    }

    @Override
    public void calculateBondForces(double[] positions, double[] forces, int[] bondIndices, double[] bondLengths,
            double[] bondConstants) {
        int numBonds = bondIndices.length / 2;
        for (int i = 0; i < numBonds; i++) {
            int a1 = bondIndices[i * 2] * 3;
            int a2 = bondIndices[i * 2 + 1] * 3;

            double dx = positions[a2] - positions[a1];
            double dy = positions[a2 + 1] - positions[a1 + 1];
            double dz = positions[a2 + 2] - positions[a1 + 2];

            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double diff = dist - bondLengths[i];
            double f = bondConstants[i] * diff;

            double fx = (dx / dist) * f;
            double fy = (dy / dist) * f;
            double fz = (dz / dist) * f;

            // Atomic updates for thread safety
            synchronized (this) {
                forces[a1] += fx;
                forces[a1 + 1] += fy;
                forces[a1 + 2] += fz;
                forces[a2] -= fx;
                forces[a2 + 1] -= fy;
                forces[a2 + 2] -= fz;
            }
        }
    }

    @Override
    public void calculateNonBondedForces(double[] positions, double[] forces, double epsilon, double sigma,
            double cutoff) {
        int n = positions.length / 3;
        double s6 = Math.pow(sigma, 6);
        double s12 = s6 * s6;
        double cutoff2 = cutoff * cutoff;

        IntStream.range(0, n).parallel().forEach(i -> {
            int a1 = i * 3;
            for (int j = i + 1; j < n; j++) {
                int a2 = j * 3;
                double dx = positions[a2] - positions[a1];
                double dy = positions[a2 + 1] - positions[a1 + 1];
                double dz = positions[a2 + 2] - positions[a1 + 2];

                double r2 = dx * dx + dy * dy + dz * dz;
                if (r2 < cutoff2) {
                    double r6inv = s6 / Math.pow(r2, 3);
                    double r12inv = s12 / Math.pow(r2, 6);
                    double f = 24 * epsilon * (2 * r12inv - r6inv) / r2;

                    double fx = f * dx;
                    double fy = f * dy;
                    double fz = f * dz;

                    synchronized (this) {
                        forces[a1] -= fx;
                        forces[a1 + 1] -= fy;
                        forces[a1 + 2] -= fz;
                        forces[a2] += fx;
                        forces[a2 + 1] += fy;
                        forces[a2 + 2] += fz;
                    }
                }
            }
        });
    }

    @Override
    public String getName() {
        return "Multicore Molecular Dynamics";
    }

    @Override
    public void calculateNonBondedForces(Real[] positions, Real[] forces, Real epsilon, Real sigma, Real cutoff) {
        int numAtoms = positions.length / 3;
        double epsVal = epsilon.doubleValue();
        double sigmaVal = sigma.doubleValue();
        double cutoffVal = cutoff.doubleValue();
        double cutoffSq = cutoffVal * cutoffVal;

        java.util.stream.IntStream.range(0, numAtoms).parallel().forEach(i -> {
            int idx1 = i * 3;
            double px = positions[idx1].doubleValue();
            double py = positions[idx1 + 1].doubleValue();
            double pz = positions[idx1 + 2].doubleValue();

            double fx = 0, fy = 0, fz = 0;

            for (int j = 0; j < numAtoms; j++) {
                if (i == j)
                    continue;

                int idx2 = j * 3;
                double dx = positions[idx2].doubleValue() - px;
                double dy = positions[idx2 + 1].doubleValue() - py;
                double dz = positions[idx2 + 2].doubleValue() - pz;

                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq < cutoffSq && distSq > 1e-8) {
                    double invDist2 = 1.0 / distSq;
                    // double invDist6 = invDist2 * invDist2 * invDist2;
                    // double invDist12 = invDist6 * invDist6;

                    double sigmaSq = sigmaVal * sigmaVal;
                    double s2 = sigmaSq * invDist2;
                    double s6 = s2 * s2 * s2; // (sigma^2/r^2)^3 = (sigma/r)^6

                    // Force Factor F/r
                    // F = 24 eps/r2 * s6 * (2*s6 - 1)
                    double forceFactor = (24.0 * epsVal * invDist2) * s6 * (2.0 * s6 - 1.0);

                    fx -= forceFactor * dx;
                    fy -= forceFactor * dy;
                    fz -= forceFactor * dz;
                }
            }

            Real rx = Real.of(fx);
            Real ry = Real.of(fy);
            Real rz = Real.of(fz);

            // Assume thread safety for simple accumulation if multiple threads targeted
            // different 'i'
            // We are writing to idx1 (i). No other thread writes to 'i'.
            // However, forces is shared.
            // array[idx] = val is safe.
            // But forces[idx].add(...) reads old val. If old val is effectively
            // final/stable, it's ok.
            // integrate() clears forces? No, integrate USES forces.
            // We assume forces are cleared before this call or we are accumulating on top
            // of 0.
            // We do a read-modify-write on forces[idx1].
            // Only thread 'i' touches forces[idx1]. So it's safe.

            forces[idx1] = forces[idx1].add(rx);
            forces[idx1 + 1] = forces[idx1 + 1].add(ry);
            forces[idx1 + 2] = forces[idx1 + 2].add(rz);
        });
    }
}
