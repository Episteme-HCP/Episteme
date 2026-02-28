/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.ProviderSelector;

/**
 * High-level utility for matrix decompositions.
 * <p>
 * This class abstracts the provider system, automatically selecting the most
 * efficient implementation (Native LAPACK, EJML, or Core) based on matrix
 * characteristics and available hardware.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class Decomposition {

    private Decomposition() {}

    /**
     * Computes the QR decomposition of the specified matrix.
     *
     * @param <E>    the element type
     * @param matrix the matrix to decompose
     * @return the QR result
     */
    @SuppressWarnings("unchecked")
    public static <E> QRResult<E> qr(Matrix<E> matrix) {
        OperationContext ctx = createContext(matrix);
        return ProviderSelector.execute(LinearAlgebraProvider.class, ctx,
                p -> ((LinearAlgebraProvider<E>) p).qr(matrix));
    }

    /**
     * Computes the Singular Value Decomposition (SVD) of the specified matrix.
     *
     * @param <E>    the element type
     * @param matrix the matrix to decompose
     * @return the SVD result
     */
    @SuppressWarnings("unchecked")
    public static <E> SVDResult<E> svd(Matrix<E> matrix) {
        OperationContext ctx = createContext(matrix);
        return ProviderSelector.execute(LinearAlgebraProvider.class, ctx,
                p -> ((LinearAlgebraProvider<E>) p).svd(matrix));
    }

    /**
     * Computes the eigenvalue decomposition of the specified matrix.
     *
     * @param <E>    the element type
     * @param matrix the matrix to decompose
     * @return the eigenvalue result
     */
    @SuppressWarnings("unchecked")
    public static <E> EigenResult<E> eigen(Matrix<E> matrix) {
        OperationContext ctx = createContext(matrix);
        return ProviderSelector.execute(LinearAlgebraProvider.class, ctx,
                p -> ((LinearAlgebraProvider<E>) p).eigen(matrix));
    }

    /**
     * Computes the LU decomposition of the specified matrix.
     *
     * @param <E>    the element type
     * @param matrix the matrix to decompose
     * @return the LU result
     */
    @SuppressWarnings("unchecked")
    public static <E> LUResult<E> lu(Matrix<E> matrix) {
        OperationContext ctx = createContext(matrix);
        return ProviderSelector.execute(LinearAlgebraProvider.class, ctx,
                p -> ((LinearAlgebraProvider<E>) p).lu(matrix));
    }

    /**
     * Computes the Cholesky decomposition of the specified matrix.
     *
     * @param <E>    the element type
     * @param matrix the matrix to decompose
     * @return the Cholesky result
     */
    @SuppressWarnings("unchecked")
    public static <E> CholeskyResult<E> cholesky(Matrix<E> matrix) {
        OperationContext ctx = createContext(matrix);
        return ProviderSelector.execute(LinearAlgebraProvider.class, ctx,
                p -> ((LinearAlgebraProvider<E>) p).cholesky(matrix));
    }

    /**
     * Solves the linear system AX = B using the most appropriate decomposition.
     *
     * @param <E> the element type
     * @param a   the coefficient matrix A
     * @param b   the right-hand side vector B
     * @return the solution vector X
     */
    @SuppressWarnings("unchecked")
    public static <E> Vector<E> solve(Matrix<E> a, Vector<E> b) {
        OperationContext ctx = createContext(a);
        return ProviderSelector.execute(LinearAlgebraProvider.class, ctx,
                p -> ((LinearAlgebraProvider<E>) p).solve(a, b));
    }

    private static OperationContext createContext(Matrix<?> matrix) {
        return new OperationContext.Builder()
                .dataSize((long) matrix.rows() * matrix.cols())
                // We could add more specific hints here, like isSquare, isSymmetric, etc.
                .build();
    }
}
