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
 * This class models the distribution of the number of matches in a random
 * permutation.  A match occurs whenever an element is in its natural order.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class MatchDistribution extends Distribution implements Serializable {
    /** DOCUMENT ME! */
    private int parameter;

/**
     * This general constructor creates a new matching distribution with a
     * specified parameter.
     *
     * @param n the number of elements
     */
    public MatchDistribution(int n) {
        setParameter(n);
    }

/**
     * This default constructor creates a new mathcing distribuiton with
     * parameter 5.
     */
    public MatchDistribution() {
        this(5);
    }

    /**
     * This method sets the parameter of the distribution.
     *
     * @param n the size of the random permutation
     */
    public void setParameter(int n) {
        if (n < 1) {
            n = 1;
        }

        parameter = n;
        setDomain(0, parameter, 1, DISCRETE);
    }

    /**
     * This method computes the probability density function.
     *
     * @param x a number in the domain {0, 1, 2, ...}
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        int k = (int) Math.rint(x);
        double sum = 0;
        int sign = -1;

        for (int j = 0; j <= (parameter - k); j++) {
            sign = -sign;
            sum = sum + (sign / StatisticsUtils.factorial(j));
        }

        return sum / StatisticsUtils.factorial(k);
    }

    /**
     * This method gives the maximum value of the probability density
     * function.
     *
     * @return the maximum value of the probability density function
     */
    public double getMaxDensity() {
        if (parameter == 2) {
            return getDensity(0);
        } else {
            return getDensity(1);
        }
    }

    /**
     * This method returns the mean, which is 1, regardless of the
     * parameter value.
     *
     * @return the mean of the distribution
     */
    public double getMean() {
        return 1;
    }

    /**
     * This method returns the variance, which is 1 regardless of the
     * parameter value.
     *
     * @return the variance of the distribution
     */
    public double getVariance() {
        return 1;
    }

    /**
     * This method gets the parameter.
     *
     * @return the size of the random permutation
     */
    public int getParameter() {
        return parameter;
    }

    /**
     * This method simulates a value from the distribution, by
     * generating a random permutation and computing the number of matches.
     *
     * @return DOCUMENT ME!
     */
    public double simulate() {
        int[] p = StatisticsUtils.getSample(parameter, parameter,
                StatisticsUtils.WITHOUT_REPLACEMENT);
        int matches = 0;

        for (int i = 0; i < parameter; i++)
            if (p[i] == (i + 1)) {
                matches++;
            }

        return matches;
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Matching distribution [parameter = " + parameter + "]";
    }
}
