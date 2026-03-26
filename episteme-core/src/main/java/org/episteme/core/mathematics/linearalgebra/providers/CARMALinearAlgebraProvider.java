/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.providers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.algorithms.RealDoubleCARMAAlgorithm;
import org.episteme.core.mathematics.linearalgebra.algorithms.RealCARMAAlgorithm;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;

/**
 * Linear Algebra Provider that forces the use of the CARMA algorithm.
 * Intended for benchmarking and comparison purposes.
 */
@AutoService({LinearAlgebraProvider.class})
public class CARMALinearAlgebraProvider<E> extends CPUDenseLinearAlgebraProvider<E> {

    public CARMALinearAlgebraProvider() {
        super(null);
    }

    @Override
    public String getEnvironmentInfo() {
        return "CPU (CARMA)";
    }

    @Override
    public String getName() {
        return "Episteme (CARMA)";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        // Generic CARMA implementation
        // SIMD fast path
        if (a instanceof SIMDRealDoubleMatrix && b instanceof SIMDRealDoubleMatrix) {
            return (Matrix<E>) (Matrix<?>) RealDoubleCARMAAlgorithm.multiply(
                    (SIMDRealDoubleMatrix) a, (SIMDRealDoubleMatrix) b);
        }
        
        // Generic path (if E is Real)
        if (a.getScalarRing().zero() instanceof Real) {
            LinearAlgebraProvider<Real> leaf = getLeafProvider((Ring<Real>) a.getScalarRing());
            return wrap((Matrix<E>) (Matrix<?>) RealCARMAAlgorithm.multiply((Matrix<Real>) a, (Matrix<Real>) b, leaf));
        }

        return wrap(super.multiply(a, b));
    }

    @SuppressWarnings("unchecked")
    private LinearAlgebraProvider<Real> getLeafProvider(Ring<Real> ring) {
        org.episteme.core.technical.algorithm.OperationContext ctx = org.episteme.core.technical.algorithm.OperationContext.DEFAULT;
        if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealBig) {
            ctx = new org.episteme.core.technical.algorithm.OperationContext.Builder()
                    .addHint(org.episteme.core.technical.algorithm.OperationContext.Hint.HIGH_PRECISION)
                    .build();
        }

        return (LinearAlgebraProvider<Real>) org.episteme.core.technical.algorithm.ProviderSelector.select(
                LinearAlgebraProvider.class,
                ctx,
                p -> p != this && !(p instanceof org.episteme.core.mathematics.linearalgebra.providers.StrassenLinearAlgebraProvider)
                        && p.isCompatible(ring)
        );
    }

    @Override public Matrix<E> exp(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).exp((Matrix<Real>)a)) : super.exp(a); }
    @Override public Matrix<E> log(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).log((Matrix<Real>)a)) : super.log(a); }
    @Override public Matrix<E> log10(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).log10((Matrix<Real>)a)) : super.log10(a); }
    @Override public Matrix<E> sin(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).sin((Matrix<Real>)a)) : super.sin(a); }
    @Override public Matrix<E> cos(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).cos((Matrix<Real>)a)) : super.cos(a); }
    @Override public Matrix<E> tan(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).tan((Matrix<Real>)a)) : super.tan(a); }
    @Override public Matrix<E> asin(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).asin((Matrix<Real>)a)) : super.asin(a); }
    @Override public Matrix<E> acos(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).acos((Matrix<Real>)a)) : super.acos(a); }
    @Override public Matrix<E> atan(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).atan((Matrix<Real>)a)) : super.atan(a); }
    @Override public Matrix<E> sinh(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).sinh((Matrix<Real>)a)) : super.sinh(a); }
    @Override public Matrix<E> cosh(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).cosh((Matrix<Real>)a)) : super.cosh(a); }
    @Override public Matrix<E> tanh(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).tanh((Matrix<Real>)a)) : super.tanh(a); }
    @Override public Matrix<E> sqrt(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).sqrt((Matrix<Real>)a)) : super.sqrt(a); }
    @Override public Matrix<E> cbrt(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).cbrt((Matrix<Real>)a)) : super.cbrt(a); }
    @Override public Matrix<E> pow(Matrix<E> a, E exponent) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).pow((Matrix<Real>)a, (Real)exponent)) : super.pow(a, exponent); }

    private Matrix<E> wrap(Matrix<E> m) {
        if (m instanceof GenericMatrix) {
            return ((GenericMatrix<E>) m).withProvider(this);
        }
        return m;
    }
    
    @Override
    public int getPriority() {
        return -10;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void shutdown() {
        // No-op
    }
}
