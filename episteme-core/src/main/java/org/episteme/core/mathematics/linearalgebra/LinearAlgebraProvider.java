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
        E n = norm(a);
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            E invNorm = field.inverse(n);
            return multiply(a, invNorm);
        }
        throw new UnsupportedOperationException("Normalization requires a Field for inversion.");
    }

    /**
     * Returns the cross product of two 3D vectors.
     */
    default Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) {
            throw new ArithmeticException("Cross product is only defined for 3D vectors.");
        }
        Ring<E> ring = a.getScalarRing();
        E u1 = a.get(0), u2 = a.get(1), u3 = a.get(2);
        E v1 = b.get(0), v2 = b.get(1), v3 = b.get(2);
        
        E c1 = ring.subtract(ring.multiply(u2, v3), ring.multiply(u3, v2)); 
        E c2 = ring.subtract(ring.multiply(u3, v1), ring.multiply(u1, v3)); 
        E c3 = ring.subtract(ring.multiply(u1, v2), ring.multiply(u2, v1));
        
        return Vector.of(java.util.Arrays.asList(c1, c2, c3), ring);
    }

    /**
     * Returns the angle between two vectors in radians.
     */
    default E angle(Vector<E> a, Vector<E> b) {
        E dAB = dot(a, b);
        E nA = norm(a);
        E nB = norm(b);
        Ring<E> ring = a.getScalarRing();
        E denom = ring.multiply(nA, nB);
        
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            E cosTheta = field.divide(dAB, denom);
            // Reuse matrix transcendental fallback for element-wise acos
            @SuppressWarnings("unchecked")
            E[][] data = (E[][]) new Object[][]{{cosTheta}};
            return acos(Matrix.of(data, ring)).get(0, 0);
        }
        throw new UnsupportedOperationException("Angle calculation requires a Field for division.");
    }

    /**
     * Returns the projection of vector a onto vector b.
     */
    default Vector<E> projection(Vector<E> a, Vector<E> b) {
        E dAB = dot(a, b);
        E dBB = dot(b, b);
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            E scalar = field.divide(dAB, dBB);
            return b.multiply(scalar);
        }
        throw new UnsupportedOperationException("Projection requires a Field for division.");
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
    @SuppressWarnings("unchecked")
    default Matrix<E> exp(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).exp();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).exp();
        throw new UnsupportedOperationException("exp not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> log(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).log();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).log();
        throw new UnsupportedOperationException("log not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> log10(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).log10();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).log10();
        throw new UnsupportedOperationException("log10 not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> sin(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).sin();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).sin();
        throw new UnsupportedOperationException("sin not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> cos(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).cos();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).cos();
        throw new UnsupportedOperationException("cos not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> tan(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).tan();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).tan();
        throw new UnsupportedOperationException("tan not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> asin(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).asin();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).asin();
        throw new UnsupportedOperationException("asin not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> acos(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).acos();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).acos();
        throw new UnsupportedOperationException("acos not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> atan(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).atan();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).atan();
        throw new UnsupportedOperationException("atan not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> sinh(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).sinh();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).sinh();
        throw new UnsupportedOperationException("sinh not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> cosh(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).cosh();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).cosh();
        throw new UnsupportedOperationException("cosh not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> tanh(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).tanh();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).tanh();
        throw new UnsupportedOperationException("tanh not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> asinh(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).asinh();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).asinh();
        throw new UnsupportedOperationException("asinh not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> acosh(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).acosh();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).acosh();
        throw new UnsupportedOperationException("acosh not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> atanh(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).atanh();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).atanh();
        throw new UnsupportedOperationException("atanh not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> pow(Matrix<E> a, E exponent) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig && exponent instanceof org.episteme.core.mathematics.numbers.real.Real) 
            return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).pow((org.episteme.core.mathematics.numbers.real.Real)exponent);
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex && exponent instanceof org.episteme.core.mathematics.numbers.complex.Complex) 
            return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).pow((org.episteme.core.mathematics.numbers.complex.Complex)exponent);
        throw new UnsupportedOperationException("pow not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> sqrt(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).sqrt();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).sqrt();
        throw new UnsupportedOperationException("sqrt not supported for " + val.getClass().getSimpleName());
    }); }

    @SuppressWarnings("unchecked")
    default Matrix<E> cbrt(Matrix<E> a) { return a.map(val -> {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig) return (E)((org.episteme.core.mathematics.numbers.real.RealBig)val).cbrt();
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) return (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).cbrt();
        throw new UnsupportedOperationException("cbrt not supported for " + val.getClass().getSimpleName());
    }); }

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
