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

/**
 * Implementation of the Communication-Avoiding Recursive Matrix Multiplication (CARMA) Algorithm.
 * <p>
 * CARMA is a communication-optimal algorithm that adapts to any matrix shape 
 * and any number of processors by recursively splitting the largest dimension.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class CARMAAlgorithm {

    /**
     * Performs CARMA distributed matrix multiplication C = A × B.
     *
     * @param A Left matrix (tiled)
     * @param B Right matrix (tiled)
     * @return Result matrix C
     */
    public static TiledMatrix multiply(TiledMatrix A, TiledMatrix B) {
        if (A.cols() != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible");
        }

        DistributedContext ctx = ComputeContext.current().getDistributedContext();
        int p = ctx.getParallelism();

        TiledMatrix C = new TiledMatrix(A, A.getTileSize(), A.getTileSize());
        
        System.out.println("[CARMA] Executing on " + p + " processors.");
        
        carmaRecursive(A, B, C, p, ctx);
        
        return C;
    }

    private static void carmaRecursive(TiledMatrix A, TiledMatrix B, TiledMatrix C, int p, DistributedContext ctx) {
        if (p <= 1) {
            // Base case: Local multiplication
            standardMultiply(A, B, C);
            return;
        }

        int m = A.getNumTileRows();
        int k = A.getNumTileCols();
        int n = B.getNumTileCols();

        // Find the largest dimension to split
        if (k >= m && k >= n) {
            // Split k - summation split
            int kHalf = k / 2;
            TiledMatrix A1 = A.getSubTiledMatrix(0, m, 0, kHalf);
            TiledMatrix A2 = A.getSubTiledMatrix(0, m, kHalf, k);
            TiledMatrix B1 = B.getSubTiledMatrix(0, kHalf, 0, n);
            TiledMatrix B2 = B.getSubTiledMatrix(kHalf, k, 0, n);

            // Parallel summation
            Future<?> f1 = ctx.submit(() -> {
                carmaRecursive(A1, B1, C, p / 2, ctx);
                return null;
            });
            Future<?> f2 = ctx.submit(() -> {
                carmaRecursive(A2, B2, C, p - p / 2, ctx);
                return null;
            });
            
            try { f1.get(); f2.get(); } catch (Exception e) { throw new RuntimeException(e); }
            
        } else if (m >= k && m >= n) {
            // Split m - row split
            int mHalf = m / 2;
            TiledMatrix A1 = A.getSubTiledMatrix(0, mHalf, 0, k);
            TiledMatrix A2 = A.getSubTiledMatrix(mHalf, m, 0, k);
            TiledMatrix C1 = C.getSubTiledMatrix(0, mHalf, 0, n);
            TiledMatrix C2 = C.getSubTiledMatrix(mHalf, m, 0, n);

            Future<?> f1 = ctx.submit(() -> {
                carmaRecursive(A1, B, C1, p / 2, ctx);
                return null;
            });
            Future<?> f2 = ctx.submit(() -> {
                carmaRecursive(A2, B, C2, p - p / 2, ctx);
                return null;
            });
            
            try { f1.get(); f2.get(); } catch (Exception e) { throw new RuntimeException(e); }
            
        } else {
            // Split n - column split
            int nHalf = n / 2;
            TiledMatrix B1 = B.getSubTiledMatrix(0, k, 0, nHalf);
            TiledMatrix B2 = B.getSubTiledMatrix(0, k, nHalf, n);
            TiledMatrix C1 = C.getSubTiledMatrix(0, m, 0, nHalf);
            TiledMatrix C2 = C.getSubTiledMatrix(0, m, nHalf, n);

            Future<?> f1 = ctx.submit(() -> {
                carmaRecursive(A, B1, C1, p / 2, ctx);
                return null;
            });
            Future<?> f2 = ctx.submit(() -> {
                carmaRecursive(A, B2, C2, p - p / 2, ctx);
                return null;
            });
            
            try { f1.get(); f2.get(); } catch (Exception e) { throw new RuntimeException(e); }
        }
    }

    private static void standardMultiply(TiledMatrix A, TiledMatrix B, TiledMatrix C) {
        int m = A.getNumTileRows();
        int k = A.getNumTileCols();
        int n = B.getNumTileCols();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Matrix<Real> sum = null;
                for (int l = 0; l < k; l++) {
                    Matrix<Real> aTile = A.getTile(i, l);
                    Matrix<Real> bTile = B.getTile(l, j);
                    Matrix<Real> prod = aTile.multiply(bTile);
                    if (sum == null) sum = prod;
                    else sum = sum.add(prod);
                }
                
                synchronized (C) {
                    Matrix<Real> current = C.getTile(i, j);
                    if (current == null) C.setTile(i, j, sum);
                    else C.setTile(i, j, current.add(sum));
                }
            }
        }
    }
}
