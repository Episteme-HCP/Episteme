package org.jscience.mathematics.analysis;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.VectorFactory;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class VectorFunctionTest {

    @Test
    public void testJacobian() {
        // Define function f(x,y) = [x^2, x*y]
        VectorFunction<Real> func = new VectorFunction<Real>() {
            @Override
            public Vector<Real> evaluate(Vector<Real> point) {
                Real x = point.get(0);
                Real y = point.get(1);
                return VectorFactory.create(Arrays.asList(
                        x.multiply(x),
                        x.multiply(y)), Reals.getInstance());
            }

            @Override
            public int outputDimension() {
                return 2;
            }
        };

        // Evaluate at point (2, 3)
        // Jacobian should be:
        // [ 2x 0 ] -> [ 4 0 ]
        // [ y x ] -> [ 3 2 ]

        Vector<Real> point = VectorFactory.create(Arrays.asList(Real.of(2), Real.of(3)), Reals.getInstance());

        Matrix<Real> J = func.jacobian(point);

        assertEquals(4.0, J.get(0, 0).doubleValue(), 1e-4);
        assertEquals(0.0, J.get(0, 1).doubleValue(), 1e-4);
        assertEquals(3.0, J.get(1, 0).doubleValue(), 1e-4);
        assertEquals(2.0, J.get(1, 1).doubleValue(), 1e-4);
    }
}
