/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.matter.fluids.providers;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.matter.fluids.SPHFluidProvider;
import com.google.auto.service.AutoService;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.util.stream.IntStream;
import java.util.Arrays;

/**
 * Multicore CPU implementation of SPH Fluid Provider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreSPHFluidProvider implements SPHFluidProvider {

    @Override
    public int getPriority() {
        return 40;
    }

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
                double r = Math.sqrt(r2);
                density += mass * kernelPoly6(r, smoothingRadius);
            }
            densities[i] = density;
            pressures[i] = stiffness * (density - restDensity);
        });

        // 2. Compute Forces
        Arrays.fill(forces, 0);

        IntStream.range(0, numParticles).parallel().forEach(i -> {
            int idx_i = i * 3;
            double fx = 0, fy = 0, fz = 0;

            fx += densities[i] * gravity[0];
            fy += densities[i] * gravity[1];
            fz += densities[i] * gravity[2];

            for (int j = 0; j < numParticles; j++) {
                if (i == j) continue;

                int idx_j = j * 3;
                double dx = positions[idx_i] - positions[idx_j];
                double dy = positions[idx_i + 1] - positions[idx_j + 1];
                double dz = positions[idx_i + 2] - positions[idx_j + 2];
                double r = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (r < smoothingRadius && r > 1e-10) {
                    double pressureForce = -mass * (pressures[i] + pressures[j])
                            / (2 * densities[j]) * kernelSpikyGrad(r, smoothingRadius);

                    fx += pressureForce * dx / r;
                    fy += pressureForce * dy / r;
                    fz += pressureForce * dz / r;

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

            velocities[idx] += dt * forces[idx] * invDensity;
            velocities[idx + 1] += dt * forces[idx + 1] * invDensity;
            velocities[idx + 2] += dt * forces[idx + 2] * invDensity;

            positions[idx] += dt * velocities[idx];
            positions[idx + 1] += dt * velocities[idx + 1];
            positions[idx + 2] += dt * velocities[idx + 2];
        });
    }

    @Override
    public void stepReal(Real[] positions, Real[] velocities, Real[] densities, Real[] pressures, Real[] forces,
            int numParticles, Real dt, Real mass, Real restDensity, Real stiffness, Real viscosity,
            Real smoothingRadius, Real[] gravity) {
        
        // Parallel Density and Pressure computation
        IntStream.range(0, numParticles).parallel().forEach(i -> {
            Real density = Real.ZERO;
            int idx_i = i * 3;

            for (int j = 0; j < numParticles; j++) {
                int idx_j = j * 3;
                Real dx = positions[idx_i].subtract(positions[idx_j]);
                Real dy = positions[idx_i + 1].subtract(positions[idx_j + 1]);
                Real dz = positions[idx_i + 2].subtract(positions[idx_j + 2]);
                Real r2 = dx.multiply(dx).add(dy.multiply(dy)).add(dz.multiply(dz));
                Real r = r2.sqrt();
                density = density.add(mass.multiply(kernelPoly6Real(r, smoothingRadius)));
            }
            densities[i] = density;
            pressures[i] = stiffness.multiply(density.subtract(restDensity));
        });

        // Reset forces
        for(int i=0; i<numParticles*3; i++) forces[i] = Real.ZERO;

        // Force computation
        IntStream.range(0, numParticles).parallel().forEach(i -> {
            int idx_i = i * 3;
            Real fx = densities[i].multiply(gravity[0]);
            Real fy = densities[i].multiply(gravity[1]);
            Real fz = densities[i].multiply(gravity[2]);

            for (int j = 0; j < numParticles; j++) {
                if (i == j) continue;

                int idx_j = j * 3;
                Real dx = positions[idx_i].subtract(positions[idx_j]);
                Real dy = positions[idx_i + 1].subtract(positions[idx_j + 1]);
                Real dz = positions[idx_i + 2].subtract(positions[idx_j + 2]);
                Real r2 = dx.multiply(dx).add(dy.multiply(dy)).add(dz.multiply(dz));
                Real r = r2.sqrt();

                if (r.compareTo(smoothingRadius) < 0 && r.compareTo(Real.of(1e-10)) > 0) {
                    // Pressure force
                    Real pressureForce = mass.multiply(pressures[i].add(pressures[j]))
                            .divide(densities[j].multiply(Real.of(2.0))).negate().multiply(kernelSpikyGradReal(r, smoothingRadius));

                    fx = fx.add(pressureForce.multiply(dx).divide(r));
                    fy = fy.add(pressureForce.multiply(dy).divide(r));
                    fz = fz.add(pressureForce.multiply(dz).divide(r));

                    // Viscosity force
                    Real viscForce = viscosity.multiply(mass).divide(densities[j])
                            .multiply(kernelViscosityLaplacianReal(r, smoothingRadius));

                    fx = fx.add(viscForce.multiply(velocities[idx_j].subtract(velocities[idx_i])));
                    fy = fy.add(viscForce.multiply(velocities[idx_j + 1].subtract(velocities[idx_i + 1])));
                    fz = fz.add(viscForce.multiply(velocities[idx_j + 2].subtract(velocities[idx_i + 2])));
                }
            }
            forces[idx_i] = fx;
            forces[idx_i + 1] = fy;
            forces[idx_i + 2] = fz;
        });

        // Integration
        IntStream.range(0, numParticles).parallel().forEach(i -> {
            int idx = i * 3;
            Real invDensity = Real.ONE.divide(densities[i].compareTo(Real.of(1e-10)) > 0 ? densities[i] : Real.of(1e-10));

            velocities[idx] = velocities[idx].add(dt.multiply(forces[idx]).multiply(invDensity));
            velocities[idx + 1] = velocities[idx + 1].add(dt.multiply(forces[idx + 1]).multiply(invDensity));
            velocities[idx + 2] = velocities[idx + 2].add(dt.multiply(forces[idx + 2]).multiply(invDensity));

            positions[idx] = positions[idx].add(dt.multiply(velocities[idx]));
            positions[idx + 1] = positions[idx + 1].add(dt.multiply(velocities[idx + 1]));
            positions[idx + 2] = positions[idx + 2].add(dt.multiply(velocities[idx + 2]));
        });
    }

    private double kernelPoly6(double r, double h) {
        if (r > h) return 0;
        double h2 = h * h, r2 = r * r, diff = h2 - r2;
        return 315.0 / (64.0 * Math.PI * Math.pow(h, 9)) * diff * diff * diff;
    }

    private Real kernelPoly6Real(Real r, Real h) {
        if (r.compareTo(h) > 0) return Real.ZERO;
        Real h2 = h.multiply(h), r2 = r.multiply(r), diff = h2.subtract(r2);
        Real factor = Real.of(315.0).divide(Real.of(64.0).multiply(Real.of(Math.PI)).multiply(h.pow(9)));
        return factor.multiply(diff.multiply(diff).multiply(diff));
    }

    private double kernelSpikyGrad(double r, double h) {
        if (r > h || r < 1e-10) return 0;
        double diff = h - r;
        return -45.0 / (Math.PI * Math.pow(h, 6)) * diff * diff;
    }

    private Real kernelSpikyGradReal(Real r, Real h) {
        if (r.compareTo(h) > 0 || r.compareTo(Real.of(1e-10)) < 0) return Real.ZERO;
        Real diff = h.subtract(r);
        Real factor = Real.of(-45.0).divide(Real.of(Math.PI).multiply(h.pow(6)));
        return factor.multiply(diff.multiply(diff));
    }

    private double kernelViscosityLaplacian(double r, double h) {
        if (r > h) return 0;
        return 45.0 / (Math.PI * Math.pow(h, 6)) * (h - r);
    }

    private Real kernelViscosityLaplacianReal(Real r, Real h) {
        if (r.compareTo(h) > 0) return Real.ZERO;
        return Real.of(45.0).divide(Real.of(Math.PI).multiply(h.pow(6))).multiply(h.subtract(r));
    }

    @Override
    public String getName() {
        return "Multicore SPH Fluid (CPU)";
    }
}
