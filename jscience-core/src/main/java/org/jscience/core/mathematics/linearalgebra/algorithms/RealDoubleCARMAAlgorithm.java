package org.jscience.core.mathematics.linearalgebra.algorithms;

import org.jscience.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

/**
 * Implementation of the CARMA Algorithm specialized for the double primitive type.
 * <p>
 * This version uses SIMDDirect recursion on indices to avoid data copying
 * and achieve communication-optimal performance with minimal GC overhead.
 * </p>
 */
public class RealDoubleCARMAAlgorithm {

    private static final int RECURSION_THRESHOLD = 256; 
    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    public static SIMDRealDoubleMatrix multiply(SIMDRealDoubleMatrix A, SIMDRealDoubleMatrix B) {
        org.jscience.core.ComputeContext.checkCurrentCancelled();
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();

        if (k != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication");
        }

        double[] aData = A.getInternalData();
        double[] bData = B.getInternalData();
        double[] cData = new double[m * n];

        carmaRecursive(aData, 0, 0, k,
                      bData, 0, 0, n,
                      cData, 0, 0, n,
                      m, k, n);

        return new SIMDRealDoubleMatrix(m, n, cData);
    }

    private static void carmaRecursive(double[] A, int aRow, int aCol, int aStride,
                                     double[] B, int bRow, int bCol, int bStride,
                                     double[] C, int cRow, int cCol, int cStride,
                                     int m, int k, int n) {
        if (m <= RECURSION_THRESHOLD && n <= RECURSION_THRESHOLD && k <= RECURSION_THRESHOLD) {
            standardMultiply(A, aRow, aCol, aStride,
                             B, bRow, bCol, bStride,
                             C, cRow, cCol, cStride,
                             m, k, n);
            return;
        }

        if (m >= n && m >= k) {
            // Split M
            int mHalf = m / 2;
            carmaRecursive(A, aRow, aCol, aStride, B, bRow, bCol, bStride, C, cRow, cCol, cStride, mHalf, k, n);
            carmaRecursive(A, aRow + mHalf, aCol, aStride, B, bRow, bCol, bStride, C, cRow + mHalf, cCol, cStride, m - mHalf, k, n);
        } else if (n >= m && n >= k) {
            // Split N
            int nHalf = n / 2;
            carmaRecursive(A, aRow, aCol, aStride, B, bRow, bCol, bStride, C, cRow, cCol, cStride, m, k, nHalf);
            carmaRecursive(A, aRow, aCol, aStride, B, bRow, bCol + nHalf, bStride, C, cRow, cCol + nHalf, cStride, m, k, n - nHalf);
        } else {
            // Split K
            int kHalf = k / 2;
            carmaRecursive(A, aRow, aCol, aStride, B, bRow, bCol, bStride, C, cRow, cCol, cStride, m, kHalf, n);
            carmaRecursive(A, aRow, aCol + kHalf, aStride, B, bRow + kHalf, bCol, bStride, C, cRow, cCol, cStride, m, k - kHalf, n);
        }
    }

    private static void standardMultiply(double[] A, int aRow, int aCol, int aStride,
                                       double[] B, int bRow, int bCol, int bStride,
                                       double[] C, int cRow, int cCol, int cStride,
                                       int m, int k, int n) {
        for (int i = 0; i < m; i++) {
            int rowA = (aRow + i) * aStride + aCol;
            int rowC = (cRow + i) * cStride + cCol;
            for (int l = 0; l < k; l++) {
                double aik = A[rowA + l];
                int rowB = (bRow + l) * bStride + bCol;
                
                int j = 0;
                for (; j < SPECIES.loopBound(n); j += SPECIES.length()) {
                    var bVec = DoubleVector.fromArray(SPECIES, B, rowB + j);
                    var cVec = DoubleVector.fromArray(SPECIES, C, rowC + j);
                    bVec.fma(DoubleVector.broadcast(SPECIES, aik), cVec).intoArray(C, rowC + j);
                }
                for (; j < n; j++) {
                    C[rowC + j] += aik * B[rowB + j];
                }
            }
        }
    }
}
