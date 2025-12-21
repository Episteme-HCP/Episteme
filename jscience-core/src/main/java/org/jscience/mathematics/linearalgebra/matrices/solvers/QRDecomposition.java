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

/**
 * QR Decomposition: A = QR where Q is orthogonal, R is upper triangular.
 * <p>
 * Uses Gram-Schmidt orthogonalization. Essential for least squares and
 * eigenvalues.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QRDecomposition {

    private final Matrix<Real> Q;
    private final Matrix<Real> R;

    private QRDecomposition(Matrix<Real> Q, Matrix<Real> R) {
        this.Q = Q;
        this.R = R;
    }

    /**
     * Computes QR decomposition using Gram-Schmidt.
     */
    public static QRDecomposition decompose(Matrix<Real> matrix) {
        int m = matrix.rows();
        int n = matrix.cols();

        Real[][] q = new Real[m][n];
        Real[][] r = new Real[n][n];

        // Gram-Schmidt orthogonalization
        for (int j = 0; j < n; j++) {
            // Get column j
            Real[] v = new Real[m];
            for (int i = 0; i < m; i++) {
                v[i] = matrix.get(i, j);
            }

            // Subtract projections on previous columns
            for (int k = 0; k < j; k++) {
                r[k][j] = Real.ZERO;
                for (int i = 0; i < m; i++) {
                    r[k][j] = r[k][j].add(q[i][k].multiply(v[i]));
                }
                for (int i = 0; i < m; i++) {
                    v[i] = v[i].subtract(r[k][j].multiply(q[i][k]));
                }
            }

            // Compute norm
            r[j][j] = Real.ZERO;
            for (int i = 0; i < m; i++) {
                r[j][j] = r[j][j].add(v[i].multiply(v[i]));
            }
            r[j][j] = r[j][j].sqrt();

            // Normalize
            if (r[j][j].compareTo(Real.of(1e-10)) > 0) {
                for (int i = 0; i < m; i++) {
                    q[i][j] = v[i].divide(r[j][j]);
                }
            } else {
                for (int i = 0; i < m; i++) {
                    q[i][j] = Real.ZERO;
                }
            }

            // Fill rest of R row with zeros
            for (int k = j + 1; k < n; k++) {
                r[j][k] = Real.ZERO;
            }
        }

        // Convert to matrices
        List<List<Real>> qRows = new ArrayList<>();
        List<List<Real>> rRows = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(q[i][j]);
            }
            qRows.add(row);
        }

        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(r[i][j]);
            }
            rRows.add(row);
        }

        Matrix<Real> Q = DenseMatrix.of(qRows, Reals.getInstance());
        Matrix<Real> R = DenseMatrix.of(rRows, Reals.getInstance());

        return new QRDecomposition(Q, R);
    }

    public Matrix<Real> getQ() {
        return Q;
    }

    public Matrix<Real> getR() {
        return R;
    }

    /**
     * Solves least squares problem: min ||Ax - b||₂
     */
    public Real[] solveLeastSquares(Real[] b) {
        int n = R.rows();

        // Compute Q^T * b
        Real[] qtb = new Real[n];
        for (int i = 0; i < n; i++) {
            qtb[i] = Real.ZERO;
            for (int j = 0; j < b.length; j++) {
                qtb[i] = qtb[i].add(Q.get(j, i).multiply(b[j]));
            }
        }

        // Back substitution on R
        Real[] x = new Real[n];
        for (int i = n - 1; i >= 0; i--) {
            Real sum = qtb[i];
            for (int j = i + 1; j < n; j++) {
                sum = sum.subtract(R.get(i, j).multiply(x[j]));
            }
            x[i] = sum.divide(R.get(i, i));
        }

        return x;
    }
}