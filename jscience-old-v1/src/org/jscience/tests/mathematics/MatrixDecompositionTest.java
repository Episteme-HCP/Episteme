/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.tests.mathematics;

/**
 * Testcase for matrix decomposition methods.
 *
 * @author Mark Hale
 */
public class MatrixDecompositionTest extends junit.framework.TestCase {
    /** DOCUMENT ME! */
    private final int N = 5;

/**
     * Creates a new MatrixDecompositionTest object.
     *
     * @param name DOCUMENT ME!
     */
    public MatrixDecompositionTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(MatrixDecompositionTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setUp() {
        org.jscience.GlobalSettings.ZERO_TOL = 1.0e-6;
    }

    /**
     * DOCUMENT ME!
     */
    public void testSquareLU() {
        DoubleSquareMatrix mat = MatrixToolkit.randomSquareMatrix(N);
        int[] p = new int[N + 1];
        DoubleSquareMatrix[] luArray = mat.luDecompose(p);
        DoubleSquareMatrix lu = luArray[0].multiply(luArray[1]);
        double[][] pmatArray = new double[N][N];

        for (int j, i = 0; i < N; i++) {
            for (j = 0; j < N; j++)
                pmatArray[i][j] = mat.getElement(p[i], j);
        }

        DoubleSquareMatrix pmat = new DoubleSquareMatrix(pmatArray);
        assertEquals(pmat, lu);
    }

    /**
     * DOCUMENT ME!
     */
    public void testSquareCholesky() {
        DoubleSquareMatrix mat = MatrixToolkit.randomSquareMatrix(N);
        mat = (DoubleSquareMatrix) mat.multiply(mat.transpose()); // make symmetric and positive

        DoubleSquareMatrix[] lu = mat.choleskyDecompose();
        assertEquals(mat, lu[0].multiply(lu[1]));
    }

    /**
     * DOCUMENT ME!
     */
    public void testSquareQR() {
        DoubleSquareMatrix mat = MatrixToolkit.randomSquareMatrix(N);
        DoubleSquareMatrix[] qr = mat.qrDecompose();
        assertEquals(mat, qr[0].multiply(qr[1]));
    }

    /**
     * DOCUMENT ME!
     */
    public void testSquareSVD() {
        DoubleSquareMatrix mat = MatrixToolkit.randomSquareMatrix(N);
        DoubleSquareMatrix[] svdArray = mat.singularValueDecompose();
        DoubleSquareMatrix svd = (DoubleSquareMatrix) svdArray[0].multiply(svdArray[1])
                                                                 .multiply(svdArray[2].transpose());
        assertEquals(mat, svd);
    }

    /**
     * DOCUMENT ME!
     */
    public void testTridiagonalCholesky() {
        DoubleSquareMatrix mat = MatrixToolkit.randomTridiagonalMatrix(N);
        mat = (DoubleSquareMatrix) mat.multiply(mat.transpose()); // make symmetric and positive

        DoubleSquareMatrix[] lu = mat.choleskyDecompose();
        assertEquals(mat, lu[0].multiply(lu[1]));
    }

    /**
     * DOCUMENT ME!
     */
    public void testTridiagonalQR() {
        DoubleSquareMatrix mat = MatrixToolkit.randomTridiagonalMatrix(N);
        DoubleSquareMatrix[] qr = mat.qrDecompose();
        assertTrue(qr[0].multiply(qr[1]).equals(mat));
    }

    /**
     * DOCUMENT ME!
     */
    public void testTridiagonalSVD() {
        DoubleSquareMatrix mat = MatrixToolkit.randomTridiagonalMatrix(N);
        DoubleSquareMatrix[] svdArray = mat.singularValueDecompose();
        DoubleSquareMatrix svd = (DoubleSquareMatrix) svdArray[0].multiply(svdArray[1])
                                                                 .multiply(svdArray[2].transpose());
        assertTrue(svd.equals(mat));
    }

    /**
     * DOCUMENT ME!
     */
    public void testInverse() {
        DoubleSquareMatrix mat = MatrixToolkit.randomSquareMatrix(N);
        DoubleSquareMatrix inv = mat.inverse();
        assertTrue(mat.multiply(inv).equals(DoubleDiagonalMatrix.identity(N)));
    }

    /**
     * DOCUMENT ME!
     */
    public void testComplexSquareLU() {
        ComplexSquareMatrix mat = MatrixToolkit.randomComplexSquareMatrix(N);
        int[] p = new int[N + 1];
        ComplexSquareMatrix[] luArray = mat.luDecompose(p);
        ComplexSquareMatrix lu = luArray[0].multiply(luArray[1]);
        Complex[][] pmatArray = new Complex[N][N];

        for (int j, i = 0; i < N; i++) {
            for (j = 0; j < N; j++)
                pmatArray[i][j] = mat.getElement(p[i], j);
        }

        ComplexSquareMatrix pmat = new ComplexSquareMatrix(pmatArray);
        assertEquals(pmat, lu);
    }

    /**
     * DOCUMENT ME!
     */
    public void testComplexInverse() {
        ComplexSquareMatrix mat = MatrixToolkit.randomComplexSquareMatrix(N);
        ComplexSquareMatrix inv = mat.inverse();
        assertTrue(mat.multiply(inv).equals(ComplexDiagonalMatrix.identity(N)));
    }
}
