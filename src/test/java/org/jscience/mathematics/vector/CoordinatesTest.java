package org.jscience.mathematics.vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Reals;
import java.util.Arrays;

/**
 * Tests for Coordinate Systems (Vectors/Matrices).
 */
public class CoordinatesTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testVectorAddition() {
        Vector<Real> v1 = DenseVector.of(Arrays.asList(Real.of(1.0), Real.of(2.0), Real.of(3.0)), Reals.getInstance());
        Vector<Real> v2 = DenseVector.of(Arrays.asList(Real.of(4.0), Real.of(5.0), Real.of(6.0)), Reals.getInstance());

        Vector<Real> sum = v1.add(v2);

        assertEquals(5.0, sum.get(0).doubleValue(), DELTA);
        assertEquals(7.0, sum.get(1).doubleValue(), DELTA);
        assertEquals(9.0, sum.get(2).doubleValue(), DELTA);
    }

    @Test
    public void testVectorScaling() {
        Vector<Real> v1 = DenseVector.of(Arrays.asList(Real.of(1.0), Real.of(2.0)), Reals.getInstance());
        Vector<Real> scaled = v1.multiply(Real.of(2.0));

        assertEquals(2.0, scaled.get(0).doubleValue(), DELTA);
        assertEquals(4.0, scaled.get(1).doubleValue(), DELTA);
    }
}
