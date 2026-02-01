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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Implementation of the 2.5D Matrix Multiplication Algorithm.
 * <p>
 * 2.5D algorithm reduces communication by utilizing extra memory (c layers).
 * Communication is reduced by a factor of sqrt(c) compared to 2D algorithms like Cannon.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class TwoAndHalfDAlgorithm {

    /**
     * Performs 2.5D distributed matrix multiplication C = A × B.
     *
     * @param A Left matrix (tiled)
     * @param B Right matrix (tiled)
     * @param c Number of layers (replication factor)
     * @return Result matrix C
     */
    public static TiledMatrix multiply(TiledMatrix A, TiledMatrix B, int c) {
        if (A.cols() != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible");
        }

        DistributedContext ctx = ComputeContext.current().getDistributedContext();
        int pTotal = ctx.getParallelism();
        
        if (pTotal % c != 0) {
            throw new IllegalArgumentException("Total processors must be divisible by number of layers c");
        }
        
        int pLayer = pTotal / c;
        int pSqrt = (int) Math.sqrt(pLayer);
        
        if (pSqrt * pSqrt != pLayer) {
            throw new IllegalArgumentException("Processors per layer (P/c) must be a perfect square for 2.5D algorithm");
        }

        int m = A.getNumTileRows();
        int n = B.getNumTileCols();
        int k = A.getNumTileCols();

        // C result is the same size as A.rows x B.cols
        TiledMatrix C = new TiledMatrix(A, A.getTileSize(), A.getTileSize());

        System.out.println("[2.5D] Executing on " + pSqrt + "x" + pSqrt + "x" + c + " grid.");

        // In 2.5D, each layer l computes a partial sum
        // Layer l handles k/c of the inner product summation
        int kPerLayer = (k + c - 1) / c;

        List<Future<?>> tasks = new ArrayList<>();

        for (int l = 0; l < c; l++) {
            final int layer = l;
            final int kStart = layer * kPerLayer;
            final int kEnd = Math.min((layer + 1) * kPerLayer, k);
            
            if (kStart >= kEnd) continue;

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    final int row = i;
                    final int col = j;
                    
                    tasks.add(ctx.submit(() -> {
                        Matrix<Real> partialSum = null;
                        
                        for (int kIdx = kStart; kIdx < kEnd; kIdx++) {
                            Matrix<Real> aTile = A.getTile(row, kIdx);
                            Matrix<Real> bTile = B.getTile(kIdx, col);
                            Matrix<Real> product = aTile.multiply(bTile);
                            
                            if (partialSum == null) {
                                partialSum = product;
                            } else {
                                partialSum = partialSum.add(product);
                            }
                        }
                        
                        // Concurrent reduction logic for C
                        synchronized (C) {
                            Matrix<Real> current = C.getTile(row, col);
                            if (current != null) {
                                C.setTile(row, col, current.add(partialSum));
                            } else {
                                C.setTile(row, col, partialSum);
                            }
                        }
                        return null;
                    }));
                }
            }
        }

        // Wait for all computations to finish
        tasks.forEach(f -> {
            try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
        });

        return C;
    }
}
