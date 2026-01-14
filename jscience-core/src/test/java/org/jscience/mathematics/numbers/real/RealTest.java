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

package org.jscience.mathematics.numbers.real;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Real} class.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RealTest {

    @Test
    public void testFactory() {
        Real r1 = Real.of(1.0);
        Real r2 = Real.of(1.0f);
        Real r3 = Real.of(BigDecimal.ONE);
        Real r4 = Real.of("3.14159");

        assertNotNull(r1);
        assertNotNull(r2);
        assertNotNull(r3);
        assertNotNull(r4);
    }

    @Test
    public void testArithmetic() {
        Real a = Real.of(2.0);
        Real b = Real.of(3.0);

        assertEquals(5.0, a.add(b).doubleValue(), 1e-9);
        assertEquals(-1.0, a.subtract(b).doubleValue(), 1e-9);
        assertEquals(6.0, a.multiply(b).doubleValue(), 1e-9);
        assertEquals(0.6666666666666666, a.divide(b).doubleValue(), 1e-9);
        assertEquals(-2.0, a.negate().doubleValue(), 1e-9);
        assertEquals(0.5, a.inverse().doubleValue(), 1e-9);
    }

    @Test
    public void testComparison() {
        Real a = Real.of(1.0);
        Real b = Real.of(2.0);
        Real c = Real.of(1.0);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(c));

        assertEquals(a, c);
        assertNotEquals(a, b);
        assertEquals(a.hashCode(), c.hashCode());
    }

    @Test
    public void testConversions() {
        Real r = Real.of(123.456);
        assertEquals(123, r.intValue());
        assertEquals(123L, r.longValue());
        assertEquals(123.456f, r.floatValue(), 1e-6);
        assertEquals(123.456, r.doubleValue(), 1e-9);
        assertNotNull(r.bigDecimalValue());
    }

    @Test
    public void testSummary() {
        assertNotNull(Real.ZERO);
        assertNotNull(Real.ONE);
        assertNotNull(Real.PI);
        assertNotNull(Real.E);

        assertEquals(0.0, Real.ZERO.doubleValue());
        assertEquals(1.0, Real.ONE.doubleValue());
        assertEquals(Math.PI, Real.PI.doubleValue(), 1e-15);
    }

    @Test
    public void testState() {
        assertTrue(Real.ZERO.isZero());
        assertFalse(Real.ONE.isZero());
        assertTrue(Real.ONE.isOne());
        assertTrue(Real.of(Double.NaN).isNaN());
        assertTrue(Real.POSITIVE_INFINITY.isInfinite());
        assertTrue(Real.NEGATIVE_INFINITY.isInfinite());
    }
}


