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

package org.jscience.core.mathematics.linearalgebra;

import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.numbers.real.RealDouble;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.ejml.simple.SimpleMatrix;
import java.util.Random;

public class LinearAlgebraVerificationTest {

    @Test
    public void testRealDoubleMatrixCreation() {
        Double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        Real[][] realData = new Real[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                realData[i][j] = RealDouble.create(data[i][j]);

        Matrix<Real> m = Matrix.of(realData, org.jscience.core.mathematics.sets.Reals.getInstance());

        assertTrue(m instanceof RealDoubleMatrix, "Should be instance of RealDoubleMatrix");
        assertEquals(1.0, m.get(0, 0).doubleValue(), 1e-9, "Element check");
    }

    @Test
    public void testAutoStorageSelection() {
        Real[][] sparseData = new Real[10][10];
        Real zero = Real.ZERO;
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                sparseData[i][j] = zero;

        sparseData[0][0] = RealDouble.create(1.0); // 1% density

        Matrix<Real> m = Matrix.of(sparseData, org.jscience.core.mathematics.sets.Reals.getInstance());
        // Assuming SparseMatrix or GenericMatrix with SparseStorage
        // System.out.println("Auto storage class: " + m.getClass().getName());
        assertFalse(m instanceof RealDoubleMatrix, "Should not be RealDoubleMatrix (Sparse)");
        // Note: RealDoubleMatrix only handles Dense currently.
    }

    @Test
    public void testMatrixToTensor() {
        RealDoubleMatrix m = RealDoubleMatrix.of(new double[][] { { 1, 2 }, { 3, 4 } });
        Tensor<Real> t = m.toTensor();

        assertEquals(2, t.rank(), "Tensor rank should be 2");
        assertEquals(1.0, t.get(0, 0).doubleValue(), 1e-9, "Value check");
        assertEquals(4.0, t.get(1, 1).doubleValue(), 1e-9, "Value check");
    }

    @Test
    public void verifyOperationsAgainstEJML() {
        int size = 100;
        Random rand = new Random(42);
        double[][] aData = randomData(size, size, rand);
        double[][] bData = randomData(size, size, rand);

        RealDoubleMatrix a = RealDoubleMatrix.of(aData);
        RealDoubleMatrix b = RealDoubleMatrix.of(bData);
        
        SimpleMatrix aEjml = new SimpleMatrix(aData);
        SimpleMatrix bEjml = new SimpleMatrix(bData);

        // 1. Multiplication
        Matrix<Real> multResult = a.multiply(b);
        SimpleMatrix multEjml = aEjml.mult(bEjml);
        assertMatrixEquals(multEjml, multResult, 1e-9, "Multiplication mismatch");

        // 2. Addition
        Matrix<Real> addResult = a.add(b);
        SimpleMatrix addEjml = aEjml.plus(bEjml);
        assertMatrixEquals(addEjml, addResult, 1e-9, "Addition mismatch");

        // 3. Inverse
        Matrix<Real> invResult = a.inverse();
        SimpleMatrix invEjml = aEjml.invert();
        assertMatrixEquals(invEjml, invResult, 1e-7, "Inverse mismatch");

        // 4. Solve
        double[] vData = new double[size];
        for (int i = 0; i < size; i++) vData[i] = rand.nextDouble();
        Vector<Real> v = org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(vData);
        Real[] vArr = new Real[size];
        for(int i=0; i<size; i++) vArr[i] = v.get(i);
        Real[] solveResultArr = org.jscience.core.mathematics.linearalgebra.matrices.solvers.MatrixSolver.solve(a, vArr);
        
        SimpleMatrix vEjml = new SimpleMatrix(size, 1);
        for(int i=0; i<size; i++) vEjml.set(i, 0, vData[i]);
        SimpleMatrix xEjml = aEjml.solve(vEjml);
        
        for (int i = 0; i < size; i++) {
            assertEquals(xEjml.get(i, 0), solveResultArr[i].doubleValue(), 1e-7, "Solve mismatch at index " + i);
        }
    }

    private double[][] randomData(int rows, int cols, Random rand) {
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = rand.nextDouble() * 10 - 5;
            }
        }
        return data;
    }

    private void assertMatrixEquals(SimpleMatrix expected, Matrix<Real> actual, double tol, String msg) {
        assertEquals(expected.getNumRows(), actual.rows(), msg + ": rows count");
        assertEquals(expected.getNumCols(), actual.cols(), msg + ": cols count");
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.cols(); j++) {
                assertEquals(expected.get(i, j), actual.get(i, j).doubleValue(), tol, msg + " at [" + i + "," + j + "]");
            }
        }
    }
}



