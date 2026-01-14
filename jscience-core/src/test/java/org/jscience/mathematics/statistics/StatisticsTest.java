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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.mathematics.statistics.distributions.NormalDistribution;
import org.jscience.mathematics.statistics.distributions.UniformDistribution;

public class StatisticsTest {

    @Test
    public void testNormalDistribution() {
        NormalDistribution normal = new NormalDistribution(Real.ZERO, Real.ONE);

        assertEquals(0.0, normal.mean().doubleValue(), 1e-10);
        assertEquals(1.0, normal.getStdDev().doubleValue(), 1e-10);

        // PDF at mean should be maximum (Ã¢â€°Ë†0.3989)
        Real pdfAtMean = normal.density(Real.ZERO);
        assertTrue(pdfAtMean.doubleValue() > 0.39 && pdfAtMean.doubleValue() < 0.40);

        // CDF at mean should be 0.5
        Real cdfAtMean = normal.cdf(Real.ZERO);
        assertEquals(0.5, cdfAtMean.doubleValue(), 0.01);
    }

    @Test
    public void testUniformDistribution() {
        UniformDistribution uniform = new UniformDistribution(Real.ZERO, Real.ONE);

        assertEquals(0.5, uniform.mean().doubleValue(), 1e-10);

        // PDF should be constant 1.0 in [0,1]
        assertEquals(1.0, uniform.density(Real.of(0.5)).doubleValue(), 1e-10);
        assertEquals(0.0, uniform.density(Real.of(1.5)).doubleValue(), 1e-10); // Outside range

        // CDF
        assertEquals(0.0, uniform.cdf(Real.of(-0.5)).doubleValue(), 1e-10);
        assertEquals(0.5, uniform.cdf(Real.of(0.5)).doubleValue(), 1e-10);
        assertEquals(1.0, uniform.cdf(Real.of(1.5)).doubleValue(), 1e-10);

        // Sampling
        Real sample = uniform.sample();
        assertTrue(sample.compareTo(Real.ZERO) >= 0 && sample.compareTo(Real.ONE) <= 0);
    }
}


