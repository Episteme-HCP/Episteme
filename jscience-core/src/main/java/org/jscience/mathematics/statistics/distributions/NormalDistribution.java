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

package org.jscience.mathematics.statistics.distributions;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.statistics.ContinuousDistribution;

/**
 * Normal (Gaussian) distribution N(ÃŽÂ¼, ÃÆ’Ã‚Â²).
 * <p>
 * PDF: f(x) = (1/(ÃÆ’Ã¢Ë†Å¡(2Ãâ‚¬))) exp(-(x-ÃŽÂ¼)Ã‚Â²/(2ÃÆ’Ã‚Â²))
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NormalDistribution extends ContinuousDistribution {

    private final Real mean;
    private final Real stdDev;
    private final Real variance;
    private static final Real SQRT_2PI = Real.of(Math.sqrt(2 * Math.PI));

    public NormalDistribution() {
        this(Real.ZERO, Real.ONE);
    }

    public NormalDistribution(Real mean, Real stdDev) {
        if (stdDev.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Standard deviation must be positive");
        }
        this.mean = mean;
        this.stdDev = stdDev;
        this.variance = stdDev.multiply(stdDev);
    }

    @Override
    public Real density(Real x) {
        Real diff = x.subtract(mean);
        Real exponent = diff.multiply(diff).divide(variance.multiply(Real.TWO)).negate();
        Real coefficient = Real.ONE.divide(stdDev.multiply(SQRT_2PI));
        return coefficient.multiply(Real.of(Math.exp(exponent.doubleValue())));
    }

    @Override
    public Real mean() {
        return mean;
    }

    @Override
    public Real variance() {
        return variance;
    }

    public Real getStdDev() {
        return stdDev;
    }

    @Override
    public String toString() {
        return String.format("N(ÃŽÂ¼=%.4f, ÃÆ’=%.4f)", mean.doubleValue(), stdDev.doubleValue());
    }
}

