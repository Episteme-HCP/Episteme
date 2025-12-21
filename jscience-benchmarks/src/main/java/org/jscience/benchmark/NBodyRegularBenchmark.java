/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.benchmark;

/**
 * N-body gravitational simulation benchmark - NAIVE OPTIMIZED IMPLEMENTATION.
 * <p>
 * Uses primitive arrays for maximum performance. Serves as baseline
 * comparison for the JScience API-based implementation.
 * </p>
 * <p>
 * Tests performance of direct N-body gravitational simulation
 * with O(N²) force calculations.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NBodyRegularBenchmark {

    /** Gravitational constant (m³/(kg·s²)) */
    private static final double G = 6.67430e-11;

    /** Default time step (seconds) */
    private static final double DT = 3600.0; // 1 hour

    private final int numBodies;
    private final double[] masses;
    private final double[] px, py, pz; // Positions
    private final double[] vx, vy, vz; // Velocities
    private final double[] fx, fy, fz; // Forces

    /**
     * Creates N-body simulation with random initial conditions.
     * 
     * @param numBodies number of bodies
     */
    public NBodyRegularBenchmark(int numBodies) {
        this.numBodies = numBodies;
        this.masses = new double[numBodies];
        this.px = new double[numBodies];
        this.py = new double[numBodies];
        this.pz = new double[numBodies];
        this.vx = new double[numBodies];
        this.vy = new double[numBodies];
        this.vz = new double[numBodies];
        this.fx = new double[numBodies];
        this.fy = new double[numBodies];
        this.fz = new double[numBodies];

        initializeSolarSystemLike();
    }

    /**
     * Initialize with solar-system-like distribution.
     */
    private void initializeSolarSystemLike() {
        java.util.Random rand = new java.util.Random(42);

        // Central star
        masses[0] = 1.989e30; // Solar mass
        px[0] = py[0] = pz[0] = 0;
        vx[0] = vy[0] = vz[0] = 0;

        // Planets/asteroids in orbit
        for (int i = 1; i < numBodies; i++) {
            masses[i] = 1e20 + rand.nextDouble() * 1e25; // Asteroid to planet mass

            // Position in orbital plane
            double r = 1e11 + rand.nextDouble() * 5e12; // 1 AU to 30 AU
            double theta = rand.nextDouble() * 2 * Math.PI;
            px[i] = r * Math.cos(theta);
            py[i] = r * Math.sin(theta);
            pz[i] = (rand.nextDouble() - 0.5) * 1e10; // Small z-component

            // Circular orbital velocity
            double v = Math.sqrt(G * masses[0] / r);
            vx[i] = -v * Math.sin(theta);
            vy[i] = v * Math.cos(theta);
            vz[i] = 0;
        }
    }

    /**
     * Calculate forces between all pairs (O(N²)).
     */
    public void calculateForces() {
        // Reset forces
        for (int i = 0; i < numBodies; i++) {
            fx[i] = fy[i] = fz[i] = 0;
        }

        // Pairwise force calculation
        for (int i = 0; i < numBodies; i++) {
            for (int j = i + 1; j < numBodies; j++) {
                double dx = px[j] - px[i];
                double dy = py[j] - py[i];
                double dz = pz[j] - pz[i];

                double distSq = dx * dx + dy * dy + dz * dz;
                double dist = Math.sqrt(distSq);

                // Softening to avoid singularity
                double softening = 1e9;
                double denom = distSq + softening * softening;

                double force = G * masses[i] * masses[j] / denom;
                double fx_ij = force * dx / dist;
                double fy_ij = force * dy / dist;
                double fz_ij = force * dz / dist;

                fx[i] += fx_ij;
                fy[i] += fy_ij;
                fz[i] += fz_ij;

                fx[j] -= fx_ij;
                fy[j] -= fy_ij;
                fz[j] -= fz_ij;
            }
        }
    }

    /**
     * Update velocities using calculated forces (leapfrog half-step).
     */
    public void updateVelocities(double dt) {
        for (int i = 0; i < numBodies; i++) {
            double ax = fx[i] / masses[i];
            double ay = fy[i] / masses[i];
            double az = fz[i] / masses[i];

            vx[i] += ax * dt;
            vy[i] += ay * dt;
            vz[i] += az * dt;
        }
    }

    /**
     * Update positions using velocities.
     */
    public void updatePositions(double dt) {
        for (int i = 0; i < numBodies; i++) {
            px[i] += vx[i] * dt;
            py[i] += vy[i] * dt;
            pz[i] += vz[i] * dt;
        }
    }

    /**
     * Perform one simulation step (leapfrog integration).
     */
    public void step(double dt) {
        updateVelocities(dt / 2);
        updatePositions(dt);
        calculateForces();
        updateVelocities(dt / 2);
    }

    /**
     * Calculate total system energy (kinetic + potential).
     */
    public double totalEnergy() {
        double kinetic = 0;
        for (int i = 0; i < numBodies; i++) {
            double v2 = vx[i] * vx[i] + vy[i] * vy[i] + vz[i] * vz[i];
            kinetic += 0.5 * masses[i] * v2;
        }

        double potential = 0;
        for (int i = 0; i < numBodies; i++) {
            for (int j = i + 1; j < numBodies; j++) {
                double dx = px[j] - px[i];
                double dy = py[j] - py[i];
                double dz = pz[j] - pz[i];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                potential -= G * masses[i] * masses[j] / dist;
            }
        }

        return kinetic + potential;
    }

    /**
     * Run benchmark simulation.
     * 
     * @param numBodies number of bodies
     * @param numSteps  number of simulation steps
     * @return time in milliseconds
     */
    public static long runBenchmark(int numBodies, int numSteps) {
        NBodyRegularBenchmark sim = new NBodyRegularBenchmark(numBodies);
        sim.calculateForces(); // Initial force calculation

        long start = System.nanoTime();
        for (int i = 0; i < numSteps; i++) {
            sim.step(DT);
        }
        long end = System.nanoTime();

        return (end - start) / 1_000_000; // Convert to ms
    }

    /**
     * Main benchmark runner.
     */
    public static void main(String[] args) {
        System.out.println("N-Body Gravitational Simulation Benchmark");
        System.out.println("==========================================");

        int[] bodyCounts = { 100, 500, 1000, 2000 };
        int steps = 100;

        for (int n : bodyCounts) {
            // Warmup
            runBenchmark(n, 10);

            // Actual benchmark
            long time = runBenchmark(n, steps);
            double stepsPerSec = steps * 1000.0 / time;
            double pairwiseOps = (long) n * (n - 1) / 2 * steps;

            System.out.printf("N=%4d: %6d ms for %d steps (%.1f steps/s, %.1e interactions)%n",
                    n, time, steps, stepsPerSec, pairwiseOps);
        }
    }
}