/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
import org.jscience.mathematics.numbers.complex.Complex;
import static org.junit.jupiter.api.Assertions.*;

public class ComplexMathTest {

    @Test
    public void testExpLog() {
        Complex z = Complex.of(0, Math.PI); // i*pi
        Complex expZ = z.exp(); // e^(i*pi) = -1
        assertEquals(-1.0, expZ.real(), 1e-9);
        assertEquals(0.0, expZ.imaginary(), 1e-9);

        Complex one = Complex.ONE;
        Complex logOne = one.log(); // ln(1) = 0
        assertEquals(0.0, logOne.real(), 1e-9);
        assertEquals(0.0, logOne.imaginary(), 1e-9);
    }

    @Test
    public void testTrig() {
        Complex z = Complex.ZERO;
        assertEquals(0.0, z.sin().real(), 1e-9);
        assertEquals(1.0, z.cos().real(), 1e-9);
        assertEquals(0.0, z.tan().real(), 1e-9);
    }

    @Test
    public void testHyperbolic() {
        Complex z = Complex.ZERO;
        assertEquals(0.0, z.sinh().real(), 1e-9);
        assertEquals(1.0, z.cosh().real(), 1e-9);
        assertEquals(0.0, z.tanh().real(), 1e-9);
    }
}
