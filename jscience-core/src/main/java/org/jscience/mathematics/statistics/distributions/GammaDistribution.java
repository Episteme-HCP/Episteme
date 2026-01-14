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

package org.jscience.mathematics.statistics.distributions;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.statistics.ContinuousDistribution;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GammaDistribution extends ContinuousDistribution {
    private final Real shape, rate;

    public GammaDistribution(Real shape, Real rate) {
        if (shape.compareTo(Real.ZERO) <= 0 || rate.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Shape and rate must be positive");
        }
        this.shape = shape;
        this.rate = rate;

    }

    private double lgamma(double x) {
        return (x - 0.5) * Math.log(x) - x + 0.5 * Math.log(2 * Math.PI);
    }

    @Override
    public Real density(Real x) {
        if (x.compareTo(Real.ZERO) <= 0)
            return Real.ZERO;
        double alpha = shape.doubleValue();
        double beta = rate.doubleValue();
        double xVal = x.doubleValue();
        double logPdf = alpha * Math.log(beta) - lgamma(alpha) +
                (alpha - 1) * Math.log(xVal) - beta * xVal;
        return Real.of(Math.exp(logPdf));
    }

    @Override
    public Real mean() {
        return shape.divide(rate);
    }

    @Override
    public Real variance() {
        return shape.divide(rate.multiply(rate));
    }

    @Override
    public String toString() {
        return String.format("Gamma(ÃŽÂ±=%.4f, ÃŽÂ²=%.4f)", shape.doubleValue(), rate.doubleValue());
    }
}

