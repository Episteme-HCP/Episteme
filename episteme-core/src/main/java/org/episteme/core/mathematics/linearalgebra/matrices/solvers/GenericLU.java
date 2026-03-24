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
import org.episteme.core.mathematics.context.MathContext;

/**
 * Generic LU Decomposition.
 */
public class GenericLU {

    public static <E> LUResult<E> decompose(Matrix<E> matrix, Field<E> field) {
        int n = matrix.rows();
        if (n != matrix.cols()) throw new IllegalArgumentException("Matrix must be square");

        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = matrix.get(i, j);

        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;

        for (int k = 0; k < n; k++) {
            int maxRow = k;
            double maxVal = absValue(data[k][k], field);
            for (int i = k + 1; i < n; i++) {
                double val = absValue(data[i][k], field);
                if (val > maxVal) {
                    maxVal = val;
                    maxRow = i;
                }
            }

            if (maxRow != k) {
                E[] temp = data[k];
                data[k] = data[maxRow];
                data[maxRow] = temp;
                int tempPerm = perm[k];
                perm[k] = perm[maxRow];
                perm[maxRow] = tempPerm;
            }

            for (int i = k + 1; i < n; i++) {
                if (absValue(data[k][k], field) > 1e-20) {
                    E factor = field.divide(data[i][k], data[k][k]);
                    data[i][k] = factor;
                    for (int j = k + 1; j < n; j++) {
                        data[i][j] = field.add(data[i][j], field.negate(field.multiply(factor, data[k][j])));
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        E[][] lData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
        @SuppressWarnings("unchecked")
        E[][] uData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) { lData[i][j] = data[i][j]; uData[i][j] = field.zero(); }
                else if (i == j) { lData[i][j] = field.one(); uData[i][j] = data[i][j]; }
                else { lData[i][j] = field.zero(); uData[i][j] = data[i][j]; }
            }
        }

        @SuppressWarnings("unchecked")
        E[] pData = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
        for (int i = 0; i < n; i++) {
            // Safe cast assuming the field can handle Real or is a field of Reals/Complex
            pData[i] = (E) (Object) Real.of(perm[i]);
            if (field.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
                pData[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(Real.of(perm[i]));
            }
        }
        Vector<E> pVec = org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(pData), field);
        
        return new LUResult<>(
            new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(lData, field),
            new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(uData, field),
            pVec
        );
    }

    public static <E> Vector<E> solve(LUResult<E> lu, Vector<E> b, Field<E> field) {
        int n = lu.L().rows();
        @SuppressWarnings("unchecked")
        E[] x = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
        @SuppressWarnings("unchecked")
        E[] pb = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);

        Vector<E> p = lu.P();
        for (int i = 0; i < n; i++) {
            pb[i] = b.get((int) ((Number) p.get(i)).doubleValue());
        }

        @SuppressWarnings("unchecked")
        E[] y = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
        Matrix<E> l = lu.L();
        for (int i = 0; i < n; i++) {
            E sum = field.zero();
            for (int j = 0; j < i; j++) sum = field.add(sum, field.multiply(l.get(i, j), y[j]));
            y[i] = field.subtract(pb[i], sum);
        }

        Matrix<E> u = lu.U();
        for (int i = n - 1; i >= 0; i--) {
            E sum = field.zero();
            for (int j = i + 1; j < n; j++) sum = field.add(sum, field.multiply(u.get(i, j), x[j]));
            x[i] = field.divide(field.subtract(y[i], sum), u.get(i, i));
        }

        return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), field);
    }

    private static double absValue(Object element, Field<?> field) {
        if (element instanceof Real) return ((Real) element).doubleValue();
        if (element instanceof Complex) return ((Complex) element).abs().doubleValue();
        if (element instanceof Number) return ((Number) element).doubleValue();
        return 0.0;
    }
}
