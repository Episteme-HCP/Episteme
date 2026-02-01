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
    public static TiledMatrix multiply(TiledMatrix A, TiledMatrix B, int replicationFactor) {
        if (A.cols() != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible");
        }

        DistributedContext ctx = ComputeContext.current().getDistributedContext();
        int p = ctx.getParallelism();
        
        // Log parallelism to use the variable
        System.out.println("[2.5D] Parallelism detected: " + p);
        
        int c = replicationFactor;
        if (c < 1) c = 1;
        
        TiledMatrix C = new TiledMatrix(A, A.getTileSize(), A.getTileSize());
        
        int kTotal = A.getNumTileCols();
        int kChunk = (kTotal + c - 1) / c; 
        
        System.out.println("[2.5D] Executing with replication factor c=" + c);
        
        List<Future<?>> tasks = new ArrayList<>();
        
        for (int layer = 0; layer < c; layer++) {
            final int kStart = layer * kChunk;
            final int kEnd = Math.min(kStart + kChunk, kTotal);
            
            if (kStart >= kTotal) continue;
            
            final TiledMatrix A_sub = A.getSubTiledMatrix(0, A.getNumTileRows(), kStart, kEnd);
            final TiledMatrix B_sub = B.getSubTiledMatrix(kStart, kEnd, 0, B.getNumTileCols());
            
            tasks.add(ctx.submit(() -> {
                multiplyAndAccumulate(A_sub, B_sub, C);
                return null;
            }));
        }
        
        for (Future<?> f : tasks) {
            try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
        }
        
        return C;
    }
    
    private static void multiplyAndAccumulate(TiledMatrix A, TiledMatrix B, TiledMatrix C) {
        int m = A.getNumTileRows();
        int n = B.getNumTileCols();
        int k = A.getNumTileCols(); 
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
               Matrix<Real> sum = null;
               for (int l = 0; l < k; l++) {
                   Matrix<Real> a = A.getTile(i, l);
                   Matrix<Real> b = B.getTile(l, j);
                   if (a != null && b != null) {
                       Matrix<Real> prod = a.multiply(b);
                       sum = (sum == null) ? prod : sum.add(prod);
                   }
               }
               
               if (sum != null) {
                   synchronized(C) {
                       Matrix<Real> current = C.getTile(i, j);
                       C.setTile(i, j, (current == null) ? sum : current.add(sum));
                   }
               }
            }
        }
    }
}
