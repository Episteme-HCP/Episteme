/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.providers;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.algorithms.RealDoubleStrassenAlgorithm;
import org.jscience.core.mathematics.linearalgebra.algorithms.RealStrassenAlgorithm;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;

/**
 * Linear Algebra Provider that forces the use of the Strassen algorithm.
 * Intended for benchmarking and comparison purposes.
 */
@AutoService(LinearAlgebraProvider.class)
public class StrassenLinearAlgebraProvider<E extends org.jscience.core.mathematics.structures.rings.Field<E>> extends CPUDenseLinearAlgebraProvider<E> {

    @SuppressWarnings("unchecked")
    public StrassenLinearAlgebraProvider() {
        super((org.jscience.core.mathematics.structures.rings.Field<E>) Reals.getInstance());
    }

    @Override
    public String getName() {
        return "JScience (Strassen)";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) {
            throw new IllegalArgumentException("Matrix inner dimensions must match");
        }
        
        // SIMD fast path
        if (a instanceof SIMDRealDoubleMatrix && b instanceof SIMDRealDoubleMatrix) {
            return (Matrix<E>) RealDoubleStrassenAlgorithm.multiply(
                    (SIMDRealDoubleMatrix) a, (SIMDRealDoubleMatrix) b);
        }
        
        // Generic path (if E is Real)
        if (a.get(0,0) instanceof Real) {
             return (Matrix<E>) RealStrassenAlgorithm.multiply((Matrix<Real>) a, (Matrix<Real>) b);
        }

        // Fallback for non-Real fields where Strassen might not be implemented generic enough or needs Ring vs Field check
        // RealStrassen is for Real. For generic E, we don't have a generic Strassen implementation exposed as utility yet
        // (CPUDense has strassenRecursive but it's internal).
        // For benchmarking we care about Real.
        return super.multiply(a, b);
    }

    @Override
    public int getPriority() {
        return -10;
    }
}
