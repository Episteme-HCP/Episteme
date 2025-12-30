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

package org.jscience.measure;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Time;

import org.junit.jupiter.api.Test;
import static org.jscience.measure.Units.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Quantities} factory and {@link Quantity} operations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuantitiesTest {

    @Test
    public void testCreate() {
        Quantity<Length> d1 = Quantities.create(100.0, METER);
        Quantity<Length> d2 = Quantities.create(100L, METER);
        Quantity<Length> d3 = Quantities.create(100, METER);
        Quantity<Length> d4 = Quantities.create(Real.of(100.0), METER);

        assertEquals(100.0, d1.getValue().doubleValue());
        assertEquals(METER, d1.getUnit());
        assertEquals(d1, d2);
        assertEquals(d1, d3);
        assertEquals(d1, d4);
    }

    @Test
    public void testArithmetic() {
        Quantity<Length> d1 = Quantities.create(100.0, METER);
        Quantity<Length> d2 = Quantities.create(50.0, METER);

        assertEquals(150.0, d1.add(d2).getValue().doubleValue());
        assertEquals(50.0, d1.subtract(d2).getValue().doubleValue());
        assertEquals(200.0, d1.multiply(2.0).getValue().doubleValue());
        assertEquals(50.0, d1.divide(2.0).getValue().doubleValue());

        assertEquals(-100.0, d1.negate().getValue().doubleValue());
        assertEquals(100.0, d1.negate().abs().getValue().doubleValue());
    }

    @Test
    public void testMultipleUnits() {
        Quantity<Length> d1 = Quantities.create(1.0, KILOMETER);
        Quantity<Length> d2 = Quantities.create(500.0, METER);

        // Result should be in d1's unit (KILOMETER)
        Quantity<Length> sum = d1.add(d2);
        assertEquals(1.5, sum.getValue().doubleValue(), 1e-9);
        assertEquals(KILOMETER, sum.getUnit());

        // Comparison across units
        assertTrue(d1.compareTo(d2) > 0);
        assertTrue(d2.compareTo(d1) < 0);
        assertEquals(0, d1.compareTo(Quantities.create(1000.0, METER)));
    }

    @Test
    public void testMultiplicationDivisionTyped() {
        Quantity<Length> distance = Quantities.create(100.0, METER);
        Quantity<Time> time = Quantities.create(10.0, SECOND);

        Quantity<?> speed = distance.divide(time);
        assertEquals(10.0, speed.getValue().doubleValue());
        assertEquals(METER_PER_SECOND, speed.getUnit());

        Quantity<?> area = distance.multiply(distance);
        assertEquals(10000.0, area.getValue().doubleValue());
        // Square meter is not explicitly in Units but should be represented
        assertNotNull(area.getUnit());
    }

    @Test
    public void testParse() {
        Quantity<Length> d = Quantities.parse("100 m", METER);
        assertEquals(100.0, d.getValue().doubleValue());
        assertEquals(METER, d.getUnit());

        Quantity<Length> dk = Quantities.parse("1 km", METER);
        assertEquals(1000.0, dk.getValue().doubleValue());
        assertEquals(METER, dk.getUnit());

        Quantity<?> unknown = Quantities.parse("50 kg");
        assertEquals(50.0, unknown.getValue().doubleValue());
        assertEquals(KILOGRAM, unknown.getUnit());
    }

    @Test
    public void testIsZero() {
        assertTrue(Quantities.create(0.0, METER).isZero());
        assertTrue(Quantities.create(1e-11, METER).isZero());
        assertFalse(Quantities.create(1e-9, METER).isZero());
    }

    @Test
    public void testAsType() {
        Quantity<Length> d = Quantities.create(100.0, METER);
        Quantity<Length> d2 = d.asType(Length.class);
        assertSame(d, d2);
    }
}
