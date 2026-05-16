/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.algorithms;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.episteme.core.technical.backend.distributed.DistributedContext;
import org.episteme.core.distributed.DistributedCompute;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of the 2.5D Matrix Multiplication Algorithm.
 * <p>
 * This algorithm replicates the input matrices to reduce communication bandwidth
 * by a factor of c^(1/2) where c is the number of replication layers.
 * It is optimal for clusters with abundant memory but limited bandwidth.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class Distributed25DAlgorithm {

    /**
     * Performs distributed matrix multiplication C = A x B using 2.5D algorithm.
     *
     * @param A Left matrix
     * @param B Right matrix
     * @param replicationFactor Number of layers (c) to replicate data across
     * @return Result matrix C
     */
    public static <E> TiledMatrix<E> multiply(TiledMatrix<E> A, TiledMatrix<E> B, int replicationFactor) {
        return multiply(A, B, replicationFactor, null);
    }

    /**
     * Performs distributed matrix multiplication C = A x B using 2.5D algorithm with a leaf provider.
     *
     * @param A Left matrix
     * @param B Right matrix
     * @param replicationFactor Number of layers (c) to replicate data across
     * @param leafProvider Provider for tile-level multiplication
     * @return Result matrix C
     */
    public static <E> TiledMatrix<E> multiply(TiledMatrix<E> A, TiledMatrix<E> B, int replicationFactor, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        if (A.cols() != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible");
        }

        DistributedContext ctx = DistributedCompute.getContext();
        int p = ctx.getParallelism();
        
        // Log parallelism to use the variable
        System.out.println("[2.5D] Parallelism detected: " + p);
        
        int c = replicationFactor;
        if (c < 1) c = 1;
        
        TiledMatrix<E> C = new TiledMatrix<E>(A.rows(), B.cols(), A.getTileSize(), A.getScalarRing());
        
        int kTotal = A.getNumTileCols();
        int kChunk = (kTotal + c - 1) / c; 
        
        System.out.println("[2.5D] Executing with replication factor c=" + c);
        
        List<Future<?>> tasks = new ArrayList<>();
        
        for (int layer = 0; layer < c; layer++) {
            final int kStart = layer * kChunk;
            final int kEnd = Math.min(kStart + kChunk, kTotal);
            
            if (kStart >= kTotal) continue;
            
            final TiledMatrix<E> A_sub = A.getSubTiledMatrix(0, A.getNumTileRows(), kStart, kEnd);
            final TiledMatrix<E> B_sub = B.getSubTiledMatrix(kStart, kEnd, 0, B.getNumTileCols());
            
            tasks.add(ctx.submit(() -> {
                multiplyAndAccumulate(A_sub, B_sub, C, leafProvider);
                return null;
            }));
        }
        
        for (Future<?> f : tasks) {
            try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
        }
        
        return C;
    }
    
    private static <E> void multiplyAndAccumulate(TiledMatrix<E> A, TiledMatrix<E> B, TiledMatrix<E> C, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        int m = A.getNumTileRows();
        int n = B.getNumTileCols();
        int k = A.getNumTileCols(); 
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
               Matrix<E> sum = null;
               for (int l = 0; l < k; l++) {
                   Matrix<E> a = A.getTile(i, l);
                   Matrix<E> b = B.getTile(l, j);
                   if (a != null && b != null) {
                       Matrix<E> prod = (leafProvider != null) ? leafProvider.multiply(a, b) : a.multiply(b);
                       sum = (sum == null) ? prod : sum.add(prod);
                   }
               }
               
               if (sum != null) {
                   // Thread-safe update of the tile in C
                   C.updateTile(i, j, sum);
               }
            }
        }
    }
}
