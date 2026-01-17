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

import java.util.stream.IntStream;

/**
 * Primitive-based support for Lattice Boltzmann Fluid Simulation.
 * Optimized for high-performance double-precision computation.
 
 * <p>
 * <b>Reference:</b><br>
 * Prim, R. C. (1957). Shortest connection networks and some generalizations. <i>Bell System Technical Journal</i>, 36(6), 1389-1401.
 * </p>
 *
 */
public class FluidSimPrimitiveSupport {

    // D2Q9 constants
    private static final double[] WEIGHTS = { 4.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 18.0,
            1.0 / 18.0, 1.0 / 18.0, 1.0 / 18.0 };
    private static final int[] CX = { 0, 1, 0, -1, 0, 1, -1, -1, 1 };
    private static final int[] CY = { 0, 0, 1, 0, -1, 1, 1, -1, -1 };
    private static final int[] OPP = { 0, 3, 4, 1, 2, 7, 8, 5, 6 };

    /**
     * Evolves the Lattice Boltzmann simulation by one time step using double
     * primitives.
     * 
     * @param f        Lattice populations [width][height][9]
     * @param obstacle Obstacle mask [width][height]
     * @param omega    Relaxation parameter
     * @param width    Width
     * @param height   Height
     */
    public void evolve(double[][][] f, boolean[][] obstacle, double omega, int width, int height) {
        double[][][] nextF = new double[width][height][9];

        // 1. Collision and Streaming (Push-based)
        IntStream.range(0, width).parallel().forEach(x -> {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y]) {
                    // Bounce-back
                    for (int i = 0; i < 9; i++) {
                        int nx = (x + CX[i] + width) % width;
                        int ny = (y + CY[i] + height) % height;
                        nextF[nx][ny][OPP[i]] = f[x][y][i];
                    }
                    continue;
                }

                // Compute macroscopic density and velocity
                double rho = 0, ux = 0, uy = 0;
                for (int i = 0; i < 9; i++) {
                    rho += f[x][y][i];
                    ux += f[x][y][i] * CX[i];
                    uy += f[x][y][i] * CY[i];
                }
                if (rho > 0) {
                    ux /= rho;
                    uy /= rho;
                }

                // Collision and Stream
                for (int i = 0; i < 9; i++) {
                    double cu = 3.0 * (CX[i] * ux + CY[i] * uy);
                    double feq = WEIGHTS[i] * rho * (1.0 + cu + 0.5 * cu * cu - 1.5 * (ux * ux + uy * uy));
                    double fnew = f[x][y][i] + omega * (feq - f[x][y][i]);

                    int nx = (x + CX[i] + width) % width;
                    int ny = (y + CY[i] + height) % height;
                    nextF[nx][ny][i] = fnew;
                }
            }
        });

        // Copy back
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                System.arraycopy(nextF[x][y], 0, f[x][y], 0, 9);
            }
        }
    }
}
