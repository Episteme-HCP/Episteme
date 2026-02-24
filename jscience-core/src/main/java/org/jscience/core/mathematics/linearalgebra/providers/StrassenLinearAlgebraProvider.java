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
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;

/**
 * Linear Algebra Provider that forces the use of the Strassen algorithm.
 * Intended for benchmarking and comparison purposes.
 */
@AutoService(LinearAlgebraProvider.class)
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
        
        // Fast path for RealDoubleMatrix: Avoid boxing GC storm
        if (a instanceof org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix && b instanceof org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
            org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix rda = (org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) a;
            org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix rdb = (org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) b;
            SIMDRealDoubleMatrix simda = new SIMDRealDoubleMatrix(a.rows(), a.cols(), rda.toDoubleArray());
            SIMDRealDoubleMatrix simdb = new SIMDRealDoubleMatrix(b.rows(), b.cols(), rdb.toDoubleArray());
            
            SIMDRealDoubleMatrix res = RealDoubleStrassenAlgorithm.multiply(simda, simdb);
            // Convert back to RealDoubleMatrix to preserve expected contract or just return as SIMD? 
            // The provider expects Matrix<E> so returning SIMD is fine, they both implement Matrix.
            return (Matrix<E>) res;
        }
        
        // Generic path (if E is Real)
        if (a.get(0,0) instanceof Real) {
             return (Matrix<E>) RealStrassenAlgorithm.multiply((Matrix<Real>) a, (Matrix<Real>) b);
        }

        return super.multiply(a, b);
    }
    
    @Override
    public int getPriority() {
        return -10;
    }
}
