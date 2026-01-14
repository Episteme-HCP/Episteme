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
 * This class models the distribution of the maximum value of a symmetric
 * random walk on the interval [0, n].
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class WalkMaxDistribution extends Distribution implements Serializable {
    //Paramters
    /** DOCUMENT ME! */
    private int steps;

/**
     * This general constructor creates a new max walk distribution with a
     * specified time parameter.
     *
     * @param n the number of steps
     */
    public WalkMaxDistribution(int n) {
        setSteps(n);
    }

/**
     * This default constructor creates a new walk max distribution with time
     * parameter 10.
     */
    public WalkMaxDistribution() {
        this(10);
    }

    /**
     * This method sets the time parameter.
     *
     * @param n the number of steps
     */
    public void setSteps(int n) {
        if (n < 1) {
            n = 1;
        }

        steps = n;
        setDomain(0, steps, 1, DISCRETE);
    }

    /**
     * This method defines the density function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        int k = (int) Math.rint(x);
        int m;

        if (((k + steps) % 2) == 0) {
            m = (k + steps) / 2;
        } else {
            m = (k + steps + 1) / 2;
        }

        return StatisticsUtils.combinations(steps, m) / Math.pow(2, steps);
    }

    /**
     * This method returns the maximum value of the density function.
     *
     * @return the maximum value of the density function
     */
    public double getMaxDensity() {
        return getDensity(0);
    }

    /**
     * This method returns the time parameter.
     *
     * @return the number of steps
     */
    public double getSteps() {
        return steps;
    }

    /**
     * This method simulates a value from the distribution.
     *
     * @return a simulated value from the distribution
     */
    public double simulate() {
        int step;
        int max = 0;
        int position = 0;

        for (int i = 1; i <= steps; i++) {
            if (Math.random() < 0.5) {
                step = 1;
            } else {
                step = -1;
            }

            position = position + step;

            if (position > max) {
                max = position;
            }
        }

        return max;
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Walk max distribution [steps = " + steps + "]";
    }
}
