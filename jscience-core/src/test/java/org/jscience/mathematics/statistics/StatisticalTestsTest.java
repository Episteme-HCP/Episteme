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
package org.jscience.mathematics.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Tests for StatisticalTests.
 */
public class StatisticalTestsTest {

    private static final double DELTA = 1e-4;

    @Test
    public void testAnova() {
        // Group 1: 2, 3, 4 (mean 3)
        // Group 2: 6, 7, 8 (mean 7)
        // Grand mean = 5
        // SSB = 3 * (3-5)^2 + 3 * (7-5)^2 = 3*4 + 3*4 = 24
        // SSW = (2-3)^2 + (3-3)^2 + (4-3)^2 + (6-7)^2 + (7-7)^2 + (8-7)^2 = 1+0+1 +
        // 1+0+1 = 4
        // dfB = 1, dfW = 4
        // MSB = 24 / 1 = 24
        // MSW = 4 / 4 = 1
        // F = 24 / 1 = 24

        List<Real> g1 = Arrays.asList(Real.of(2), Real.of(3), Real.of(4));
        List<Real> g2 = Arrays.asList(Real.of(6), Real.of(7), Real.of(8));

        List<List<Real>> groups = new ArrayList<>();
        groups.add(g1);
        groups.add(g2);

        Real f = StatisticalTests.oneWayAnova(groups);
        assertEquals(24.0, f.doubleValue(), DELTA);
    }
}
