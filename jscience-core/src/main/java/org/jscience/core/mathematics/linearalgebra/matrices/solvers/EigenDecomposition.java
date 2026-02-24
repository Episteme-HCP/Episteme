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

import java.util.ArrayList;
import java.util.List;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;

/**
 * Computes the Eigen Decomposition of a square matrix.
 * <p>
 * This class delegates to Commons Math 3 for industrial-grade
 * numerical precision, fast convergence, and robust stability.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EigenDecomposition {

    public enum Algorithm {
        SIMPLIFIED_POWER_ITERATION,
        QR_ALGORITHM
    }

    private final Real[] eigenvalues;
    private final Matrix<Real> eigenvectors;

    private EigenDecomposition(Real[] eigenvalues, Matrix<Real> eigenvectors) {
        this.eigenvalues = eigenvalues;
        this.eigenvectors = eigenvectors;
    }

    public static EigenDecomposition decompose(Matrix<Real> matrix, Algorithm algo) {
        if (algo == Algorithm.SIMPLIFIED_POWER_ITERATION) {
            return powerIteration(matrix);
        }

        int n = matrix.rows();
        double[][] data = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = matrix.get(i, j).doubleValue();
            }
        }

        org.apache.commons.math3.linear.RealMatrix cmMat = 
                org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(data);
        org.apache.commons.math3.linear.EigenDecomposition cmEig = 
                new org.apache.commons.math3.linear.EigenDecomposition(cmMat);

        Real[] realEigenvalues = new Real[n];
        Real[][] eigenvectorMatrix = new Real[n][n];

        double[] realEv = cmEig.getRealEigenvalues();
        for (int i = 0; i < n; i++) {
            realEigenvalues[i] = Real.of(realEv[i]);
            org.apache.commons.math3.linear.RealVector vec = cmEig.getEigenvector(i);
            if (vec != null) {
                // Normalize eigenvector for consistency
                double norm = 0.0;
                for(int j = 0; j < n; j++) { norm += vec.getEntry(j) * vec.getEntry(j); }
                norm = Math.sqrt(norm);
                if (norm == 0.0) norm = 1.0;
                for (int j = 0; j < n; j++) {
                    eigenvectorMatrix[j][i] = Real.of(vec.getEntry(j) / norm);
                }
            } else {
                for (int j = 0; j < n; j++) {
                    eigenvectorMatrix[j][i] = Real.ZERO;
                }
            }
        }

        return new EigenDecomposition(realEigenvalues, createMatrix(eigenvectorMatrix));
    }

    public static EigenDecomposition decompose(Matrix<Real> matrix) {
        return decompose(matrix, Algorithm.QR_ALGORITHM);
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

    public Real[] getEigenvector(int index) {
        Real[] vec = new Real[eigenvectors.rows()];
        for (int i = 0; i < vec.length; i++) {
            vec[i] = eigenvectors.get(i, index);
        }
        return vec;
    }

    /**
     * Simplified fallback for quick large matrix testing.
     * Uses Power Iteration with Wielandt Deflation.
     * Only calculates highest Real Eigenvalues cleanly.
     */
    private static EigenDecomposition powerIteration(Matrix<Real> matrix) {
        int n = matrix.rows();
        Real[] realEigenvalues = new Real[n];
        Real[][] eigenvectorMatrix = new Real[n][n];

        Matrix<Real> currentA = matrix;
        for (int k = 0; k < n; k++) {
            double[] b_k = new double[n];
            for (int i = 0; i < n; i++) {
                b_k[i] = Math.random();
            }
            
            double normB = 0.0;
            for (int i = 0; i < n; i++) {
                normB += b_k[i] * b_k[i];
            }
            normB = Math.sqrt(normB);
            for (int i = 0; i < n; i++) {
                b_k[i] /= normB;
            }

            double prevNorm = 0.0;
            int numIterations = 50; 

            for (int iter = 0; iter < numIterations; iter++) {
                double[] next_b = new double[n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        next_b[i] += currentA.get(i, j).doubleValue() * b_k[j];
                    }
                }

                double normNextB = 0.0;
                for (int i = 0; i < n; i++) {
                    normNextB += next_b[i] * next_b[i];
                }
                normNextB = Math.sqrt(normNextB);

                if (normNextB == 0.0 || Math.abs(normNextB - prevNorm) < 1e-9) {
                    break;
                }

                for (int i = 0; i < n; i++) {
                    b_k[i] = next_b[i] / normNextB;
                }
                prevNorm = normNextB;
            }

            double[] Ab_k = new double[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Ab_k[i] += currentA.get(i, j).doubleValue() * b_k[j];
                }
            }

            double rayleighNumerator = 0.0;
            double rayleighDenominator = 0.0;
            for (int i = 0; i < n; i++) {
                rayleighNumerator += b_k[i] * Ab_k[i];
                rayleighDenominator += b_k[i] * b_k[i];
            }

            double eigenvalue = (rayleighDenominator != 0.0) ? (rayleighNumerator / rayleighDenominator) : 0.0;

            realEigenvalues[k] = Real.of(eigenvalue);
            for (int i = 0; i < n; i++) {
                eigenvectorMatrix[i][k] = Real.of(b_k[i]);
            }

            List<List<Real>> deflatedRows = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                List<Real> row = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    double deflatedValue = currentA.get(i, j).doubleValue() - (eigenvalue * b_k[i] * b_k[j]);
                    row.add(Real.of(deflatedValue));
                }
                deflatedRows.add(row);
            }
            currentA = DenseMatrix.of(deflatedRows, Reals.getInstance());
        }

        return new EigenDecomposition(realEigenvalues, createMatrix(eigenvectorMatrix));
    }
}
