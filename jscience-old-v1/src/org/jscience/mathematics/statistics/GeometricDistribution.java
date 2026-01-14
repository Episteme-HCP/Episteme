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

import java.io.Serializable;


/**
 * This class models the geometric distribution with a given success
 * probability. This distribution models the trial number of the first success
 * in a sequence of Bernoulli trials.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class GeometricDistribution extends NegativeBinomialDistribution
    implements Serializable {
/**
     * This general constructor creates a new geometric distribution with a
     * specified success probabilitiy
     *
     * @param p the success probability
     */
    public GeometricDistribution(double p) {
        super(1, p);
    }

/**
     * This default constructor creates a new geometric distribution with
     * parameter 0.5
     */
    public GeometricDistribution() {
        this(0.5);
    }

    /**
     * This method computes the factorial moment of a specified order.
     *
     * @param k the order
     *
     * @return the factorial moment of order k
     */
    public double getFactorialMoment(int k) {
        double p = getProbability();

        return (StatisticsUtils.factorial(k) * Math.pow(1 - p, k - 1)) / Math.pow(p,
            k);
    }

    /**
     * This method ensures that the number of successes is set at 1.
     *
     * @param k the number of successes
     */
    public void setSuccesses(int k) {
        super.setSuccesses(1);
    }

    /**
     * This method sets the negative binomial parameters, and ensures
     * that the number of successes is set at 1.
     *
     * @param k the number of successes
     * @param p the probability of success
     */
    public void setParameters(int k, double p) {
        super.setParameters(1, p);
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Geometric distribution [probability = " + getProbability() +
        "]";
    }
}
