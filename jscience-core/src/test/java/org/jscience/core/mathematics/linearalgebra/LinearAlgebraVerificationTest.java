package org.jscience.core.mathematics.linearalgebra;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.*;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.numbers.real.RealDouble;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.ejml.simple.SimpleMatrix;
import java.util.Random;

public class LinearAlgebraVerificationTest {

    private static final double TOLERANCE = 1e-9;
    private static final int DEFAULT_SIZE = 50;

    @Test
    public void testRealDoubleMatrixCreation() {
        Double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        Real[][] realData = new Real[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                realData[i][j] = RealDouble.create(data[i][j]);

        Matrix<Real> m = Matrix.of(realData, org.jscience.core.mathematics.sets.Reals.getInstance());

        assertTrue(m instanceof RealDoubleMatrix, "Should be instance of RealDoubleMatrix");
        assertEquals(1.0, m.get(0, 0).doubleValue(), TOLERANCE, "Element check");
    }

    @Test
    public void verifyOperationsAgainstEJML() {
        int size = DEFAULT_SIZE;
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
        assertMatrixEquals(multEjml, multResult, TOLERANCE, "Multiplication mismatch");

        // 2. Addition
        Matrix<Real> addResult = a.add(b);
        SimpleMatrix addEjml = aEjml.plus(bEjml);
        assertMatrixEquals(addEjml, addResult, TOLERANCE, "Addition mismatch");

        // 3. Inverse
        Matrix<Real> invResult = a.inverse();
        SimpleMatrix invEjml = aEjml.invert();
        assertMatrixEquals(invEjml, invResult, 1e-7, "Inverse mismatch");

        // 4. Transpose
        Matrix<Real> transResult = a.transpose();
        SimpleMatrix transEjml = aEjml.transpose();
        assertMatrixEquals(transEjml, transResult, TOLERANCE, "Transpose mismatch");

        // 5. Solve
        double[] vData = new double[size];
        for (int i = 0; i < size; i++) vData[i] = rand.nextDouble();
        Vector<Real> v = org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(vData);
        
        // Test solvers directly and via hub
        Vector<Real> solveResult = Decomposition.solve(a, v);
        
        SimpleMatrix vEjml = new SimpleMatrix(size, 1);
        for(int i=0; i<size; i++) vEjml.set(i, 0, vData[i]);
        SimpleMatrix xEjml = aEjml.solve(vEjml);
        
        for (int i = 0; i < size; i++) {
            assertEquals(xEjml.get(i, 0), solveResult.get(i).doubleValue(), 1e-7, "Solve mismatch at index " + i);
        }
    }

    @Test
    public void testQRDecomposition() {
        int size = DEFAULT_SIZE;
        Random rand = new Random(42);
        double[][] aData = randomData(size, size, rand);
        RealDoubleMatrix a = RealDoubleMatrix.of(aData);
        
        QRResult<Real> result = Decomposition.qr(a);
        
        // Q * R should equal A
        Matrix<Real> reconstructed = result.Q().multiply(result.R());
        assertMatrixEquals(new SimpleMatrix(aData), reconstructed, 1e-7, "QR Reconstruction mismatch");
        
        // Q should be orthogonal
        Matrix<Real> qTq = result.Q().transpose().multiply(result.Q());
        SimpleMatrix identity = SimpleMatrix.identity(size);
        assertMatrixEquals(identity, qTq, 1e-7, "Q is not orthogonal");
    }

    @Test
    public void testSVDDecomposition() {
        int size = 20; // SVD is expensive
        Random rand = new Random(42);
        double[][] aData = randomData(size, size, rand);
        RealDoubleMatrix a = RealDoubleMatrix.of(aData);
        
        SVDResult<Real> result = Decomposition.svd(a);
        
        // U * diag(S) * V^T should equal A
        // Reconstruct S as matrix
        Real[][] sMatrix = new Real[size][size];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                sMatrix[i][j] = (i == j) ? result.S().get(i) : Real.ZERO;
            }
        }
        Matrix<Real> S = Matrix.of(sMatrix, org.jscience.core.mathematics.sets.Reals.getInstance());
        
        Matrix<Real> reconstructed = result.U().multiply(S).multiply(result.V().transpose());
        assertMatrixEquals(new SimpleMatrix(aData), reconstructed, 1e-7, "SVD Reconstruction mismatch");
    }

    @Test
    public void testLUDecomposition() {
        int size = DEFAULT_SIZE;
        Random rand = new Random(42);
        double[][] aData = randomData(size, size, rand);
        RealDoubleMatrix a = RealDoubleMatrix.of(aData);
        
        LUResult<Real> result = Decomposition.lu(a);
        
        // L * U should equal P * A
        Matrix<Real> luResult = result.L().multiply(result.U());
        
        // Apply permutation vector to A
        Real[][] paData = new Real[size][size];
        for(int i=0; i<size; i++) {
            int pivotIndex = (int) result.P().get(i).doubleValue();
            for(int j=0; j<size; j++) {
                paData[i][j] = a.get(pivotIndex, j);
            }
        }
        Matrix<Real> PA = Matrix.of(paData, org.jscience.core.mathematics.sets.Reals.getInstance());
        
        assertMatrixEquals(new SimpleMatrix(toDoubleArray(PA)), luResult, 1e-7, "LU Reconstruction mismatch");
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

    private double[][] toDoubleArray(Matrix<Real> matrix) {
        double[][] data = new double[matrix.rows()][matrix.cols()];
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.cols(); j++) {
                data[i][j] = matrix.get(i, j).doubleValue();
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



