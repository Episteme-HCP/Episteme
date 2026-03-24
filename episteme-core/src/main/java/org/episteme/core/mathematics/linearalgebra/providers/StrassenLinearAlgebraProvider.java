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
import com.google.auto.service.AutoService;
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
        
        // Fast path for RealDoubleMatrix: Avoid boxing GC storm
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix && b instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix rda = (org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) a;
            org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix rdb = (org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) b;
            SIMDRealDoubleMatrix simda = new SIMDRealDoubleMatrix(a.rows(), a.cols(), rda.toDoubleArray());
            SIMDRealDoubleMatrix simdb = new SIMDRealDoubleMatrix(b.rows(), b.cols(), rdb.toDoubleArray());
            
            SIMDRealDoubleMatrix res = RealDoubleStrassenAlgorithm.multiply(simda, simdb);
            // Convert back to RealDoubleMatrix to preserve expected contract or just return as SIMD? 
            // The provider expects Matrix<E> so returning SIMD is fine, they both implement Matrix.
            return (Matrix<E>) (Matrix<?>) res;
        }
        
        // Generic path (if E is Real)
        if (a.get(0,0) instanceof Real) {
             LinearAlgebraProvider<Real> leaf = (LinearAlgebraProvider<Real>) org.episteme.core.technical.algorithm.ProviderSelector.select(
                 LinearAlgebraProvider.class,
                 org.episteme.core.technical.algorithm.OperationContext.DEFAULT,
                 p -> p != this && !(p instanceof org.episteme.core.mathematics.linearalgebra.providers.CARMALinearAlgebraProvider) 
                      && p.isCompatible(a.getScalarRing())
             );
             return wrap((Matrix<E>) (Matrix<?>) RealStrassenAlgorithm.multiply((Matrix<Real>) a, (Matrix<Real>) b, leaf));
        }

        return wrap(super.multiply(a, b));
    }

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
