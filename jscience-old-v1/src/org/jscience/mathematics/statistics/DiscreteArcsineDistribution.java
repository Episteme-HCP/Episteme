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
 * This class models the discrete arcsine distribution. The distribution
 * governs the last zero in a symmetric random walk on an interval.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class DiscreteArcsineDistribution extends Distribution
    implements Serializable {
    //Paramters
    /** DOCUMENT ME! */
    private int steps;

/**
     * This general constructor creates a new discrete arcsine distribution
     * with a specified number of steps.
     *
     * @param n the number of steps
     */
    public DiscreteArcsineDistribution(int n) {
        setSteps(n);
    }

/**
     * This default constructor creates a new discrete arcsine distribution
     * with 10 steps.
     */
    public DiscreteArcsineDistribution() {
        this(10);
    }

    /**
     * This method sets the steps, the number of steps, and then
     * defines the default domain.
     *
     * @param n the number of steps
     */
    public void setSteps(int n) {
        steps = n;
        setDomain(0, steps, 2, DISCRETE);
    }

    /**
     * This method computes the probability density function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        int k = (int) x;

        return (StatisticsUtils.combinations(k, k / 2) * StatisticsUtils.combinations(steps -
            k, (steps - k) / 2)) / Math.pow(2, steps);
    }

    /**
     * This method computes the maximum value of the density function.
     * The maximum value is the value at 0, one of the modes of the
     * distribution.
     *
     * @return the maximum value of the probabiltiy density function
     */
    public double getMaxDensity() {
        return getDensity(0);
    }

    /**
     * This method gets the steps, the number of steps.
     *
     * @return the number of setps
     */
    public int getSteps() {
        return steps;
    }

    /**
     * This method computes the mean of the distribution.
     *
     * @return the mean
     */
    public double getMean() {
        return steps / 2;
    }

    /**
     * This method simulates a value from the distribution, by
     * simulating a random walk on the interval and computing the time of the
     * last zero.
     *
     * @return a simulated value from the distribution
     */
    public double simulate() {
        int step;
        int lastZero = 0;
        int position = 0;

        for (int i = 1; i <= steps; i++) {
            if (Math.random() < 0.5) {
                step = 1;
            } else {
                step = -1;
            }

            position = position + step;

            if (position == 0) {
                lastZero = i;
            }
        }

        return lastZero;
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Discrete arcsine distribution [steps = " + steps + "]";
    }
}
