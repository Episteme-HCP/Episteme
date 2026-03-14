/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;


/**
 * Service provider interface for linear algebra operations.
 * <p>
 * Each provider implements only the operations it supports. Unsupported operations
 * throw {@link UnsupportedOperationException}. The {@code AlgorithmManager} is
 * responsible for selecting the best available provider and falling back to the
 * next one if the selected provider does not support a given operation.
 * </p>
 * <p>
 * This interface is parameterized by element type {@code E}. Two main
 * parameterizations exist in the codebase:
 * </p>
 * <ul>
 *   <li><strong>{@code LinearAlgebraProvider<Real>}</strong> — Public-facing API.
 *       Users and high-level code operate through this type.</li>
 *   <li><strong>{@code LinearAlgebraProvider<Double>}</strong> — Internal optimization layer.
 *       Used by native BLAS backends that operate directly on raw {@code double} arrays.</li>
 * </ul>
 * 
 * @param <E> the element type (typically {@code Real} for public API or {@code Double} for native)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface LinearAlgebraProvider<E> extends AlgorithmProvider {

    /**
     * Checks if this provider is compatible with the given ring.
     */
    default boolean isCompatible(Ring<?> ring) {
        return true; 
    }

    /**
     * Priority of this provider (higher means more preferred).
     * Used for automatic backend selection.
     */
    default int getPriority() {
        return 0;
    }

    /**
     * Configure the provider with context parameters.
     * @param properties configuration map
     */
    default void configure(java.util.Map<String, Object> properties) {
        // No-op by default
    }

    // --- Vector Operations ---
    default Vector<E> add(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support Vector add()");
    }
    default Vector<E> subtract(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support Vector subtract()");
    }
    default Vector<E> multiply(Vector<E> vector, E scalar) {
        throw new UnsupportedOperationException(getName() + " does not support Vector multiply()");
    }
    default E dot(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support dot()");
    }
    default E norm(Vector<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support norm()");
    }

    // --- Matrix Operations ---
    default Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support Matrix add()");
    }
    default Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support Matrix subtract()");
    }
    default Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support Matrix multiply()");
    }
    default Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support Matrix-Vector multiply()");
    }
    default Matrix<E> inverse(Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support inverse()");
    }
    default E determinant(Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support determinant()");
    }
    default Vector<E> solve(Matrix<E> a, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support solve()");
    }
    default Matrix<E> transpose(Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support transpose()");
    }
    default Matrix<E> scale(E scalar, Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support scale()");
    }

    /**
     * Computes the QR decomposition of the specified matrix.
     */
    default QRResult<E> qr(Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support qr()");
    }

    /**
     * Computes the Singular Value Decomposition (SVD) of the specified matrix.
     */
    default SVDResult<E> svd(Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support svd()");
    }

    /**
     * Computes the eigenvalue decomposition of the specified matrix.
     */
    default EigenResult<E> eigen(Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support eigen()");
    }

    /**
     * Computes the LU decomposition of the specified matrix.
     */
    default LUResult<E> lu(Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support lu()");
    }

    /**
     * Computes the Cholesky decomposition of the specified matrix.
     */
    default CholeskyResult<E> cholesky(Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support cholesky()");
    }

    /**
     * Solves Ax = b using a previously computed LU decomposition.
     */
    default Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support solve(LUResult, Vector)");
    }

    /**
     * Solves Ax = b using a previously computed QR decomposition.
     */
    default Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support solve(QRResult, Vector)");
    }

    /**
     * Solves Ax = b using a previously computed Cholesky decomposition.
     */
    default Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support solve(CholeskyResult, Vector)");
    }

    // --- Iterative Solvers ---

    /**
     * Solves Ax = b using BiCGSTAB (BiConjugate Gradient Stabilized) method.
     */
    default Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        throw new UnsupportedOperationException(getName() + " does not support bicgstab()");
    }

    /**
     * Solves Ax = b using Conjugate Gradient method.
     */
    default Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        throw new UnsupportedOperationException(getName() + " does not support conjugateGradient()");
    }

    /**
     * Solves Ax = b using GMRES (Generalized Minimal Residual) method.
     */
    default Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        throw new UnsupportedOperationException(getName() + " does not support gmres()");
    }

    @Override
    default double score(OperationContext context) {
        return AutoTuningManager.getDynamicScore(getName(), context.getDimensionality(), getPriority());
    }

    /**
     * Returns a string describing the execution environment (e.g., "CPU (AVX2)", "GPU (CUDA 12.0)").
     */
    default String getEnvironmentInfo() {
        return "Generic JVM";
    }

    @Override
    default String getName() {
        return "Linear Algebra Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Linear Algebra";
    }
}
