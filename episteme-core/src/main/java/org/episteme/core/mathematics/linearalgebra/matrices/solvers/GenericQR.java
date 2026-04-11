/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.solvers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;

/**
 * Generic QR Decomposition.
 */
public class GenericQR {

    public static <E> QRResult<E> decompose(Matrix<E> matrix, Field<E> field) {
        return decompose(matrix, field, null);
    }

    public static <E> QRResult<E> decompose(Matrix<E> matrix, Field<E> field, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider) {
        int m = matrix.rows();
        int n = matrix.cols();
        
        @SuppressWarnings("unchecked")
        E[][] qData = (E[][]) java.lang.reflect.Array.newInstance(componentType(field), m, n);
        @SuppressWarnings("unchecked")
        E[][] rData = (E[][]) java.lang.reflect.Array.newInstance(componentType(field), n, n);
        
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) qData[i][j] = field.zero();
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) rData[i][j] = field.zero();
        
        for (int j = 0; j < n; j++) {
            @SuppressWarnings("unchecked")
            E[] v = (E[]) java.lang.reflect.Array.newInstance(componentType(field), m);
            for (int i = 0; i < m; i++) v[i] = matrix.get(i, j);
            
            for (int i = 0; i < j; i++) {
                E dot = field.zero();
                for (int k = 0; k < m; k++) dot = field.add(dot, field.multiply(conjugate(qData[k][i], field), v[k]));
                rData[i][j] = dot;
                for (int k = 0; k < m; k++) v[k] = field.subtract(v[k], field.multiply(dot, qData[k][i]));
            }
            
            E norm = norm(v, field);
            rData[j][j] = norm;
            
            // Stability check: use epsilon for high-precision stability
            boolean isNonZero = false;
            if (norm instanceof Real) isNonZero = ((Real) norm).doubleValue() > 1e-30;
            else if (norm instanceof Complex) isNonZero = ((Complex) norm).abs().doubleValue() > 1e-30;
            else isNonZero = isNonZero(norm, field);

            if (isNonZero) {
                for (int i = 0; i < m; i++) qData[i][j] = field.divide(v[i], norm);
            } else {
                for (int i = 0; i < m; i++) qData[i][j] = field.zero();
            }
        }
        
        @SuppressWarnings("unchecked")
        E[] qFlat = (E[]) new Object[m * n];
        @SuppressWarnings("unchecked")
        E[] rFlat = (E[]) new Object[n * n];
        for (int i = 0; i < m; i++) for (int k = 0; k < n; k++) qFlat[i * n + k] = qData[i][k];
        for (int i = 0; i < n; i++) for (int k = 0; k < n; k++) rFlat[i * n + k] = rData[i][k];
        
        Matrix<E> qMat = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(m, n, qFlat), provider, field);
        Matrix<E> rMat = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(n, n, rFlat), provider, field);

        return new QRResult<>(qMat, rMat);
    }

    public static <E> Vector<E> solve(QRResult<E> qr, Vector<E> b, Field<E> field) {
        return solve(qr, b, field, null);
    }

    public static <E> Vector<E> solve(QRResult<E> qr, Vector<E> b, Field<E> field, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider) {
        Matrix<E> q = qr.Q();
        Matrix<E> r = qr.R();
        int n = r.cols();
        
        // x = R^-1 * Q^T * b
        @SuppressWarnings("unchecked")
        E[] qtB = (E[]) java.lang.reflect.Array.newInstance(componentType(field), n);
        for (int i = 0; i < n; i++) {
            E dot = field.zero();
            for (int k = 0; k < q.rows(); k++) dot = field.add(dot, field.multiply(conjugate(q.get(k, i), field), b.get(k)));
            qtB[i] = dot;
        }
        
        @SuppressWarnings("unchecked")
        E[] x = (E[]) java.lang.reflect.Array.newInstance(componentType(field), n);
        for (int i = n - 1; i >= 0; i--) {
            E sum = field.zero();
            for (int j = i + 1; j < n; j++) sum = field.add(sum, field.multiply(r.get(i, j), x[j]));
            E r_ii = r.get(i, i);
            if (isNonZero(r_ii, field)) {
                x[i] = field.divide(field.subtract(qtB[i], sum), r_ii);
            } else {
                x[i] = field.zero(); // Or handle singular case differently
            }
        }
        
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<E>(java.util.Arrays.asList(x)), provider, field);
    }

    private static <E> boolean isNonZero(E element, Field<E> field) {
        if (element instanceof Real) return !((Real) element).isZero();
        if (element instanceof Complex) return !((Complex) element).isZero();
        if (element instanceof Number) return ((Number) element).doubleValue() != 0.0;
        return !field.zero().equals(element);
    }

    @SuppressWarnings("unchecked")
    private static <E> E conjugate(E element, Field<E> field) {
        if (element instanceof Complex) return (E) ((Complex) element).conjugate();
        return element;
    }

    private static Class<?> componentType(Field<?> field) {
        Class<?> c = field.zero().getClass();
        if (Real.class.isAssignableFrom(c)) return Real.class;
        if (Complex.class.isAssignableFrom(c)) return Complex.class;
        return c;
    }

    @SuppressWarnings("unchecked")
    private static <E> E norm(E[] v, Field<E> field) {
        E dot = field.zero();
        for (E e : v) dot = field.add(dot, field.multiply(conjugate(e, field), e));
        
        Object res = null;
        if (dot instanceof Real) res = ((Real) dot).sqrt();
        else if (dot instanceof Complex) res = ((Complex) dot).sqrt();
        else {
            try {
                res = dot.getClass().getMethod("sqrt").invoke(dot);
            } catch (Exception e) {
                res = dot;
            }
        }
        
        if (field.zero() instanceof Complex && res instanceof Real) {
            return (E) Complex.of((Real) res);
        }
        return (E) res;
    }
}
