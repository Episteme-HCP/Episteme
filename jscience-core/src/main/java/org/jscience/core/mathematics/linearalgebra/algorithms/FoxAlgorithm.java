/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.algorithms;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.distributed.DistributedContext;
import org.jscience.core.ComputeContext;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;

/**
 * Fox's Algorithm for distributed matrix multiplication.
 * <p>
 * This algorithm uses a 2D processor grid and broadcasts tiles systematically
 * along rows and columns. It's more flexible than Cannon's algorithm and doesn't
 * require initial skewing.
 * </p>
 * <p>
 * <b>Algorithm Overview:</b>
 * <ol>
 * <li>For each step k:
 *   <ul>
 *   <li>Broadcast A[i][(i+k) mod √P] along row i</li>
 *   <li>Multiply with local B tiles</li>
 *   <li>Shift B tiles up by one position</li>
 *   </ul>
 * </li>
 * <li>Accumulate results locally</li>
 * </ol>
 * </p>
 * <p>
 * <b>Communication Complexity:</b> O(√P) per processor<br>
 * <b>Computation Complexity:</b> O(n³/P) per processor<br>
 * <b>Reference:</b> Fox, G. C., et al. (1988). Solving Problems on Concurrent Processors.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class FoxAlgorithm {

    /**
     * Performs distributed matrix multiplication C = A × B using Fox's algorithm.
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
            throw new IllegalArgumentException("Fox's algorithm requires square tile grid");
        }

        DistributedContext ctx = ComputeContext.current().getDistributedContext();
        int P = ctx.getParallelism();
        int gridSize = (int) Math.sqrt(P);

        if (gridSize * gridSize != P) {
            throw new IllegalArgumentException("Fox's algorithm requires square processor grid (P = k²)");
        }

        // Create result matrix
        TiledMatrix C = new TiledMatrix(A.rows(), B.cols(), A.getTileSize());

        // Working copy of B for shifting
        TiledMatrix B_current = copyTiledMatrix(B);

        // Main loop
        for (int step = 0; step < gridSize; step++) {
            final int currentStep = step;

            // Broadcast and multiply
            @SuppressWarnings("rawtypes")
            List<Future> tasks = new ArrayList<>();

            final TiledMatrix B_step = B_current;

            for (int i = 0; i < m; i++) {
                final int row = i;
                // Determine which A tile to broadcast in this row
                int broadcastCol = (row + currentStep) % gridSize;
                final Matrix<Real> aTileToBroadcast = A.getTile(row, broadcastCol);

                for (int j = 0; j < n; j++) {
                    final int col = j;

                    tasks.add(ctx.submit(() -> {
                        // Each processor in row i receives A[i][(i+step) mod gridSize]
                        Matrix<Real> aTile = aTileToBroadcast;
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

            // Shift B up (circular) for next iteration
            if (step < gridSize - 1) {
                B_current = shiftUp(B_current);
            }
        }

        return C;
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

    /**
     * Creates a copy of a tiled matrix.
     */
    private static TiledMatrix copyTiledMatrix(TiledMatrix M) {
        int m = M.getNumTileRows();
        int n = M.getNumTileCols();
        TiledMatrix copy = new TiledMatrix(M.rows(), M.cols(), M.getTileSize());

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                copy.setTile(i, j, M.getTile(i, j));
            }
        }
        return copy;
    }

    /**
     * Compares performance of Fox's algorithm vs. SUMMA.
     *
     * @param A Left matrix
     * @param B Right matrix
     * @return Performance comparison report
     */
    public static String compareWithSUMMA(TiledMatrix A, TiledMatrix B) {
        long startFox = System.nanoTime();
        multiply(A, B);  // Don't store result, just measure time
        long timeFox = System.nanoTime() - startFox;

        long startSUMMA = System.nanoTime();
        SUMMAAlgorithm.multiply(A, B);  // Don't store result, just measure time
        long timeSUMMA = System.nanoTime() - startSUMMA;

        return String.format(
            "Performance Comparison:%n" +
            "  Fox's Algorithm: %.3f ms%n" +
            "  SUMMA Algorithm: %.3f ms%n" +
            "  Speedup: %.2fx%n",
            timeFox / 1_000_000.0,
            timeSUMMA / 1_000_000.0,
            (double) timeSUMMA / timeFox
        );
    }
}

