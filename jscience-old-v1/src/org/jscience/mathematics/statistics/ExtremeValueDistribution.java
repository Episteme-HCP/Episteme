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

import org.jscience.mathematics.MathConstants;

import java.io.Serializable;


/**
 * This class models the exponential-type extreme value distribution.  The
 * version for maximums or the version for minimums can be specified.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version May, 2004
 */
public class ExtremeValueDistribution extends Distribution
    implements Serializable {
    //Constants
    /** DOCUMENT ME! */
    public final static int MAXIMUM = 0;

    //Constants
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    public final static int MINIMUM = 1;

    //Variables
    /** DOCUMENT ME! */
    private double order;

/**
     * This general constructor creates a new ExtremeValueDistribution of a
     * specified type.
     *
     * @param k the type (maximum or minimum)
     */
    public ExtremeValueDistribution(int k) {
        setOrder(k);
    }

/**
     * This default constructor creates a new ExtremeValueDistribution of type
     * MAXIMUM.
     */
    public ExtremeValueDistribution() {
        this(0);
    }

    /**
     * This method defines the type and the default domian..
     *
     * @param k the type parameter (maximum or minimum)
     */
    public void setOrder(int k) {
        double upper;
        double lower;
        double width;

        if (k != 1) {
            k = 0;
        }

        //Define interval
        lower = Math.ceil(getMean() - (4 * getSD()));
        upper = Math.ceil(getMean() + (4 * getSD()));
        width = (upper - lower) / 100;
        setDomain(lower, upper, width, CONTINUOUS);
    }

    /**
     * This method computes the denstiy function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        if (order == MINIMUM) {
            return Math.exp(x) * Math.exp(-Math.exp(x));
        } else {
            return Math.exp(-x) * Math.exp(-Math.exp(-x));
        }
    }

    /**
     * This method returns the maximum value of the density function.
     *
     * @return the maximum value of the probability density function
     */
    public double getMaxDensity() {
        return getDensity(0);
    }

    /**
     * The method returns the mean of the distribution.
     *
     * @return the mean
     */
    public double getMean() {
        if (order == MAXIMUM) {
            return MathConstants.GAMMA;
        } else {
            return -MathConstants.GAMMA;
        }
    }

    /**
     * This method returns the variance of the distribution.
     *
     * @return the variance
     */
    public double getVariance() {
        return Math.pow(Math.PI, 2) / 6;
    }

    /**
     * This method computes the cumulative distribution function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the cumulative distsribution at x.
     */
    public double getCDF(double x) {
        if (order == MAXIMUM) {
            return Math.exp(-Math.exp(-x));
        } else {
            return 1 - Math.exp(-Math.exp(x));
        }
    }

    /**
     * This method returns the quantile function
     *
     * @param p a number in (0, 1)
     *
     * @return the quantile of order p
     */
    public double getQuantile(double p) {
        if (order == MAXIMUM) {
            return -Math.log(-Math.log(p));
        } else {
            return Math.log(-Math.log(1 - p));
        }
    }

    /**
     * This method returns the type parameter.
     *
     * @return the type parameter (maximum or minimum)
     */
    public double getOrder() {
        return order;
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the value of the type parameter.
     *
     * @return a string giving the name of the distribution and the value of
     *         the type parameter
     */
    public String toString() {
        return "Extreme value distribution [type = " + order + "]";
    }
}
