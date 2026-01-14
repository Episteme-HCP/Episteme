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

package org.jscience.mathematics.sequences;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;
import static org.junit.jupiter.api.Assertions.*;

public class SequenceTest {

    @Test
    public void testFibonacci() {
        FibonacciSequence fib = new FibonacciSequence();
        assertEquals(0, fib.get(0).longValue());
        assertEquals(1, fib.get(1).longValue());
        assertEquals(1, fib.get(2).longValue());
        assertEquals(2, fib.get(3).longValue());
        assertEquals(3, fib.get(4).longValue());
        assertEquals(5, fib.get(5).longValue());
        assertEquals(8, fib.get(6).longValue());
    }

    @Test
    public void testHarmonic() {
        HarmonicSeries harmonic = new HarmonicSeries();
        // H_1 = 1
        assertEquals(1.0, harmonic.get(1).doubleValue(), 1e-9);
        // H_2 = 1 + 1/2 = 1.5
        assertEquals(1.5, harmonic.get(2).doubleValue(), 1e-9);

        assertFalse(harmonic.isConvergent());
    }

    @Test
    public void testGeometric() {
        // 1 + 0.5 + 0.25 + ... -> 2
        GeometricSeries geo = new GeometricSeries(Real.ONE, Real.of(0.5));
        assertTrue(geo.isConvergent());
        assertEquals(2.0, geo.limit().doubleValue(), 1e-9);

        // Partial sum n=1: 1 + 0.5 = 1.5
        assertEquals(1.5, geo.get(1).doubleValue(), 1e-9);
    }
}


