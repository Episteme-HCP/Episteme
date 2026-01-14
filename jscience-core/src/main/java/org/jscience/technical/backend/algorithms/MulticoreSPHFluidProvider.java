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

import java.util.stream.IntStream;
import java.util.Arrays;

/**
 * Multicore CPU implementation of SPH Fluid Provider.
 * Uses Java Streams for parallelization.
 */
public class MulticoreSPHFluidProvider implements SPHFluidProvider {

    @Override
    public void step(double[] positions, double[] velocities, double[] densities, double[] pressures, double[] forces,
            int numParticles, double dt, double mass, double restDensity, double stiffness, double viscosity,
            double smoothingRadius, double[] gravity) {

        // 1. Compute Density and Pressure
        IntStream.range(0, numParticles).parallel().forEach(i -> {
            double density = 0;
            int idx_i = i * 3;

            for (int j = 0; j < numParticles; j++) {
                int idx_j = j * 3;
                double dx = positions[idx_i] - positions[idx_j];
                double dy = positions[idx_i + 1] - positions[idx_j + 1];
                double dz = positions[idx_i + 2] - positions[idx_j + 2];
                double r2 = dx * dx + dy * dy + dz * dz;

                // Optimization: skip sqrt if > h^2 ? No, kernel needs r
                // But we can check r2 < h2
                double r = Math.sqrt(r2);
                density += mass * kernelPoly6(r, smoothingRadius);
            }
            densities[i] = density;
            // Tait equation
            pressures[i] = stiffness * (density - restDensity);
        });

        // 2. Compute Forces
        // We need to clear forces first? Yes.
        // But inside parallel loop, we can't easily clear simpler than Arrays.fill
        Arrays.fill(forces, 0);
        // Note: Arrays.fill is sequential. For large arrays parallel setAll might be
        // faster but O(N) is small part compared to O(N^2)

        // However, we accumulate forces.
        // The original logic accumulated gravity then interactions.
        // In parallel, writing to 'forces' array at 'idx_i' is safe (one thread per i).

        IntStream.range(0, numParticles).parallel().forEach(i -> {
            int idx_i = i * 3;
            double fx = 0, fy = 0, fz = 0;

            // Gravity
            fx += densities[i] * gravity[0];
            fy += densities[i] * gravity[1];
            fz += densities[i] * gravity[2];

            for (int j = 0; j < numParticles; j++) {
                if (i == j)
                    continue;

                int idx_j = j * 3;
                double dx = positions[idx_i] - positions[idx_j];
                double dy = positions[idx_i + 1] - positions[idx_j + 1];
                double dz = positions[idx_i + 2] - positions[idx_j + 2];
                double r = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (r < smoothingRadius && r > 1e-10) {
                    // Pressure force
                    double pressureForce = -mass * (pressures[i] + pressures[j])
                            / (2 * densities[j]) * kernelSpikyGrad(r, smoothingRadius);

                    fx += pressureForce * dx / r;
                    fy += pressureForce * dy / r;
                    fz += pressureForce * dz / r;

                    // Viscosity force
                    double viscForce = viscosity * mass / densities[j]
                            * kernelViscosityLaplacian(r, smoothingRadius);

                    fx += viscForce * (velocities[idx_j] - velocities[idx_i]);
                    fy += viscForce * (velocities[idx_j + 1] - velocities[idx_i + 1]);
                    fz += viscForce * (velocities[idx_j + 2] - velocities[idx_i + 2]);
                }
            }
            forces[idx_i] = fx;
            forces[idx_i + 1] = fy;
            forces[idx_i + 2] = fz;
        });

        // 3. Integration
        IntStream.range(0, numParticles).parallel().forEach(i -> {
            int idx = i * 3;
            double invDensity = 1.0 / Math.max(densities[i], 1e-10);

            // Update velocity
            velocities[idx] += dt * forces[idx] * invDensity;
            velocities[idx + 1] += dt * forces[idx + 1] * invDensity;
            velocities[idx + 2] += dt * forces[idx + 2] * invDensity;

            // Update position
            positions[idx] += dt * velocities[idx];
            positions[idx + 1] += dt * velocities[idx + 1];
            positions[idx + 2] += dt * velocities[idx + 2];
        });
    }

    // --- Kernels ---

    private double kernelPoly6(double r, double h) {
        if (r > h)
            return 0;
        double h2 = h * h;
        double r2 = r * r;
        double diff = h2 - r2;
        return 315.0 / (64.0 * Math.PI * Math.pow(h, 9)) * diff * diff * diff;
    }

    private double kernelSpikyGrad(double r, double h) {
        if (r > h || r < 1e-10)
            return 0;
        double diff = h - r;
        return -45.0 / (Math.PI * Math.pow(h, 6)) * diff * diff;
    }

    private double kernelViscosityLaplacian(double r, double h) {
        if (r > h)
            return 0;
        return 45.0 / (Math.PI * Math.pow(h, 6)) * (h - r);
    }

    @Override
    public String getName() {
        return "Multicore CPU (Java Streams)";
    }
}
