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

package org.jscience.mathematics.numbers;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;
import static org.junit.jupiter.api.Assertions.*;

public class RealMathTest {

    @Test
    public void testHyperbolic() {
        Real x = Real.of(1.0);
        assertEquals(Math.sinh(1.0), x.sinh().doubleValue(), 1e-9);
        assertEquals(Math.cosh(1.0), x.cosh().doubleValue(), 1e-9);
        assertEquals(Math.tanh(1.0), x.tanh().doubleValue(), 1e-9);
    }

    @Test
    public void testInverseHyperbolic() {
        Real x = Real.of(0.5);
        // asinh(0.5)
        assertEquals(Math.log(0.5 + Math.sqrt(0.5 * 0.5 + 1)), x.asinh().doubleValue(), 1e-9);

        Real y = Real.of(1.5);
        // acosh(1.5)
        assertEquals(Math.log(1.5 + Math.sqrt(1.5 * 1.5 - 1)), y.acosh().doubleValue(), 1e-9);

        // atanh(0.5)
        assertEquals(0.5 * Math.log((1 + 0.5) / (1 - 0.5)), x.atanh().doubleValue(), 1e-9);
    }

    @Test
    public void testUtilities() {
        Real x = Real.of(1.0);
        Real y = Real.of(1.0);
        assertEquals(Math.atan2(1.0, 1.0), y.atan2(x).doubleValue(), 1e-9);

        Real z = Real.of(8.0);
        assertEquals(2.0, z.cbrt().doubleValue(), 1e-9);

        assertEquals(Math.hypot(3, 4), Real.of(3).hypot(Real.of(4)).doubleValue(), 1e-9);

        assertEquals(Math.ceil(1.2), Real.of(1.2).ceil().doubleValue(), 1e-9);
        assertEquals(Math.floor(1.8), Real.of(1.8).floor().doubleValue(), 1e-9);
        assertEquals(Math.round(1.5), Real.of(1.5).round().doubleValue(), 1e-9);

        assertEquals(180.0, Real.of(Math.PI).toDegrees().doubleValue(), 1e-9);
        assertEquals(Math.PI, Real.of(180.0).toRadians().doubleValue(), 1e-9);
    }
}
