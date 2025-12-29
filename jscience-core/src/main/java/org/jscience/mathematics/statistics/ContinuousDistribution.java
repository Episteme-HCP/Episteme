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

package org.jscience.mathematics.statistics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Abstract base class for continuous probability distributions.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class ContinuousDistribution implements ProbabilityDistribution {

    @Override
    public boolean isDiscrete() {
        return false;
    }

    @Override
    public Real cdf(Real x) {
        // Numerical integration of PDF from -infinity to x
        // We approximate -infinity as mean - 20 * stdDev
        Real mean = mean();
        Real stdDev = standardDeviation();
        Real lowerBound = mean.subtract(stdDev.multiply(Real.of(20)));

        // If x is below our effective lower bound, return 0
        if (x.compareTo(lowerBound) <= 0) {
            return Real.ZERO;
        }

        // Use Trapezoidal rule
        int steps = 1000;
        Real h = x.subtract(lowerBound).divide(Real.of(steps));
        Real sum = density(lowerBound).divide(Real.TWO);

        for (int i = 1; i < steps; i++) {
            Real xi = lowerBound.add(h.multiply(Real.of(i)));
            sum = sum.add(density(xi));
        }

        sum = sum.add(density(x).divide(Real.TWO));
        return sum.multiply(h);
    }

    @Override
    public Real quantile(Real p) {
        // Simple bisection search for inverse CDF
        if (p.compareTo(Real.ZERO) < 0 || p.compareTo(Real.ONE) > 0) {
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        }

        Real mean = mean();
        Real stdDev = standardDeviation();
        Real min = mean.subtract(stdDev.multiply(Real.of(10)));
        Real max = mean.add(stdDev.multiply(Real.of(10)));
        Real tolerance = Real.of(1e-9);

        return inverseCdfBisection(p, min, max, tolerance);
    }

    private Real inverseCdfBisection(Real p, Real min, Real max, Real tol) {
        Real mid = min.add(max).divide(Real.TWO);
        Real val = cdf(mid);

        if (val.subtract(p).abs().compareTo(tol) < 0)
            return mid;
        if (max.subtract(min).compareTo(tol) < 0)
            return mid;

        if (val.compareTo(p) < 0)
            return inverseCdfBisection(p, mid, max, tol);
        else
            return inverseCdfBisection(p, min, mid, tol);
    }

    @Override
    public Real sample() {
        return quantile(Real.of(Math.random()));
    }
}