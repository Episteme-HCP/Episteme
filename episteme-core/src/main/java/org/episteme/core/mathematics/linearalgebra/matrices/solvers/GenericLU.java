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
 * Generic LU Decomposition.
 */
public class GenericLU {

    public static <E> LUResult<E> decompose(Matrix<E> matrix, Field<E> field) {
        return decompose(matrix, field, null);
    }

    public static <E> LUResult<E> decompose(Matrix<E> matrix, Field<E> field, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider) {
        int n = matrix.rows();
        if (n != matrix.cols()) throw new IllegalArgumentException("Matrix must be square");

        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = matrix.get(i, j);

        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;

        for (int k = 0; k < n; k++) {
            int maxRow = k;
            E maxVal = data[k][k];
            double maxValDouble = absValue(maxVal, field);
            
            for (int i = k + 1; i < n; i++) {
                double valDouble = absValue(data[i][k], field);
                if (valDouble > maxValDouble) {
                    maxValDouble = valDouble;
                    maxRow = i;
                }
            }

            if (maxRow != k) {
                swap(data, k, maxRow);
                int tempPerm = perm[k];
                perm[k] = perm[maxRow];
                perm[maxRow] = tempPerm;
            }

            if (maxValDouble > 0) { // Check for non-singularity
                E pivot = data[k][k];
                for (int i = k + 1; i < n; i++) {
                    E factor = field.divide(data[i][k], pivot);
                    data[i][k] = factor;
                    for (int j = k + 1; j < n; j++) {
                        data[i][j] = field.subtract(data[i][j], field.multiply(factor, data[k][j]));
                    }
                }
            }
        }

        // Extract L and U

        @SuppressWarnings("unchecked")
        E[] lFlat = (E[]) new Object[n * n];
        @SuppressWarnings("unchecked")
        E[] uFlat = (E[]) new Object[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) { lFlat[i * n + j] = data[i][j]; uFlat[i * n + j] = field.zero(); }
                else if (i == j) { lFlat[i * n + j] = field.one(); uFlat[i * n + j] = data[i][j]; }
                else { lFlat[i * n + j] = field.zero(); uFlat[i * n + j] = data[i][j]; }
            }
        }

        @SuppressWarnings("unchecked")
        E[] pData = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
        for (int i = 0; i < n; i++) {
            if (field.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
                @SuppressWarnings("unchecked")
                E cVal = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(Real.of(perm[i]));
                pData[i] = cVal;
            } else {
                @SuppressWarnings("unchecked")
                E rVal = (E) (Object) Real.of(perm[i]);
                pData[i] = rVal;
            }
        }
        
        Vector<E> pVec = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<E>(java.util.Arrays.asList(pData)), provider, field);
        Matrix<E> lMat = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(n, n, lFlat), provider, field);
        Matrix<E> uMat = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(n, n, uFlat), provider, field);

        return new LUResult<>(lMat, uMat, pVec);
    }

    private static void swap(Object[] a, int i, int j) {
        Object temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static <E> Vector<E> solve(LUResult<E> lu, Vector<E> b, Field<E> field) {
        return solve(lu, b, field, null);
    }

    public static <E> Vector<E> solve(LUResult<E> lu, Vector<E> b, Field<E> field, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider) {
        int n = lu.L().rows();
        @SuppressWarnings("unchecked")
        E[] x = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
        @SuppressWarnings("unchecked")
        E[] pb = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);

        Vector<E> p = lu.P();
        for (int i = 0; i < n; i++) {
            pb[i] = b.get(toInt(p.get(i)));
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

        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<E>(java.util.Arrays.asList(x)), provider, field);
    }

    public static <E> Vector<E> solve(Matrix<E> a, Vector<E> b, Field<E> field) {
        return solve(a, b, field, null);
    }

    public static <E> Vector<E> solve(Matrix<E> a, Vector<E> b, Field<E> field, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider) {
        return solve(decompose(a, field, provider), b, field, provider);
    }

    public static <E> E determinant(Matrix<E> a, Field<E> field) {
        LUResult<E> lu = decompose(a, field);
        E det = field.one();
        int n = a.rows();
        for (int i = 0; i < n; i++) det = field.multiply(det, lu.U().get(i, i));
        
        int swaps = 0;
        for (int i = 0; i < n; i++) {
            if (toInt(lu.P().get(i)) != i) swaps++;
        }
        if (swaps % 2 != 0) det = field.negate(det);
        return det;
    }

    public static <E> Matrix<E> inverse(Matrix<E> a, Field<E> field) {
        return inverse(a, field, null);
    }

    public static <E> Matrix<E> inverse(Matrix<E> a, Field<E> field, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider) {
        int n = a.rows();
        LUResult<E> lu = decompose(a, field, provider);
        @SuppressWarnings("unchecked")
        E[] invFlat = (E[]) new Object[n * n];
        for (int j = 0; j < n; j++) {
            @SuppressWarnings("unchecked")
            E[] e = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = 0; i < n; i++) e[i] = (i == j) ? field.one() : field.zero();
            Vector<E> ev = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<E>(java.util.Arrays.asList(e)), provider, field);
            Vector<E> x = solve(lu, ev, field, provider);
            for (int i = 0; i < n; i++) invFlat[i * n + j] = x.get(i);
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(n, n, invFlat), provider, field);
    }

    private static double absValue(Object element, Field<?> field) {
        if (element == null) return 0.0;
        if (element instanceof org.episteme.core.mathematics.numbers.real.RealBig rb) {
            // Use doubleValue() for pivot selection is generally fine, but rb.abs() is safer
            return rb.abs().doubleValue();
        }
        if (element instanceof Real r) return r.doubleValue();
        if (element instanceof Complex c) return c.abs().doubleValue();
        if (element instanceof Number n) return n.doubleValue();
        
        // Fallback for types that might not implement interfaces but have an abs() method
        try {
            java.lang.reflect.Method absMethod = element.getClass().getMethod("abs");
            Object absVal = absMethod.invoke(element);
            if (absVal instanceof Number n) return n.doubleValue();
            java.lang.reflect.Method doubleValueMethod = absVal.getClass().getMethod("doubleValue");
            return (double) doubleValueMethod.invoke(absVal);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static int toInt(Object val) {
        if (val == null) return 0;
        if (val instanceof Integer i) return i;
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealBig rb) return rb.bigDecimalValue().intValue();
        if (val instanceof Real r) return (int) r.doubleValue();
        if (val instanceof Complex c) return (int) c.getReal().doubleValue();
        if (val instanceof Number n) return n.intValue();
        return 0;
    }
}
