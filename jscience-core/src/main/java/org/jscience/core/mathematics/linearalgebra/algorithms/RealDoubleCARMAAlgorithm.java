package org.jscience.core.mathematics.linearalgebra.algorithms;

import org.jscience.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;

/**
 * Implementation of the CARMA Algorithm specialized for the double primitive type.
 * <p>
 * This version uses SIMDDoubleMatrix and direct double[] manipulations
 * to achieve maximum performance on hardware supporting vector instructions.
 * </p>
 */
public class RealDoubleCARMAAlgorithm {

    private static final int RECURSION_THRESHOLD = 256; // Higher threshold for SIMD

    public static SIMDRealDoubleMatrix multiply(SIMDRealDoubleMatrix A, SIMDRealDoubleMatrix B) {
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();

        if (k != B.rows()) {
            throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication");
        }

        // Base case: Use the SIMD-optimized multiply of SIMDDoubleMatrix
        if (m <= RECURSION_THRESHOLD && n <= RECURSION_THRESHOLD && k <= RECURSION_THRESHOLD) {
            return standardMultiply(A, B);
        }

        if (m >= n && m >= k) {
            SIMDRealDoubleMatrix A1 = (SIMDRealDoubleMatrix) A.getSubMatrix(0, m / 2, 0, k);
            SIMDRealDoubleMatrix A2 = (SIMDRealDoubleMatrix) A.getSubMatrix(m / 2, m, 0, k);
            return combineVertical(multiply(A1, B), multiply(A2, B));
        } else if (n >= m && n >= k) {
            SIMDRealDoubleMatrix B1 = (SIMDRealDoubleMatrix) B.getSubMatrix(0, k, 0, n / 2);
            SIMDRealDoubleMatrix B2 = (SIMDRealDoubleMatrix) B.getSubMatrix(0, k, n / 2, n);
            return combineHorizontal(multiply(A, B1), multiply(A, B2));
        } else {
            SIMDRealDoubleMatrix A1 = (SIMDRealDoubleMatrix) A.getSubMatrix(0, m, 0, k / 2);
            SIMDRealDoubleMatrix A2 = (SIMDRealDoubleMatrix) A.getSubMatrix(0, m, k / 2, k);
            SIMDRealDoubleMatrix B1 = (SIMDRealDoubleMatrix) B.getSubMatrix(0, k / 2, 0, n);
            SIMDRealDoubleMatrix B2 = (SIMDRealDoubleMatrix) B.getSubMatrix(k / 2, k, 0, n);
            
            SIMDRealDoubleMatrix C1 = multiply(A1, B1);
            SIMDRealDoubleMatrix C2 = multiply(A2, B2);
            return (SIMDRealDoubleMatrix) C1.add(C2);
        }
    }

    private static SIMDRealDoubleMatrix combineVertical(SIMDRealDoubleMatrix top, SIMDRealDoubleMatrix bottom) {
        int rows = top.rows() + bottom.rows();
        int cols = top.cols();
        double[] combinedData = new double[rows * cols];
        
        System.arraycopy(top.getInternalData(), 0, combinedData, 0, top.getInternalData().length);
        System.arraycopy(bottom.getInternalData(), 0, combinedData, top.getInternalData().length, bottom.getInternalData().length);
        
        return new SIMDRealDoubleMatrix(rows, cols, combinedData);
    }

    private static SIMDRealDoubleMatrix combineHorizontal(SIMDRealDoubleMatrix left, SIMDRealDoubleMatrix right) {
        int rows = left.rows();
        int leftCols = left.cols();
        int rightCols = right.cols();
        int totalCols = leftCols + rightCols;
        double[] combinedData = new double[rows * totalCols];
        
        double[] lData = left.getInternalData();
        double[] rData = right.getInternalData();
        
        for (int i = 0; i < rows; i++) {
            System.arraycopy(lData, i * leftCols, combinedData, i * totalCols, leftCols);
            System.arraycopy(rData, i * rightCols, combinedData, i * totalCols + leftCols, rightCols);
        }
        
        return new SIMDRealDoubleMatrix(rows, totalCols, combinedData);
    }

    private static SIMDRealDoubleMatrix standardMultiply(SIMDRealDoubleMatrix A, SIMDRealDoubleMatrix B) {
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        double[] aData = A.getInternalData();
        double[] bData = B.getInternalData();
        double[] cData = new double[m * n];
        
        for (int i = 0; i < m; i++) {
            for (int l = 0; l < k; l++) {
                double aik = aData[i * k + l];
                for (int j = 0; j < n; j++) {
                    cData[i * n + j] += aik * bData[l * n + j];
                }
            }
        }
        return new SIMDRealDoubleMatrix(m, n, cData);
    }
}
