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

package org.jscience.physics.classical.matter.fluids;

/**
 * Smoothed Particle Hydrodynamics (SPH) fluid simulation.
 * <p>
 * Lagrangian particle-based method for incompressible fluids.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SPHFluid {

    private final int numParticles;
    private final double[] positions; // [x,y,z, ...]
    private final double[] velocities;
    private final double[] densities;
    private final double[] pressures;
    private final double[] forces;

    // Simulation parameters
    private double mass = 1.0; // Particle mass
    private double restDensity = 1000; // kg/mÃ‚Â³ (water)
    private double stiffness = 100; // Pressure stiffness
    private double viscosity = 0.1; // Viscosity coefficient
    private double smoothingRadius = 0.1; // h
    private double[] gravity = { 0, -9.81, 0 };

    public SPHFluid(int numParticles) {
        this.numParticles = numParticles;
        this.positions = new double[numParticles * 3];
        this.velocities = new double[numParticles * 3];
        this.densities = new double[numParticles];
        this.pressures = new double[numParticles];
        this.forces = new double[numParticles * 3];
    }

    public void setParticle(int i, double x, double y, double z) {
        int idx = i * 3;
        positions[idx] = x;
        positions[idx + 1] = y;
        positions[idx + 2] = z;
    }

    public void setParameters(double mass, double restDensity, double stiffness,
            double viscosity, double smoothingRadius) {
        this.mass = mass;
        this.restDensity = restDensity;
        this.stiffness = stiffness;
        this.viscosity = viscosity;
        this.smoothingRadius = smoothingRadius;
    }

    public void setGravity(double gx, double gy, double gz) {
        gravity[0] = gx;
        gravity[1] = gy;
        gravity[2] = gz;
    }

    /**
     * Poly6 smoothing kernel.
     */
    private double kernelPoly6(double r) {
        double h = smoothingRadius;
        if (r > h)
            return 0;
        double h2 = h * h;
        double r2 = r * r;
        double diff = h2 - r2;
        return 315.0 / (64.0 * Math.PI * Math.pow(h, 9)) * diff * diff * diff;
    }

    /**
     * Spiky kernel gradient magnitude (for pressure).
     */
    private double kernelSpikyGrad(double r) {
        double h = smoothingRadius;
        if (r > h || r < 1e-10)
            return 0;
        double diff = h - r;
        return -45.0 / (Math.PI * Math.pow(h, 6)) * diff * diff;
    }

    /**
     * Viscosity kernel Laplacian.
     */
    private double kernelViscosityLaplacian(double r) {
        double h = smoothingRadius;
        if (r > h)
            return 0;
        return 45.0 / (Math.PI * Math.pow(h, 6)) * (h - r);
    }

    /**
     * Compute densities and pressures.
     */
    public void computeDensityPressure() {
        for (int i = 0; i < numParticles; i++) {
            densities[i] = 0;
            int idx_i = i * 3;

            for (int j = 0; j < numParticles; j++) {
                int idx_j = j * 3;
                double dx = positions[idx_i] - positions[idx_j];
                double dy = positions[idx_i + 1] - positions[idx_j + 1];
                double dz = positions[idx_i + 2] - positions[idx_j + 2];
                double r = Math.sqrt(dx * dx + dy * dy + dz * dz);

                densities[i] += mass * kernelPoly6(r);
            }

            // Equation of state (Tait equation)
            pressures[i] = stiffness * (densities[i] - restDensity);
        }
    }

    /**
     * Compute forces (pressure + viscosity + gravity).
     */
    public void computeForces() {
        java.util.Arrays.fill(forces, 0);

        for (int i = 0; i < numParticles; i++) {
            int idx_i = i * 3;

            // Gravity
            forces[idx_i] += densities[i] * gravity[0];
            forces[idx_i + 1] += densities[i] * gravity[1];
            forces[idx_i + 2] += densities[i] * gravity[2];

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
                            / (2 * densities[j]) * kernelSpikyGrad(r);
                    forces[idx_i] += pressureForce * dx / r;
                    forces[idx_i + 1] += pressureForce * dy / r;
                    forces[idx_i + 2] += pressureForce * dz / r;

                    // Viscosity force
                    double viscForce = viscosity * mass / densities[j]
                            * kernelViscosityLaplacian(r);
                    forces[idx_i] += viscForce * (velocities[idx_j] - velocities[idx_i]);
                    forces[idx_i + 1] += viscForce * (velocities[idx_j + 1] - velocities[idx_i + 1]);
                    forces[idx_i + 2] += viscForce * (velocities[idx_j + 2] - velocities[idx_i + 2]);
                }
            }
        }
    }

    /**
     * Advance simulation by dt.
     */
    public void step(double dt) {
        computeDensityPressure();
        computeForces();

        for (int i = 0; i < numParticles; i++) {
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
        }
    }

    /**
     * Enforce boundary conditions (simple box).
     */
    public void enforceBoundary(double minX, double maxX, double minY, double maxY,
            double minZ, double maxZ, double restitution) {
        for (int i = 0; i < numParticles; i++) {
            int idx = i * 3;

            if (positions[idx] < minX) {
                positions[idx] = minX;
                velocities[idx] *= -restitution;
            } else if (positions[idx] > maxX) {
                positions[idx] = maxX;
                velocities[idx] *= -restitution;
            }

            if (positions[idx + 1] < minY) {
                positions[idx + 1] = minY;
                velocities[idx + 1] *= -restitution;
            } else if (positions[idx + 1] > maxY) {
                positions[idx + 1] = maxY;
                velocities[idx + 1] *= -restitution;
            }

            if (positions[idx + 2] < minZ) {
                positions[idx + 2] = minZ;
                velocities[idx + 2] *= -restitution;
            } else if (positions[idx + 2] > maxZ) {
                positions[idx + 2] = maxZ;
                velocities[idx + 2] *= -restitution;
            }
        }
    }

    public double[] getPositions() {
        return positions;
    }

    public double[] getVelocities() {
        return velocities;
    }

    public double[] getDensities() {
        return densities;
    }

    public int getNumParticles() {
        return numParticles;
    }

    // --- Factory ---

    /**
     * Create a block of fluid particles.
     */
    public static SPHFluid createBlock(int nx, int ny, int nz, double spacing,
            double startX, double startY, double startZ) {
        SPHFluid fluid = new SPHFluid(nx * ny * nz);
        int idx = 0;
        for (int z = 0; z < nz; z++) {
            for (int y = 0; y < ny; y++) {
                for (int x = 0; x < nx; x++) {
                    fluid.setParticle(idx++,
                            startX + x * spacing,
                            startY + y * spacing,
                            startZ + z * spacing);
                }
            }
        }
        return fluid;
    }
}


