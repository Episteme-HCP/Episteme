/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.linearalgebra.algorithms;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.TiledMatrix;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.distributed.DistributedContext;
import org.jscience.ComputeContext;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;

/**
 * Cannon's Algorithm for distributed matrix multiplication.
 * <p>
 * This algorithm uses a 2D torus topology for efficient communication patterns.
 * It requires a square processor grid (√P × √P processors).
 * </p>
 * <p>
 * <b>Algorithm Overview:</b>
 * <ol>
 * <li>Initial skew: Shift A tiles left by row index, B tiles up by column index</li>
 * <li>For each step: Multiply aligned tiles, shift A left, shift B up</li>
 * <li>Accumulate results locally</li>
 * </ol>
 * </p>
 * <p>
 * <b>Communication Complexity:</b> O(√P) per processor<br>
 * <b>Computation Complexity:</b> O(n³/P) per processor<br>
 * <b>Reference:</b> Cannon, L. E. (1969). A cellular computer to implement the Kalman filter algorithm.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class CannonAlgorithm {

    /**
     * Performs distributed matrix multiplication C = A × B using Cannon's algorithm.
     *
     * @param A Left matrix (must be square-tiled)
     * @param B Right matrix (must be square-tiled)
     * @return Result matrix C
     * @throws IllegalArgumentException if matrices are incompatible or processor grid is not square
     */
    public static TiledMatrix multiply(TiledMatrix A, TiledMatrix B) {
        if (A.cols() != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible");
        }

        int m = A.getNumTileRows();
        int n = B.getNumTileCols();
        int k = A.getNumTileCols();

        if (m != n || n != k) {
            throw new IllegalArgumentException("Cannon's algorithm requires square tile grid");
        }

        DistributedContext ctx = ComputeContext.current().getDistributedContext();
        int P = ctx.getParallelism();
        int gridSize = (int) Math.sqrt(P);

        if (gridSize * gridSize != P) {
            throw new IllegalArgumentException("Cannon's algorithm requires square processor grid (P = k²)");
        }

        // Create result matrix
        TiledMatrix C = new TiledMatrix(A.rows(), B.cols(), A.getTileSize());

        // Initial skew
        TiledMatrix A_current = initialSkewA(A);
        TiledMatrix B_current = initialSkewB(B);

        // Main loop
        for (int step = 0; step < gridSize; step++) {
            // Multiply aligned tiles
            @SuppressWarnings("rawtypes")
            List<Future> tasks = new ArrayList<>();

            final TiledMatrix A_step = A_current;
            final TiledMatrix B_step = B_current;

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    final int row = i;
                    final int col = j;

                    tasks.add(ctx.submit(() -> {
                        Matrix<Real> aTile = A_step.getTile(row, col);
                        Matrix<Real> bTile = B_step.getTile(row, col);
                        Matrix<Real> product = aTile.multiply(bTile);

                        Matrix<Real> current = C.getTile(row, col);
                        if (current != null) {
                            C.setTile(row, col, current.add(product));
                        } else {
                            C.setTile(row, col, product);
                        }
                        return null;
                    }));
                }
            }

            // Wait for computation
            tasks.forEach(f -> {
                try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
            });

            // Shift A left, B up (circular) for next iteration
            if (step < gridSize - 1) {
                A_current = shiftLeft(A_current);
                B_current = shiftUp(B_current);
            }
        }

        return C;
    }

    /**
     * Initial skew for matrix A: shift row i left by i positions.
     */
    private static TiledMatrix initialSkewA(TiledMatrix A) {
        int m = A.getNumTileRows();
        int n = A.getNumTileCols();
        TiledMatrix skewed = new TiledMatrix(A.rows(), A.cols(), A.getTileSize());

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int newJ = (j - i + n) % n;
                skewed.setTile(i, newJ, A.getTile(i, j));
            }
        }
        return skewed;
    }

    /**
     * Initial skew for matrix B: shift column j up by j positions.
     */
    private static TiledMatrix initialSkewB(TiledMatrix B) {
        int m = B.getNumTileRows();
        int n = B.getNumTileCols();
        TiledMatrix skewed = new TiledMatrix(B.rows(), B.cols(), B.getTileSize());

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int newI = (i - j + m) % m;
                skewed.setTile(newI, j, B.getTile(i, j));
            }
        }
        return skewed;
    }

    /**
     * Shifts all tiles left by one position (circular).
     */
    private static TiledMatrix shiftLeft(TiledMatrix M) {
        int m = M.getNumTileRows();
        int n = M.getNumTileCols();
        TiledMatrix shifted = new TiledMatrix(M.rows(), M.cols(), M.getTileSize());

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int newJ = (j - 1 + n) % n;
                shifted.setTile(i, newJ, M.getTile(i, j));
            }
        }
        return shifted;
    }

    /**
     * Shifts all tiles up by one position (circular).
     */
    private static TiledMatrix shiftUp(TiledMatrix M) {
        int m = M.getNumTileRows();
        int n = M.getNumTileCols();
        TiledMatrix shifted = new TiledMatrix(M.rows(), M.cols(), M.getTileSize());

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int newI = (i - 1 + m) % m;
                shifted.setTile(newI, j, M.getTile(i, j));
            }
        }
        return shifted;
    }
}
