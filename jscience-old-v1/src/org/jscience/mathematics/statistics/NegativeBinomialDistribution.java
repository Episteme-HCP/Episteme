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
 * This class models the negative binomial distribution with specified
 * successes parameter and probability parameter.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class NegativeBinomialDistribution extends Distribution
    implements Serializable {
    //Paramters
    /** DOCUMENT ME! */
    private int successes;

    /** DOCUMENT ME! */
    private double probability;

/**
     * This general constructor creates a new negative binomial distribution
     * with given parameter values.
     *
     * @param k the number of successes
     * @param p the probability of success
     */
    public NegativeBinomialDistribution(int k, double p) {
        setParameters(k, p);
    }

/**
     * This default constructor creates a new negative binomial distribution
     * with successes parameter 1 and probability parameter 0.5.
     */
    public NegativeBinomialDistribution() {
        this(1, 0.5);
    }

    /**
     * This method set the paramters and the default domain.
     *
     * @param k the number of successes
     * @param p the probability of success
     */
    public void setParameters(int k, double p) {
        //Correct for invalid parameters
        if (k < 1) {
            k = 1;
        }

        if (p <= 0) {
            p = 0.05;
        }

        if (p > 1) {
            p = 1;
        }

        //Assign parameters
        successes = k;
        probability = p;

        //Set truncated values
        setDomain(successes, Math.ceil(getMean() + (4 * getSD())), 1, DISCRETE);
    }

    /**
     * This method set the successes parameter.
     *
     * @param k the number of successes
     */
    public void setSuccesses(int k) {
        setParameters(k, probability);
    }

    /**
     * This method returns the successes parameter.
     *
     * @return the number of successes
     */
    public int getSuccesses() {
        return successes;
    }

    /**
     * This method returns the probability parameter.
     *
     * @return the probability of success
     */
    public double getProbability() {
        return probability;
    }

    /**
     * This method sets the probability parameter.
     *
     * @param p the probability of success
     */
    public void setProbability(double p) {
        setParameters(successes, p);
    }

    /**
     * This method computes the probability density function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        int n = (int) Math.rint(x);

        if (n < successes) {
            return 0;
        } else {
            return StatisticsUtils.combinations(n - 1, successes - 1) * Math.pow(probability,
                successes) * Math.pow(1 - probability, n - successes);
        }
    }

    /**
     * This method computes the maximum value of the density function.
     *
     * @return the maximum value of the probability density function
     */
    public double getMaxDensity() {
        double mode = Math.floor(((successes - 1) / probability) + 1);

        return getDensity(mode);
    }

    /**
     * This method computes the mean of the distribution.
     *
     * @return the mean
     */
    public double getMean() {
        return successes / probability;
    }

    /**
     * This method computes the variance of the distribution.
     *
     * @return the variance
     */
    public double getVariance() {
        return (successes * (1 - probability)) / (probability * probability);
    }

    /**
     * This method computes the probability generating function.
     *
     * @param t a real number
     *
     * @return the probability generating function at t
     */
    public double getPGF(double t) {
        double r = 1 / (1 - probability);

        if (Math.abs(t) < r) {
            return Math.pow((t * probability) / (1 - t + (t * probability)),
                successes);
        } else if (t >= r) {
            return Double.POSITIVE_INFINITY;
        } else {
            return Double.NaN;
        }
    }

    /**
     * This method computes moment generating function.
     *
     * @param t a real number
     *
     * @return the moment generating function
     */
    public double getMGF(double t) {
        return getPGF(Math.exp(t));
    }

    /**
     * This method simulates a value from the distribution, overriding
     * the correspondin default method in Distribution.
     *
     * @return a simulated value from the distribution
     */
    public double simulate() {
        int count = 0;
        int trials = 0;

        while (count <= successes) {
            if (Math.random() < probability) {
                count++;
            }

            trials++;
        }

        return trials;
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Negative binomial distribution [successes = " + successes +
        ", probability = " + probability + "]";
    }
}
