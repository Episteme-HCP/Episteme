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
package org.jscience.mathematics.statistics.distributions;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.statistics.ContinuousDistribution;

/**
 * Weibull distribution.
 * PDF: f(x) = (k/λ)(x/λ)^(k-1) * exp(-(x/λ)^k) for x ≥ 0
 */
public class WeibullDistribution extends ContinuousDistribution {
    private final Real shape; // k
    private final Real scale; // λ

    public WeibullDistribution(Real shape, Real scale) {
        if (shape.compareTo(Real.ZERO) <= 0 || scale.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Shape and scale must be positive");
        }
        this.shape = shape;
        this.scale = scale;
    }

    @Override
    public Real density(Real x) {
        if (x.compareTo(Real.ZERO) < 0)
            return Real.ZERO;

        double k = shape.doubleValue();
        double lambda = scale.doubleValue();
        double xVal = x.doubleValue();

        double result = (k / lambda) * Math.pow(xVal / lambda, k - 1) *
                Math.exp(-Math.pow(xVal / lambda, k));
        return Real.of(result);
    }

    @Override
    public Real mean() {
        return scale.multiply(Real.of(gamma(1 + 1.0 / shape.doubleValue())));
    }

    @Override
    public Real variance() {
        double k = shape.doubleValue();
        double lambda = scale.doubleValue();
        double g1 = gamma(1 + 2.0 / k);
        double g2 = gamma(1 + 1.0 / k);
        return Real.of(lambda * lambda * (g1 - g2 * g2));
    }

    private double gamma(double x) {
        // Stirling approx
        return Math.exp((x - 0.5) * Math.log(x) - x + 0.5 * Math.log(2 * Math.PI));
    }

    @Override
    public String toString() {
        return String.format("Weibull(k=%.4f, λ=%.4f)", shape.doubleValue(), scale.doubleValue());
    }
}


