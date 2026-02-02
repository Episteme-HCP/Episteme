/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.physics;

import java.util.stream.IntStream;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.MolecularDynamicsProvider;

/**
 * Multicore implementation of MolecularDynamicsProvider.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class MulticoreMolecularDynamicsProvider implements MolecularDynamicsProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public void integrate(Real[] positions, Real[] velocities, Real[] forces, Real[] masses, Real dt, Real damping) {
        int numAtoms = masses.length;
        double dtVal = dt.doubleValue();
        double dampVal = damping.doubleValue();

        IntStream.range(0, numAtoms).parallel().forEach(i -> {
            int idx = i * 3;
            double m = masses[i].doubleValue();
            if (m == 0) m = 1e-27;

            double ax = forces[idx].doubleValue() / m;
            double ay = forces[idx + 1].doubleValue() / m;
            double az = forces[idx + 2].doubleValue() / m;

            double vx = (velocities[idx].doubleValue() + ax * dtVal) * dampVal;
            double vy = (velocities[idx + 1].doubleValue() + ay * dtVal) * dampVal;
            double vz = (velocities[idx + 2].doubleValue() + az * dtVal) * dampVal;

            velocities[idx] = Real.of(vx);
            velocities[idx + 1] = Real.of(vy);
            velocities[idx + 2] = Real.of(vz);

            positions[idx] = Real.of(positions[idx].doubleValue() + vx * dtVal);
            positions[idx + 1] = Real.of(positions[idx + 1].doubleValue() + vy * dtVal);
            positions[idx + 2] = Real.of(positions[idx + 2].doubleValue() + vz * dtVal);
        });
    }

    @Override
    public void calculateBondForces(Real[] positions, Real[] forces, int[] bondIndices, Real[] bondLengths, Real[] bondConstants) {
        int numBonds = bondIndices.length / 2;
        for (int i = 0; i < numBonds; i++) {
            int idx1 = bondIndices[i * 2] * 3;
            int idx2 = bondIndices[i * 2 + 1] * 3;

            double dx = positions[idx2].doubleValue() - positions[idx1].doubleValue();
            double dy = positions[idx2 + 1].doubleValue() - positions[idx1 + 1].doubleValue();
            double dz = positions[idx2 + 2].doubleValue() - positions[idx1 + 2].doubleValue();

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
    public void integrate(double[] positions, double[] velocities, double[] forces, double[] masses, double dt, double damping) {
        int n = masses.length;
        IntStream.range(0, n).parallel().forEach(i -> {
            int idx = i * 3;
            double m = masses[i];
            for (int d = 0; d < 3; d++) {
                double a = forces[idx + d] / m;
                velocities[idx + d] = (velocities[idx + d] + a * dt) * (1.0 - damping);
                positions[idx + d] += velocities[idx + d] * dt;
                forces[idx + d] = 0;
            }
        });
    }

    @Override
    public void calculateBondForces(double[] positions, double[] forces, int[] bondIndices, double[] bondLengths, double[] bondConstants) {
        int numBonds = bondIndices.length / 2;
        for (int i = 0; i < numBonds; i++) {
            int a1 = bondIndices[i * 2] * 3;
            int a2 = bondIndices[i * 2 + 1] * 3;

            double dx = positions[a2] - positions[a1], dy = positions[a2 + 1] - positions[a1 + 1], dz = positions[a2 + 2] - positions[a1 + 2];
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double f = bondConstants[i] * (dist - bondLengths[i]);
            double fx = (dx / dist) * f, fy = (dy / dist) * f, fz = (dz / dist) * f;

            synchronized (this) {
                forces[a1] += fx; forces[a1 + 1] += fy; forces[a1 + 2] += fz;
                forces[a2] -= fx; forces[a2 + 1] -= fy; forces[a2 + 2] -= fz;
            }
        }
    }

    @Override
    public void calculateNonBondedForces(double[] positions, double[] forces, double epsilon, double sigma, double cutoff) {
        int n = positions.length / 3;
        double s6 = Math.pow(sigma, 6), s12 = s6 * s6, cutoff2 = cutoff * cutoff;

        IntStream.range(0, n).parallel().forEach(i -> {
            int a1 = i * 3;
            for (int j = i + 1; j < n; j++) {
                int a2 = j * 3;
                double dx = positions[a2] - positions[a1], dy = positions[a2 + 1] - positions[a1 + 1], dz = positions[a2 + 2] - positions[a1 + 2];
                double r2 = dx * dx + dy * dy + dz * dz;
                if (r2 < cutoff2) {
                    double r6inv = s6 / Math.pow(r2, 3), r12inv = s12 / Math.pow(r2, 6);
                    double f = 24 * epsilon * (2 * r12inv - r6inv) / r2;
                    double fx = f * dx, fy = f * dy, fz = f * dz;
                    synchronized (this) {
                        forces[a1] -= fx; forces[a1 + 1] -= fy; forces[a1 + 2] -= fz;
                        forces[a2] += fx; forces[a2 + 1] += fy; forces[a2 + 2] += fz;
                    }
                }
            }
        });
    }

    @Override
    public void calculateNonBondedForces(Real[] positions, Real[] forces, Real epsilon, Real sigma, Real cutoff) {
        int numAtoms = positions.length / 3;
        double epsVal = epsilon.doubleValue(), sigmaVal = sigma.doubleValue(), cutoffSq = Math.pow(cutoff.doubleValue(), 2);

        IntStream.range(0, numAtoms).parallel().forEach(i -> {
            int idx1 = i * 3;
            double px = positions[idx1].doubleValue(), py = positions[idx1+1].doubleValue(), pz = positions[idx1+2].doubleValue();
            double fx = 0, fy = 0, fz = 0;

            for (int j = 0; j < numAtoms; j++) {
                if (i == j) continue;
                int idx2 = j * 3;
                double dx = positions[idx2].doubleValue() - px, dy = positions[idx2+1].doubleValue() - py, dz = positions[idx2+2].doubleValue() - pz;
                double distSq = dx * dx + dy * dy + dz * dz;
                if (distSq < cutoffSq && distSq > 1e-8) {
                    double invDist2 = 1.0 / distSq;
                    double s2 = (sigmaVal * sigmaVal) * invDist2;
                    double s6 = s2 * s2 * s2;
                    double forceFactor = (24.0 * epsVal * invDist2) * s6 * (2.0 * s6 - 1.0);
                    fx -= forceFactor * dx; fy -= forceFactor * dy; fz -= forceFactor * dz;
                }
            }
            forces[idx1] = forces[idx1].add(Real.of(fx));
            forces[idx1 + 1] = forces[idx1 + 1].add(Real.of(fy));
            forces[idx1 + 2] = forces[idx1 + 2].add(Real.of(fz));
        });
    }

    @Override
    public String getName() {
        return "Multicore Molecular Dynamics (CPU)";
    }
}
