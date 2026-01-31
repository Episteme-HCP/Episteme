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
import java.nio.DoubleBuffer;

/**
 * SUMMA (Scalable Universal Matrix Multiplication Algorithm) implementation.
 * <p>
 * This algorithm distributes matrix multiplication across a 2D processor grid,
 * minimizing communication overhead through systematic broadcasting.
 * </p>
 * <p>
 * <b>Reference:</b><br>
 * Van De Geijn, R. A., & Watts, J. (1997). SUMMA: Scalable universal matrix 
 * multiplication algorithm. <i>Concurrency: Practice and Experience</i>, 9(4), 255-274.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class SUMMAAlgorithm {

    /**
     * Performs distributed matrix multiplication C = A × B using SUMMA.
     *
     * @param A Left matrix (tiled)
     * @param B Right matrix (tiled)
     * @return Result matrix C
     */
    public static TiledMatrix multiply(TiledMatrix A, TiledMatrix B) {
        if (A.cols() != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication");
        }

        int m = A.getNumTileRows();
        int n = B.getNumTileCols();
        int k = A.getNumTileCols();

        DistributedContext ctx = ComputeContext.current().getDistributedContext();

        // Create result tiled matrix
        TiledMatrix C = new TiledMatrix(A, A.getTileSize(), A.getTileSize());

        // SUMMA main loop
        for (int step = 0; step < k; step++) {
            final int currentStep = step;
            
            // Broadcast A tiles from column 'step'
            @SuppressWarnings("rawtypes")
            List<Future> broadcastTasks = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                final int row = i;
                broadcastTasks.add(ctx.submit(() -> {
                    Matrix<Real> aTile = A.getTile(row, currentStep);
                    // Broadcast to all processors in this row
                    broadcastTile(aTile, row, currentStep, ctx);
                    return null;
                }));
            }

            // Broadcast B tiles from row 'step'
            for (int j = 0; j < n; j++) {
                final int col = j;
                broadcastTasks.add(ctx.submit(() -> {
                    Matrix<Real> bTile = B.getTile(currentStep, col);
                    // Broadcast to all processors in this column
                    broadcastTile(bTile, currentStep, col, ctx);
                    return null;
                }));
            }

            // Wait for broadcasts
            broadcastTasks.forEach(f -> {
                try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
            });

            // Local multiplication and accumulation
            @SuppressWarnings("rawtypes")
            List<Future> computeTasks = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    final int row = i;
                    final int col = j;
                    computeTasks.add(ctx.submit(() -> {
                        Matrix<Real> aTile = A.getTile(row, currentStep);
                        Matrix<Real> bTile = B.getTile(currentStep, col);
                        Matrix<Real> product = aTile.multiply(bTile);
                        
                        // Accumulate into C
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
            computeTasks.forEach(f -> {
                try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
            });
        }

        return C;
    }

    /**
     * Broadcasts a tile to all nodes using RDMA if available.
     */
    private static void broadcastTile(Matrix<Real> tile, int row, int col, DistributedContext ctx) {
        // Convert tile to DoubleBuffer for RDMA transfer
        int size = tile.rows() * tile.cols();
        DoubleBuffer buffer = DoubleBuffer.allocate(size);
        
        for (int i = 0; i < tile.rows(); i++) {
            for (int j = 0; j < tile.cols(); j++) {
                buffer.put(tile.get(i, j).doubleValue());
            }
        }
        buffer.flip();

        // Broadcast using RDMA Put operations
        int parallelism = ctx.getParallelism();
        for (int rank = 0; rank < parallelism; rank++) {
            long offset = (row * 1000L + col) * size;
            ctx.put(buffer, rank, offset);
        }
        
        ctx.fence(); // Synchronize
    }
}

