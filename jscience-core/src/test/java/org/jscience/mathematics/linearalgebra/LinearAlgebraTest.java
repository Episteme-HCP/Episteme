package org.jscience.mathematics.linearalgebra;

import org.jscience.mathematics.linearalgebra.backends.CPUDenseLinearAlgebraProvider;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class LinearAlgebraTest {

    @Test
    public void testMatrixVectorMultiplication() {
        System.out.println("Testing Matrix-Vector Multiplication...");
        Field<Real> reals = org.jscience.mathematics.sets.Reals.INSTANCE;
        CPUDenseLinearAlgebraProvider<Real> provider = new CPUDenseLinearAlgebraProvider<>(reals);

        // 2x2 Matrix: [1 2]
        //             [3 4]
        Real[][] mData = new Real[][]{
            {Real.of(1), Real.of(2)},
            {Real.of(3), Real.of(4)}
        };
        Matrix<Real> M = new DenseMatrix<>(mData, reals);

        // Vector: [1]
        //         [2]
        Real[] vData = new Real[]{Real.of(1), Real.of(2)};
        Vector<Real> V = new DenseVector<>(Arrays.asList(vData), reals);

        // Result should be: [1*1 + 2*2] = [5]
        //                   [3*1 + 4*2] = [11]
        
        Vector<Real> result = provider.multiply(M, V);
        
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.dimension());
        assertEquals(5.0, result.get(0).doubleValue(), 0.0001);
        assertEquals(11.0, result.get(1).doubleValue(), 0.0001);
        System.out.println("Matrix-Vector Multiplication Passed.");
    }

    @Test
    public void testMatrixInverse() {
        System.out.println("Testing Matrix Inverse...");
        Field<Real> reals = org.jscience.mathematics.sets.Reals.INSTANCE;
        CPUDenseLinearAlgebraProvider<Real> provider = new CPUDenseLinearAlgebraProvider<>(reals);

        // 2x2 Matrix: [4 7]
        //             [2 6]
        // Det = 24 - 14 = 10
        // Inv = (1/10) * [6 -7]
        //                [-2 4]
        //     = [0.6 -0.7]
        //       [-0.2 0.4]
        
        Real[][] mData = new Real[][]{
            {Real.of(4), Real.of(7)},
            {Real.of(2), Real.of(6)}
        };
        Matrix<Real> M = new DenseMatrix<>(mData, reals);
        
        Matrix<Real> inv = provider.inverse(M);
        
        assertNotNull(inv, "Inverse should not be null");
        assertEquals(0.6, inv.get(0, 0).doubleValue(), 0.0001);
        assertEquals(-0.7, inv.get(0, 1).doubleValue(), 0.0001);
        assertEquals(-0.2, inv.get(1, 0).doubleValue(), 0.0001);
        assertEquals(0.4, inv.get(1, 1).doubleValue(), 0.0001);
        System.out.println("Matrix Inverse Passed.");
    }
}
