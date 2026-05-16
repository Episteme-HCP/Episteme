package org.episteme.core.mathematics.linearalgebra.algorithms;

import org.episteme.core.mathematics.linearalgebra.Matrix;

/**
 * Implementation of the CARMA (Communication-Avoidant Recursive Matrix Multiplication) Algorithm
 * for generic ring elements.
 * <p>
 * This version uses index-based recursion to avoid unnecessary data copying (getSubMatrix/combine)
 * which reduces memory pressure and resolves performance stalls.
 * </p>
 */
public class RealCARMAAlgorithm {

    private static final int RECURSION_THRESHOLD = 64;

    public static <E> Matrix<E> multiply(Matrix<E> A, Matrix<E> B, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();

        if (k != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication");
        }

        org.episteme.core.mathematics.structures.rings.Ring<E> ring = A.getScalarRing();
        
        Class<?> componentType = ring.zero().getClass();
        if (org.episteme.core.mathematics.numbers.real.Real.class.isAssignableFrom(componentType)) componentType = org.episteme.core.mathematics.numbers.real.Real.class;
        if (org.episteme.core.mathematics.numbers.complex.Complex.class.isAssignableFrom(componentType)) componentType = org.episteme.core.mathematics.numbers.complex.Complex.class;

        @SuppressWarnings("unchecked")
        E[][] res = (E[][]) java.lang.reflect.Array.newInstance(componentType, m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = ring.zero();
            }
        }

        carmaRecursive(A, 0, 0, B, 0, 0, res, 0, 0, m, k, n, ring, leafProvider);

        return Matrix.of(res, ring);
    }

    private static <E> void carmaRecursive(Matrix<E> A, int aRowOffset, int aColOffset,
                                     Matrix<E> B, int bRowOffset, int bColOffset,
                                     E[][] C, int cRowOffset, int cColOffset,
                                     int m, int k, int n, org.episteme.core.mathematics.structures.rings.Ring<E> ring, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<E> leafProvider) {
        if (m <= RECURSION_THRESHOLD && n <= RECURSION_THRESHOLD && k <= RECURSION_THRESHOLD) {
            if (leafProvider != null) {
                // Use leaf provider for base case. 
                // We need to extract the sub-matrices and then add the result to C.
                Matrix<E> aSub = A.getSubMatrix(aRowOffset, aRowOffset + m, aColOffset, aColOffset + k);
                Matrix<E> bSub = B.getSubMatrix(bRowOffset, bRowOffset + k, bColOffset, bColOffset + n);
                Matrix<E> cSub = leafProvider.multiply(aSub, bSub);
                
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        C[cRowOffset + i][cColOffset + j] = ring.add(C[cRowOffset + i][cColOffset + j], cSub.get(i, j));
                    }
                }
            } else {
                standardMultiply(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset, cColOffset, m, k, n, ring);
            }
            return;
        }

        if (m >= n && m >= k) {
            // Split M
            int mHalf = m / 2;
            carmaRecursive(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset, cColOffset, mHalf, k, n, ring, leafProvider);
            carmaRecursive(A, aRowOffset + mHalf, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset + mHalf, cColOffset, m - mHalf, k, n, ring, leafProvider);
        } else if (n >= m && n >= k) {
            // Split N
            int nHalf = n / 2;
            carmaRecursive(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset, cColOffset, m, k, nHalf, ring, leafProvider);
            carmaRecursive(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset + nHalf, C, cRowOffset, cColOffset + nHalf, m, k, n - nHalf, ring, leafProvider);
        } else {
            // Split K
            int kHalf = k / 2;
            carmaRecursive(A, aRowOffset, aColOffset, B, bRowOffset, bColOffset, C, cRowOffset, cColOffset, m, kHalf, n, ring, leafProvider);
            carmaRecursive(A, aRowOffset, aColOffset + kHalf, B, bRowOffset + kHalf, bColOffset, C, cRowOffset, cColOffset, m, k - kHalf, n, ring, leafProvider);
        }
    }

    private static <E> void standardMultiply(Matrix<E> A, int aRow, int aCol,
                                       Matrix<E> B, int bRow, int bCol,
                                       E[][] C, int cRow, int cCol,
                                       int m, int k, int n, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        for (int i = 0; i < m; i++) {
            for (int l = 0; l < k; l++) {
                E aik = A.get(aRow + i, aCol + l);
                for (int j = 0; j < n; j++) {
                    C[cRow + i][cCol + j] = ring.add(C[cRow + i][cCol + j], ring.multiply(aik, B.get(bRow + l, bCol + j)));
                }
            }
        }
    }
}
