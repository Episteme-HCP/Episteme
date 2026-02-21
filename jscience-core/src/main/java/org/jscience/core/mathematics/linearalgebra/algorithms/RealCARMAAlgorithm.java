package org.jscience.core.mathematics.linearalgebra.algorithms;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Implementation of the CARMA (Communication-Avoidant Recursive Matrix Multiplication) Algorithm
 * for the generic Real type.
 * <p>
 * This version preserves arbitrary precision (e.g., BigDecimalReal) by using
 * high-level Matrix and Real operations.
 * </p>
 */
public class RealCARMAAlgorithm {

    private static final int RECURSION_THRESHOLD = 64;

    public static Matrix<Real> multiply(Matrix<Real> A, Matrix<Real> B) {
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();

        if (k != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication");
        }

        if (m <= RECURSION_THRESHOLD && n <= RECURSION_THRESHOLD && k <= RECURSION_THRESHOLD) {
            return standardMultiply(A, B);
        }

        if (m >= n && m >= k) {
            Matrix<Real> A1 = A.getSubMatrix(0, m / 2, 0, k);
            Matrix<Real> A2 = A.getSubMatrix(m / 2, m, 0, k);
            return combineVertical(multiply(A1, B), multiply(A2, B));
        } else if (n >= m && n >= k) {
            Matrix<Real> B1 = B.getSubMatrix(0, k, 0, n / 2);
            Matrix<Real> B2 = B.getSubMatrix(0, k, n / 2, n);
            return combineHorizontal(multiply(A, B1), multiply(A, B2));
        } else {
            Matrix<Real> A1 = A.getSubMatrix(0, m, 0, k / 2);
            Matrix<Real> A2 = A.getSubMatrix(0, m, k / 2, k);
            Matrix<Real> B1 = B.getSubMatrix(0, k / 2, 0, n);
            Matrix<Real> B2 = B.getSubMatrix(k / 2, k, 0, n);
            return multiply(A1, B1).add(multiply(A2, B2));
        }
    }

    private static Matrix<Real> combineVertical(Matrix<Real> top, Matrix<Real> bottom) {
        int rows = top.rows() + bottom.rows();
        int cols = top.cols();
        Real[][] data = new Real[rows][cols];
        for (int i = 0; i < top.rows(); i++)
            for (int j = 0; j < cols; j++) data[i][j] = top.get(i, j);
        for (int i = 0; i < bottom.rows(); i++)
            for (int j = 0; j < cols; j++) data[top.rows() + i][j] = bottom.get(i, j);
        return Matrix.of(data, org.jscience.core.mathematics.sets.Reals.getInstance());
    }

    private static Matrix<Real> combineHorizontal(Matrix<Real> left, Matrix<Real> right) {
        int rows = left.rows();
        int cols = left.cols() + right.cols();
        Real[][] data = new Real[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < left.cols(); j++) data[i][j] = left.get(i, j);
            for (int j = 0; j < right.cols(); j++) data[i][left.cols() + j] = right.get(i, j);
        }
        return Matrix.of(data, org.jscience.core.mathematics.sets.Reals.getInstance());
    }

    private static Matrix<Real> standardMultiply(Matrix<Real> A, Matrix<Real> B) {
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        Real[][] res = new Real[m][n];
        org.jscience.core.mathematics.sets.Reals reals = org.jscience.core.mathematics.sets.Reals.getInstance();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Real sum = reals.zero();
                for (int l = 0; l < k; l++) {
                    sum = reals.add(sum, reals.multiply(A.get(i, l), B.get(l, j)));
                }
                res[i][j] = sum;
            }
        }
        return Matrix.of(res, reals);
    }
}
