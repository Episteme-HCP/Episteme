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
 * Placeholder for Generic Eigen Decomposition.
 */
public class GenericEigen {
    public static <E> EigenResult<E> decompose(Matrix<E> matrix, Field<E> field) {
        int n = matrix.rows();
        if (n != matrix.cols()) throw new IllegalArgumentException("Matrix must be square");

        @SuppressWarnings("unchecked")
        E[][] A = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) A[i][j] = matrix.get(i, j);

        @SuppressWarnings("unchecked")
        E[][] V = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) V[i][j] = (i == j) ? field.one() : field.zero();

        int maxSweeps = 50;
        
        for (int sweep = 0; sweep < maxSweeps; sweep++) {
            int p = 0, q = 0;
            E maxOff = field.zero();
            double maxOffDouble = -1.0;

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    double valDouble = absValueDouble(A[i][j], field);
                    if (valDouble > maxOffDouble) {
                        maxOffDouble = valDouble;
                        maxOff = A[i][j];
                        p = i;
                        q = j;
                    }
                }
            }

            // Convergence check using maxOffDouble relative to diagonal or absolute
            if (maxOffDouble < 1e-35) break; // Using a much smaller epsilon for HP

            E app = A[p][p];
            E aqq = A[q][q];
            E apq = A[p][q];

            // theta = (aqq - app) / (2 * apq)
            E diff = field.subtract(aqq, app);
            E twoApq = field.add(apq, apq);
            E theta = field.divide(diff, twoApq);
            
            // t = sign(theta) / (|theta| + sqrt(theta^2 + 1))
            E theta2plus1 = field.add(field.multiply(theta, theta), field.one());
            E sqrtTheta2plus1 = sqrt(theta2plus1, field);
            
            E absTheta = abs(theta, field);
            E t = field.divide(field.one(), field.add(absTheta, sqrtTheta2plus1));
            if (isNegative(theta, field)) t = field.negate(t);

            // c = 1 / sqrt(t^2 + 1)
            E t2plus1 = field.add(field.multiply(t, t), field.one());
            E cE = field.divide(field.one(), sqrt(t2plus1, field));
            // s = t * c
            E sE = field.multiply(t, cE);
            
            for (int i = 0; i < n; i++) {
                if (i != p && i != q) {
                    E api = A[p][i];
                    E aqi = A[q][i];
                    A[p][i] = field.subtract(field.multiply(cE, api), field.multiply(sE, aqi));
                    A[q][i] = field.add(field.multiply(sE, api), field.multiply(cE, aqi));
                    // Symmetry
                    A[i][p] = A[p][i];
                    A[i][q] = A[q][i];
                }
            }

            // Update diagonals and off-diagonal
            // app = c^2*app - 2sc*apq + s^2*aqq
            // aqq = s^2*app + 2sc*apq + c^2*aqq
            // apq = 0
            E c2 = field.multiply(cE, cE);
            E s2 = field.multiply(sE, sE);
            E twoSC = field.multiply(field.add(sE, sE), cE);
            
            E newApp = field.add(field.subtract(field.multiply(c2, app), field.multiply(twoSC, apq)), field.multiply(s2, aqq));
            E newAqq = field.add(field.add(field.multiply(s2, app), field.multiply(twoSC, apq)), field.multiply(c2, aqq));
            
            A[p][p] = newApp;
            A[q][q] = newAqq;
            A[p][q] = field.zero();
            A[q][p] = field.zero();

            // V = V * J
            for (int i = 0; i < n; i++) {
                E vip = V[i][p];
                E viq = V[i][q];
                V[i][p] = field.subtract(field.multiply(cE, vip), field.multiply(sE, viq));
                V[i][q] = field.add(field.multiply(sE, vip), field.multiply(cE, viq));
            }
        }

        @SuppressWarnings("unchecked")
        E[] eigenvalues = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
        for (int i = 0; i < n; i++) eigenvalues[i] = A[i][i];

        return new EigenResult<>(
            new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(V, field),
            org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(eigenvalues), field)
        );
    }

    private static <E> E abs(E element, Field<E> field) {
        if (element instanceof Real) return (E) ((Real) element).abs();
        if (element instanceof Complex) return (E) ((Complex) element).abs();
        return element; // Fallback
    }

    private static boolean isNegative(Object element, Field<?> field) {
        if (element instanceof Real) return ((Real) element).doubleValue() < 0;
        if (element instanceof Complex) return ((Complex) element).real() < 0;
        return false;
    }

    private static double absValueDouble(Object element, Field<?> field) {
        if (element instanceof Real) return ((Real) element).doubleValue();
        if (element instanceof Complex) return ((Complex) element).abs().doubleValue();
        if (element instanceof Number) return ((Number) element).doubleValue();
        return 0.0;
    }

    @SuppressWarnings("unchecked")
    private static <E> E sqrt(E element, Field<E> field) {
        if (element instanceof Real) return (E) ((Real) element).sqrt();
        if (element instanceof Complex) return (E) ((Complex) element).sqrt();
        try {
            return (E) element.getClass().getMethod("sqrt").invoke(element);
        } catch (Exception e) {}
        return element;
    }
}

