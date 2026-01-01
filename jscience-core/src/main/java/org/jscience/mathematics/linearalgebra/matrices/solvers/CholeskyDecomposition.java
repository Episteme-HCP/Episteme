/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.linearalgebra.matrices.solvers;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import java.util.List;
import java.util.ArrayList;

public class CholeskyDecomposition {

    private final Matrix<Real> L; // Lower triangular

    private CholeskyDecomposition(Matrix<Real> L) {
        this.L = L;
    }

    /**
     * Computes Cholesky decomposition A = LL^T.
     * <p>
     * <b>Algorithm steps</b>:
     * <ol>
     * <li>For each diagonal element: L[i][i] = sqrt(A[i][i] - ÃŽÂ£ L[i][k]Ã‚Â²)</li>
     * <li>For each off-diagonal: L[i][j] = (A[i][j] - ÃŽÂ£ L[i][k]*L[j][k]) /
     * L[j][j]</li>
     * </ol>
     * </p>
     * 
     * <p>
     * <b>Invariants</b>:
     * </p>
     * <ul>
     * <li>L is lower triangular (L[i][j] = 0 for j > i)</li>
     * <li>A = LL^T exactly (within numerical precision)</li>
     * <li>All diagonal elements of L are positive</li>
     * </ul>
     * 
     * @param matrix symmetric positive definite matrix to decompose
     * @return Cholesky decomposition containing L
     * @throws IllegalArgumentException if matrix is not square
     * @throws IllegalArgumentException if matrix is not positive definite
     */
    public static CholeskyDecomposition decompose(Matrix<Real> matrix) {
        int n = matrix.rows();

        if (n != matrix.cols()) {
            throw new IllegalArgumentException("Matrix must be square");
        }

        Real[][] L = new Real[n][n];

        // Initialize to zero
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                L[i][j] = Real.ZERO;
            }
        }

        // Cholesky-Banachiewicz algorithm
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                Real sum = Real.ZERO;

                if (j == i) {
                    // Diagonal elements
                    for (int k = 0; k < j; k++) {
                        sum = sum.add(L[j][k].multiply(L[j][k]));
                    }

                    Real diag = matrix.get(j, j).subtract(sum);
                    if (diag.compareTo(Real.ZERO) <= 0) {
                        throw new IllegalArgumentException(
                                "Matrix is not positive definite (diagonal[" + j + "] = " + diag + ")");
                    }
                    L[j][j] = diag.sqrt();
                } else {
                    // Off-diagonal elements
                    for (int k = 0; k < j; k++) {
                        sum = sum.add(L[i][k].multiply(L[j][k]));
                    }
                    L[i][j] = matrix.get(i, j).subtract(sum).divide(L[j][j]);
                }
            }
        }

        return new CholeskyDecomposition(createMatrix(L));
    }

    /**
     * Solves Ax = b using Cholesky decomposition.
     * <p>
     * <b>Algorithm steps</b>:
     * <ol>
     * <li>Forward substitution: Solve Ly = b for y</li>
     * <li>Backward substitution: Solve L^T x = y for x</li>
     * </ol>
     * Complexity: O(nÃ‚Â²)
     * </p>
     * 
     * @param b right-hand side vector
     * @return solution vector x such that Ax = b
     */
    public Real[] solve(Real[] b) {
        int n = b.length;
        Real[] y = new Real[n];
        Real[] x = new Real[n];

        // Forward substitution: Ly = b
        for (int i = 0; i < n; i++) {
            Real sum = Real.ZERO;
            for (int j = 0; j < i; j++) {
                sum = sum.add(L.get(i, j).multiply(y[j]));
            }
            y[i] = b[i].subtract(sum).divide(L.get(i, i));
        }

        // Backward substitution: L^T x = y
        for (int i = n - 1; i >= 0; i--) {
            Real sum = Real.ZERO;
            for (int j = i + 1; j < n; j++) {
                sum = sum.add(L.get(j, i).multiply(x[j]));
            }
            x[i] = y[i].subtract(sum).divide(L.get(i, i));
        }

        return x;
    }

    /**
     * Computes determinant efficiently: det(A) = (Ã¢Ë†Â L[i][i])Ã‚Â²
     * <p>
     * Complexity: O(n)
     * </p>
     * 
     * @return determinant of original matrix A
     */
    public Real determinant() {
        Real prod = Real.ONE;
        for (int i = 0; i < L.rows(); i++) {
            prod = prod.multiply(L.get(i, i));
        }
        return prod.multiply(prod);
    }

    /**
     * Computes matrix inverse using Cholesky.
     * <p>
     * Solves AX = I by solving Ax_i = e_i for each column.
     * Complexity: O(nÃ‚Â³)
     * </p>
     * 
     * @return inverse of original matrix A
     */
    public Matrix<Real> inverse() {
        int n = L.rows();
        List<List<Real>> inv = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Real[] ei = new Real[n];
            for (int j = 0; j < n; j++) {
                ei[j] = (i == j) ? Real.ONE : Real.ZERO;
            }

            Real[] col = solve(ei);
            List<Real> colList = new ArrayList<>();
            for (Real val : col) {
                colList.add(val);
            }
            inv.add(colList);
        }

        // Transpose result
        List<List<Real>> invT = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(inv.get(j).get(i));
            }
            invT.add(row);
        }

        return DenseMatrix.of(invT, Reals.getInstance());
    }

    /**
     * Returns the lower triangular matrix L.
     * 
     * @return L such that A = LL^T
     */
    public Matrix<Real> getL() {
        return L;
    }

    /**
     * Returns L^T (upper triangular).
     * 
     * @return transpose of L
     */
    public Matrix<Real> getLT() {
        int n = L.rows();
        List<List<Real>> lt = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(L.get(j, i));
            }
            lt.add(row);
        }

        return DenseMatrix.of(lt, Reals.getInstance());
    }

    private static Matrix<Real> createMatrix(Real[][] data) {
        List<List<Real>> rows = new ArrayList<>();
        for (Real[] row : data) {
            List<Real> rowList = new ArrayList<>();
            for (Real val : row) {
                rowList.add(val);
            }
            rows.add(rowList);
        }
        return DenseMatrix.of(rows, Reals.getInstance());
    }
}

