/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;
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
public interface LinearAlgebraProvider<E> extends AlgorithmProvider, java.lang.AutoCloseable {

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

    /**
     * Returns the normalized vector (unit vector).
     */
    default Vector<E> normalize(Vector<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support normalize()");
    }

    /**
     * Returns the cross product of two 3D vectors.
     */
    default Vector<E> cross(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support cross()");
    }

    /**
     * Returns the angle between two vectors in radians.
     */
    default E angle(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support angle()");
    }

    /**
     * Returns the projection of vector a onto vector b.
     */
    default Vector<E> projection(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException(getName() + " does not support projection()");
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
    default Matrix<E> conjugateTranspose(Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support conjugateTranspose()");
    }
    /**
     * Solves the triangular system Ax = b.
     * @param A the triangular matrix
     * @param b the right-hand side vector
     * @param upper true if A is upper triangular, false if lower
     * @param transpose true if solving A^T x = b
     * @param conjugate true if solving A^H x = b (only if transpose is true and complex)
     * @param unit true if A is unit triangular (diagonal is all ones)
     */
    default Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        throw new UnsupportedOperationException(getName() + " does not support solveTriangular()");
    }
    default Matrix<E> scale(E scalar, Matrix<E> a) {
        throw new UnsupportedOperationException(getName() + " does not support scale()");
    }
    
    /**
     * Computes the matrix exponential e^A.
     */
    default Matrix<E> exp(Matrix<E> a) {
        try {
            EigenResult<E> eigen = eigen(a);
            // exp(A) = V * exp(D) * V^-1
            // This only works if A is diagonalizable.
            // For general case, should use Pade approximation or Taylor series.
            // But for now, eigenvalue based is better than nothing.
            Matrix<E> V = eigen.V();
            Vector<E> D = eigen.D();
            
            // exp(D)
            E[] expD = (E[]) new Object[D.dimension()];
            for (int i = 0; i < D.dimension(); i++) {
                // This is tricky because we need a way to compute scalar exp
                // For now, let's just use the provider's scalar methods if available
                // Actually, let's just throw for now until we have a better way to handle scalar math on E
                throw new UnsupportedOperationException("Matrix exp not fully implemented in default provider");
            }
            return null; // Placeholder
        } catch (Exception e) {
            throw new UnsupportedOperationException(getName() + " does not support exp()", e);
        }
    }

    /**
     * Returns the rank of the matrix.
     */
    default int rank(Matrix<E> a) {
        SVDResult<E> svd = svd(a);
        Vector<E> s = svd.S();
        int rank = 0;
        double tol = 1e-12; // Should be dynamic based on precision
        for (int i = 0; i < s.dimension(); i++) {
            E val = s.get(i);
            double dVal = 0;
            if (val instanceof Number n) dVal = n.doubleValue();
            else if (val instanceof Real r) dVal = r.doubleValue();
            else if (val instanceof Complex c) dVal = c.abs().doubleValue();
            
            if (dVal > tol) rank++;
        }
        return rank;
    }

    /**
     * Returns the condition number of the matrix (L2 norm).
     */
    default E conditionNumber(Matrix<E> a) {
        SVDResult<E> svd = svd(a);
        Vector<E> s = svd.S();
        if (s.dimension() == 0) return null;
        
        double maxS = 0;
        double minS = Double.MAX_VALUE;
        
        for (int i = 0; i < s.dimension(); i++) {
            E val = s.get(i);
            double dVal = 0;
            if (val instanceof Number n) dVal = n.doubleValue();
            else if (val instanceof Real r) dVal = r.doubleValue();
            else if (val instanceof Complex c) dVal = c.abs().doubleValue();
            
            if (dVal > maxS) maxS = dVal;
            if (dVal < minS && dVal > 1e-18) minS = dVal;
        }
        
        double cond = maxS / minS;
        // Need to cast back to E. This is a bit hacky but consistent with current provider pattern
        Ring<E> ring = a.getScalarRing();
        if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return (E) org.episteme.core.mathematics.numbers.real.RealDouble.of(cond);
        if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float)cond);
        return null;
    }

    default Matrix<E> log(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support log()"); }
    default Matrix<E> log10(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support log10()"); }
    default Matrix<E> sin(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support sin()"); }
    default Matrix<E> cos(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support cos()"); }
    default Matrix<E> tan(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support tan()"); }
    default Matrix<E> asin(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support asin()"); }
    default Matrix<E> acos(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support acos()"); }
    default Matrix<E> atan(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support atan()"); }
    default Matrix<E> sinh(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support sinh()"); }
    default Matrix<E> cosh(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support cosh()"); }
    default Matrix<E> tanh(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support tanh()"); }
    default Matrix<E> asinh(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support asinh()"); }
    default Matrix<E> acosh(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support acosh()"); }
    default Matrix<E> atanh(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support atanh()"); }
    default Matrix<E> pow(Matrix<E> a, E exponent) { throw new UnsupportedOperationException(getName() + " does not support pow()"); }
    default Matrix<E> sqrt(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support sqrt()"); }
    default Matrix<E> cbrt(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support cbrt()"); }
    default E trace(Matrix<E> a) { throw new UnsupportedOperationException(getName() + " does not support trace()"); }

    /**
     * Computes the QR decomposition of the specified matrix.
     */
    default QRResult<E> qr(Matrix<E> a) {
        System.out.println("[DIAGNOSTIC] Falling back to default qr() for provider: " + (this != null ? getName() : "null"));
        throw new UnsupportedOperationException(getName() + " does not support qr()");
    }

    /**
     * Computes the Singular Value Decomposition (SVD) of the specified matrix.
     */
    default SVDResult<E> svd(Matrix<E> a) {
        System.out.println("[DIAGNOSTIC] Falling back to default svd() for provider: " + (this != null ? getName() : "null"));
        throw new UnsupportedOperationException(getName() + " does not support svd()");
    }

    /**
     * Computes the eigenvalue decomposition of the specified matrix.
     */
    default EigenResult<E> eigen(Matrix<E> a) {
        System.out.println("[DIAGNOSTIC] Falling back to default eigen() for provider: " + (this != null ? getName() : "null"));
        throw new UnsupportedOperationException(getName() + " does not support eigen()");
    }

    /**
     * Computes the LU decomposition of the specified matrix.
     */
    default LUResult<E> lu(Matrix<E> a) {
        System.out.println("[DIAGNOSTIC] Falling back to default lu() for provider: " + (this != null ? getName() : "null"));
        throw new UnsupportedOperationException(getName() + " does not support lu()");
    }

    /**
     * Computes the Cholesky decomposition of the specified matrix.
     */
    default CholeskyResult<E> cholesky(Matrix<E> a) {
        System.out.println("[DIAGNOSTIC] Falling back to default cholesky() for provider: " + (this != null ? getName() : "null"));
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

    @Override
    default void close() {
        // No-op by default
    }
}
