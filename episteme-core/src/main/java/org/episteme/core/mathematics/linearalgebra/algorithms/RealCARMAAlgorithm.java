package org.episteme.core.mathematics.linearalgebra.algorithms;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;

/**
 * Implementation of the CARMA (Communication-Avoidant Recursive Matrix Multiplication) Algorithm
 * for the generic Real type.
 * <p>
 * This version uses index-based recursion to avoid unnecessary data copying (getSubMatrix/combine)
 * which reduces memory pressure and resolves performance stalls.
 * </p>
 */
public class RealCARMAAlgorithm {

    private static final int RECURSION_THRESHOLD = 64;

    public static Matrix<Real> multiply(Matrix<Real> A, Matrix<Real> B) {
        org.episteme.core.ComputeContext.checkCurrentCancelled();
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();

        if (k != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication");
        }

        Reals reals = Reals.getInstance();
        Real[][] res = new Real[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = reals.zero();
            }
        }

        carmaRecursive(A, 0, 0, B, 0, 0, res, 0, 0, m, k, n, reals);

        return Matrix.of(res, reals);
    }

    private static void carmaRecursive(Matrix<Real> A, int aRowOffset, int aColOffset,
                                     Matrix<Real> B, int bRowOffset, int bColOffset,
                                     Real[][] C, int cRowOffset, int cColOffset,
                                     int m, int k, int n, Reals reals) {
        if (m <= RECURSION_THRESHOLD && n <= RECURSION_THRESHOLD && k <= RECURSION_THRESHOLD) {
            standardMultiply(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset, cColOffset, m, k, n, reals);
            return;
        }

        if (m >= n && m >= k) {
            // Split M
            int mHalf = m / 2;
            carmaRecursive(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset, cColOffset, mHalf, k, n, reals);
            carmaRecursive(A, aRowOffset + mHalf, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset + mHalf, cColOffset, m - mHalf, k, n, reals);
        } else if (n >= m && n >= k) {
            // Split N
            int nHalf = n / 2;
            carmaRecursive(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset, cColOffset, m, k, nHalf, reals);
            carmaRecursive(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset + nHalf, C, cRowOffset, cColOffset + nHalf, m, k, n - nHalf, reals);
        } else {
            // Split K
            int kHalf = k / 2;
            carmaRecursive(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset, cColOffset, m, kHalf, n, reals);
            carmaRecursive(A, aRowOffset, aColOffset + kHalf, B, bRowOffset + kHalf, bColOffset, C, cRowOffset, cColOffset, m, k - kHalf, n, reals);
        }
    }

    private static void standardMultiply(Matrix<Real> A, int aRow, int aCol,
                                       Matrix<Real> B, int bRow, int bCol,
                                       Real[][] C, int cRow, int cCol,
                                       int m, int k, int n, Reals reals) {
        for (int i = 0; i < m; i++) {
            for (int l = 0; l < k; l++) {
                Real aik = A.get(aRow + i, aCol + l);
                for (int j = 0; j < n; j++) {
                    C[cRow + i][cCol + j] = reals.add(C[cRow + i][cCol + j], reals.multiply(aik, B.get(bRow + l, bCol + j)));
                }
            }
        }
    }
}
