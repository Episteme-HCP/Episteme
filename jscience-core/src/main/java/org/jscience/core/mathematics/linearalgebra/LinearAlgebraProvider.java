/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra;

import org.jscience.core.mathematics.linearalgebra.solvers.*;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

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

    // --- Vector Operations ---
    default Vector<E> add(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("add not supported by " + getName());
    }
    default Vector<E> subtract(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("subtract not supported by " + getName());
    }
    default Vector<E> multiply(Vector<E> vector, E scalar) {
        throw new UnsupportedOperationException("multiply not supported by " + getName());
    }
    default E dot(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("dot not supported by " + getName());
    }
    default E norm(Vector<E> a) {
        throw new UnsupportedOperationException("norm not supported by " + getName());
    }

    // --- Matrix Operations ---
    default Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException("add not supported by " + getName());
    }
    default Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException("subtract not supported by " + getName());
    }
    default Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException("multiply not supported by " + getName());
    }
    default Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("multiply not supported by " + getName());
    }
    default Matrix<E> inverse(Matrix<E> a) {
        throw new UnsupportedOperationException("inverse not supported by " + getName());
    }
    default E determinant(Matrix<E> a) {
        throw new UnsupportedOperationException("determinant not supported by " + getName());
    }
    default Vector<E> solve(Matrix<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("solve not supported by " + getName());
    }
    default Matrix<E> transpose(Matrix<E> a) {
        throw new UnsupportedOperationException("transpose not supported by " + getName());
    }
    default Matrix<E> scale(E scalar, Matrix<E> a) {
        throw new UnsupportedOperationException("scale not supported by " + getName());
    }

    /**
     * Computes the QR decomposition of the specified matrix.
     */
    default QRResult<E> qr(Matrix<E> a) {
        throw new UnsupportedOperationException("QR decomposition not supported by " + getName());
    }

    /**
     * Computes the Singular Value Decomposition (SVD) of the specified matrix.
     */
    default SVDResult<E> svd(Matrix<E> a) {
        throw new UnsupportedOperationException("SVD decomposition not supported by " + getName());
    }

    /**
     * Computes the eigenvalue decomposition of the specified matrix.
     */
    default EigenResult<E> eigen(Matrix<E> a) {
        throw new UnsupportedOperationException("Eigenvalue decomposition not supported by " + getName());
    }

    /**
     * Computes the LU decomposition of the specified matrix.
     */
    default LUResult<E> lu(Matrix<E> a) {
        throw new UnsupportedOperationException("LU decomposition not supported by " + getName());
    }

    /**
     * Computes the Cholesky decomposition of the specified matrix.
     */
    default CholeskyResult<E> cholesky(Matrix<E> a) {
        throw new UnsupportedOperationException("Cholesky decomposition not supported by " + getName());
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
