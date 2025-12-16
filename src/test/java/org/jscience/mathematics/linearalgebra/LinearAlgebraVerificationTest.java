package org.jscience.mathematics.linearalgebra;

import org.jscience.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.real.RealDouble;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinearAlgebraVerificationTest {

    @Test
    public void testRealDoubleMatrixCreation() {
        Double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        Real[][] realData = new Real[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                realData[i][j] = RealDouble.create(data[i][j]);

        Matrix<Real> m = MatrixFactory.create(realData, org.jscience.mathematics.sets.Reals.getInstance(),
                MatrixFactory.Storage.DENSE);

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

        Matrix<Real> m = MatrixFactory.create(sparseData, org.jscience.mathematics.sets.Reals.getInstance(),
                MatrixFactory.Storage.AUTO);
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
        // shape check if available
        assertEquals(1.0, t.get(0, 0).doubleValue(), 1e-9, "Value check");
        assertEquals(4.0, t.get(1, 1).doubleValue(), 1e-9, "Value check");
    }
}
