package org.jscience.core.mathematics.linearalgebra.algorithms;

import org.jscience.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;

/**
 * Implementation of the Strassen Algorithm specialized for the double primitive type.
 * Uses SIMDRealDoubleMatrix for optimized additions and subtractions.
 */
public class RealDoubleStrassenAlgorithm {

    private static final int THRESHOLD = 256;

    public static SIMDRealDoubleMatrix multiply(SIMDRealDoubleMatrix A, SIMDRealDoubleMatrix B) {
        int n = A.rows();
        if (n <= THRESHOLD) {
            return (SIMDRealDoubleMatrix) A.multiply(B);
        }

        int newSize = n / 2;

        SIMDRealDoubleMatrix A11 = (SIMDRealDoubleMatrix) A.getSubMatrix(0, newSize, 0, newSize);
        SIMDRealDoubleMatrix A12 = (SIMDRealDoubleMatrix) A.getSubMatrix(0, newSize, newSize, n);
        SIMDRealDoubleMatrix A21 = (SIMDRealDoubleMatrix) A.getSubMatrix(newSize, n, 0, newSize);
        SIMDRealDoubleMatrix A22 = (SIMDRealDoubleMatrix) A.getSubMatrix(newSize, n, newSize, n);

        SIMDRealDoubleMatrix B11 = (SIMDRealDoubleMatrix) B.getSubMatrix(0, newSize, 0, newSize);
        SIMDRealDoubleMatrix B12 = (SIMDRealDoubleMatrix) B.getSubMatrix(0, newSize, newSize, n);
        SIMDRealDoubleMatrix B21 = (SIMDRealDoubleMatrix) B.getSubMatrix(newSize, n, 0, newSize);
        SIMDRealDoubleMatrix B22 = (SIMDRealDoubleMatrix) B.getSubMatrix(newSize, n, newSize, n);

        SIMDRealDoubleMatrix M1 = multiply((SIMDRealDoubleMatrix) A11.add(A22), (SIMDRealDoubleMatrix) B11.add(B22));
        SIMDRealDoubleMatrix M2 = multiply((SIMDRealDoubleMatrix) A21.add(A22), B11);
        SIMDRealDoubleMatrix M3 = multiply(A11, (SIMDRealDoubleMatrix) B12.subtract(B22));
        SIMDRealDoubleMatrix M4 = multiply(A22, (SIMDRealDoubleMatrix) B21.subtract(B11));
        SIMDRealDoubleMatrix M5 = multiply((SIMDRealDoubleMatrix) A11.add(A12), B22);
        SIMDRealDoubleMatrix M6 = multiply((SIMDRealDoubleMatrix) A21.subtract(A11), (SIMDRealDoubleMatrix) B11.add(B12));
        SIMDRealDoubleMatrix M7 = multiply((SIMDRealDoubleMatrix) A12.subtract(A22), (SIMDRealDoubleMatrix) B21.add(B22));

        SIMDRealDoubleMatrix C11 = (SIMDRealDoubleMatrix) ((SIMDRealDoubleMatrix) M1.add(M4)).subtract((SIMDRealDoubleMatrix) M5.subtract(M7));
        SIMDRealDoubleMatrix C12 = (SIMDRealDoubleMatrix) M3.add(M5);
        SIMDRealDoubleMatrix C21 = (SIMDRealDoubleMatrix) M2.add(M4);
        SIMDRealDoubleMatrix C22 = (SIMDRealDoubleMatrix) ((SIMDRealDoubleMatrix) M1.subtract(M2)).add((SIMDRealDoubleMatrix) M3.add(M6));

        return combine(C11, C12, C21, C22);
    }

    private static SIMDRealDoubleMatrix combine(SIMDRealDoubleMatrix C11, SIMDRealDoubleMatrix C12, SIMDRealDoubleMatrix C21, SIMDRealDoubleMatrix C22) {
        int n = C11.rows() * 2;
        double[] combinedData = new double[n * n];
        int half = n / 2;
        
        double[] d11 = C11.getInternalData();
        double[] d12 = C12.getInternalData();
        double[] d21 = C21.getInternalData();
        double[] d22 = C22.getInternalData();
        
        for (int i = 0; i < half; i++) {
            System.arraycopy(d11, i * half, combinedData, i * n, half);
            System.arraycopy(d12, i * half, combinedData, i * n + half, half);
            System.arraycopy(d21, i * half, combinedData, (i + half) * n, half);
            System.arraycopy(d22, i * half, combinedData, (i + half) * n + half, half);
        }
        
        return new SIMDRealDoubleMatrix(n, n, combinedData);
    }
}
