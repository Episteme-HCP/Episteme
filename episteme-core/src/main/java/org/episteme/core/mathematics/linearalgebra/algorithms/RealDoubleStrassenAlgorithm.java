package org.episteme.core.mathematics.linearalgebra.algorithms;

import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

/**
 * Implementation of the Strassen Algorithm specialized for the double primitive type.
 * <p>
 * This version uses optimized sub-matrix operations and index-based splitting
 * to reduce garbage collection overhead during recursive steps.
 * </p>
 */
public class RealDoubleStrassenAlgorithm {

    private static final int THRESHOLD = 256;
    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    public static SIMDRealDoubleMatrix multiply(SIMDRealDoubleMatrix A, SIMDRealDoubleMatrix B) {
        org.episteme.core.ComputeContext.checkCurrentCancelled();
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        
        if (m <= THRESHOLD || k <= THRESHOLD || n <= THRESHOLD || m != k || k != n || (m & (m - 1)) != 0) {
            if (m != k || k != n || (m & (m - 1)) != 0) {
                 return padAndMultiply(A, B);
            }
            return standardMultiply(A, B);
        }

        double[] aData = A.getInternalData();
        double[] bData = B.getInternalData();
        double[] cData = new double[n * n];

        strassenRecursive(aData, 0, 0, n, bData, 0, 0, n, cData, 0, 0, n, n);

        return new SIMDRealDoubleMatrix(n, n, cData);
    }

    private static void strassenRecursive(double[] A, int aRow, int aCol, int aStride,
                                        double[] B, int bRow, int bCol, int bStride,
                                        double[] C, int cRow, int cCol, int cStride,
                                        int n) {
        if (n <= THRESHOLD) {
            standardMultiply(A, aRow, aCol, aStride, B, bRow, bCol, bStride, C, cRow, cCol, cStride, n, n, n);
            return;
        }

        int h = n / 2;

        // Strassen formulas:
        // M1 = (A11 + A22) * (B11 + B22)
        // M2 = (A21 + A22) * B11
        // M3 = A11 * (B12 - B22)
        // M4 = A22 * (B21 - B11)
        // M5 = (A11 + A12) * B22
        // M6 = (A21 - A11) * (B11 + B12)
        // M7 = (A12 - A22) * (B21 + B22)

        double[] sA = new double[h * h];
        double[] sB = new double[h * h];

        // M1
        add(A, aRow, aCol, aStride, A, aRow + h, aCol + h, aStride, sA, h);
        add(B, bRow, bCol, bStride, B, bRow + h, bCol + h, bStride, sB, h);
        double[] m1 = new double[h * h];
        strassenRecursive(sA, 0, 0, h, sB, 0, 0, h, m1, 0, 0, h, h);

        // M2
        add(A, aRow + h, aCol, aStride, A, aRow + h, aCol + h, aStride, sA, h);
        double[] m2 = new double[h * h];
        strassenRecursive(sA, 0, 0, h, B, bRow, bCol, bStride, m2, 0, 0, h, h);

        // M3
        sub(B, bRow, bCol + h, bStride, B, bRow + h, bCol + h, bStride, sB, h);
        double[] m3 = new double[h * h];
        strassenRecursive(A, aRow, aCol, aStride, sB, 0, 0, h, m3, 0, 0, h, h);

        // M4
        sub(B, bRow + h, bCol, bStride, B, bRow, bCol, bStride, sB, h);
        double[] m4 = new double[h * h];
        strassenRecursive(A, aRow + h, aCol + h, aStride, sB, 0, 0, h, m4, 0, 0, h, h);

        // M5
        add(A, aRow, aCol, aStride, A, aRow, aCol + h, aStride, sA, h);
        double[] m5 = new double[h * h];
        strassenRecursive(sA, 0, 0, h, B, bRow + h, bCol + h, bStride, m5, 0, 0, h, h);

        // M6
        sub(A, aRow + h, aCol, aStride, A, aRow, aCol, aStride, sA, h);
        add(B, bRow, bCol, bStride, B, bRow, bCol + h, bStride, sB, h);
        double[] m6 = new double[h * h];
        strassenRecursive(sA, 0, 0, h, sB, 0, 0, h, m6, 0, 0, h, h);

        // M7
        sub(A, aRow, aCol + h, aStride, A, aRow + h, aCol + h, aStride, sA, h);
        add(B, bRow + h, bCol, bStride, B, bRow + h, bCol + h, bStride, sB, h);
        double[] m7 = new double[h * h];
        strassenRecursive(sA, 0, 0, h, sB, 0, 0, h, m7, 0, 0, h, h);

        // C11 = M1 + M4 - M5 + M7
        for(int i=0; i<h; i++) {
            int offsetC = (cRow + i) * cStride + cCol;
            int offsetH = i * h;
            for(int j=0; j<h; j++) {
                C[offsetC + j] = m1[offsetH + j] + m4[offsetH + j] - m5[offsetH + j] + m7[offsetH + j];
            }
        }
        // C12 = M3 + M5
        for(int i=0; i<h; i++) {
            int offsetC = (cRow + i) * cStride + cCol + h;
            int offsetH = i * h;
            for(int j=0; j<h; j++) {
                C[offsetC + j] = m3[offsetH + j] + m5[offsetH + j];
            }
        }
        // C21 = M2 + M4
        for(int i=0; i<h; i++) {
            int offsetC = (cRow + h + i) * cStride + cCol;
            int offsetH = i * h;
            for(int j=0; j<h; j++) {
                C[offsetC + j] = m2[offsetH + j] + m4[offsetH + j];
            }
        }
        // C22 = M1 - M2 + M3 + M6
        for(int i=0; i<h; i++) {
            int offsetC = (cRow + h + i) * cStride + cCol + h;
            int offsetH = i * h;
            for(int j=0; j<h; j++) {
                C[offsetC + j] = m1[offsetH + j] - m2[offsetH + j] + m3[offsetH + j] + m6[offsetH + j];
            }
        }
    }

    private static void add(double[] A, int aR, int aC, int aS, double[] B, int bR, int bC, int bS, double[] R, int n) {
        for(int i=0; i<n; i++) {
            int oA = (aR + i) * aS + aC;
            int oB = (bR + i) * bS + bC;
            int oR = i * n;
            int j = 0;
            for (; j < SPECIES.loopBound(n); j += SPECIES.length()) {
                var v1 = DoubleVector.fromArray(SPECIES, A, oA + j);
                var v2 = DoubleVector.fromArray(SPECIES, B, oB + j);
                v1.add(v2).intoArray(R, oR + j);
            }
            for (; j < n; j++) R[oR + j] = A[oA + j] + B[oB + j];
        }
    }

    private static void sub(double[] A, int aR, int aC, int aS, double[] B, int bR, int bC, int bS, double[] R, int n) {
        for(int i=0; i<n; i++) {
            int oA = (aR + i) * aS + aC;
            int oB = (bR + i) * bS + bC;
            int oR = i * n;
            int j = 0;
            for (; j < SPECIES.loopBound(n); j += SPECIES.length()) {
                var v1 = DoubleVector.fromArray(SPECIES, A, oA + j);
                var v2 = DoubleVector.fromArray(SPECIES, B, oB + j);
                v1.sub(v2).intoArray(R, oR + j);
            }
            for (; j < n; j++) R[oR + j] = A[oA + j] - B[oB + j];
        }
    }

    private static SIMDRealDoubleMatrix padAndMultiply(SIMDRealDoubleMatrix A, SIMDRealDoubleMatrix B) {
        int m = A.rows(), k = A.cols(), n = B.cols();
        int max = Math.max(m, Math.max(k, n));
        int p = 1;
        while (p < max) p <<= 1;
        if (p < THRESHOLD) return standardMultiply(A, B);
        SIMDRealDoubleMatrix aPatted = new SIMDRealDoubleMatrix(p, p);
        SIMDRealDoubleMatrix bPatted = new SIMDRealDoubleMatrix(p, p);
        for(int i=0; i<m; i++) for(int j=0; j<k; j++) aPatted.set(i, j, A.get(i, j).doubleValue());
        for(int i=0; i<k; i++) for(int j=0; j<n; j++) bPatted.set(i, j, B.get(i, j).doubleValue());
        SIMDRealDoubleMatrix resPadded = multiply(aPatted, bPatted);
        double[] resData = new double[m * n];
        double[] pData = resPadded.getInternalData();
        for(int i=0; i<m; i++) System.arraycopy(pData, i*p, resData, i*n, n);
        return new SIMDRealDoubleMatrix(m, n, resData);
    }

    private static SIMDRealDoubleMatrix standardMultiply(SIMDRealDoubleMatrix A, SIMDRealDoubleMatrix B) {
        int m = A.rows(), k = A.cols(), n = B.cols();
        double[] aData = A.getInternalData(), bData = B.getInternalData(), cData = new double[m * n];
        standardMultiply(aData, 0, 0, k, bData, 0, 0, n, cData, 0, 0, n, m, k, n);
        return new SIMDRealDoubleMatrix(m, n, cData);
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
                for (; j < n; j++) C[rowC + j] += aik * B[rowB + j];
            }
        }
    }
}
