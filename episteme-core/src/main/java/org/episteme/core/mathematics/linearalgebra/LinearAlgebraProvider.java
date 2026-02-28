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
 * This interface is parameterized by element type {@code E}. Two main
 * parameterizations exist in the codebase:
 * </p>
 * <ul>
 *   <li><strong>{@code LinearAlgebraProvider<Real>}</strong> — Public-facing API.
 *       Users and high-level code operate through this type. Providers like
 *       {@code ND4JLinearAlgebraProvider} and {@code NativeMulticoreLinearAlgebraProvider}
 *       implement this interface and bridge to native backends internally.</li>
 *   <li><strong>{@code LinearAlgebraProvider<Double>}</strong> — Internal optimization layer.
 *       Used by native BLAS backends (e.g., {@code NativeFFMBLASBackend}) that operate
 *       directly on raw {@code double} arrays via FFM/Panama for maximum performance.</li>
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
     * Used for automatic backend selection and fallbacks.
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

    /**
     * Returns a fallback provider for this instance.
     * <p>
     * Defaults to the next-best available provider in priority order.
     * </p>
     */
    @SuppressWarnings("unchecked")
    default LinearAlgebraProvider<E> fallback() {
        return (LinearAlgebraProvider<E>) org.episteme.core.technical.algorithm.AlgorithmManager.getNextProvider(LinearAlgebraProvider.class, this);
    }

    // --- Vector Operations ---
    default Vector<E> add(Vector<E> a, Vector<E> b) {
        return fallback().add(a, b);
    }
    default Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return fallback().subtract(a, b);
    }
    default Vector<E> multiply(Vector<E> vector, E scalar) {
        return fallback().multiply(vector, scalar);
    }
    default E dot(Vector<E> a, Vector<E> b) {
        return fallback().dot(a, b);
    }
    default E norm(Vector<E> a) {
        return fallback().norm(a);
    }

    // --- Matrix Operations ---
    default Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return fallback().add(a, b);
    }
    default Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return fallback().subtract(a, b);
    }
    default Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        return fallback().multiply(a, b);
    }
    default Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return fallback().multiply(a, b);
    }
    default Matrix<E> inverse(Matrix<E> a) {
        return fallback().inverse(a);
    }
    default E determinant(Matrix<E> a) {
        return fallback().determinant(a);
    }
    default Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return fallback().solve(a, b);
    }
    default Matrix<E> transpose(Matrix<E> a) {
        return fallback().transpose(a);
    }
    default Matrix<E> scale(E scalar, Matrix<E> a) {
        return fallback().scale(scalar, a);
    }

    /**
     * Computes the QR decomposition of the specified matrix.
     */
    default QRResult<E> qr(Matrix<E> a) {
        return fallback().qr(a);
    }

    /**
     * Computes the Singular Value Decomposition (SVD) of the specified matrix.
     */
    default SVDResult<E> svd(Matrix<E> a) {
        return fallback().svd(a);
    }

    /**
     * Computes the eigenvalue decomposition of the specified matrix.
     */
    default EigenResult<E> eigen(Matrix<E> a) {
        return fallback().eigen(a);
    }

    /**
     * Computes the LU decomposition of the specified matrix.
     */
    default LUResult<E> lu(Matrix<E> a) {
        return fallback().lu(a);
    }

    /**
     * Computes the Cholesky decomposition of the specified matrix.
     */
    default CholeskyResult<E> cholesky(Matrix<E> a) {
        return fallback().cholesky(a);
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
