/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.providers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.algorithms.RealDoubleStrassenAlgorithm;
import org.episteme.core.mathematics.linearalgebra.algorithms.RealStrassenAlgorithm;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;

/**
 * Linear Algebra Provider that forces the use of the Strassen algorithm.
 * Intended for benchmarking and comparison purposes.
 */
@AutoService({LinearAlgebraProvider.class})
public class StrassenLinearAlgebraProvider<E> extends CPUDenseLinearAlgebraProvider<E> {

    public StrassenLinearAlgebraProvider() {
        super(null);
    }

    @Override
    public String getEnvironmentInfo() {
        return "CPU (Strassen)";
    }

    @Override
    public String getName() {
        return "Episteme (Strassen)";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        // SIMD fast path
        if (a instanceof SIMDRealDoubleMatrix && b instanceof SIMDRealDoubleMatrix) {
            return (Matrix<E>) (Matrix<?>) RealDoubleStrassenAlgorithm.multiply(
                    (SIMDRealDoubleMatrix) a, (SIMDRealDoubleMatrix) b);
        }
        
        // Fast path for RealDoubleMatrix
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix && b instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix rda = (org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) a;
            org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix rdb = (org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) b;
            SIMDRealDoubleMatrix simda = new SIMDRealDoubleMatrix(a.rows(), a.cols(), rda.toDoubleArray());
            SIMDRealDoubleMatrix simdb = new SIMDRealDoubleMatrix(b.rows(), b.cols(), rdb.toDoubleArray());
            
            SIMDRealDoubleMatrix res = RealDoubleStrassenAlgorithm.multiply(simda, simdb);
            return (Matrix<E>) (Matrix<?>) res;
        }
        
        // Generic path (if E is Real)
        if (a.getScalarRing().zero() instanceof Real) {
            LinearAlgebraProvider<Real> leaf = getLeafProvider((Ring<Real>) a.getScalarRing());
            return wrap((Matrix<E>) (Matrix<?>) RealStrassenAlgorithm.multiply((Matrix<Real>) a, (Matrix<Real>) b, leaf));
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
                p -> p != this && !(p instanceof org.episteme.core.mathematics.linearalgebra.providers.CARMALinearAlgebraProvider)
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
    @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { return (a.getScalarRing().zero() instanceof Real) ? (Vector<E>)(Vector<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).solve((Matrix<Real>)a, (Vector<Real>)b) : super.solve(a, b); }
    @Override public Matrix<E> inverse(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? wrap((Matrix<E>)(Matrix<?>)getLeafProvider((Ring<Real>)a.getScalarRing()).inverse((Matrix<Real>)a)) : super.inverse(a); }
    @Override public E determinant(Matrix<E> a) { return (a.getScalarRing().zero() instanceof Real) ? (E)getLeafProvider((Ring<Real>)a.getScalarRing()).determinant((Matrix<Real>)a) : super.determinant(a); }

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
