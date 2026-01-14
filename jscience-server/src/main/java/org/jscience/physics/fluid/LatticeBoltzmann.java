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

package org.jscience.physics.fluid;

import java.io.Serializable;

/**
 * Lattice Boltzmann Method (LBM) solver using the D2Q9 model.
 * 
 * Provides a high-performance, parallelizable fluid dynamics engine.
 * 
 * <p>
 * References:
 * <ul>
 * <li>Chen, S., & Doolen, G. D. (1998). Lattice Boltzmann method for fluid
 * flows. Annual Review of Fluid Mechanics, 30(1), 329-364.</li>
 * <li>Succi, S. (2001). The lattice Boltzmann equation: for fluid dynamics and
 * beyond. Oxford University Press.</li>
 * </ul>
 * </p>
 *
 * @javadoc Complexity: O(Nx * Ny) per step (Linear with grid size).
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LatticeBoltzmann implements Serializable {

    private final int nx, ny;
    private final double tau; // Relaxation time
    private final double[][][] f; // Distribution functions [x][y][9]
    private final boolean[][] obstacle;

    // D2Q9 Directions
    // private static final int[] CX = { 0, 1, 0, -1, 0, 1, -1, -1, 1 };
    // private static final int[] CY = { 0, 0, 1, 0, -1, 1, 1, -1, -1 };
    private static final double[] W = { 4.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 36.0, 1.0 / 36.0,
            1.0 / 36.0, 1.0 / 36.0 };
    // private static final int[] OPP = { 0, 3, 4, 1, 2, 7, 8, 5, 6 }; // Opposite
    // directions

    private org.jscience.technical.backend.algorithms.LatticeBoltzmannProvider provider;
    private final org.jscience.mathematics.numbers.real.Real[][][] fReal;
    private final org.jscience.mathematics.numbers.real.Real omega;
    private final org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;

    public LatticeBoltzmann(int nx, int ny, double viscosity) {
        this.nx = nx;
        this.ny = ny;
        this.tau = 3.0 * viscosity + 0.5;
        this.f = new double[nx][ny][9];
        this.fReal = new org.jscience.mathematics.numbers.real.Real[nx][ny][9];
        this.obstacle = new boolean[nx][ny];
        this.provider = new org.jscience.technical.backend.algorithms.MulticoreLatticeBoltzmannProvider();
        this.omega = org.jscience.mathematics.numbers.real.Real.of(1.0 / tau);
        initialize();
    }

    public void setProvider(org.jscience.technical.backend.algorithms.LatticeBoltzmannProvider provider) {
        this.provider = provider;
    }

    private void initialize() {
        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                for (int i = 0; i < 9; i++) {
                    f[x][y][i] = W[i];
                    fReal[x][y][i] = org.jscience.mathematics.numbers.real.Real.of(W[i]);
                }
            }
        }
    }

    public void setObstacle(int x, int y, boolean obs) {
        if (x >= 0 && x < nx && y >= 0 && y < ny)
            obstacle[x][y] = obs;
    }

    public void step() {
        // Delegate to Provider (Multicore usually)
        provider.evolve(fReal, obstacle, omega);

        // Sync back to double[][] f for legacy getters/renderers if needed
        // Ideally we refactor App to use Real, but for minimal breakage we sync.
        // Or we assume the App uses getDensity() which we can refactor.
    }

    public double[][][] getDistributions() {
        // Sync from Real to double
        for (int x = 0; x < nx; x++)
            for (int y = 0; y < ny; y++)
                for (int i = 0; i < 9; i++)
                    f[x][y][i] = fReal[x][y][i].doubleValue();
        return f;
    }

    public double[][] getDensity() {
        double[][] rho = new double[nx][ny];
        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                org.jscience.mathematics.numbers.real.Real sum = zero;
                for (int i = 0; i < 9; i++) {
                    sum = sum.add(fReal[x][y][i]);
                }
                rho[x][y] = sum.doubleValue();
            }
        }
        return rho;
    }
}
