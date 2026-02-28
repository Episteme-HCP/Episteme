/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.algorithms;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.backend.distributed.DistributedContext;
import org.episteme.core.ComputeContext;
import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.nio.DoubleBuffer;

/**
 * Distributed implementation of the SUMMA (Scalable Universal Matrix Multiplication Algorithm).
 * Optimized for both high-precision Real types and high-performance SIMDDoubleMatrix types.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class DistributedSUMMAAlgorithm {

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
        TiledMatrix C = new TiledMatrix(A, A.getTileSize(), A.getTileSize());

        for (int step = 0; step < k; step++) {
            final int currentStep = step;
            
            List<Future<?>> broadcastTasks = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                final int row = i;
                broadcastTasks.add(ctx.submit(() -> {
                    Matrix<Real> aTile = A.getTile(row, currentStep);
                    broadcastTile(aTile, row, currentStep, ctx);
                    return null;
                }));
            }

            for (int j = 0; j < n; j++) {
                final int col = j;
                broadcastTasks.add(ctx.submit(() -> {
                    Matrix<Real> bTile = B.getTile(currentStep, col);
                    broadcastTile(bTile, currentStep, col, ctx);
                    return null;
                }));
            }

            broadcastTasks.forEach(f -> {
                try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
            });

            List<Future<?>> computeTasks = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    final int row = i;
                    final int col = j;
                    computeTasks.add(ctx.submit(() -> {
                        Matrix<Real> aTile = A.getTile(row, currentStep);
                        Matrix<Real> bTile = B.getTile(currentStep, col);
                        Matrix<Real> product = aTile.multiply(bTile);
                        
                        synchronized (C) {
                            Matrix<Real> current = C.getTile(row, col);
                            if (current != null) {
                                C.setTile(row, col, current.add(product));
                            } else {
                                C.setTile(row, col, product);
                            }
                        }
                        return null;
                    }));
                }
            }

            computeTasks.forEach(f -> {
                try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
            });
        }

        return C;
    }

    private static void broadcastTile(Matrix<Real> tile, int row, int col, DistributedContext ctx) {
        if (tile instanceof SIMDRealDoubleMatrix) {
            broadcastTileFast((SIMDRealDoubleMatrix) tile, row, col, ctx);
        } else {
            broadcastTileSlow(tile, row, col, ctx);
        }
    }

    private static void broadcastTileFast(SIMDRealDoubleMatrix tile, int row, int col, DistributedContext ctx) {
        double[] data = tile.getInternalData();
        DoubleBuffer buffer = DoubleBuffer.wrap(data);
        
        int parallelism = ctx.getParallelism();
        for (int rank = 0; rank < parallelism; rank++) {
            long offset = (row * 1000L + col) * data.length;
            ctx.put(buffer, rank, offset);
        }
        ctx.fence();
    }

    private static void broadcastTileSlow(Matrix<Real> tile, int row, int col, DistributedContext ctx) {
        int size = tile.rows() * tile.cols();
        DoubleBuffer buffer = DoubleBuffer.allocate(size);
        for (int i = 0; i < tile.rows(); i++) {
            for (int j = 0; j < tile.cols(); j++) {
                buffer.put(tile.get(i, j).doubleValue());
            }
        }
        buffer.flip();

        int parallelism = ctx.getParallelism();
        for (int rank = 0; rank < parallelism; rank++) {
            long offset = (row * 1000L + col) * size;
            ctx.put(buffer, rank, offset);
        }
        ctx.fence();
    }
}
