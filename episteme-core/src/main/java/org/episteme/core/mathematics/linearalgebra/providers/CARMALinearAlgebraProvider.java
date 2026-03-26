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
        if (a.get(0, 0) instanceof Real) {
            org.episteme.core.technical.algorithm.OperationContext ctx = org.episteme.core.technical.algorithm.OperationContext.DEFAULT;
            if (a.get(0, 0) instanceof org.episteme.core.mathematics.numbers.real.RealBig) {
                ctx = new org.episteme.core.technical.algorithm.OperationContext.Builder()
                        .addHint(org.episteme.core.technical.algorithm.OperationContext.Hint.HIGH_PRECISION)
                        .build();
            }

            LinearAlgebraProvider<Real> leaf = (LinearAlgebraProvider<Real>) org.episteme.core.technical.algorithm.ProviderSelector.select(
                    LinearAlgebraProvider.class,
                    ctx,
                    p -> p != this && !(p instanceof org.episteme.core.mathematics.linearalgebra.providers.StrassenLinearAlgebraProvider)
                            && p.isCompatible(a.getScalarRing())
            );
            return wrap((Matrix<E>) (Matrix<?>) RealCARMAAlgorithm.multiply((Matrix<Real>) a, (Matrix<Real>) b, leaf));
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
