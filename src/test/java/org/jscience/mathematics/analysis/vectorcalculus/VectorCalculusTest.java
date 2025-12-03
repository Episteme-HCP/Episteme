package org.jscience.mathematics.analysis.vectorcalculus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.mathematics.analysis.ScalarField;
import org.jscience.mathematics.analysis.VectorField;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.geometry.PointND;

import java.util.Arrays;

public class VectorCalculusTest {

    private static final Real H = Real.of(1e-5);
    private static final Real TOLERANCE = Real.of(1e-4);

    @Test
    public void testGradient() {
        // f(x,y) = x^2 + y^2
        ScalarField<PointND> field = p -> {
            Real x = p.get(0);
            Real y = p.get(1);
            return x.multiply(x).add(y.multiply(y));
        };

        PointND point = PointND.of(Real.of(1.0), Real.of(2.0));
        // Gradient should be (2x, 2y) -> (2, 4)
        Vector<Real> grad = Gradient.compute(field, point, H);

        assertEquals(2.0, grad.get(0).doubleValue(), TOLERANCE.doubleValue());
        assertEquals(4.0, grad.get(1).doubleValue(), TOLERANCE.doubleValue());
    }

    @Test
    public void testDivergence() {
        // F(x,y,z) = (x, y, z)
        VectorField<PointND> field = p -> {
            return p.toVector();
        };

        PointND point = PointND.of(Real.of(1.0), Real.of(2.0), Real.of(3.0));
        // Divergence should be 1 + 1 + 1 = 3
        Real div = Divergence.compute(field, point, H);

        assertEquals(3.0, div.doubleValue(), TOLERANCE.doubleValue());
    }

    @Test
    public void testCurl() {
        // F(x,y,z) = (-y, x, 0) (rotation around z-axis)
        VectorField<PointND> field = p -> {
            Real x = p.get(0);
            Real y = p.get(1);
            Real z = p.get(2);
            return new DenseVector<>(Arrays.asList(y.negate(), x, Real.ZERO),
                    org.jscience.mathematics.sets.Reals.getInstance());
        };

        PointND point = PointND.of(Real.of(1.0), Real.of(0.0), Real.of(0.0));
        // Curl should be (0, 0, 2)
        // curl_z = dFy/dx - dFx/dy = 1 - (-1) = 2
        Vector<Real> curl = Curl.compute(field, point, H);

        assertEquals(0.0, curl.get(0).doubleValue(), TOLERANCE.doubleValue());
        assertEquals(0.0, curl.get(1).doubleValue(), TOLERANCE.doubleValue());
        assertEquals(2.0, curl.get(2).doubleValue(), TOLERANCE.doubleValue());
    }

    @Test
    public void testLaplacian() {
        // f(x,y) = x^2 + y^2
        ScalarField<PointND> field = p -> {
            Real x = p.get(0);
            Real y = p.get(1);
            return x.multiply(x).add(y.multiply(y));
        };

        PointND point = PointND.of(Real.of(1.0), Real.of(2.0));
        // Laplacian = d2f/dx2 + d2f/dy2 = 2 + 2 = 4
        Real laplacian = Laplacian.compute(field, point, H);

        assertEquals(4.0, laplacian.doubleValue(), TOLERANCE.doubleValue());
    }
}
