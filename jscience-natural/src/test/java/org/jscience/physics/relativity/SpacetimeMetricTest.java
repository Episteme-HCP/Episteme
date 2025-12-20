package org.jscience.physics.relativity;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpacetimeMetricTest {

    // Mock Schwartzschild-like metric (diagonal only for simplicity of test)
    // g_uv = diag(-1, 1+x^2, 1, 1)

    @Test
    public void testChristoffel() {
        SpacetimeMetric metric = new SpacetimeMetric() {
            @Override
            public Tensor<Real> getMetricTensor(Vector4D point) {
                Real x = point.x(); // x^1
                Real one = Real.ONE;
                Real g11 = one.add(x.multiply(x));

                // Construct diag matrix tensor
                int dim = 4;
                Tensor<Real> g = org.jscience.mathematics.linearalgebra.tensors.TensorFactory.zeros(Real.class, dim,
                        dim);
                g.set(Real.of(-1), 0, 0); // ct
                g.set(g11, 1, 1); // x
                g.set(one, 2, 2); // y
                g.set(one, 3, 3); // z
                return g;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Vector4D element) {
                return true;
            }
        };

        // Point at x=2, others 0
        Vector4D p = new Vector4D(Real.ZERO, Real.of(2.0), Real.ZERO, Real.ZERO);

        Tensor<Real> gamma = metric.getChristoffelSymbols(p);

        // Check rank
        assertEquals(3, gamma.rank());

        // Check Gamma^1_11
        // Expected: x / (1+x^2) at x=2 -> 2 / 5 = 0.4
        Real gamma1_11 = gamma.get(1, 1, 1);
        assertEquals(0.4, gamma1_11.doubleValue(), 1e-4);

        // Check Gamma^0_00 (should be 0 as constant)
        assertEquals(0.0, gamma.get(0, 0, 0).doubleValue(), 1e-6);
    }
}
