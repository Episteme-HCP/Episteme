/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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


