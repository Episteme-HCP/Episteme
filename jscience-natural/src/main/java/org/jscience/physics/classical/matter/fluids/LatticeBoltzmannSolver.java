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

import org.jscience.technical.backend.algorithms.LatticeBoltzmannProvider;
import org.jscience.technical.backend.algorithms.OpenCLLatticeBoltzmannProvider;

/**
 * 2D Lattice Boltzmann Method (LBM) solver for incompressible fluid flow.
 * Uses D2Q9 lattice (2D, 9 velocity directions).
 *
 * <p>
 * References:
 * <ul>
 * <li>Succi, S. (2001). The Lattice Boltzmann Equation: For Fluid Dynamics and
 * Beyond. Oxford University Press.</li>
 * <li>Chen, S., & Doolen, G. D. (1998). Lattice Boltzmann method for fluid
 * flows. Annual Review of Fluid Mechanics, 30(1), 329-364.</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LatticeBoltzmannSolver {

    private static final int[][] VELOCITIES_ALIGNED = {
            { 0, 0 }, { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 },
            { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 }
    };

    private static final double[] WEIGHTS_ALIGNED = {
            4.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0,
            1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0
    };

    private final int width;
    private final int height;
    private final double omega; // Relaxation parameter = 1/tau

    // Distribution functions
    private double[][][] f; // f[x][y][direction]

    // Macroscopic quantities
    private double[][] rho; // Density
    private double[][] ux; // X velocity
    private double[][] uy; // Y velocity

    // Obstacle mask
    private boolean[][] obstacle;

    private LatticeBoltzmannProvider provider;

    public void setProvider(LatticeBoltzmannProvider p) {
        this.provider = p;
    }

    /**
     * Creates an LBM solver.
     * 
     * @param width     Grid width
     * @param height    Grid height
     * @param viscosity Kinematic viscosity (determines relaxation time)
     */
    public LatticeBoltzmannSolver(int width, int height, double viscosity) {
        this.width = width;
        this.height = height;

        // tau = 3*viscosity + 0.5 (lattice units)
        double tau = 3.0 * viscosity + 0.5;
        this.omega = 1.0 / tau;

        // Initialize arrays
        f = new double[width][height][9];
        // fEq, fTemp removed
        rho = new double[width][height];
        ux = new double[width][height];
        uy = new double[width][height];
        obstacle = new boolean[width][height];

        // Initialize to equilibrium at rest (rho=1, u=0)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rho[x][y] = 1.0;
                ux[x][y] = 0.0;
                uy[x][y] = 0.0;
                for (int i = 0; i < 9; i++) {
                    f[x][y][i] = WEIGHTS_ALIGNED[i];
                }
            }
        }
    }

    /**
     * Sets an obstacle at a grid point.
     */
    public void setObstacle(int x, int y, boolean isObstacle) {
        obstacle[x][y] = isObstacle;
    }

    /**
     * Sets inlet velocity (left boundary).
     */
    public void setInletVelocity(double ux0, double uy0) {
        for (int y = 1; y < height - 1; y++) {
            ux[0][y] = ux0;
            uy[0][y] = uy0;
        }
    }

    /**
     * Performs one LBM time step using the provider.
     */
    public void step() {
        if (provider == null) {
            provider = new OpenCLLatticeBoltzmannProvider();
        }

        // Evolve using provider (Collision + Streaming + BCs)
        provider.evolve(f, obstacle, omega);

        // 4. Compute macroscopic quantities (still needed for visualization/access)
        // If provider computed them internally it discarded them. We recompute or ask
        // provider.
        // Current API recomputes.
        computeMacroscopic();
    }

    private void computeMacroscopic() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y]) {
                    rho[x][y] = 0;
                    ux[x][y] = 0;
                    uy[x][y] = 0;
                    continue;
                }

                double localRho = 0;
                double localUx = 0;
                double localUy = 0;

                for (int i = 0; i < 9; i++) {
                    localRho += f[x][y][i];
                    localUx += f[x][y][i] * VELOCITIES_ALIGNED[i][0];
                    localUy += f[x][y][i] * VELOCITIES_ALIGNED[i][1];
                }

                if (localRho > 0) {
                    ux[x][y] = localUx / localRho;
                    uy[x][y] = localUy / localRho;
                } else {
                    ux[x][y] = 0;
                    uy[x][y] = 0;
                }
                rho[x][y] = localRho;
            }
        }
    }

    // Accessors
    public double[][] getDensity() {
        return rho;
    }

    public double[][] getVelocityX() {
        return ux;
    }

    public double[][] getVelocityY() {
        return uy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
