package org.jscience.mathematics.vector;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for Vector operations.
 */
public class VectorTest {

    @Test
    public void testVectorCreation() {
        List<Real> data = Arrays.asList(Real.of(1), Real.of(2), Real.of(3));
        Vector<Real> v = DenseVector.of(data, Reals.getInstance());
        assertEquals(3, v.dimension());
        assertEquals(Real.of(1), v.get(0));
        assertEquals(Real.of(3), v.get(2));
    }

    @Test
    public void testDotProduct() {
        // v1 = [1, 2, 3], v2 = [4, 5, 6]
        // dot = 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        List<Real> data1 = Arrays.asList(Real.of(1), Real.of(2), Real.of(3));
        List<Real> data2 = Arrays.asList(Real.of(4), Real.of(5), Real.of(6));

        Vector<Real> v1 = DenseVector.of(data1, Reals.getInstance());
        Vector<Real> v2 = DenseVector.of(data2, Reals.getInstance());
        Real dot = v1.dot(v2);

        assertEquals(Real.of(32), dot);
    }

    @Test
    public void testNorm() {
        // v = [3, 4]
        // norm = sqrt(3^2 + 4^2) = sqrt(9 + 16) = 5
        List<Real> data = Arrays.asList(Real.of(3), Real.of(4));
        Vector<Real> v = DenseVector.of(data, Reals.getInstance());
        Real norm = v.norm();

        assertEquals(Real.of(5), norm);
    }
}
