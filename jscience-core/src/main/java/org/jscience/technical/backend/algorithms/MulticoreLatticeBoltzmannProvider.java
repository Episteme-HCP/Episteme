/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.technical.backend.algorithms;

import org.jscience.mathematics.numbers.real.Real;
import java.util.stream.IntStream;

/**
 * Multicore implementation of Lattice Boltzmann Provider using Java Streams.
 */
public class MulticoreLatticeBoltzmannProvider implements LatticeBoltzmannProvider {

    // D2Q9 Constants
    private static final int[] CX = { 0, 1, 0, -1, 0, 1, -1, -1, 1 };
    private static final int[] CY = { 0, 0, 1, 0, -1, 1, 1, -1, -1 };
    private static final Real[] W;
    private static final int[] OPP = { 0, 3, 4, 1, 2, 7, 8, 5, 6 };

    static {
        W = new Real[9];
        W[0] = Real.of(4.0 / 9.0);
        W[1] = Real.of(1.0 / 9.0);
        W[2] = Real.of(1.0 / 9.0);
        W[3] = Real.of(1.0 / 9.0);
        W[4] = Real.of(1.0 / 9.0);
        W[5] = Real.of(1.0 / 36.0);
        W[6] = Real.of(1.0 / 36.0);
        W[7] = Real.of(1.0 / 36.0);
        W[8] = Real.of(1.0 / 36.0);
    }

    @Override
    public void evolve(Real[][][] f, boolean[][] obstacle, Real omega) {
        int nx = f.length;
        int ny = f[0].length;

        // We need a temporary buffer for streaming
        // Since Real is immutable (conceptually), we assume we replace references in f
        // Note: allocating full grid every step is slow, but consistent with Real
        // immutability
        Real[][][] nextF = new Real[nx][ny][9];

        // Parallel Collision and Streaming combined
        // Or separate? Streaming requires neighbors.
        // Standard procedure: Compute Post-Collision (store in temp or same if safe),
        // then Stream.
        // If we compute Post-Collision into `f`, we overwrite values needed by
        // neighbors if we were to stream in one pass without care.
        // Actually, distinct steps:
        // 1. Collision: local operation. Can update in-place if we don't need previous
        // values for neighbors?
        // Yes, collision is point-wise.
        // 2. Streaming: Moves values to neighbors. Needs buffering or careful ordering.

        // Step 1: Collision (In-Place)
        IntStream.range(0, nx).parallel().forEach(x -> {
            for (int y = 0; y < ny; y++) {
                if (obstacle != null && obstacle[x][y])
                    continue;

                // Macroscopic properties
                Real rho = Real.ZERO;
                Real uxNum = Real.ZERO;
                Real uyNum = Real.ZERO;

                for (int i = 0; i < 9; i++) {
                    rho = rho.add(f[x][y][i]);
                    uxNum = uxNum.add(f[x][y][i].multiply(Real.of(CX[i])));
                    uyNum = uyNum.add(f[x][y][i].multiply(Real.of(CY[i])));
                }

                // final Real finalRho = rho;
                Real ux = rho.compareTo(Real.ZERO) > 0 ? uxNum.divide(rho) : Real.ZERO;
                Real uy = rho.compareTo(Real.ZERO) > 0 ? uyNum.divide(rho) : Real.ZERO;

                // Equilibrium & Relaxation
                Real u2 = ux.multiply(ux).add(uy.multiply(uy));
                Real c1 = Real.of(3.0);
                Real c2 = Real.of(4.5);
                Real c3 = Real.of(1.5);
                Real one = Real.ONE;

                for (int i = 0; i < 9; i++) {
                    Real cu = ux.multiply(Real.of(CX[i])).add(uy.multiply(Real.of(CY[i])));
                    Real term = one.add(c1.multiply(cu))
                            .add(c2.multiply(cu).multiply(cu))
                            .subtract(c3.multiply(u2));
                    Real feq = rho.multiply(W[i]).multiply(term);

                    // f = f + omega * (feq - f)
                    f[x][y][i] = f[x][y][i].add(omega.multiply(feq.subtract(f[x][y][i])));
                }
            }
        });

        // Step 2: Streaming (Push-Based) implemented below
        // Legacy Pull-Based logic removed for clarity

        // Re-implementing simplified Push Streaming as per original class logic
        // We write to nextF. Since specific indices [nextX][nextY] are written,
        // parallel write to shared array is safe ONLY if writes don't overlap.
        // LBM Streaming is 1-to-1 mapping (permutation). No overlap.

        IntStream.range(0, nx).parallel().forEach(x -> {
            for (int y = 0; y < ny; y++) {
                for (int i = 0; i < 9; i++) {
                    int nextX = (x + CX[i] + nx) % nx;
                    int nextY = (y + CY[i] + ny) % ny;

                    if (obstacle != null && obstacle[nextX][nextY]) {
                        // Bounce-back: reflect back to [x][y] in opposite direction
                        nextF[x][y][OPP[i]] = f[x][y][i];
                    } else {
                        // Standard propagation
                        nextF[nextX][nextY][i] = f[x][y][i];
                    }
                }
            }
        });

        // Copy back
        IntStream.range(0, nx).parallel().forEach(x -> {
            if (ny >= 0)
                System.arraycopy(nextF[x], 0, f[x], 0, ny);
        });
    }

    @Override
    public void evolve(double[][][] f, boolean[][] obstacle, double omega) {
        int nx = f.length;
        int ny = f[0].length;
        double[][][] nextF = new double[nx][ny][9];

        // D2Q9 Static Weights (for double)
        double[] Dw = { 4.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0,
                1.0 / 36.0 };

        IntStream.range(0, nx).parallel().forEach(x -> {
            for (int y = 0; y < ny; y++) {
                if (obstacle != null && obstacle[x][y]) {
                    // Bounce-back during local collision step for obstacles
                    for (int i = 0; i < 9; i++) {
                        int nextX = (x + CX[i] + nx) % nx;
                        int nextY = (y + CY[i] + ny) % ny;
                        nextF[nextX][nextY][OPP[i]] = f[x][y][i];
                    }
                    continue;
                }

                double rho = 0, uxIdx = 0, uyIdx = 0;
                for (int i = 0; i < 9; i++) {
                    rho += f[x][y][i];
                    uxIdx += f[x][y][i] * CX[i];
                    uyIdx += f[x][y][i] * CY[i];
                }

                double ux = rho > 0 ? uxIdx / rho : 0;
                double uy = rho > 0 ? uyIdx / rho : 0;
                double u2 = ux * ux + uy * uy;

                for (int i = 0; i < 9; i++) {
                    double cu = 3.0 * (ux * CX[i] + uy * CY[i]);
                    double feq = rho * Dw[i] * (1.0 + cu + 0.5 * cu * cu - 1.5 * u2);
                    double fnew = f[x][y][i] + omega * (feq - f[x][y][i]);

                    int nextX = (x + CX[i] + nx) % nx;
                    int nextY = (y + CY[i] + ny) % ny;
                    nextF[nextX][nextY][i] = fnew;
                }
            }
        });

        for (int x = 0; x < nx; x++) {
            System.arraycopy(nextF[x], 0, f[x], 0, ny);
        }
    }

    @Override
    public String getName() {
        return "Multicore CPU (Java Streams)";
    }
}
