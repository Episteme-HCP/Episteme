/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.providers;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.algorithms.RealDoubleCARMAAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.RealCARMAAlgorithm;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;

/**
 * Linear Algebra Provider that forces the use of the CARMA algorithm.
 * Intended for benchmarking and comparison purposes.
 */
@AutoService(LinearAlgebraProvider.class)
public class CARMALinearAlgebraProvider<E extends org.jscience.core.mathematics.structures.rings.Field<E>> extends CPUDenseLinearAlgebraProvider<E> {

    public CARMALinearAlgebraProvider() {
        super((org.jscience.core.mathematics.structures.rings.Field<E>) Reals.getInstance());
    }

    @Override
    public String getName() {
        return "JScience (CARMA)";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) {
            throw new IllegalArgumentException("Matrix inner dimensions must match");
        }
        
        // SIMD fast path
        if (a instanceof SIMDRealDoubleMatrix && b instanceof SIMDRealDoubleMatrix) {
            return (Matrix<E>) RealDoubleCARMAAlgorithm.multiply(
                    (SIMDRealDoubleMatrix) a, (SIMDRealDoubleMatrix) b);
        }
        
        // Generic path (if E is Real)
        if (a.get(0,0) instanceof Real) {
             return (Matrix<E>) RealCARMAAlgorithm.multiply((Matrix<Real>) a, (Matrix<Real>) b);
        }

        return super.multiply(a, b);
    }
    
    @Override
    public int getPriority() {
        return -10;
    }
}
