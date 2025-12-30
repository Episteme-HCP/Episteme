/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.relativity;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.Vector4D;
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
