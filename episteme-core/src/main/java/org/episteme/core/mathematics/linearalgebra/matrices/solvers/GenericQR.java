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
        int m = matrix.rows();
        int n = matrix.cols();
        
        @SuppressWarnings("unchecked")
        E[][] qData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), m, n);
        @SuppressWarnings("unchecked")
        E[][] rData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
        
        for (int j = 0; j < n; j++) {
            @SuppressWarnings("unchecked")
            E[] v = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), m);
            for (int i = 0; i < m; i++) v[i] = matrix.get(i, j);
            
            for (int i = 0; i < j; i++) {
                E dot = field.zero();
                for (int k = 0; k < m; k++) dot = field.add(dot, field.multiply(conjugate(qData[k][i], field), v[k]));
                rData[i][j] = dot;
                for (int k = 0; k < m; k++) v[k] = field.subtract(v[k], field.multiply(dot, qData[k][i]));
            }
            
            E norm = norm(v, field);
            rData[j][j] = norm;
            if (absValue(norm, field) > 1e-20) {
                for (int i = 0; i < m; i++) qData[i][j] = field.divide(v[i], norm);
            } else {
                for (int i = 0; i < m; i++) qData[i][j] = field.zero();
            }
        }
        
        return new QRResult<>(
            new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(qData, field),
            new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(rData, field)
        );
    }

    public static <E> Vector<E> solve(QRResult<E> qr, Vector<E> b, Field<E> field) {
        Matrix<E> q = qr.Q();
        Matrix<E> r = qr.R();
        int n = r.cols();
        
        // x = R^-1 * Q^T * b
        @SuppressWarnings("unchecked")
        E[] qtB = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
        for (int i = 0; i < n; i++) {
            E dot = field.zero();
            for (int k = 0; k < q.rows(); k++) dot = field.add(dot, field.multiply(conjugate(q.get(k, i), field), b.get(k)));
            qtB[i] = dot;
        }
        
        @SuppressWarnings("unchecked")
        E[] x = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
        for (int i = n - 1; i >= 0; i--) {
            E sum = field.zero();
            for (int j = i + 1; j < n; j++) sum = field.add(sum, field.multiply(r.get(i, j), x[j]));
            x[i] = field.divide(field.subtract(qtB[i], sum), r.get(i, i));
        }
        
        return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), field);
    }

    private static double absValue(Object element, Field<?> field) {
        if (element instanceof Real) return ((Real) element).doubleValue();
        if (element instanceof Complex) return ((Complex) element).abs().doubleValue();
        if (element instanceof Number) return ((Number) element).doubleValue();
        return 0.0;
    }

    @SuppressWarnings("unchecked")
    private static <E> E conjugate(E element, Field<E> field) {
        if (element instanceof Complex) return (E) ((Complex) element).conjugate();
        return element;
    }

    @SuppressWarnings("unchecked")
    private static <E> E norm(E[] v, Field<E> field) {
        E dot = field.zero();
        for (E e : v) dot = field.add(dot, field.multiply(conjugate(e, field), e));
        if (dot instanceof Real) return (E) ((Real) dot).sqrt();
        if (dot instanceof Complex) return (E) ((Complex) dot).sqrt();
        return dot; // Fallback
    }
}
