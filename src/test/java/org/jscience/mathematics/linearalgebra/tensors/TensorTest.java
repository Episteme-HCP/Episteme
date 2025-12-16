package org.jscience.mathematics.linearalgebra.tensors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Tests for Tensor operations and Einstein Summation.
 */
public class TensorTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testEinsteinSummationDotProduct() {
        // Vector dot product: i,i->
        Real[] d1 = { Real.of(1), Real.of(2), Real.of(3) };
        Real[] d2 = { Real.of(4), Real.of(5), Real.of(6) };
        Tensor<Real> v1 = new DenseTensor<>(d1, 3);
        Tensor<Real> v2 = new DenseTensor<>(d2, 3);

        Tensor<Real> result = v1.einsum("i,i->", v2);

        assertEquals(0, result.rank());
        assertEquals(32.0, result.get().doubleValue(), DELTA); // 1*4 + 2*5 + 3*6 = 4+10+18 = 32
    }

    @Test
    public void testEinsteinSummationMatrixMult() {
        // Matrix multiplication: ij,jk->ik
        // A = [[1, 2], [3, 4]]
        // B = [[2, 0], [1, 2]]
        // Result = [[4, 4], [10, 8]]

        Real[] dataA = { Real.of(1), Real.of(2), Real.of(3), Real.of(4) };
        Tensor<Real> A = new DenseTensor<>(dataA, 2, 2);

        Real[] dataB = { Real.of(2), Real.of(0), Real.of(1), Real.of(2) };
        Tensor<Real> B = new DenseTensor<>(dataB, 2, 2);

        Tensor<Real> C = A.einsum("ij,jk->ik", B);

        assertArrayEquals(new int[] { 2, 2 }, C.shape());
        assertEquals(4.0, C.get(0, 0).doubleValue(), DELTA);
        assertEquals(4.0, C.get(0, 1).doubleValue(), DELTA);
        assertEquals(10.0, C.get(1, 0).doubleValue(), DELTA);
        assertEquals(8.0, C.get(1, 1).doubleValue(), DELTA);
    }

    @Test
    public void testEinsteinSummationTranspose() {
        // Transpose: ij->ji
        Real[] data = { Real.of(1), Real.of(2), Real.of(3), Real.of(4) };
        Tensor<Real> A = new DenseTensor<>(data, 2, 2);

        Tensor<Real> T = A.einsum("ij->ji");

        assertEquals(1.0, T.get(0, 0).doubleValue(), DELTA);
        assertEquals(3.0, T.get(0, 1).doubleValue(), DELTA);
        assertEquals(2.0, T.get(1, 0).doubleValue(), DELTA);
        assertEquals(4.0, T.get(1, 1).doubleValue(), DELTA);
    }
}
