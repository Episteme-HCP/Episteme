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

package org.jscience.physics.classical.mechanics;

import org.jscience.mathematics.numbers.real.Real;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Velocity Verlet Integrator for Molecular Dynamics.
 * 
 * Provides stable time integration for particle systems.
 * Uses Cell-Linked Lists for O(N) neighbor search.
 * 
 * <p>
 * References:
 * <ul>
 * <li>Verlet, L. (1967). Computer "experiments" on classical fluids. I.
 * Thermodynamical properties of Lennard-Jones molecules. Physical Review,
 * 159(1), 98.</li>
 * <li>Swope, W. C., et al. (1982). A computer simulation method for the
 * calculation of equilibrium constants for the formation of physical clusters
 * of
 * molecules: Application to small clusters of Ar. The Journal of Chemical
 * Physics, 76(1), 637-649.</li>
 * </ul>
 * <p>
 * References:
 * <ul>
 * <li>Verlet, L. (1967). Computer "experiments" on classical fluids. I.
 * Thermodynamical properties of units of Lennard-Jones molecules. Physical
 * Review, 159(1), 98.</li>
 * <li>Hockney, R. W., & Eastwood, J. W. (1981). Computer Simulation Using
 * Particles. McGraw-Hill. (Cell-Linked Lists for O(N) complexity)</li>
 * </ul>
 * </p>
 *
 * @javadoc Complexity: O(N) per step due to Cell-Linked Lists.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class VelocityVerlet implements Serializable {

    private final List<Particle> particles;
    private final double cutoff;
    private final double boxSize;
    private final int numCells;
    private final List<Particle>[][] cells;

    @SuppressWarnings("unchecked")
    public VelocityVerlet(List<Particle> particles, double boxSize, double cutoff) {
        this.particles = particles;
        this.boxSize = boxSize;
        this.cutoff = cutoff;
        this.numCells = (int) Math.floor(boxSize / cutoff);
        this.cells = (List<Particle>[][]) new ArrayList[numCells][numCells];
        for (int i = 0; i < numCells; i++)
            for (int j = 0; j < numCells; j++)
                cells[i][j] = new ArrayList<>();
    }

    public void step(double dt) {
        // 1. Half-step velocity and full-step position
        for (Particle p : particles) {
            double vx = p.getVelocity().get(0).doubleValue();
            double vy = p.getVelocity().get(1).doubleValue();
            double ax = p.getAcceleration().get(0).doubleValue();
            double ay = p.getAcceleration().get(1).doubleValue();

            p.setPosition(Real.of(vx + 0.5 * ax * dt), Real.of(vy + 0.5 * ay * dt), Real.ZERO);
            p.setPosition(Real.of(p.getX() + p.getVelocity().get(0).doubleValue() * dt),
                    Real.of(p.getY() + p.getVelocity().get(1).doubleValue() * dt), Real.ZERO);

            // Periodic boundary conditions
            p.setPosition(Real.of((p.getX() + boxSize) % boxSize), Real.of((p.getY() + boxSize) % boxSize), Real.ZERO);
        }

        // 2. Update forces (O(N) via Cells)
        updateForces();

        // 3. Complete velocity step
        for (Particle p : particles) {
            double vx = p.getVelocity().get(0).doubleValue();
            double vy = p.getVelocity().get(1).doubleValue();
            double ax = p.getAcceleration().get(0).doubleValue();
            double ay = p.getAcceleration().get(1).doubleValue();

            p.setVelocity(vx + 0.5 * ax * dt, vy + 0.5 * ay * dt, 0);
        }
    }

    private void updateForces() {
        // Reset and populate cells
        for (int i = 0; i < numCells; i++)
            for (int j = 0; j < numCells; j++)
                cells[i][j].clear();

        for (Particle p : particles) {
            int cx = (int) (p.getX() / cutoff) % numCells;
            int cy = (int) (p.getY() / cutoff) % numCells;
            cells[cx][cy].add(p);
        }

        for (Particle p : particles)
            p.setAcceleration(0, 0, 0);

        // Lennard-Jones pairwise forces
        for (int i = 0; i < numCells; i++) {
            for (int j = 0; j < numCells; j++) {
                for (Particle p1 : cells[i][j]) {
                    // Check local and neighboring cells
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int ni = (i + dx + numCells) % numCells;
                            int nj = (j + dy + numCells) % numCells;
                            for (Particle p2 : cells[ni][nj]) {
                                if (p1 != p2)
                                    applyLJForce(p1, p2);
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyLJForce(Particle p1, Particle p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();

        // Minimum image convention
        if (dx > boxSize / 2)
            dx -= boxSize;
        if (dx < -boxSize / 2)
            dx += boxSize;
        if (dy > boxSize / 2)
            dy -= boxSize;
        if (dy < -boxSize / 2)
            dy += boxSize;

        double r2 = dx * dx + dy * dy;
        if (r2 < cutoff * cutoff) {
            double r2inv = 1.0 / r2;
            double r6inv = r2inv * r2inv * r2inv;
            // Lennard-Jones 12-6 force: F = 24 * epsilon * (2*(sigma/r)^12 - (sigma/r)^6) /
            // r
            // Simplified with sigma = 1, epsilon = 1
            double force = 24 * r6inv * (2 * r6inv - 1) * r2inv;

            p1.setAcceleration(
                    p1.getAcceleration().get(0).doubleValue() + force * dx / p1.getMassValue(),
                    p1.getAcceleration().get(1).doubleValue() + force * dy / p1.getMassValue(), 0);
        }
    }
}
