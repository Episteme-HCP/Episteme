package org.jscience.core.mathematics.linearalgebra.algorithms;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.matrices.SIMDDoubleMatrix;

/**
 * Implementation of the Strassen Algorithm specialized for the double primitive type.
 * Uses SIMDDoubleMatrix for optimized additions and subtractions.
 */
public class RealDoubleStrassenAlgorithm {

    private static final int THRESHOLD = 256;

    public static SIMDDoubleMatrix multiply(SIMDDoubleMatrix A, SIMDDoubleMatrix B) {
        int n = A.rows();
        if (n <= THRESHOLD) {
            return (SIMDDoubleMatrix) A.multiply(B);
        }

        int newSize = n / 2;

        SIMDDoubleMatrix A11 = (SIMDDoubleMatrix) A.getSubMatrix(0, newSize, 0, newSize);
        SIMDDoubleMatrix A12 = (SIMDDoubleMatrix) A.getSubMatrix(0, newSize, newSize, n);
        SIMDDoubleMatrix A21 = (SIMDDoubleMatrix) A.getSubMatrix(newSize, n, 0, newSize);
        SIMDDoubleMatrix A22 = (SIMDDoubleMatrix) A.getSubMatrix(newSize, n, newSize, n);

        SIMDDoubleMatrix B11 = (SIMDDoubleMatrix) B.getSubMatrix(0, newSize, 0, newSize);
        SIMDDoubleMatrix B12 = (SIMDDoubleMatrix) B.getSubMatrix(0, newSize, newSize, n);
        SIMDDoubleMatrix B21 = (SIMDDoubleMatrix) B.getSubMatrix(newSize, n, 0, newSize);
        SIMDDoubleMatrix B22 = (SIMDDoubleMatrix) B.getSubMatrix(newSize, n, newSize, n);

        SIMDDoubleMatrix M1 = multiply((SIMDDoubleMatrix) A11.add(A22), (SIMDDoubleMatrix) B11.add(B22));
        SIMDDoubleMatrix M2 = multiply((SIMDDoubleMatrix) A21.add(A22), B11);
        SIMDDoubleMatrix M3 = multiply(A11, (SIMDDoubleMatrix) B12.subtract(B22));
        SIMDDoubleMatrix M4 = multiply(A22, (SIMDDoubleMatrix) B21.subtract(B11));
        SIMDDoubleMatrix M5 = multiply((SIMDDoubleMatrix) A11.add(A12), B22);
        SIMDDoubleMatrix M6 = multiply((SIMDDoubleMatrix) A21.subtract(A11), (SIMDDoubleMatrix) B11.add(B12));
        SIMDDoubleMatrix M7 = multiply((SIMDDoubleMatrix) A12.subtract(A22), (SIMDDoubleMatrix) B21.add(B22));

        SIMDDoubleMatrix C11 = (SIMDDoubleMatrix) ((SIMDDoubleMatrix) M1.add(M4)).subtract((SIMDDoubleMatrix) M5.subtract(M7));
        SIMDDoubleMatrix C12 = (SIMDDoubleMatrix) M3.add(M5);
        SIMDDoubleMatrix C21 = (SIMDDoubleMatrix) M2.add(M4);
        SIMDDoubleMatrix C22 = (SIMDDoubleMatrix) ((SIMDDoubleMatrix) M1.subtract(M2)).add((SIMDDoubleMatrix) M3.add(M6));

        return combine(C11, C12, C21, C22);
    }

    private static SIMDDoubleMatrix combine(SIMDDoubleMatrix C11, SIMDDoubleMatrix C12, SIMDDoubleMatrix C21, SIMDDoubleMatrix C22) {
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
        
        return new SIMDDoubleMatrix(n, n, combinedData);
    }
}
