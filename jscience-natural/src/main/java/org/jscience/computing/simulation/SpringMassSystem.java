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

package org.jscience.computing.simulation;

/**
 * Spring-mass particle system for cloth/soft body simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpringMassSystem {

    private final double[] positions;
    private final double[] velocities;
    private final double[] forces;
    private final double[] masses;
    private final int numParticles;
    private int[] springPairs = new int[0];
    private double[] springK = new double[0];
    private double[] springRestLength = new double[0];
    private int numSprings;
    private double damping = 0.1;
    private double[] gravity = { 0, -9.81, 0 };

    public SpringMassSystem(int n) {
        numParticles = n;
        positions = new double[n * 3];
        velocities = new double[n * 3];
        forces = new double[n * 3];
        masses = new double[n];
        for (int i = 0; i < n; i++)
            masses[i] = 1.0;
    }

    public void setParticle(int i, double x, double y, double z, double mass) {
        int idx = i * 3;
        positions[idx] = x;
        positions[idx + 1] = y;
        positions[idx + 2] = z;
        masses[i] = mass;
    }

    public void addSpring(int i, int j, double k, double rest) {
        int n = numSprings + 1;
        int[] np = new int[n * 2];
        double[] nk = new double[n];
        double[] nr = new double[n];
        System.arraycopy(springPairs, 0, np, 0, numSprings * 2);
        System.arraycopy(springK, 0, nk, 0, numSprings);
        System.arraycopy(springRestLength, 0, nr, 0, numSprings);
        np[numSprings * 2] = i;
        np[numSprings * 2 + 1] = j;
        nk[numSprings] = k;
        nr[numSprings] = rest;
        springPairs = np;
        springK = nk;
        springRestLength = nr;
        numSprings = n;
    }

    public void setGravity(double gx, double gy, double gz) {
        gravity[0] = gx;
        gravity[1] = gy;
        gravity[2] = gz;
    }

    public void setDamping(double d) {
        damping = d;
    }

    public void computeForces() {
        java.util.Arrays.fill(forces, 0);
        for (int i = 0; i < numParticles; i++) {
            int idx = i * 3;
            forces[idx] += masses[i] * gravity[0];
            forces[idx + 1] += masses[i] * gravity[1];
            forces[idx + 2] += masses[i] * gravity[2];
        }
        for (int i = 0; i < numParticles * 3; i++)
            forces[i] -= damping * velocities[i];
        for (int s = 0; s < numSprings; s++) {
            int i = springPairs[s * 2], j = springPairs[s * 2 + 1];
            int ii = i * 3, jj = j * 3;
            double dx = positions[jj] - positions[ii];
            double dy = positions[jj + 1] - positions[ii + 1];
            double dz = positions[jj + 2] - positions[ii + 2];
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist < 1e-10)
                continue;
            double fMag = springK[s] * (dist - springRestLength[s]);
            double nx = dx / dist, ny = dy / dist, nz = dz / dist;
            forces[ii] += fMag * nx;
            forces[ii + 1] += fMag * ny;
            forces[ii + 2] += fMag * nz;
            forces[jj] -= fMag * nx;
            forces[jj + 1] -= fMag * ny;
            forces[jj + 2] -= fMag * nz;
        }
    }

    public void step(double dt) {
        computeForces();
        for (int i = 0; i < numParticles; i++) {
            if (Double.isInfinite(masses[i]))
                continue; // Fixed particle
            int idx = i * 3;
            double inv = 1.0 / masses[i];
            velocities[idx] += forces[idx] * inv * dt;
            velocities[idx + 1] += forces[idx + 1] * inv * dt;
            velocities[idx + 2] += forces[idx + 2] * inv * dt;
            positions[idx] += velocities[idx] * dt;
            positions[idx + 1] += velocities[idx + 1] * dt;
            positions[idx + 2] += velocities[idx + 2] * dt;
        }
    }

    public void fixParticle(int i) {
        masses[i] = Double.POSITIVE_INFINITY;
    }

    public double[] getPositions() {
        return positions;
    }

    public int getNumParticles() {
        return numParticles;
    }

    public static SpringMassSystem chain(int n, double spacing, double mass, double k) {
        SpringMassSystem s = new SpringMassSystem(n);
        for (int i = 0; i < n; i++)
            s.setParticle(i, i * spacing, 0, 0, mass);
        for (int i = 0; i < n - 1; i++)
            s.addSpring(i, i + 1, k, spacing);
        return s;
    }
}


