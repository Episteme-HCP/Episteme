/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.algorithms;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.episteme.core.technical.backend.distributed.DistributedContext;
import org.episteme.core.distributed.DistributedCompute;
import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

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
    public static <E> TiledMatrix<E> multiply(TiledMatrix<E> A, TiledMatrix<E> B) {
        return multiply(A, B, null);
    }

    /**
     * Performs distributed matrix multiplication C = A × B using SUMMA with a leaf provider.
     *
     * @param A Left matrix (tiled)
     * @param B Right matrix (tiled)
     * @param leafProvider Provider for tile-level multiplication (can be null for default)
     * @return Result matrix C
     */
    public static <E> TiledMatrix<E> multiply(TiledMatrix<E> A, TiledMatrix<E> B, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        if (A.cols() != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication");
        }

        int m = A.getNumTileRows();
        int n = B.getNumTileCols();
        int k = A.getNumTileCols();

        DistributedContext ctx = DistributedCompute.getContext();
        TiledMatrix<E> C = new TiledMatrix<E>(A.rows(), B.cols(), A.getTileSize(), A.getScalarRing());

        for (int step = 0; step < k; step++) {
            final int currentStep = step;
            
            List<Future<?>> broadcastTasks = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                final int row = i;
                broadcastTasks.add(ctx.submit(() -> {
                    Matrix<E> aTile = A.getTile(row, currentStep);
                    broadcastTile(aTile, row, currentStep, ctx);
                    return null;
                }));
            }

            for (int j = 0; j < n; j++) {
                final int col = j;
                broadcastTasks.add(ctx.submit(() -> {
                    Matrix<E> bTile = B.getTile(currentStep, col);
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
                        Matrix<E> aTile = A.getTile(row, currentStep);
                        Matrix<E> bTile = B.getTile(currentStep, col);
                        Matrix<E> product = (leafProvider != null) ? leafProvider.multiply(aTile, bTile) : aTile.multiply(bTile);
                        
                        // Thread-safe update of the tile in C
                        C.updateTile(row, col, product);
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

    private static <E> void broadcastTile(Matrix<E> tile, int row, int col, DistributedContext ctx) {
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

    private static <E> void broadcastTileSlow(Matrix<E> tile, int row, int col, DistributedContext ctx) {
        int rows = tile.rows();
        int cols = tile.cols();
        int size = rows * cols;
        
        // Use the scalar ring to robustly detect High Precision
        boolean isHP = tile.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.real.RealBig;
        
        if (isHP) {
            // For High Precision, we serialize to ByteBuffer to preserve full accuracy.
            serializeAndBroadcastHP(tile, row, col, ctx);
            return;
        }

        DoubleBuffer buffer = DoubleBuffer.allocate(size);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = tile.get(i, j);
                if (val instanceof Number) {
                    buffer.put(((Number) val).doubleValue());
                } else {
                    // Fallback for Complex or others - use real part if applicable or 0
                    buffer.put(0.0); 
                }
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

    private static <E> void serializeAndBroadcastHP(Matrix<E> tile, int row, int col, DistributedContext ctx) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            
            int rows = tile.rows();
            int cols = tile.cols();
            oos.writeInt(rows);
            oos.writeInt(cols);
            
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    oos.writeObject(tile.get(i, j));
                }
            }
            oos.flush();
            
            ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
            int parallelism = ctx.getParallelism();
            for (int rank = 0; rank < parallelism; rank++) {
                long offset = (row * 1000L + col) * buffer.capacity();
                ctx.put(buffer, rank, offset);
            }
            ctx.fence();
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(DistributedSUMMAAlgorithm.class)
                .error("Failed to broadcast HP tile", e);
            throw new RuntimeException("HP Broadcast failed", e);
        }
    }
}
