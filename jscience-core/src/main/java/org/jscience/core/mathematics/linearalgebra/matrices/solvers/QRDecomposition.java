/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.core.mathematics.linearalgebra.matrices.solvers;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.sets.Reals;
import java.util.List;
import java.util.ArrayList;

/**
 * QR Decomposition: A = QR where Q is orthogonal, R is upper triangular.
 * <p>
 * Uses Householder reflections for maximum numerical stability.
 * Essential for least squares and eigenvalues.
 * </p>
 *
 * @author Silvere Martin-Michiellot
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
     * Computes QR decomposition using Householder reflections.
     */
    public static QRDecomposition decompose(Matrix<Real> matrix) {
        int m = matrix.rows();
        int n = matrix.cols();

        Real[][] A = new Real[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = matrix.get(i, j);
            }
        }

        Real[][] Q = new Real[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                Q[i][j] = (i == j) ? Real.ONE : Real.ZERO;
            }
        }

        // Householder QR
        for (int k = 0; k < Math.min(m, n); k++) {
            // Compute norm of column k from row k down
            Real norm = Real.ZERO;
            for (int i = k; i < m; i++) {
                norm = norm.add(A[i][k].multiply(A[i][k]));
            }
            norm = norm.sqrt();

            if (norm.isZero()) continue;

            // Compute reflection vector v
            // v = a - sign(a1) * ||a|| * e1
            Real a1 = A[k][k];
            Real alpha = (a1.compareTo(Real.ZERO) >= 0) ? norm.negate() : norm;
            
            Real[] v = new Real[m - k];
            v[0] = A[k][k].subtract(alpha);
            for (int i = 1; i < v.length; i++) {
                v[i] = A[k + i][k];
            }

            // Normalize v
            Real vNorm = Real.ZERO;
            for (Real val : v) vNorm = vNorm.add(val.multiply(val));
            vNorm = vNorm.sqrt();
            if (vNorm.isZero()) continue;
            for (int i = 0; i < v.length; i++) v[i] = v[i].divide(vNorm);

            // Apply reflection to A: A = (I - 2vv^T)A = A - 2v(v^T A)
            for (int j = k; j < n; j++) {
                Real vDotA = Real.ZERO;
                for (int i = 0; i < v.length; i++) {
                    vDotA = vDotA.add(v[i].multiply(A[k + i][j]));
                }
                Real twoVDotA = vDotA.add(vDotA);
                for (int i = 0; i < v.length; i++) {
                    A[k + i][j] = A[k + i][j].subtract(twoVDotA.multiply(v[i]));
                }
            }

            // Apply reflection to Q: Q = Q(I - 2vv^T) = Q - 2(Qv)v^T
            for (int i = 0; i < m; i++) {
                Real qDotV = Real.ZERO;
                for (int j = 0; j < v.length; j++) {
                    qDotV = qDotV.add(Q[i][k + j].multiply(v[j]));
                }
                Real twoQDotV = qDotV.add(qDotV);
                for (int j = 0; j < v.length; j++) {
                    Q[i][k + j] = Q[i][k + j].subtract(twoQDotV.multiply(v[j]));
                }
            }
        }

        // A now contains R, but due to floating point precision, lower triangle might have noise
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < Math.min(i, n); j++) {
                A[i][j] = Real.ZERO;
            }
        }
        return new QRDecomposition(createMatrix(Q), createMatrix(A));
    }

    private static Matrix<Real> createMatrix(Real[][] data) {
        List<List<Real>> rows = new ArrayList<>();
        for (Real[] row : data) {
            List<Real> rowList = new ArrayList<>();
            for (Real val : row) rowList.add(val);
            rows.add(rowList);
        }
        return DenseMatrix.of(rows, Reals.getInstance());
    }

    public Matrix<Real> getQ() {
        return Q;
    }

    public Matrix<Real> getR() {
        return R;
    }

    public Real[] solveLeastSquares(Real[] b) {
        int m = Q.rows();
        int n = R.cols();

        // Compute Q^T * b
        Real[] qtb = new Real[m];
        for (int i = 0; i < m; i++) {
            Real sum = Real.ZERO;
            for (int j = 0; j < m; j++) {
                sum = sum.add(Q.get(j, i).multiply(b[j]));
            }
            qtb[i] = sum;
        }

        // Back substitution on R
        Real[] x = new Real[n];
        for (int i = n - 1; i >= 0; i--) {
            Real sum = qtb[i];
            for (int j = i + 1; j < n; j++) {
                sum = sum.subtract(R.get(i, j).multiply(x[j]));
            }
            Real rii = R.get(i, i);
            if (rii.abs().compareTo(Real.of(1e-15)) < 0) {
                x[i] = Real.ZERO; // Singular
            } else {
                x[i] = sum.divide(rii);
            }
        }

        return x;
    }
}
