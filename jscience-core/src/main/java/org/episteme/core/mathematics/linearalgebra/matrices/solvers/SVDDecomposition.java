/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.linearalgebra.matrices.solvers;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.sets.Reals;
import java.util.List;
import java.util.ArrayList;

/**
 * Singular Value Decomposition (SVD).
 * <p>
 * For a matrix A of size m x n, the SVD is a factorization of the form:
 *
 * <pre>
 * A = U * S * V ^ T
 * </pre>
 *
 * where:
 * <ul>
 * <li>U is an m x m orthogonal matrix (columns are left singular vectors).</li>
 * <li>S is an m x n diagonal matrix with non-negative real numbers on the
 * diagonal (singular values).</li>
 * <li>V is an n x n orthogonal matrix (columns are right singular
 * vectors).</li>
 * </ul>
 * </p>
 *
 * <h2>References</h2>
 * <ul>
 * <li><a href=
 * "https://en.wikipedia.org/wiki/Singular_value_decomposition">Wikipedia:
 * Singular value decomposition</a></li>
 * <li>Golub, G. H., & Van Loan, C. F. (1996). <i>Matrix Computations</i> (3rd
 * ed.). Johns Hopkins University Press.</li>
 * <li>LAPACK Users' Guide.
 * <a href="http://www.netlib.org/lapack/lug/node53.html">Singular Value
 * Decomposition</a></li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SVDDecomposition {

    private final Matrix<Real> U;
    private final Real[] singularValues;
    private final Matrix<Real> V;

    private SVDDecomposition(Matrix<Real> U, Real[] singularValues, Matrix<Real> V) {
        this.U = U;
        this.singularValues = singularValues;
        this.V = V;
    }

    /**
     * Computes SVD using Golub-Reinsch algorithm.
     * <p>
     * Two-phase approach:
     * 1. Bidiagonalization using Householder reflections
     * 2. QR iteration on bidiagonal matrix
     * </p>
     */
    public enum Algorithm {
        GOLUB_REINSCH,
        ATA_APPROXIMATION
    }

    /**
     * Computes SVD using the specified algorithm.
     * 
     * @param matrix the matrix to decompose
     * @param algo   the algorithm to use
     * @return the SVD decomposition
     */
    public static SVDDecomposition decompose(Matrix<Real> matrix, Algorithm algo) {
        int m = matrix.rows();
        int n = matrix.cols();

        double[][] A = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = matrix.get(i, j).doubleValue();
            }
        }

        int nu = Math.min(m, n);
        double[] s = new double[Math.min(m + 1, n)];
        double[][] U = new double[m][nu];
        double[][] V = new double[n][n];
        double[] e = new double[n];
        double[] work = new double[m];
        boolean wantu = true;
        boolean wantv = true;

        // Reduce A to bidiagonal form, storing the diagonal elements
        // in s and the super-diagonal elements in e.
        int nct = Math.min(m - 1, n);
        int nrt = Math.max(0, Math.min(n - 2, m));
        for (int k = 0; k < Math.max(nct, nrt); k++) {
            if (k < nct) {
                // Compute the transformation for the k-th column and
                // place the k-th diagonal in s[k].
                // Compute 2-norm of k-th column without under/overflow.
                s[k] = 0;
                for (int i = k; i < m; i++) {
                    s[k] = Math.hypot(s[k], A[i][k]);
                }
                if (s[k] != 0.0) {
                    if (A[k][k] < 0.0) {
                        s[k] = -s[k];
                    }
                    for (int i = k; i < m; i++) {
                        A[i][k] /= s[k];
                    }
                    A[k][k] += 1.0;
                }
                s[k] = -s[k];
            }
            for (int j = k + 1; j < n; j++) {
                if ((k < nct) && (s[k] != 0.0)) {
                    // Apply the transformation.
                    double t = 0;
                    for (int i = k; i < m; i++) {
                        t += A[i][k] * A[i][j];
                    }
                    t = -t / A[k][k];
                    for (int i = k; i < m; i++) {
                        A[i][j] += t * A[i][k];
                    }
                }
                // Place the k-th row of A into e for the
                // subsequent calculation of the row transformation.
                e[j] = A[k][j];
            }
            if (wantu && (k < nct)) {
                // Place the transformation in U for subsequent back
                // multiplication.
                for (int i = k; i < m; i++) {
                    U[i][k] = A[i][k];
                }
            }
            if (k < nrt) {
                // Compute the k-th row transformation and place the
                // k-th super-diagonal in e[k].
                // Compute 2-norm without under/overflow.
                e[k] = 0;
                for (int i = k + 1; i < n; i++) {
                    e[k] = Math.hypot(e[k], e[i]);
                }
                if (e[k] != 0.0) {
                    if (e[k + 1] < 0.0) {
                        e[k] = -e[k];
                    }
                    for (int i = k + 1; i < n; i++) {
                        e[i] /= e[k];
                    }
                    e[k + 1] += 1.0;
                }
                e[k] = -e[k];
                if ((k + 1 < m) && (e[k] != 0.0)) {
                    // Apply the transformation.
                    for (int i = k + 1; i < m; i++) {
                        work[i] = 0.0;
                    }
                    for (int j = k + 1; j < n; j++) {
                        for (int i = k + 1; i < m; i++) {
                            work[i] += e[j] * A[i][j];
                        }
                    }
                    for (int j = k + 1; j < n; j++) {
                        double t = -e[j] / e[k + 1];
                        for (int i = k + 1; i < m; i++) {
                            A[i][j] += t * work[i];
                        }
                    }
                }
                if (wantv) {
                    // Place the transformation in V for subsequent
                    // back multiplication.
                    for (int i = k + 1; i < n; i++) {
                        V[i][k] = e[i];
                    }
                }
            }
        }

        // Set up the final bidiagonal matrix or order p.
        int p = Math.min(n, m + 1);
        if (nct < n) {
            s[nct] = A[nct][nct];
        }
        if (m < p) {
            s[p - 1] = 0.0;
        }
        if (nrt + 1 < p) {
            e[nrt] = A[nrt][p - 1];
        }
        e[p - 1] = 0.0;

        // If required, generate U.
        if (wantu) {
            for (int j = nct; j < nu; j++) {
                for (int i = 0; i < m; i++) {
                    U[i][j] = 0.0;
                }
                U[j][j] = 1.0;
            }
            for (int k = nct - 1; k >= 0; k--) {
                if (s[k] != 0.0) {
                    for (int j = k + 1; j < nu; j++) {
                        double t = 0;
                        for (int i = k; i < m; i++) {
                            t += U[i][k] * U[i][j];
                        }
                        t = -t / U[k][k];
                        for (int i = k; i < m; i++) {
                            U[i][j] += t * U[i][k];
                        }
                    }
                    for (int i = k; i < m; i++) {
                        U[i][k] = -U[i][k];
                    }
                    U[k][k] = 1.0 + U[k][k];
                    for (int i = 0; i < k - 1; i++) {
                        U[i][k] = 0.0;
                    }
                } else {
                    for (int i = 0; i < m; i++) {
                        U[i][k] = 0.0;
                    }
                    U[k][k] = 1.0;
                }
            }
        }

        // If required, generate V.
        if (wantv) {
            for (int k = n - 1; k >= 0; k--) {
                if ((k < nrt) && (e[k] != 0.0)) {
                    for (int j = k + 1; j < nu; j++) {
                        double t = 0;
                        for (int i = k + 1; i < n; i++) {
                            t += V[i][k] * V[i][j];
                        }
                        t = -t / V[k + 1][k];
                        for (int i = k + 1; i < n; i++) {
                            V[i][j] += t * V[i][k];
                        }
                    }
                }
                for (int i = 0; i < n; i++) {
                    V[i][k] = 0.0;
                }
                V[k][k] = 1.0;
            }
        }

        // Main iteration loop for the singular values.
        int pp = p - 1;
        int iter = 0;
        double eps = Math.pow(2.0, -52.0);
        double tiny = Math.pow(2.0, -966.0);
        while (p > 0) {
            int k, kase;

            // Here is where a test for too many iterations would go.
            if (iter >= 300) { // Limit iterations to avoid infinite loop
                break;
            }

            // This section of the program inspects for
            // negligible elements in the s and e arrays.  On
            // completion the variables kase and k are set as follows.
            // kase = 1     if s(p) and e[k-1] are negligible and k<p
            // kase = 2     if s(k) is negligible and k<p
            // kase = 3     if e[k-1] is negligible, k<p, and
            //              s(k), ..., s(p) are not negligible (qr step).
            // kase = 4     if e(p-1) is negligible (convergence).

            for (k = p - 2; k >= -1; k--) {
                if (k == -1) {
                    break;
                }
                if (Math.abs(e[k]) <= tiny + eps * (Math.abs(s[k]) + Math.abs(s[k + 1]))) {
                    e[k] = 0.0;
                    break;
                }
            }
            if (k == p - 2) {
                kase = 4;
            } else {
                int ks;
                for (ks = p - 1; ks >= k; ks--) {
                    if (ks == k) {
                        break;
                    }
                    double t = (ks != p ? Math.abs(e[ks]) : 0.) +
                            (ks != k + 1 ? Math.abs(e[ks - 1]) : 0.);
                    if (Math.abs(s[ks]) <= tiny + eps * t) {
                        s[ks] = 0.0;
                        break;
                    }
                }
                if (ks == k) {
                    kase = 3;
                } else if (ks == p - 1) {
                    kase = 1;
                } else {
                    kase = 2;
                    k = ks;
                }
            }
            k++;

            // Perform the task dictated by kase.
            switch (kase) {
                case 1: { // Deflate negligible s(p).
                    double f = e[p - 2];
                    e[p - 2] = 0.0;
                    for (int j = p - 2; j >= k; j--) {
                        double t = Math.hypot(s[j], f);
                        double cs = s[j] / t;
                        double sn = f / t;
                        s[j] = t;
                        if (j != k) {
                            f = -sn * e[j - 1];
                            e[j - 1] = cs * e[j - 1];
                        }
                        if (wantv) {
                            for (int i = 0; i < n; i++) {
                                t = cs * V[i][j] + sn * V[i][p - 1];
                                V[i][p - 1] = -sn * V[i][j] + cs * V[i][p - 1];
                                V[i][j] = t;
                            }
                        }
                    }
                }
                break;
                case 2: { // Split at negligible s(k).
                    double f = e[k - 1];
                    e[k - 1] = 0.0;
                    for (int j = k; j < p; j++) {
                        double t = Math.hypot(s[j], f);
                        double cs = s[j] / t;
                        double sn = f / t;
                        s[j] = t;
                        f = -sn * e[j];
                        e[j] = cs * e[j];
                        if (wantu) {
                            for (int i = 0; i < m; i++) {
                                t = cs * U[i][j] + sn * U[i][k - 1];
                                U[i][k - 1] = -sn * U[i][j] + cs * U[i][k - 1];
                                U[i][j] = t;
                            }
                        }
                    }
                }
                break;
                case 3: { // Perform one qr step.
                    // Calculate the shift.
                    double scale = Math.max(Math.max(Math.max(Math.max(
                            Math.abs(s[p - 1]), Math.abs(s[p - 2])), Math.abs(e[p - 2])),
                            Math.abs(s[k])), Math.abs(e[k]));
                    double sp = s[p - 1] / scale;
                    double spm1 = s[p - 2] / scale;
                    double epm1 = e[p - 2] / scale;
                    double sk = s[k] / scale;
                    double ek = e[k] / scale;
                    double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
                    double c = (sp * epm1) * (sp * epm1);
                    double shift = 0.0;
                    if ((b != 0.0) || (c != 0.0)) {
                        shift = Math.sqrt(b * b + c);
                        if (b < 0.0) {
                            shift = -shift;
                        }
                        shift = c / (b + shift);
                    }
                    double f = (sk + sp) * (sk - sp) + shift;
                    double g = sk * ek;

                    // Chase zeros.
                    for (int j = k; j < p - 1; j++) {
                        double t = Math.hypot(f, g);
                        double cs = f / t;
                        double sn = g / t;
                        if (j != k) {
                            e[j - 1] = t;
                        }
                        f = cs * s[j] + sn * e[j];
                        e[j] = cs * e[j] - sn * s[j];
                        g = sn * s[j + 1];
                        s[j + 1] = cs * s[j + 1];
                        if (wantv) {
                            for (int i = 0; i < n; i++) {
                                t = cs * V[i][j] + sn * V[i][j + 1];
                                V[i][j + 1] = -sn * V[i][j] + cs * V[i][j + 1];
                                V[i][j] = t;
                            }
                        }
                        t = Math.hypot(f, g);
                        cs = f / t;
                        sn = g / t;
                        s[j] = t;
                        f = cs * e[j] + sn * s[j + 1];
                        s[j + 1] = -sn * e[j] + cs * s[j + 1];
                        g = sn * e[j + 1];
                        e[j + 1] = cs * e[j + 1];
                        if (wantu && (j < m - 1)) {
                            for (int i = 0; i < m; i++) {
                                t = cs * U[i][j] + sn * U[i][j + 1];
                                U[i][j + 1] = -sn * U[i][j] + cs * U[i][j + 1];
                                U[i][j] = t;
                            }
                        }
                    }
                    e[p - 2] = f;
                    iter = iter + 1;
                }
                break;
                case 4: { // Convergence.
                    // Make the singular values positive.
                    if (s[k] <= 0.0) {
                        s[k] = (s[k] < 0.0 ? -s[k] : 0.0);
                        if (wantv) {
                            for (int i = 0; i <= pp; i++) {
                                V[i][k] = -V[i][k];
                            }
                        }
                    }
                    // Order the singular values.
                    while (k < pp) {
                        if (s[k] >= s[k + 1]) {
                            break;
                        }
                        double t = s[k];
                        s[k] = s[k + 1];
                        s[k + 1] = t;
                        if (wantv && (k < n - 1)) {
                            for (int i = 0; i < n; i++) {
                                t = V[i][k + 1];
                                V[i][k + 1] = V[i][k];
                                V[i][k] = t;
                            }
                        }
                        if (wantu && (k < m - 1)) {
                            for (int i = 0; i < m; i++) {
                                t = U[i][k + 1];
                                U[i][k + 1] = U[i][k];
                                U[i][k] = t;
                            }
                        }
                        k++;
                    }
                    iter = 0;
                    p--;
                }
                break;
            }
        }

        // Return Matrix<Real> wrappers
        Real[] sigmaReal = new Real[Math.min(m, n)];
        for (int i = 0; i < sigmaReal.length; i++) {
            sigmaReal[i] = Real.of(s[i]);
        }
        
        Real[][] UReal = new Real[m][nu];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < nu; j++) {
                UReal[i][j] = Real.of(U[i][j]);
            }
        }
        
        Real[][] VReal = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                VReal[i][j] = Real.of(V[i][j]);
            }
        }

        return new SVDDecomposition(createMatrix(UReal), sigmaReal, createMatrix(VReal));
    }

    /**
     * Computes SVD using the default approximation (ATA_APPROXIMATION).
     */
    public static SVDDecomposition decompose(Matrix<Real> matrix) {
        return decompose(matrix, Algorithm.ATA_APPROXIMATION);
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

    public Matrix<Real> getU() {
        return U;
    }

    public Real[] getSingularValues() {
        return singularValues;
    }

    public Matrix<Real> getV() {
        return V;
    }

    /**
     * Matrix rank (number of non-zero singular values).
     */
    public int rank(Real tolerance) {
        int rank = 0;
        for (Real sigma : singularValues) {
            if (sigma.compareTo(tolerance) > 0) {
                rank++;
            }
        }
        return rank;
    }

    /**
     * Condition number: ÃÆ’_max / ÃÆ’_min
     * <p>
     * Indicates numerical stability. ÃŽÂº > 10^15 is ill-conditioned.
     * </p>
     */
    public Real conditionNumber() {
        if (singularValues.length == 0)
            return Real.POSITIVE_INFINITY;

        Real max = singularValues[0];
        Real min = singularValues[singularValues.length - 1];

        if (min.isZero())
            return Real.POSITIVE_INFINITY;
        return max.divide(min);
    }

    /**
     * Moore-Penrose pseudoinverse: A^+ = V ÃŽÂ£^+ U^T
     */
    public Matrix<Real> pseudoInverse(Real tolerance) {
        // ÃŽÂ£^+ : invert non-zero singular values
        int n = V.rows();
        int m = U.rows();

        List<List<Real>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                Real sum = Real.ZERO;
                for (int k = 0; k < singularValues.length; k++) {
                    if (singularValues[k].compareTo(tolerance) > 0) {
                        Real invSigma = Real.ONE.divide(singularValues[k]);
                        sum = sum.add(V.get(i, k).multiply(invSigma).multiply(U.get(j, k)));
                    }
                }
                row.add(sum);
            }
            result.add(row);
        }

        return DenseMatrix.of(result, Reals.getInstance());
    }
}


