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
 * Generic Cholesky Decomposition.
 */
public class GenericCholesky {

    public static <E> CholeskyResult<E> decompose(Matrix<E> matrix, Field<E> field, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider) {
        int n = matrix.rows();
        if (n != matrix.cols()) throw new IllegalArgumentException("Matrix must be square");

        @SuppressWarnings("unchecked")
        E[][] lData = (E[][]) java.lang.reflect.Array.newInstance(componentType(field), n, n);
        for (int i = 0; i < n; i++) java.util.Arrays.fill(lData[i], field.zero());

        for (int j = 0; j < n; j++) {
            E sum = field.zero();
            for (int k = 0; k < j; k++) sum = field.add(sum, field.multiply(lData[j][k], conjugate(lData[j][k], field)));
            E val = field.subtract(matrix.get(j, j), sum);
            
            // Stability check: if val is too small, matrix is not positive definite or is singular
            if (val instanceof Real && ((Real)val).doubleValue() <= 1e-30) {
                throw new ArithmeticException("Cholesky decomposition failed: matrix is singular or not positive-definite at index " + j + " (val=" + val + ")");
            } else if (val instanceof Complex && ((Complex)val).abs().doubleValue() <= 1e-30) {
                 throw new ArithmeticException("Cholesky decomposition failed: matrix is singular or not positive-definite at index " + j + " (abs(val)=" + ((Complex)val).abs() + ")");
            }

            lData[j][j] = sqrt(val, field);

            for (int i = j + 1; i < n; i++) {
                sum = field.zero();
                for (int k = 0; k < j; k++) sum = field.add(sum, field.multiply(lData[i][k], conjugate(lData[j][k], field)));
                lData[i][j] = field.divide(field.subtract(matrix.get(i, j), sum), lData[j][j]);
            }
        }

        return new CholeskyResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(n, n, flatten(lData, n, field)), provider, field));
    }

    private static <E> E[] flatten(E[][] data, int n, Field<E> field) {
        @SuppressWarnings("unchecked")
        E[] flat = (E[]) new Object[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                flat[i * n + j] = data[i][j];
            }
        }
        return flat;
    }

    public static <E> Vector<E> solve(CholeskyResult<E> c, Vector<E> b, Field<E> field, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> provider) {
        Matrix<E> l = c.L();
        int n = l.rows();
        
        @SuppressWarnings("unchecked")
        E[] y = (E[]) java.lang.reflect.Array.newInstance(componentType(field), n);
        for (int i = 0; i < n; i++) {
            E sum = field.zero();
            for (int j = 0; j < i; j++) sum = field.add(sum, field.multiply(l.get(i, j), y[j]));
            y[i] = field.divide(field.subtract(b.get(i), sum), l.get(i, i));
        }

        @SuppressWarnings("unchecked")
        E[] x = (E[]) java.lang.reflect.Array.newInstance(componentType(field), n);
        for (int i = n - 1; i >= 0; i--) {
            E sum = field.zero();
            for (int j = i + 1; j < n; j++) sum = field.add(sum, field.multiply(conjugate(l.get(j, i), field), x[j]));
            x[i] = field.divide(field.subtract(y[i], sum), conjugate(l.get(i, i), field));
        }

        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<E>(java.util.Arrays.asList(x)), provider, field);
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
    private static <E> E sqrt(E element, Field<E> field) {
        Object res = null;
        if (element instanceof Real) res = ((Real) element).sqrt();
        else if (element instanceof Complex) res = ((Complex) element).sqrt();
        
        if (field.zero() instanceof Complex && res instanceof Real) {
            return (E) Complex.of((Real) res);
        }
        return (E) res;
    }
}
