package org.jscience.core.mathematics.linearalgebra.algorithms;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.numbers.real.Real;
// MatrixFactory removed

/**
 * Implementation of the Strassen Algorithm for matrix multiplication
 * for the generic Real type.
 */
public class RealStrassenAlgorithm {

    private static final int THRESHOLD = 64;

    public static Matrix<Real> multiply(Matrix<Real> A, Matrix<Real> B) {
        org.jscience.core.ComputeContext.checkCurrentCancelled();
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        
        if (m <= THRESHOLD || k <= THRESHOLD || n <= THRESHOLD || m != k || k != n || (m & (m - 1)) != 0) {
            // Padding or direct call for non-square/non-power-of-two
            if (m != k || k != n || (m & (m - 1)) != 0) {
                 return padAndMultiply(A, B);
            }
            return standardMultiply(A, B);
        }

        int newSize = n / 2;

        Matrix<Real> A11 = A.getSubMatrix(0, newSize, 0, newSize);
        Matrix<Real> A12 = A.getSubMatrix(0, newSize, newSize, n);
        Matrix<Real> A21 = A.getSubMatrix(newSize, n, 0, newSize);
        Matrix<Real> A22 = A.getSubMatrix(newSize, n, newSize, n);

        Matrix<Real> B11 = B.getSubMatrix(0, newSize, 0, newSize);
        Matrix<Real> B12 = B.getSubMatrix(0, newSize, newSize, n);
        Matrix<Real> B21 = B.getSubMatrix(newSize, n, 0, newSize);
        Matrix<Real> B22 = B.getSubMatrix(newSize, n, newSize, n);

        Matrix<Real> M1 = multiply(A11.add(A22), B11.add(B22));
        Matrix<Real> M2 = multiply(A21.add(A22), B11);
        Matrix<Real> M3 = multiply(A11, B12.subtract(B22));
        Matrix<Real> M4 = multiply(A22, B21.subtract(B11));
        Matrix<Real> M5 = multiply(A11.add(A12), B22);
        Matrix<Real> M6 = multiply(A21.subtract(A11), B11.add(B12));
        Matrix<Real> M7 = multiply(A12.subtract(A22), B21.add(B22));

        Matrix<Real> C11 = M1.add(M4).subtract(M5).add(M7);
        Matrix<Real> C12 = M3.add(M5);
        Matrix<Real> C21 = M2.add(M4);
        Matrix<Real> C22 = M1.subtract(M2).add(M3).add(M6);

        return combine(C11, C12, C21, C22);
    }

    private static Matrix<Real> combine(Matrix<Real> C11, Matrix<Real> C12, Matrix<Real> C21, Matrix<Real> C22) {
        int n = C11.rows() * 2;
        Real[][] data = new Real[n][n];
        int half = n / 2;
        
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < half; j++) {
                data[i][j] = C11.get(i, j);
                data[i][j + half] = C12.get(i, j);
                data[i + half][j] = C21.get(i, j);
                data[i + half][j + half] = C22.get(i, j);
            }
        }
        
        return Matrix.of(data, org.jscience.core.mathematics.sets.Reals.getInstance());
    }

    private static Matrix<Real> padAndMultiply(Matrix<Real> A, Matrix<Real> B) {
        int m = A.rows(), k = A.cols(), n = B.cols();
        int max = Math.max(m, Math.max(k, n));
        int p = 1;
        while (p < max) p <<= 1;
        
        if (p < THRESHOLD) return standardMultiply(A, B);

        // Simple padding for now. A better way would be dynamic peeling.
        Real[][] aPadded = new Real[p][p];
        Real[][] bPadded = new Real[p][p];
        Real zero = org.jscience.core.mathematics.numbers.real.Real.ZERO;
        
        for(int i=0; i<p; i++) {
            for(int j=0; j<p; j++) {
                aPadded[i][j] = (i < m && j < k) ? A.get(i, j) : zero;
                bPadded[i][j] = (i < k && j < n) ? B.get(i, j) : zero;
            }
        }
        
        Matrix<Real> resPadded = multiply(Matrix.of(aPadded, org.jscience.core.mathematics.sets.Reals.getInstance()),
                                          Matrix.of(bPadded, org.jscience.core.mathematics.sets.Reals.getInstance()));
        
        return resPadded.getSubMatrix(0, m, 0, n);
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
