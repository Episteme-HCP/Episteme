/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.solvers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;

/**
 * Result of an LU decomposition.
 * <p>
 * P * A = L * U
 * </p>
 *
 * @param <E> the element type
 * @param L   the lower triangular matrix
 * @param U   the upper triangular matrix
 * @param P   the permutation vector
 */
public record LUResult<E>(Matrix<E> L, Matrix<E> U, Vector<E> P) {
    public Matrix<E> getL() { return L; }
    public Matrix<E> getU() { return U; }
    public Vector<E> getP() { return P; }

    /**
     * Solves the system A * x = b using the LU decomposition results.
     *
     * @param b the right-hand side vector
     * @return the solution vector x
     * @throws IllegalArgumentException if dimensions mismatch
     * @throws UnsupportedOperationException if the ring is not a Field
     * @throws ArithmeticException if the matrix is singular
     */
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.Vector<E> solve(org.episteme.core.mathematics.linearalgebra.Vector<E> b) {
        int n = L.rows();
        if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = L.getScalarRing();
        if (!(ring instanceof org.episteme.core.mathematics.structures.rings.Field)) {
            throw new UnsupportedOperationException("Solve requires a Field structure");
        }
        org.episteme.core.mathematics.structures.rings.Field<E> field = (org.episteme.core.mathematics.structures.rings.Field<E>) ring;

        // 1. Apply permutation P to b: b' = P * b
        E[] bp = (E[]) new Object[n];
        for (int i = 0; i < n; i++) {
            Object pVal = P.get(i);
            int pIdx;
            if (pVal instanceof Number) pIdx = ((Number) pVal).intValue();
            else if (pVal instanceof org.episteme.core.mathematics.numbers.real.Real) pIdx = (int) ((org.episteme.core.mathematics.numbers.real.Real) pVal).doubleValue();
            else pIdx = i;
            bp[i] = b.get(pIdx);
        }

        // 2. Forward substitution: L * y = b'
        E[] y = (E[]) new Object[n];
        for (int i = 0; i < n; i++) {
            E sum = field.zero();
            for (int j = 0; j < i; j++) {
                sum = field.add(sum, field.multiply(L.get(i, j), y[j]));
            }
            y[i] = field.add(bp[i], field.negate(sum));
        }

        // 3. Backward substitution: U * x = y
        E[] x = (E[]) new Object[n];
        for (int i = n - 1; i >= 0; i--) {
            E sum = field.zero();
            for (int j = i + 1; j < n; j++) {
                sum = field.add(sum, field.multiply(U.get(i, j), x[j]));
            }
            E diag = U.get(i, i);
            if (diag.equals(field.zero())) throw new ArithmeticException("Matrix is singular (U diagonal is zero)");
            x[i] = field.divide(field.add(y[i], field.negate(sum)), diag);
        }

        return org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(x), field);
    }
}
