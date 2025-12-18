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
package org.jscience.mathematics.statistics;

import org.jscience.mathematics.numbers.real.Real;

import org.jscience.mathematics.statistics.distributions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for statistical distributions.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class DistributionsTest {

    @SuppressWarnings("unused")
    private static final Real TOLERANCE = Real.of(1e-6); // Reserved for future assertions

    @Test
    public void testNormalDistribution() {
        NormalDistribution normal = new NormalDistribution(Real.ZERO, Real.ONE);

        // Test mean
        assertEquals(0.0, normal.mean().doubleValue(), 1e-10);

        // Test variance
        assertEquals(1.0, normal.variance().doubleValue(), 1e-10);

        // Test density at mean (should be 1/sqrt(2π))
        Real densityAtMean = normal.density(Real.ZERO);
        double expected = 1.0 / Math.sqrt(2 * Math.PI);
        assertEquals(expected, densityAtMean.doubleValue(), 1e-6);
    }

    @Test
    public void testBinomialDistribution() {
        BinomialDistribution binomial = new BinomialDistribution(10, Real.of(0.5));

        // Test mean: n*p = 10*0.5 = 5
        assertEquals(5.0, binomial.mean().doubleValue(), 1e-10);

        // Test variance: n*p*(1-p) = 10*0.5*0.5 = 2.5
        assertEquals(2.5, binomial.variance().doubleValue(), 1e-10);
    }

    @Test
    public void testExponentialDistribution() {
        ExponentialDistribution exp = new ExponentialDistribution(Real.TWO);

        // Test mean: 1/λ = 1/2 = 0.5
        assertEquals(0.5, exp.mean().doubleValue(), 1e-10);

        // Test variance: 1/λ² = 1/4 = 0.25
        assertEquals(0.25, exp.variance().doubleValue(), 1e-10);
    }

    @Test
    public void testPoissonDistribution() {
        PoissonDistribution poisson = new PoissonDistribution(Real.of(3.0));

        // Test mean: λ = 3
        assertEquals(3.0, poisson.mean().doubleValue(), 1e-10);

        // Test variance: λ = 3
        assertEquals(3.0, poisson.variance().doubleValue(), 1e-10);
    }

    @Test
    public void testUniformDistribution() {
        UniformDistribution uniform = new UniformDistribution(Real.ZERO, Real.ONE);

        // Test mean: (a+b)/2 = 0.5
        assertEquals(0.5, uniform.mean().doubleValue(), 1e-10);

        // Test variance: (b-a)²/12 = 1/12
        assertEquals(1.0 / 12.0, uniform.variance().doubleValue(), 1e-10);
    }

    @Test
    public void testGammaDistribution() {
        GammaDistribution gamma = new GammaDistribution(Real.TWO, Real.TWO);

        // Test mean: α/β = 2/2 = 1
        assertEquals(1.0, gamma.mean().doubleValue(), 1e-10);

        // Test variance: α/β² = 2/4 = 0.5
        assertEquals(0.5, gamma.variance().doubleValue(), 1e-10);
    }

    @Test
    public void testChiSquareDistribution() {
        ChiSquareDistribution chiSq = new ChiSquareDistribution(5);

        // Test mean: k = 5
        assertEquals(5.0, chiSq.mean().doubleValue(), 1e-10);

        // Test variance: 2k = 10
        assertEquals(10.0, chiSq.variance().doubleValue(), 1e-10);
    }
}
