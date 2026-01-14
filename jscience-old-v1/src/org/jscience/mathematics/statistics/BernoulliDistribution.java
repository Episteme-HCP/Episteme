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
 * This class models the Bernoulli distribution with a specified parameter.
 * This is the distribution of an indicator random variable (taking values 0
 * and 1).
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version June 2002
 */
public class BernoulliDistribution extends BinomialDistribution
    implements Serializable {
/**
     * This general constructor creates a new Bernoulli distribution with a
     * specified parameter.
     *
     * @param p the probaiblity of 1
     */
    public BernoulliDistribution(double p) {
        super(1, p);
    }

/**
     * This default constructor creates a new Bernoulli distribution with
     * parameter p = 0.5.
     */
    public BernoulliDistribution() {
        this(0.5);
    }

    /**
     * This method returns the maximum value of the probability density
     * function.
     *
     * @return the maximum value of the probability density function
     */
    public double getMaxDensity() {
        return 1;
    }

    /**
     * This method sets the number of trials, which is always 1.
     *
     * @param n the number of trials.
     */
    public void setTrials(int n) {
        super.setTrials(1);
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Bernouli distribution [probability = " + getProbability() +
        "]";
    }
}
