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
 * * @author Silvere Martin-Michiellot
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
        if (algo == Algorithm.GOLUB_REINSCH) {
            throw new UnsupportedOperationException("Full Golub-Reinsch algorithm not yet implemented");
        }

        int m = matrix.rows();
        int n = matrix.cols();

        // Copy matrix data
        Real[][] A = new Real[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = matrix.get(i, j);
            }
        }

        // Use simplified approach: A^T * A for eigenvalues
        // Full Golub-Reinsch is complex - this is a working approximation and if there
        // is a performance pernalty give the user the opportunity to choose between
        // algoriths

        Real[][] AtA = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                AtA[i][j] = Real.ZERO;
                for (int k = 0; k < m; k++) {
                    AtA[i][j] = AtA[i][j].add(A[k][i].multiply(A[k][j]));
                }
            }
        }

        // Compute eigenvalues of A^T * A (these are σ²)
        EigenDecomposition eigen = EigenDecomposition.decompose(createMatrix(AtA));
        Real[] eigenvalues = eigen.getEigenvalues();

        // Singular values are sqrt(eigenvalues)
        Real[] sigma = new Real[Math.min(m, n)];
        for (int i = 0; i < sigma.length; i++) {
            // Eigenvalues of A^T A should be non-negative, but numerical error might give
            // small negative
            sigma[i] = (eigenvalues[i].compareTo(Real.ZERO) > 0) ? eigenvalues[i].sqrt() : Real.ZERO;
        }

        // V is eigenvectors of A^T * A
        Matrix<Real> V = eigen.getEigenvectors();

        // Compute U = A * V * Σ^(-1)
        List<List<Real>> uRows = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < Math.min(m, n); j++) {
                Real sum = Real.ZERO;
                for (int k = 0; k < n; k++) {
                    if (!sigma[j].isZero()) {
                        sum = sum.add(A[i][k].multiply(V.get(k, j)).divide(sigma[j]));
                    }
                }
                row.add(sum);
            }
            uRows.add(row);
        }

        Matrix<Real> U = DenseMatrix.of(uRows, Reals.getInstance());

        return new SVDDecomposition(U, sigma, V);
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
     * Condition number: σ_max / σ_min
     * <p>
     * Indicates numerical stability. κ > 10^15 is ill-conditioned.
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
     * Moore-Penrose pseudoinverse: A^+ = V Σ^+ U^T
     */
    public Matrix<Real> pseudoInverse(Real tolerance) {
        // Σ^+ : invert non-zero singular values
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