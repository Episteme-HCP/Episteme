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
public class DistributedCARMAAlgorithm {

    /**
     * Performs CARMA distributed matrix multiplication C = A × B.
     *
     * @param A Left matrix (tiled)
     * @param B Right matrix (tiled)
     * @return Result matrix C
     */
    public static <E> TiledMatrix<E> multiply(TiledMatrix<E> A, TiledMatrix<E> B) {
        return multiply(A, B, null);
    }

    /**
     * Performs CARMA distributed matrix multiplication C = A × B with a leaf provider.
     *
     * @param A Left matrix (tiled)
     * @param B Right matrix (tiled)
     * @param leafProvider Provider for tile-level multiplication
     * @return Result matrix C
     */
    public static <E> TiledMatrix<E> multiply(TiledMatrix<E> A, TiledMatrix<E> B, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        if (A.cols() != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible");
        }

        DistributedContext ctx = DistributedCompute.getContext();
        int p = ctx.getParallelism();

        TiledMatrix<E> C = new TiledMatrix<E>(A.rows(), B.cols(), A.getTileSize(), A.getScalarRing());
        
        System.out.println("[CARMA] Executing on " + p + " processors.");
        
        int m = A.getNumTileRows();
        int k = A.getNumTileCols();
        int n = B.getNumTileCols();
        
        carmaRecursive(A, 0, 0, B, 0, 0, C, 0, 0, m, k, n, p, ctx, leafProvider);
        
        return C;
    }

    private static <E> void carmaRecursive(TiledMatrix<E> A, int aRow, int aCol,
                                       TiledMatrix<E> B, int bRow, int bCol, 
                                       TiledMatrix<E> C, int cRow, int cCol, 
                                       int m, int k, int n,
                                       int p, DistributedContext ctx,
                                       org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        if (p <= 1) {
            // Base case: Local multiplication
            standardMultiply(A, aRow, aCol, B, bRow, bCol, C, cRow, cCol, m, k, n, leafProvider);
            return;
        }

        // Find the largest dimension to split
        if (k >= m && k >= n) {
            // Split k - summation split
            int kHalf = k / 2;

            Future<?> f1 = ctx.submit(() -> {
                carmaRecursive(A, aRow, aCol, B, bRow, bCol, C, cRow, cCol, m, kHalf, n, p / 2, ctx, leafProvider);
                return null;
            });
            Future<?> f2 = ctx.submit(() -> {
                carmaRecursive(A, aRow, aCol + kHalf, B, bRow + kHalf, bCol, C, cRow, cCol, m, k - kHalf, n, p - p / 2, ctx, leafProvider);
                return null;
            });
            
            try { f1.get(); f2.get(); } catch (Exception e) { throw new RuntimeException(e); }
            
        } else if (m >= k && m >= n) {
            // Split m - row split
            int mHalf = m / 2;

            Future<?> f1 = ctx.submit(() -> {
                carmaRecursive(A, aRow, aCol, B, bRow, bCol, C, cRow, cCol, mHalf, k, n, p / 2, ctx, leafProvider);
                return null;
            });
            Future<?> f2 = ctx.submit(() -> {
                carmaRecursive(A, aRow + mHalf, aCol, B, bRow, bCol, C, cRow + mHalf, cCol, m - mHalf, k, n, p - p / 2, ctx, leafProvider);
                return null;
            });
            
            try { f1.get(); f2.get(); } catch (Exception e) { throw new RuntimeException(e); }
            
        } else {
            // Split n - column split
            int nHalf = n / 2;

            Future<?> f1 = ctx.submit(() -> {
                carmaRecursive(A, aRow, aCol, B, bRow, bCol, C, cRow, cCol, m, k, nHalf, p / 2, ctx, leafProvider);
                return null;
            });
            Future<?> f2 = ctx.submit(() -> {
                carmaRecursive(A, aRow, aCol, B, bRow, bCol + nHalf, C, cRow, cCol + nHalf, m, k, n - nHalf, p - p / 2, ctx, leafProvider);
                return null;
            });
            
            try { f1.get(); f2.get(); } catch (Exception e) { throw new RuntimeException(e); }
        }
    }

    private static <E> void standardMultiply(TiledMatrix<E> A, int aRow, int aCol,
                                         TiledMatrix<E> B, int bRow, int bCol,
                                         TiledMatrix<E> C, int cRow, int cCol,
                                         int m, int k, int n,
                                         org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Matrix<E> sum = null;
                for (int l = 0; l < k; l++) {
                    Matrix<E> aTile = A.getTile(aRow + i, aCol + l);
                    Matrix<E> bTile = B.getTile(bRow + l, bCol + j);
                    Matrix<E> prod = (leafProvider != null) ? leafProvider.multiply(aTile, bTile) : aTile.multiply(bTile);
                    if (sum == null) sum = prod;
                    else sum = sum.add(prod);
                }
                
                synchronized (C) {
                    Matrix<E> current = C.getTile(cRow + i, cCol + j);
                    if (current == null) C.setTile(cRow + i, cCol + j, sum);
                    else C.setTile(cRow + i, cCol + j, current.add(sum));
                }
            }
        }
    }
}
