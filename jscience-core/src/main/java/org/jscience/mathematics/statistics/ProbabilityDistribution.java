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
import org.jscience.mathematics.analysis.ContinuousFunction;

/**
 * A probability distribution P(X Ã¢â€°Â¤ x).
 * This is a mathematical function from Ã¢â€žÂ Ã¢â€ â€™ [0, 1].
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ProbabilityDistribution extends ContinuousFunction<Real, Real> {

    /**
     * Probability density function (PDF) or probability mass function (PMF).
     * f(x) = dP/dx for continuous, P(X = x) for discrete.
     * 
     * @param x the value to evaluate density at
     * @return the probability density or mass
     */
    Real density(Real x);

    /**
     * Cumulative distribution function (CDF).
     * F(x) = P(X Ã¢â€°Â¤ x)
     * 
     * @param x the value to evaluate CDF at
     * @return the cumulative probability
     */
    Real cdf(Real x);

    /**
     * Quantile function (inverse CDF).
     * Q(p) = FÃ¢ÂÂ»Ã‚Â¹(p)
     * 
     * @param p the cumulative probability (0 to 1)
     * @return the value x such that P(X Ã¢â€°Â¤ x) = p
     */
    Real quantile(Real p);

    /**
     * Generate random sample from this distribution.
     * 
     * @return a random sample
     */
    Real sample();

    /**
     * Returns the mean (expected value) of this distribution.
     * 
     * @return the mean
     */
    Real mean();

    /**
     * Returns the variance of this distribution.
     * 
     * @return the variance
     */
    Real variance();

    /**
     * Returns the standard deviation of this distribution.
     * 
     * @return the standard deviation
     */
    default Real standardDeviation() {
        return Real.of(Math.sqrt(variance().doubleValue()));
    }

    /**
     * Function interface - delegates to CDF.
     */
    @Override
    default Real evaluate(Real x) {
        return cdf(x);
    }

    /**
     * Is this a discrete or continuous distribution?
     * 
     * @return true if discrete, false if continuous
     */
    boolean isDiscrete();
}

