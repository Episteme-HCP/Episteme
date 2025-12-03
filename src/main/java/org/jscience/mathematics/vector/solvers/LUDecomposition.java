/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.vector.solvers;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.sets.Reals;
import java.util.List;
import java.util.ArrayList;

/**
 * LU Decomposition: A = LU where L is lower triangular, U is upper triangular.
 * <p>
 * Used for solving linear systems, computing determinants, and matrix
 * inversion.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class LUDecomposition {

    private final Matrix<Real> L;
    private final Matrix<Real> U;
    private final int[] permutation;
    private final int swaps;

    private LUDecomposition(Matrix<Real> L, Matrix<Real> U, int[] permutation, int swaps) {
        this.L = L;
        this.U = U;
        this.permutation = permutation;
        this.swaps = swaps;
    }

    /**
     * Computes LU decomposition with partial pivoting.
     */
    public static LUDecomposition decompose(Matrix<Real> matrix) {
        int n = matrix.rows();
        if (n != matrix.cols()) {
            throw new IllegalArgumentException("Matrix must be square");
        }

        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = matrix.get(i, j);
            }
        }

        int[] perm = new int[n];
        for (int i = 0; i < n; i++)
            perm[i] = i;
        int swapCount = 0;

        // Gaussian elimination with partial pivoting
        for (int k = 0; k < n; k++) {
            // Find pivot
            int maxRow = k;
            Real maxVal = data[k][k].abs();
            for (int i = k + 1; i < n; i++) {
                Real val = data[i][k].abs();
                if (val.compareTo(maxVal) > 0) {
                    maxVal = val;
                    maxRow = i;
                }
            }

            // Swap rows if needed
            if (maxRow != k) {
                Real[] temp = data[k];
                data[k] = data[maxRow];
                data[maxRow] = temp;
                int tempPerm = perm[k];
                perm[k] = perm[maxRow];
                perm[maxRow] = tempPerm;
                swapCount++;
            }

            // Eliminate column
            for (int i = k + 1; i < n; i++) {
                if (!data[k][k].isZero()) {
                    Real factor = data[i][k].divide(data[k][k]);
                    data[i][k] = factor;
                    for (int j = k + 1; j < n; j++) {
                        data[i][j] = data[i][j].subtract(factor.multiply(data[k][j]));
                    }
                }
            }
        }

        // Extract L and U
        List<List<Real>> lRows = new ArrayList<>();
        List<List<Real>> uRows = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            List<Real> lRow = new ArrayList<>();
            List<Real> uRow = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                if (i > j) {
                    lRow.add(data[i][j]);
                    uRow.add(Real.ZERO);
                } else if (i == j) {
                    lRow.add(Real.ONE);
                    uRow.add(data[i][j]);
                } else {
                    lRow.add(Real.ZERO);
                    uRow.add(data[i][j]);
                }
            }

            lRows.add(lRow);
            uRows.add(uRow);
        }

        Matrix<Real> L = DenseMatrix.of(lRows, Reals.getInstance());
        Matrix<Real> U = DenseMatrix.of(uRows, Reals.getInstance());

        return new LUDecomposition(L, U, perm, swapCount);
    }

    public Matrix<Real> getL() {
        return L;
    }

    public Matrix<Real> getU() {
        return U;
    }

    /**
     * Computes determinant using LU decomposition.
     */
    public Real determinant() {
        Real det = Real.ONE;
        int n = U.rows();
        for (int i = 0; i < n; i++) {
            det = det.multiply(U.get(i, i));
        }
        return (swaps % 2 == 0) ? det : det.negate();
    }

    /**
     * Solves Ax = b using LU decomposition.
     */
    public Real[] solve(Real[] b) {
        int n = L.rows();

        // Apply permutation to b
        Real[] pb = new Real[n];
        for (int i = 0; i < n; i++) {
            pb[i] = b[permutation[i]];
        }

        // Forward substitution: Ly = Pb
        Real[] y = new Real[n];
        for (int i = 0; i < n; i++) {
            Real sum = pb[i];
            for (int j = 0; j < i; j++) {
                sum = sum.subtract(L.get(i, j).multiply(y[j]));
            }
            y[i] = sum;
        }

        // Back substitution: Ux = y
        Real[] x = new Real[n];
        for (int i = n - 1; i >= 0; i--) {
            Real sum = y[i];
            for (int j = i + 1; j < n; j++) {
                sum = sum.subtract(U.get(i, j).multiply(x[j]));
            }
            x[i] = sum.divide(U.get(i, i));
        }

        return x;
    }
}



