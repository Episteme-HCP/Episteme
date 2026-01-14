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

package org.jscience.mathematics.statistics;

import org.jscience.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DistributionsTest {

    @Test
    public void testNormalPdf() {
        Real x = Real.of(0.0);
        Real mean = Real.of(0.0);
        Real stdDev = Real.of(1.0);

        // 1/sqrt(2pi) approx 0.39894
        Real pdf = Distributions.normalPdf(x, mean, stdDev);
        assertEquals(0.39894228, pdf.doubleValue(), 1e-6);
    }

    @Test
    public void testStandardNormalCdf() {
        // CDF(0) should be 0.5
        assertEquals(0.5, Distributions.standardNormalCdf(Real.ZERO).doubleValue(), 1e-9);

        // CDF(1.96) approx 0.975
        assertEquals(0.975, Distributions.standardNormalCdf(Real.of(1.96)).doubleValue(), 1e-3);
    }

    @Test
    public void testPoissonPmf() {
        Real lambda = Real.of(3.0);
        // P(X=2) = 3^2 * e^-3 / 2! = 9 * 0.049787 / 2 = 0.22404
        assertEquals(0.2240418, Distributions.poissonPmf(2, lambda).doubleValue(), 1e-5);
    }

    @Test
    public void testExponentialCdf() {
        Real lambda = Real.of(0.5);
        Real x = Real.of(2.0);
        // F(2) = 1 - e^(-0.5 * 2) = 1 - e^-1 = 1 - 0.367879 = 0.63212
        assertEquals(0.63212056, Distributions.exponentialCdf(x, lambda).doubleValue(), 1e-5);
    }

    @Test
    public void testBinomialCoefficient() {
        // C(5, 2) = 10
        assertEquals(10.0, Distributions.binomialCoefficient(5, 2).doubleValue(), 1e-9);
        // C(10, 0) = 1
        assertEquals(1.0, Distributions.binomialCoefficient(10, 0).doubleValue(), 1e-9);
        // C(10, 10) = 1
        assertEquals(1.0, Distributions.binomialCoefficient(10, 10).doubleValue(), 1e-9);
    }
}

