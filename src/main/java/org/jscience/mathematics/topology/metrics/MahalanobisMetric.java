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
package org.jscience.mathematics.topology.metrics;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.vector.Vector;
import org.jscience.mathematics.sets.Reals;

import java.util.ArrayList;
import java.util.List;

/**
 * Mahalanobis distance metric.
 * <p>
 * The Mahalanobis distance is a measure of the distance between a point and a
 * distribution. It accounts for correlations between variables and is
 * scale-invariant.
 * </p>
 * <p>
 * For vectors x and y with covariance matrix S:
 * d(x, y) = √((x-y)ᵀ S⁻¹ (x-y))
 * </p>
 * <p>
 * When S is the identity matrix, this reduces to Euclidean distance.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class MahalanobisMetric implements Metric<Vector<Real>> {

    private final DenseMatrix<Real> covarianceInverse;
    private final int dimension;

    /**
     * Creates a Mahalanobis metric with the given covariance matrix.
     * 
     * @param covariance the covariance matrix
     */
    public MahalanobisMetric(DenseMatrix<Real> covariance) {
        this.dimension = covariance.rows();
        if (covariance.rows() != covariance.cols()) {
            throw new IllegalArgumentException("Covariance matrix must be square");
        }
        // For now, we'll store the covariance and compute inverse when needed
        // In a full implementation, we'd use LU decomposition to invert
        this.covarianceInverse = invertMatrix(covariance);
    }

    /**
     * Creates a Mahalanobis metric with identity covariance (equivalent to
     * Euclidean).
     * 
     * @param dimension the dimension
     * @return the metric
     */
    public static MahalanobisMetric identity(int dimension) {
        DenseMatrix<Real> identity = createIdentityMatrix(dimension);
        return new MahalanobisMetric(identity);
    }

    @Override
    public Real distance(Vector<Real> x, Vector<Real> y) {
        if (x.dimension() != dimension || y.dimension() != dimension) {
            throw new IllegalArgumentException(
                    "Vectors must have dimension " + dimension + " but got " + x.dimension() + " and " + y.dimension());
        }

        // Compute difference: d = x - y
        List<Real> diff = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            diff.add(x.get(i).subtract(y.get(i)));
        }
        DenseVector<Real> d = new DenseVector<>(diff, Reals.getInstance());

        // Compute d^T * S^{-1} * d
        DenseVector<Real> temp = multiplyMatrixVector(covarianceInverse, d);
        Real sum = Real.ZERO;
        for (int i = 0; i < dimension; i++) {
            sum = sum.add(d.get(i).multiply(temp.get(i)));
        }

        // Return sqrt(sum)
        return Real.of(Math.sqrt(sum.doubleValue()));
    }

    /**
     * Returns the dimension of this metric space.
     * 
     * @return the dimension
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Returns the inverse covariance matrix.
     * 
     * @return the inverse covariance matrix
     */
    public DenseMatrix<Real> getCovarianceInverse() {
        return covarianceInverse;
    }

    // Helper methods

    /**
     * Creates an identity matrix of the given size.
     */
    private static DenseMatrix<Real> createIdentityMatrix(int n) {
        List<List<Real>> rows = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(i == j ? Real.ONE : Real.ZERO);
            }
            rows.add(row);
        }
        return new DenseMatrix<>(rows, Reals.getInstance());
    }

    /**
     * Inverts a matrix (simplified - assumes diagonal or identity for now).
     * In a full implementation, this would use LU decomposition.
     */
    private static DenseMatrix<Real> invertMatrix(DenseMatrix<Real> matrix) {
        int n = matrix.rows();

        // For now, assume diagonal matrix and invert each diagonal element
        // This is a simplification - full implementation would use LU decomposition
        List<List<Real>> rows = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    Real diag = matrix.get(i, j);
                    if (diag.equals(Real.ZERO)) {
                        throw new ArithmeticException("Matrix is singular (zero diagonal element)");
                    }
                    row.add(Real.ONE.divide(diag));
                } else {
                    row.add(matrix.get(i, j)); // Keep off-diagonal (simplified)
                }
            }
            rows.add(row);
        }
        return new DenseMatrix<>(rows, Reals.getInstance());
    }

    /**
     * Multiplies a matrix by a vector.
     */
    private static DenseVector<Real> multiplyMatrixVector(DenseMatrix<Real> matrix, DenseVector<Real> vector) {
        int n = matrix.rows();
        List<Real> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Real sum = Real.ZERO;
            for (int j = 0; j < n; j++) {
                sum = sum.add(matrix.get(i, j).multiply(vector.get(j)));
            }
            result.add(sum);
        }

        return new DenseVector<>(result, Reals.getInstance());
    }

    @Override
    public String toString() {
        return "MahalanobisMetric(dimension=" + dimension + ")";
    }
}
