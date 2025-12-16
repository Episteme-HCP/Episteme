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

/**
 * Abstract base class for discrete probability distributions.
 * <p>
 * Uses {@link Real} for all calculations.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public abstract class DiscreteDistribution implements ProbabilityDistribution {

    @Override
    public boolean isDiscrete() {
        return true;
    }

    @Override
    public Real cdf(Real x) {
        // Sum probabilities from support minimum to x
        Real sum = Real.ZERO;
        int k = 0;
        while (k <= x.intValue()) {
            sum = sum.add(density(Real.of(k)));
            k++;
        }
        return sum;
    }

    @Override
    public Real quantile(Real probability) {
        if (probability.compareTo(Real.ZERO) < 0 || probability.compareTo(Real.ONE) > 0) {
            throw new IllegalArgumentException("Probability must be in [0,1]");
        }

        int k = 0;
        Real cumulativeProb = Real.ZERO;
        while (cumulativeProb.compareTo(probability) < 0) {
            cumulativeProb = cumulativeProb.add(density(Real.of(k)));
            k++;
        }
        return Real.of(k - 1);
    }

    @Override
    public Real sample() {
        return quantile(Real.of(Math.random()));
    }
}

