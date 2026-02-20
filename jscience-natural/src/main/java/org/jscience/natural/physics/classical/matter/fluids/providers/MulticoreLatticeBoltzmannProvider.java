/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.classical.matter.fluids.providers;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.physics.classical.matter.fluids.LatticeBoltzmannProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import java.util.stream.IntStream;

/**
 * Multicore implementation of Lattice Boltzmann Provider using Java Streams.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreLatticeBoltzmannProvider implements LatticeBoltzmannProvider {

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
    public int getPriority() {
        return 40;
    }

    @Override
    public void evolve(Real[][][] f, boolean[][] obstacle, Real omega) {
        int nx = f.length;
        int ny = f[0].length;
        Real[][][] nextF = new Real[nx][ny][9];

        IntStream.range(0, nx).parallel().forEach(x -> {
            for (int y = 0; y < ny; y++) {
                if (obstacle != null && obstacle[x][y]) continue;

                Real rho = Real.ZERO;
                Real uxNum = Real.ZERO;
                Real uyNum = Real.ZERO;

                for (int i = 0; i < 9; i++) {
                    rho = rho.add(f[x][y][i]);
                    uxNum = uxNum.add(f[x][y][i].multiply(Real.of(CX[i])));
                    uyNum = uyNum.add(f[x][y][i].multiply(Real.of(CY[i])));
                }

                Real ux = rho.compareTo(Real.ZERO) > 0 ? uxNum.divide(rho) : Real.ZERO;
                Real uy = rho.compareTo(Real.ZERO) > 0 ? uyNum.divide(rho) : Real.ZERO;

                Real u2 = ux.multiply(ux).add(uy.multiply(uy));
                Real c1 = Real.of(3.0);
                Real c2 = Real.of(4.5);
                Real c3 = Real.of(1.5);

                for (int i = 0; i < 9; i++) {
                    Real cu = ux.multiply(Real.of(CX[i])).add(uy.multiply(Real.of(CY[i])));
                    Real term = Real.ONE.add(c1.multiply(cu))
                            .add(c2.multiply(cu).multiply(cu))
                            .subtract(c3.multiply(u2));
                    Real feq = rho.multiply(W[i]).multiply(term);
                    f[x][y][i] = f[x][y][i].add(omega.multiply(feq.subtract(f[x][y][i])));
                }
            }
        });

        IntStream.range(0, nx).parallel().forEach(x -> {
            for (int y = 0; y < ny; y++) {
                for (int i = 0; i < 9; i++) {
                    int nextX = (x + CX[i] + nx) % nx;
                    int nextY = (y + CY[i] + ny) % ny;

                    if (obstacle != null && obstacle[nextX][nextY]) {
                        nextF[x][y][OPP[i]] = f[x][y][i];
                    } else {
                        nextF[nextX][nextY][i] = f[x][y][i];
                    }
                }
            }
        });

        IntStream.range(0, nx).parallel().forEach(x -> {
            System.arraycopy(nextF[x], 0, f[x], 0, ny);
        });
    }

    @Override
    public void evolve(double[][][] f, boolean[][] obstacle, double omega) {
        int nx = f.length;
        int ny = f[0].length;
        double[][][] nextF = new double[nx][ny][9];
        double[] Dw = { 4.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0 };

        IntStream.range(0, nx).parallel().forEach(x -> {
            for (int y = 0; y < ny; y++) {
                if (obstacle != null && obstacle[x][y]) {
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
        return "Multicore Lattice Boltzmann (CPU)";
    }
}
