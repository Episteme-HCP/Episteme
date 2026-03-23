package org.episteme.core.mathematics.linearalgebra.algorithms;

import org.episteme.core.mathematics.linearalgebra.Matrix;
// MatrixFactory removed

/**
 * Implementation of the Strassen Algorithm for matrix multiplication
 * for generic ring elements.
 */
public class RealStrassenAlgorithm {

    private static final int THRESHOLD = 64;

    public static <E> Matrix<E> multiply(Matrix<E> A, Matrix<E> B, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        
        if (m <= THRESHOLD || k <= THRESHOLD || n <= THRESHOLD || m != k || k != n || (m & (m - 1)) != 0) {
            // Padding or direct call for non-square/non-power-of-two
            if (m != k || k != n || (m & (m - 1)) != 0) {
                 return padAndMultiply(A, B, leafProvider);
            }
            return (leafProvider != null) ? leafProvider.multiply(A, B) : standardMultiply(A, B);
        }

        int newSize = n / 2;

        Matrix<E> A11 = A.getSubMatrix(0, newSize, 0, newSize);
        Matrix<E> A12 = A.getSubMatrix(0, newSize, newSize, n);
        Matrix<E> A21 = A.getSubMatrix(newSize, n, 0, newSize);
        Matrix<E> A22 = A.getSubMatrix(newSize, n, newSize, n);

        Matrix<E> B11 = B.getSubMatrix(0, newSize, 0, newSize);
        Matrix<E> B12 = B.getSubMatrix(0, newSize, newSize, n);
        Matrix<E> B21 = B.getSubMatrix(newSize, n, 0, newSize);
        Matrix<E> B22 = B.getSubMatrix(newSize, n, newSize, n);

        Matrix<E> M1 = multiply(A11.add(A22), B11.add(B22), leafProvider);
        Matrix<E> M2 = multiply(A21.add(A22), B11, leafProvider);
        Matrix<E> M3 = multiply(A11, B12.subtract(B22), leafProvider);
        Matrix<E> M4 = multiply(A22, B21.subtract(B11), leafProvider);
        Matrix<E> M5 = multiply(A11.add(A12), B22, leafProvider);
        Matrix<E> M6 = multiply(A21.subtract(A11), B11.add(B12), leafProvider);
        Matrix<E> M7 = multiply(A12.subtract(A22), B21.add(B22), leafProvider);

        Matrix<E> C11 = M1.add(M4).subtract(M5).add(M7);
        Matrix<E> C12 = M3.add(M5);
        Matrix<E> C21 = M2.add(M4);
        Matrix<E> C22 = M1.subtract(M2).add(M3).add(M6);

        return combine(C11, C12, C21, C22);
    }

    private static <E> Matrix<E> combine(Matrix<E> C11, Matrix<E> C12, Matrix<E> C21, Matrix<E> C22) {
        int n = C11.rows() * 2;
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(C11.getScalarRing().zero().getClass(), n, n);
        int half = n / 2;
        
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < half; j++) {
                data[i][j] = C11.get(i, j);
                data[i][j + half] = C12.get(i, j);
                data[i + half][j] = C21.get(i, j);
                data[i + half][j + half] = C22.get(i, j);
            }
        }
        
        return Matrix.of(data, C11.getScalarRing());
    }

    private static <E> Matrix<E> padAndMultiply(Matrix<E> A, Matrix<E> B, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        int m = A.rows(), k = A.cols(), n = B.cols();
        int max = Math.max(m, Math.max(k, n));
        int p = 1;
        while (p < max) p <<= 1;
        
        if (p < THRESHOLD) return (leafProvider != null) ? leafProvider.multiply(A, B) : standardMultiply(A, B);

        // Simple padding for now. A better way would be dynamic peeling.
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = A.getScalarRing();
        @SuppressWarnings("unchecked")
        E[][] aPadded = (E[][]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), p, p);
        @SuppressWarnings("unchecked")
        E[][] bPadded = (E[][]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), p, p);
        E zero = ring.zero();
        
        for(int i=0; i<p; i++) {
            for(int j=0; j<p; j++) {
                aPadded[i][j] = (i < m && j < k) ? A.get(i, j) : zero;
                bPadded[i][j] = (i < k && j < n) ? B.get(i, j) : zero;
            }
        }
        
        Matrix<E> resPadded = multiply(Matrix.of(aPadded, ring),
                                      Matrix.of(bPadded, ring),
                                      leafProvider);
        
        return resPadded.getSubMatrix(0, m, 0, n);
    }

    private static <E> Matrix<E> standardMultiply(Matrix<E> A, Matrix<E> B) {
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = A.getScalarRing();
        @SuppressWarnings("unchecked")
        E[][] res = (E[][]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), m, n);
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                E sum = ring.zero();
                for (int l = 0; l < k; l++) {
                    sum = ring.add(sum, ring.multiply(A.get(i, l), B.get(l, j)));
                }
                res[i][j] = sum;
            }
        }
        return Matrix.of(res, ring);
    }
}
