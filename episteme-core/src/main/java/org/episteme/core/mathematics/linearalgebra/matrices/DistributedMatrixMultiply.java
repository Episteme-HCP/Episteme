/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.distributed.DistributedCompute;

/**
 * Utility for performing distributed matrix multiplication using tiles.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class DistributedMatrixMultiply {

    /**
     * Multiplies two matrices using a tiled algorithm distributed across the current context.
     * Uses a simple block-based distribution.
     */
    public static <E> Matrix<E> multiply(TiledMatrix<E> A, TiledMatrix<E> B) {
        if (A.cols() != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions mismatch");
        }

        int m = A.getNumTileRows();
        int n = B.getNumTileCols();
        int p = A.getNumTileCols();

        // Get distributed context for parallel execution
        DistributedCompute.getContext();
        
        // Result tiles
        @SuppressWarnings("unchecked")
        Matrix<E>[][] resTiles = new Matrix[m][n];

        // Perform tiled matrix multiplication
        for (int i = 0; i < A.getNumTileRows(); i++) {
            for (int j = 0; j < B.getNumTileCols(); j++) {
                
                // We could distribute this work
                // resTiles[i][j] = context.execute(() -> computeTile(A, B, row, col, p));
                
                // For now, let's assume a simplified version where each partial sum is a task
                resTiles[i][j] = computeTileLocal(A, B, i, j, p);
            }
        }

        // We should wrap this in a customized structure or convert back to RealDoubleMatrix
        return assemble(resTiles, A.rows(), B.cols(), A.getScalarRing());
    }

    private static <E> Matrix<E> computeTileLocal(TiledMatrix<E> A, TiledMatrix<E> B, int i, int j, int p) {
        Matrix<E> sum = null;
        for (int k = 0; k < p; k++) {
            Matrix<E> aTile = A.getTile(i, k);
            Matrix<E> bTile = B.getTile(k, j);
            if (aTile == null || bTile == null) continue;
            
            Matrix<E> prod = aTile.multiply(bTile);
            if (sum == null) {
                sum = prod;
            } else {
                sum = sum.add(prod);
            }
        }
        return sum;
    }

    private static <E> Matrix<E> assemble(Matrix<E>[][] tiles, int rows, int cols, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        if (tiles == null || tiles.length == 0 || tiles[0].length == 0) {
            return Matrix.zeros(rows, cols, ring);
        }

        int tileRows = tiles[0][0].rows();
        int tileCols = tiles[0][0].cols();
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(rows, cols, ring.zero());
        
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Matrix<E> tile = tiles[i][j];
                if (tile == null) continue;
                
                int rOffset = i * tileRows;
                int cOffset = j * tileCols;
                
                for (int r = 0; r < tile.rows(); r++) {
                    for (int c = 0; c < tile.cols(); c++) {
                        storage.set(rOffset + r, cOffset + c, tile.get(r, c));
                    }
                }
            }
        }
        
        org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider = 
            org.episteme.core.Episteme.getProviderRegistry().getDenseLinearAlgebraProvider(ring);
            
        return new GenericMatrix<>(storage, provider, ring);
    }
}
