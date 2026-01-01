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

package org.jscience.mathematics.algebra;

import org.jscience.mathematics.algebra.intervals.IntervalND;

import org.jscience.mathematics.numbers.real.Real;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalNDTest {

    private static final double TOLERANCE = 1e-9;

    @Test
    public void testConstruction() {
        List<Real> min = Arrays.asList(Real.of(0), Real.of(0));
        List<Real> max = Arrays.asList(Real.of(1), Real.of(1));
        IntervalND interval = new IntervalND(min, max);

        assertEquals(2, interval.dimension());
        assertEquals(0.0, interval.getMinPoint().get(0).doubleValue(), TOLERANCE);
        assertEquals(1.0, interval.getMaxPoint().get(1).doubleValue(), TOLERANCE);
    }

    @Test
    public void testContains() {
        List<Real> min = Arrays.asList(Real.of(0), Real.of(0), Real.of(0));
        List<Real> max = Arrays.asList(Real.of(1), Real.of(1), Real.of(1));
        IntervalND interval = new IntervalND(min, max);

        // Point inside
        assertTrue(interval.contains(Arrays.asList(Real.of(0.5), Real.of(0.5), Real.of(0.5))));

        // Point on boundary
        assertTrue(interval.contains(Arrays.asList(Real.of(0), Real.of(0.5), Real.of(0.5))));

        // Point outside
        assertFalse(interval.contains(Arrays.asList(Real.of(1.5), Real.of(0.5), Real.of(0.5))));
    }

    @Test
    public void testIntersection() {
        List<Real> min1 = Arrays.asList(Real.of(0), Real.of(0));
        List<Real> max1 = Arrays.asList(Real.of(2), Real.of(2));
        IntervalND interval1 = new IntervalND(min1, max1);

        List<Real> min2 = Arrays.asList(Real.of(1), Real.of(1));
        List<Real> max2 = Arrays.asList(Real.of(3), Real.of(3));
        IntervalND interval2 = new IntervalND(min2, max2);

        IntervalND intersection = (IntervalND) interval1.intersection(interval2);
        assertNotNull(intersection);
        assertEquals(1.0, intersection.getMinPoint().get(0).doubleValue(), TOLERANCE);
        assertEquals(2.0, intersection.getMaxPoint().get(0).doubleValue(), TOLERANCE);
    }

    @Test
    public void testNoIntersection() {
        List<Real> min1 = Arrays.asList(Real.of(0), Real.of(0));
        List<Real> max1 = Arrays.asList(Real.of(1), Real.of(1));
        IntervalND interval1 = new IntervalND(min1, max1);

        List<Real> min2 = Arrays.asList(Real.of(2), Real.of(2));
        List<Real> max2 = Arrays.asList(Real.of(3), Real.of(3));
        IntervalND interval2 = new IntervalND(min2, max2);

        assertNull(interval1.intersection(interval2));
    }

    @Test
    public void testUnion() {
        List<Real> min1 = Arrays.asList(Real.of(0), Real.of(0));
        List<Real> max1 = Arrays.asList(Real.of(1), Real.of(1));
        IntervalND interval1 = new IntervalND(min1, max1);

        List<Real> min2 = Arrays.asList(Real.of(0.5), Real.of(0.5));
        List<Real> max2 = Arrays.asList(Real.of(1.5), Real.of(1.5));
        IntervalND interval2 = new IntervalND(min2, max2);

        IntervalND union = (IntervalND) interval1.union(interval2);
        assertEquals(0.0, union.getMinPoint().get(0).doubleValue(), TOLERANCE);
        assertEquals(1.5, union.getMaxPoint().get(0).doubleValue(), TOLERANCE);
    }

    @Test
    public void testVolume() {
        List<Real> min = Arrays.asList(Real.of(0), Real.of(0), Real.of(0));
        List<Real> max = Arrays.asList(Real.of(2), Real.of(3), Real.of(4));
        IntervalND interval = new IntervalND(min, max);

        // Volume = 2 * 3 * 4 = 24
        assertEquals(24.0, interval.volume().doubleValue(), TOLERANCE);
    }

    @Test
    public void testIsSubsetOf() {
        List<Real> min1 = Arrays.asList(Real.of(0.5), Real.of(0.5));
        List<Real> max1 = Arrays.asList(Real.of(1.5), Real.of(1.5));
        IntervalND small = new IntervalND(min1, max1);

        List<Real> min2 = Arrays.asList(Real.of(0), Real.of(0));
        List<Real> max2 = Arrays.asList(Real.of(2), Real.of(2));
        IntervalND large = new IntervalND(min2, max2);

        assertTrue(small.isSubsetOf(large));
        assertFalse(large.isSubsetOf(small));
    }

    @Test
    public void testIsEmpty() {
        List<Real> point = Arrays.asList(Real.of(1), Real.of(1));
        IntervalND interval = new IntervalND(point, point);
        assertFalse(interval.isEmpty());

        // Non-empty interval
        List<Real> min = Arrays.asList(Real.of(0), Real.of(0));
        List<Real> max = Arrays.asList(Real.of(1), Real.of(1));
        IntervalND nonEmpty = new IntervalND(min, max);
        assertFalse(nonEmpty.isEmpty());
    }
}


