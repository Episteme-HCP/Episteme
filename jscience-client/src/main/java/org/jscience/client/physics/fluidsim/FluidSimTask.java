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

package org.jscience.client.physics.fluidsim;

import java.io.Serializable;

/**
 * Lattice Boltzmann Fluid Simulation Task.
 * Implements D2Q9 Lattice Boltzmann Method for fluid dynamics.
 */
public class FluidSimTask implements Serializable {

    private final int width;
    private final int height;
    private double[][][] f; // Distribution functions (width x height x 9)
    private double[][] rho; // Density
    private double[][] ux; // X velocity
    private double[][] uy; // Y velocity
    private boolean[][] obstacle;
    private double viscosity = 0.02;

    private static final double[] W = { 4.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 36, 1.0 / 36, 1.0 / 36,
            1.0 / 36 };
    private static final int[] CX = { 0, 1, 0, -1, 0, 1, -1, -1, 1 };
    private static final int[] CY = { 0, 0, 1, 0, -1, 1, 1, -1, -1 };

    public FluidSimTask(int width, int height) {
        this.width = width;
        this.height = height;
        this.f = new double[width][height][9];
        this.rho = new double[width][height];
        this.ux = new double[width][height];
        this.uy = new double[width][height];
        this.obstacle = new boolean[width][height];
        initialize();
    }

    private void initialize() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rho[x][y] = 1.0;
                ux[x][y] = 0.0;
                uy[x][y] = 0.0;
                for (int i = 0; i < 9; i++) {
                    f[x][y][i] = W[i];
                }
            }
        }
    }

    public void step() {
        double omega = 1.0 / (3.0 * viscosity + 0.5);

        // Collision
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y])
                    continue;

                double r = 0, vx = 0, vy = 0;
                for (int i = 0; i < 9; i++) {
                    r += f[x][y][i];
                    vx += CX[i] * f[x][y][i];
                    vy += CY[i] * f[x][y][i];
                }
                rho[x][y] = r;
                ux[x][y] = vx / r;
                uy[x][y] = vy / r;

                double usq = ux[x][y] * ux[x][y] + uy[x][y] * uy[x][y];
                for (int i = 0; i < 9; i++) {
                    double cu = CX[i] * ux[x][y] + CY[i] * uy[x][y];
                    double feq = W[i] * rho[x][y] * (1 + 3 * cu + 4.5 * cu * cu - 1.5 * usq);
                    f[x][y][i] += omega * (feq - f[x][y][i]);
                }
            }
        }

        // Streaming
        double[][][] fNew = new double[width][height][9];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int i = 0; i < 9; i++) {
                    int xn = (x + CX[i] + width) % width;
                    int yn = (y + CY[i] + height) % height;
                    fNew[xn][yn][i] = f[x][y][i];
                }
            }
        }
        f = fNew;
    }

    public void setObstacle(int x, int y, boolean isObstacle) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            obstacle[x][y] = isObstacle;
        }
    }

    public double[][] getRho() {
        return rho;
    }

    public double[][] getUx() {
        return ux;
    }

    public double[][] getUy() {
        return uy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setViscosity(double v) {
        this.viscosity = v;
    }
}
