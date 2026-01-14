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

package org.jscience.mathematics.linearalgebra.matrices.solvers;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Eigenvalue and Eigenvector decomposition.
 * <p>
 * Finds eigenvalues λ and eigenvectors v such that Av = λv.
 * Uses QR algorithm for general matrices, power iteration for largest
 * eigenvalue.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EigenDecomposition {

    private final Real[] eigenvalues;
    private final Matrix<Real> eigenvectors;

    private EigenDecomposition(Real[] eigenvalues, Matrix<Real> eigenvectors) {
        this.eigenvalues = eigenvalues;
        this.eigenvectors = eigenvectors;
    }

    /**
     * Computes eigendecomposition using QR algorithm.
     * <p>
     * Iteratively applies QR decomposition: A_k+1 = R_k * Q_k
     * Converges to Schur form (upper triangular), diagonal = eigenvalues.
     * </p>
     */
    public enum Algorithm {
        /**
         * @deprecated Not implemented in 1.0. Use SIMPLIFIED_POWER_ITERATION instead.
         */
        @Deprecated
        INVERSE_ITERATION,

        SIMPLIFIED_POWER_ITERATION
    }

    /**
     * Computes eigendecomposition using QR algorithm and specified eigenvector
     * method.
     * <p>
     * Iteratively applies QR decomposition: A_k+1 = R_k * Q_k
     * Converges to Schur form (upper triangular), diagonal = eigenvalues.
     * </p>
     */
    public static EigenDecomposition decompose(Matrix<Real> matrix, Algorithm algo) {
        if (algo == Algorithm.INVERSE_ITERATION) {
            throw new UnsupportedOperationException("Inverse iteration not yet implemented");
        }

        int n = matrix.rows();
        if (n != matrix.cols()) {
            throw new IllegalArgumentException("Matrix must be square");
        }

        // Copy matrix
        Real[][] A = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = matrix.get(i, j);
            }
        }

        // QR algorithm
        int maxIterations = 100;
        for (int iter = 0; iter < maxIterations; iter++) {
            // QR decomposition
            QRDecomposition qr = QRDecomposition.decompose(createMatrix(A));
            Matrix<Real> Q = qr.getQ();
            Matrix<Real> R = qr.getR();

            // A = R * Q
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Real sum = Real.ZERO;
                    for (int k = 0; k < n; k++) {
                        sum = sum.add(R.get(i, k).multiply(Q.get(k, j)));
                    }
                    A[i][j] = sum;
                }
            }

            // Check convergence (off-diagonal elements near zero)
            Real offDiagSum = Real.ZERO;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        offDiagSum = offDiagSum.add(A[i][j].abs());
                    }
                }
            }
            if (offDiagSum.compareTo(Real.of(1e-10)) < 0) {
                break; // Converged
            }
        }

        // Extract eigenvalues from diagonal
        Real[] eigenvalues = new Real[n];
        for (int i = 0; i < n; i++) {
            eigenvalues[i] = A[i][i];
        }

        // Sort eigenvalues (descending)
        Arrays.sort(eigenvalues, (a, b) -> b.compareTo(a));

        // Compute eigenvectors (simplified - use power iteration for each)
        List<List<Real>> eigenvectorList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Real[] eigenvector = powerIteration(matrix, eigenvalues[i], 100);
            List<Real> vec = new ArrayList<>();
            for (Real val : eigenvector) {
                vec.add(val);
            }
            eigenvectorList.add(vec);
        }

        // Transpose to column vectors
        List<List<Real>> transposed = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(eigenvectorList.get(j).get(i));
            }
            transposed.add(row);
        }

        Matrix<Real> eigenvectors = DenseMatrix.of(transposed, Reals.getInstance());

        return new EigenDecomposition(eigenvalues, eigenvectors);
    }

    /**
     * Computes eigendecomposition using default simplified algorithm.
     */
    public static EigenDecomposition decompose(Matrix<Real> matrix) {
        return decompose(matrix, Algorithm.SIMPLIFIED_POWER_ITERATION);
    }

    /**
     * Power iteration to find eigenvector for given eigenvalue.
     */
    private static Real[] powerIteration(Matrix<Real> A, Real lambda, int maxIter) {
        int n = A.rows();
        Real[] v = new Real[n];

        // Random initial vector
        for (int i = 0; i < n; i++) {
            v[i] = Real.of(Math.random());
        }

        for (int iter = 0; iter < maxIter; iter++) {
            // v = (A - λI) * v
            Real[] newV = new Real[n];
            for (int i = 0; i < n; i++) {
                newV[i] = Real.ZERO;
                for (int j = 0; j < n; j++) {
                    Real aij = A.get(i, j);
                    if (i == j) {
                        aij = aij.subtract(lambda);
                    }
                    newV[i] = newV[i].add(aij.multiply(v[j]));
                }
            }

            // Normalize
            Real norm = Real.ZERO;
            for (Real val : newV) {
                norm = norm.add(val.multiply(val));
            }
            norm = norm.sqrt();

            if (!norm.isZero()) {
                for (int i = 0; i < n; i++) {
                    v[i] = newV[i].divide(norm);
                }
            }
        }

        return v;
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

    public Real[] getEigenvalues() {
        return eigenvalues;
    }

    public Matrix<Real> getEigenvectors() {
        return eigenvectors;
    }

    /**
     * Gets specific eigenvector (column i).
     */
    public Real[] getEigenvector(int index) {
        Real[] vec = new Real[eigenvectors.rows()];
        for (int i = 0; i < vec.length; i++) {
            vec[i] = eigenvectors.get(i, index);
        }
        return vec;
    }

    /**
     * Spectral radius (largest absolute eigenvalue).
     */
    public Real spectralRadius() {
        Real max = Real.ZERO;
        for (Real lambda : eigenvalues) {
            if (lambda.abs().compareTo(max) > 0) {
                max = lambda.abs();
            }
        }
        return max;
    }
}
