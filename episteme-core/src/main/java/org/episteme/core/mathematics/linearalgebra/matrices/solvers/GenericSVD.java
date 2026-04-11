/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.solvers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;

/**
 * Placeholder for Generic SVD Decomposition.
 */
public class GenericSVD {
    public static <E> SVDResult<E> decompose(Matrix<E> matrix, Field<E> field, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider) {
        int m = matrix.rows();
        int n = matrix.cols();
        
        // A*A^T for U and Sigma^2
        Matrix<E> selfAdjU = matrix.multiply(matrix.transpose()); 
        EigenResult<E> eigenU = GenericEigen.decompose(selfAdjU, field, provider);
        Matrix<E> U = eigenU.V();

        // A^T*A for V and Sigma^2 (more stable for V)
        Matrix<E> selfAdjV = matrix.transpose().multiply(matrix); 
        EigenResult<E> eigenV = GenericEigen.decompose(selfAdjV, field, provider);
        Matrix<E> V = eigenV.V();
        
        // Use a more generic class if possible to avoid precision leakage
        Class<?> componentType = field.zero().getClass();
        if (Real.class.isAssignableFrom(componentType)) componentType = Real.class;
        if (Complex.class.isAssignableFrom(componentType)) componentType = Complex.class;

        @SuppressWarnings("unchecked")
        E[] sValues = (E[]) java.lang.reflect.Array.newInstance(componentType, Math.min(m, n));
        for (int i = 0; i < sValues.length; i++) {
            E val = eigenV.D().get(i); // Use eigenvalues of A^T*A
            // Stability: ensure non-negative and handle numerical noise
            double dVal = absValueDouble(val, field);
            // High-precision safety: singular values of A^T*A should be >= 0.
            // If slightly negative due to noise, clamp to zero.
            if (dVal < 1e-60) sValues[i] = field.zero();
            else sValues[i] = sqrt(max(val, field.zero(), field), field);
        }
        
        return new SVDResult<E>(U, new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<E>(java.util.Arrays.asList(sValues)), provider, field), V);
    }

    @SuppressWarnings("unchecked")
    private static <E> E sqrt(E element, Field<E> field) {
        Object res = null;
        if (element instanceof Real) res = ((Real) element).sqrt();
        else if (element instanceof Complex) res = ((Complex) element).sqrt();
        else {
            try {
                res = element.getClass().getMethod("sqrt").invoke(element);
            } catch (Exception e) {
                res = element;
            }
        }
        
        if (field.zero() instanceof Complex && res instanceof Real) {
            return (E) Complex.of((Real) res);
        }
        return (E) res;
    }

    private static <E> E max(E a, E b, Field<E> field) {
        if (a instanceof Real && b instanceof Real) {
            return ((Real) a).compareTo((Real) b) >= 0 ? a : b;
        }
        if (a instanceof Complex && b instanceof Complex) {
            // Singular values of A^T*A are real even for complex A
            return ((Complex) a).abs().compareTo(((Complex) b).abs()) >= 0 ? a : b;
        }
        
        double aD = absValueDouble(a, field);
        double bD = absValueDouble(b, field);
        return (aD >= bD) ? a : b;
    }

    private static double absValueDouble(Object element, Field<?> field) {
        if (element instanceof Real) return ((Real) element).doubleValue();
        if (element instanceof Complex) return ((Complex) element).abs().doubleValue();
        if (element instanceof Number) return ((Number) element).doubleValue();
        return 0.0;
    }
}

