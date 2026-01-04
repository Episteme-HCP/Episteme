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
    private final org.jscience.mathematics.numbers.real.Real omega; // Relaxation parameter = 1/tau

    // Distribution functions
    private org.jscience.mathematics.numbers.real.Real[][][] f; // f[x][y][direction]

    // Macroscopic quantities
    private org.jscience.mathematics.numbers.real.Real[][] rho; // Density
    private org.jscience.mathematics.numbers.real.Real[][] ux; // X velocity
    private org.jscience.mathematics.numbers.real.Real[][] uy; // Y velocity

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
        // omega = 1 / tau
        org.jscience.mathematics.numbers.real.Real v = org.jscience.mathematics.numbers.real.Real.of(viscosity);
        org.jscience.mathematics.numbers.real.Real tau = v.multiply(org.jscience.mathematics.numbers.real.Real.of(3.0))
                .add(org.jscience.mathematics.numbers.real.Real.of(0.5));
        this.omega = org.jscience.mathematics.numbers.real.Real.ONE.divide(tau);

        // Initialize arrays
        f = new org.jscience.mathematics.numbers.real.Real[width][height][9];
        rho = new org.jscience.mathematics.numbers.real.Real[width][height];
        ux = new org.jscience.mathematics.numbers.real.Real[width][height];
        uy = new org.jscience.mathematics.numbers.real.Real[width][height];
        obstacle = new boolean[width][height];

        org.jscience.mathematics.numbers.real.Real one = org.jscience.mathematics.numbers.real.Real.ONE;
        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;

        // Initialize to equilibrium at rest (rho=1, u=0)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rho[x][y] = one;
                ux[x][y] = zero;
                uy[x][y] = zero;
                for (int i = 0; i < 9; i++) {
                    f[x][y][i] = org.jscience.mathematics.numbers.real.Real.of(WEIGHTS_ALIGNED[i]);
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
        org.jscience.mathematics.numbers.real.Real rUx = org.jscience.mathematics.numbers.real.Real.of(ux0);
        org.jscience.mathematics.numbers.real.Real rUy = org.jscience.mathematics.numbers.real.Real.of(uy0);
        for (int y = 1; y < height - 1; y++) {
            ux[0][y] = rUx;
            uy[0][y] = rUy;
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
        computeMacroscopic();
    }

    private void computeMacroscopic() {
        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y]) {
                    rho[x][y] = zero;
                    ux[x][y] = zero;
                    uy[x][y] = zero;
                    continue;
                }

                org.jscience.mathematics.numbers.real.Real localRho = zero;
                org.jscience.mathematics.numbers.real.Real localUx = zero;
                org.jscience.mathematics.numbers.real.Real localUy = zero;

                for (int i = 0; i < 9; i++) {
                    localRho = localRho.add(f[x][y][i]);
                    // localUx += f * cx
                    localUx = localUx.add(f[x][y][i]
                            .multiply(org.jscience.mathematics.numbers.real.Real.of(VELOCITIES_ALIGNED[i][0])));
                    // localUy += f * cy
                    localUy = localUy.add(f[x][y][i]
                            .multiply(org.jscience.mathematics.numbers.real.Real.of(VELOCITIES_ALIGNED[i][1])));
                }

                if (localRho.compareTo(zero) > 0) {
                    ux[x][y] = localUx.divide(localRho);
                    uy[x][y] = localUy.divide(localRho);
                } else {
                    ux[x][y] = zero;
                    uy[x][y] = zero;
                }
                rho[x][y] = localRho;
            }
        }
    }

    // Accessors - converting to double for visualization compatibility
    public double[][] getDensity() {
        double[][] res = new double[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                res[x][y] = rho[x][y].doubleValue();
        return res;
    }

    public double[][] getVelocityX() {
        double[][] res = new double[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                res[x][y] = ux[x][y].doubleValue();
        return res;
    }

    public double[][] getVelocityY() {
        double[][] res = new double[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                res[x][y] = uy[x][y].doubleValue();
        return res;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
