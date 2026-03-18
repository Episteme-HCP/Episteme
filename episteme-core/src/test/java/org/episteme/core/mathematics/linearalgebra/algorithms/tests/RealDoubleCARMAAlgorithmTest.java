/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.core.mathematics.linearalgebra.algorithms.tests;

import org.episteme.core.mathematics.linearalgebra.algorithms.RealDoubleCARMAAlgorithm;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SIMD-optimized CARMA algorithm.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
class RealDoubleCARMAAlgorithmTest {

    private static final double TOLERANCE = 1e-10;

    @Test
    void testSmallLeafCase() {
        // Small enough to trigger standard multiplication immediately if logic is correct
        testMultiplication(16, 16, 16);
    }

    @Test
    void testPowerOfTwo() {
        // Triggers recursive splitting
        testMultiplication(256, 256, 256);
    }

    @Test
    void testNonPowerOfTwo() {
        // Tests padding/splitting logic for irregular sizes
        testMultiplication(127, 127, 127);
        testMultiplication(300, 300, 300);
    }

    @Test
    void testRectangular() {
        testMultiplication(100, 200, 150);
        testMultiplication(256, 128, 64);
    }

    @Test
    void testLargeRecursive() {
        // Large enough to split multiple times
        testMultiplication(512, 512, 512);
    }

    private void testMultiplication(int m, int k, int n) {
        Random rand = new Random(42);
        double[][] aData = generateRandomData(m, k, rand);
        double[][] bData = generateRandomData(k, n, rand);

        RealDoubleMatrix a = RealDoubleMatrix.of(aData);
        RealDoubleMatrix b = RealDoubleMatrix.of(bData);

        // CARMA Multiplication
        SIMDRealDoubleMatrix result = RealDoubleCARMAAlgorithm.multiply(
            SIMDRealDoubleMatrix.from(a), 
            SIMDRealDoubleMatrix.from(b)
        );

        // Reference Multiplication (O(n^3))
        double[][] expected = referenceMultiply(aData, bData);

        // Verification
        assertEquals(m, result.rows());
        assertEquals(n, result.cols());

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                assertEquals(expected[i][j], result.get(i, j).doubleValue(), TOLERANCE,
                    "Mismatch at (" + i + "," + j + ") for dimensions " + m + "x" + k + "x" + n);
            }
        }
    }

    private double[][] generateRandomData(int rows, int cols, Random rand) {
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = rand.nextDouble() * 2 - 1;
            }
        }
        return data;
    }

    private double[][] referenceMultiply(double[][] a, double[][] b) {
        int m = a.length;
        int k = a[0].length;
        int n = b[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int l = 0; l < k; l++) {
                double aik = a[i][l];
                for (int j = 0; j < n; j++) {
                    c[i][j] += aik * b[l][j];
                }
            }
        }
        return c;
    }
}
