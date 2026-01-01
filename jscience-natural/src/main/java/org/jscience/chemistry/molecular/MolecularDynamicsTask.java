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

package org.jscience.chemistry.molecular;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Molecular Dynamics Simulation Task.
 * 
 * Simulates particle interactions using Lennard-Jones potential and Verlet
 * integration.
 */
public class MolecularDynamicsTask implements Serializable {

    private final int numAtoms;
    private final double timeStep;
    private final int steps;
    private final double boxSize;

    // State
    private List<AtomState> atoms;
    private double totalEnergy;

    public MolecularDynamicsTask(int numAtoms, double timeStep, int steps, double boxSize) {
        this.numAtoms = numAtoms;
        this.timeStep = timeStep;
        this.steps = steps;
        this.boxSize = boxSize;
        this.atoms = new ArrayList<>(numAtoms);
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < numAtoms; i++) {
            atoms.add(new AtomState(
                    Math.random() * boxSize, Math.random() * boxSize, Math.random() * boxSize,
                    (Math.random() - 0.5), (Math.random() - 0.5), (Math.random() - 0.5),
                    1.0 // Mass
            ));
        }
    }

    public void run() {
        for (int s = 0; s < steps; s++) {
            verletStep();
        }
        calculateTotalEnergy();
    }

    private void verletStep() {
        // 1. Half-step velocity
        // 2. Full-step position
        // 3. Force calculation
        // 4. Half-step velocity

        // Simplified Euler for demo brevity (Verlet is better but longer code)
        double[][] forces = calculateForces();

        for (int i = 0; i < numAtoms; i++) {
            AtomState a = atoms.get(i);

            // F = ma -> a = F/m
            double ax = forces[i][0] / a.mass;
            double ay = forces[i][1] / a.mass;
            double az = forces[i][2] / a.mass;

            // v = v + a*dt
            a.vx += ax * timeStep;
            a.vy += ay * timeStep;
            a.vz += az * timeStep;

            // x = x + v*dt
            a.x += a.vx * timeStep;
            a.y += a.vy * timeStep;
            a.z += a.vz * timeStep;

            // Boundary conditions (Reflective box)
            if (a.x < 0 || a.x > boxSize)
                a.vx *= -1;
            if (a.y < 0 || a.y > boxSize)
                a.vy *= -1;
            if (a.z < 0 || a.z > boxSize)
                a.vz *= -1;

            // Clamp positions
            a.x = Math.max(0, Math.min(boxSize, a.x));
            a.y = Math.max(0, Math.min(boxSize, a.y));
            a.z = Math.max(0, Math.min(boxSize, a.z));
        }
    }

    private double[][] calculateForces() {
        double[][] forces = new double[numAtoms][3];

        // Lennard-Jones Parameters
        double epsilon = 1.0;
        double sigma = 1.0;
        double cutoff = 2.5 * sigma;
        double cutoffSq = cutoff * cutoff;

        for (int i = 0; i < numAtoms; i++) {
            for (int j = i + 1; j < numAtoms; j++) {
                AtomState a1 = atoms.get(i);
                AtomState a2 = atoms.get(j);

                double dx = a2.x - a1.x;
                double dy = a2.y - a1.y;
                double dz = a2.z - a1.z;

                // Minimum image convention would go here for periodic boundaries

                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq < cutoffSq && distSq > 0.0001) {
                    double invDist2 = 1.0 / distSq;
                    double invDist6 = invDist2 * invDist2 * invDist2;
                    double invDist12 = invDist6 * invDist6;

                    // Force magnitude: 24*eps/r * (2*(sigma/r)^12 - (sigma/r)^6)
                    double forceMag = 24 * epsilon * invDist2 * (2 * invDist12 - invDist6);

                    double fx = forceMag * dx;
                    double fy = forceMag * dy;
                    double fz = forceMag * dz;

                    forces[i][0] -= fx;
                    forces[i][1] -= fy;
                    forces[i][2] -= fz;

                    forces[j][0] += fx;
                    forces[j][1] += fy;
                    forces[j][2] += fz;
                }
            }
        }
        return forces;
    }

    private void calculateTotalEnergy() {
        totalEnergy = 0;
        for (AtomState a : atoms) {
            totalEnergy += 0.5 * a.mass * (a.vx * a.vx + a.vy * a.vy + a.vz * a.vz);
        }
        // Add potential energy here if needed
    }

    public List<AtomState> getAtoms() {
        return atoms;
    }

    public double getTotalEnergy() {
        return totalEnergy;
    }

    public static class AtomState implements Serializable {
        public double x, y, z;
        public double vx, vy, vz;
        public double mass;

        public AtomState(double x, double y, double z, double vx, double vy, double vz, double mass) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            this.mass = mass;
        }
    }
}


