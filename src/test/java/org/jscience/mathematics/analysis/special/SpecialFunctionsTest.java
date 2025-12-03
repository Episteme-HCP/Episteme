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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.analysis.special;

import org.jscience.mathematics.number.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SpecialFunctions.
 */
public class SpecialFunctionsTest {

    private static final double TOLERANCE = 1e-8;

    @Test
    public void testLegendrePolynomials() {
        Real x = Real.of(0.5);

        // P0(x) = 1
        assertEquals(1.0, SpecialFunctions.legendre(0, x).doubleValue(), TOLERANCE);

        // P1(x) = x
        assertEquals(0.5, SpecialFunctions.legendre(1, x).doubleValue(), TOLERANCE);

        // P2(x) = 0.5 * (3x^2 - 1)
        // P2(0.5) = 0.5 * (3*0.25 - 1) = 0.5 * (0.75 - 1) = 0.5 * -0.25 = -0.125
        assertEquals(-0.125, SpecialFunctions.legendre(2, x).doubleValue(), TOLERANCE);

        // P3(x) = 0.5 * (5x^3 - 3x)
        // P3(0.5) = 0.5 * (5*0.125 - 1.5) = 0.5 * (0.625 - 1.5) = 0.5 * -0.875 =
        // -0.4375
        assertEquals(-0.4375, SpecialFunctions.legendre(3, x).doubleValue(), TOLERANCE);
    }

    @Test
    public void testHermitePolynomials() {
        Real x = Real.of(0.5);

        // H0(x) = 1
        assertEquals(1.0, SpecialFunctions.hermite(0, x).doubleValue(), TOLERANCE);

        // H1(x) = 2x
        assertEquals(1.0, SpecialFunctions.hermite(1, x).doubleValue(), TOLERANCE);

        // H2(x) = 4x^2 - 2
        // H2(0.5) = 4*0.25 - 2 = 1 - 2 = -1
        assertEquals(-1.0, SpecialFunctions.hermite(2, x).doubleValue(), TOLERANCE);

        // H3(x) = 8x^3 - 12x
        // H3(0.5) = 8*0.125 - 12*0.5 = 1 - 6 = -5
        assertEquals(-5.0, SpecialFunctions.hermite(3, x).doubleValue(), TOLERANCE);
    }

    @Test
    public void testLaguerrePolynomials() {
        Real x = Real.of(0.5);

        // L0(x) = 1
        assertEquals(1.0, SpecialFunctions.laguerre(0, x).doubleValue(), TOLERANCE);

        // L1(x) = 1 - x
        assertEquals(0.5, SpecialFunctions.laguerre(1, x).doubleValue(), TOLERANCE);

        // L2(x) = 0.5 * (x^2 - 4x + 2)
        // L2(0.5) = 0.5 * (0.25 - 2 + 2) = 0.125
        assertEquals(0.125, SpecialFunctions.laguerre(2, x).doubleValue(), TOLERANCE);

        // L3(x) = (1/6) * (-x^3 + 9x^2 - 18x + 6)
        // L3(0.5) = (1/6) * (-0.125 + 9*0.25 - 18*0.5 + 6)
        // = (1/6) * (-0.125 + 2.25 - 9 + 6)
        // = (1/6) * (-0.875) = -0.1458333333
        assertEquals(-0.1458333333, SpecialFunctions.laguerre(3, x).doubleValue(), 1e-8);
    }

    @Test
    public void testGamma() {
        // Gamma(n) = (n-1)!
        assertEquals(1.0, SpecialFunctions.gamma(Real.of(1)).doubleValue(), TOLERANCE);
        assertEquals(1.0, SpecialFunctions.gamma(Real.of(2)).doubleValue(), TOLERANCE);
        assertEquals(2.0, SpecialFunctions.gamma(Real.of(3)).doubleValue(), TOLERANCE);
        assertEquals(6.0, SpecialFunctions.gamma(Real.of(4)).doubleValue(), TOLERANCE);
        assertEquals(24.0, SpecialFunctions.gamma(Real.of(5)).doubleValue(), TOLERANCE);

        // Gamma(0.5) = sqrt(pi)
        assertEquals(Math.sqrt(Math.PI), SpecialFunctions.gamma(Real.of(0.5)).doubleValue(), 1e-5);
    }

    @Test
    public void testErf() {
        // erf(0) = 0
        assertEquals(0.0, SpecialFunctions.erf(Real.ZERO).doubleValue(), TOLERANCE);

        // erf(infinity) -> 1
        assertEquals(1.0, SpecialFunctions.erf(Real.of(10)).doubleValue(), 1e-5);

        // erf(-x) = -erf(x)
        Real x = Real.of(0.5);
        assertEquals(-SpecialFunctions.erf(x).doubleValue(), SpecialFunctions.erf(x.negate()).doubleValue(), TOLERANCE);
    }
}
