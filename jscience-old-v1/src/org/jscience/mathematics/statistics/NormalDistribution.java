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
 * This class encapsulates the normal distribution with specified
 * parameters.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class NormalDistribution extends Distribution implements Serializable {
    //Paramters
    /** DOCUMENT ME! */
    public final static double SQRT2PI = Math.sqrt(2 * Math.PI);

    /** DOCUMENT ME! */
    private double location;

    /** DOCUMENT ME! */
    private double scale;

    /** DOCUMENT ME! */
    private double c;

/**
     * This general constructor creates a new normal distribution with
     * specified parameter values.
     *
     * @param m the location parameter
     * @param s the scale parameter
     */
    public NormalDistribution(double m, double s) {
        setParameters(m, s);
    }

/**
     * This default constructor creates a new standard normal distribution
     * (with location parameter 0 and scale parameter 1).
     */
    public NormalDistribution() {
        this(0, 1);
    }

    /**
     * This method sets the parameters and defines the default domain.
     *
     * @param m the location parameter
     * @param s the scale parameter
     */
    public void setParameters(double m, double s) {
        double lower;
        double upper;
        double width;

        //Correct for invalid scale
        if (s < 0) {
            s = 1;
        }

        location = m;
        scale = s;
        c = SQRT2PI * scale;
        upper = location + (4 * scale);
        lower = location - (4 * scale);
        width = (upper - lower) / 100;
        setDomain(lower, upper, width, CONTINUOUS);
    }

    /**
     * This method defines the probability density function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        double z = (x - location) / scale;

        return Math.exp((-z * z) / 2) / c;
    }

    /**
     * This method returns the maximum value of the density function.
     *
     * @return the maximum value of the probability density function
     */
    public double getMaxDensity() {
        return getDensity(location);
    }

    /**
     * This method returns the median, which is the same as the
     * location parameter.
     *
     * @return the median
     */
    public double getMedian() {
        return location;
    }

    /**
     * This method returns the mean, which is the same as the location
     * parameter.
     *
     * @return the mean
     */
    public double getMean() {
        return location;
    }

    /**
     * This method returns the variance of the distribution.
     *
     * @return the variance
     */
    public double getVariance() {
        return scale * scale;
    }

    /**
     * This method computes the central moment of a specifed order.
     *
     * @param n the order
     *
     * @return the central moment of order n
     */
    public double getCentralMoment(int n) {
        if (n == (2 * (n / 2))) {
            return (StatisticsUtils.factorial(n) * Math.pow(scale, n)) / (StatisticsUtils.factorial(n / 2) * Math.pow(2,
                n / 2));
        } else {
            return 0;
        }
    }

    /**
     * This method computes the moment of a specified order about a
     * specified point.
     *
     * @param a the center
     * @param n the order
     *
     * @return the moment of order n about a
     */
    public double getMoment(double a, int n) {
        double sum = 0;

        for (int k = 0; k <= n; k++)
            sum = sum +
                (StatisticsUtils.combinations(n, k) * getCentralMoment(k) * Math.pow(location -
                    a, n - k));

        return sum;
    }

    /**
     * This method returns the moment generating function.
     *
     * @param t a real number
     *
     * @return the moment generating function at t
     */
    public double getMGF(double t) {
        return Math.exp((location * t) + ((scale * scale * t * t) / 2));
    }

    /**
     * This method simulates a value from the distribution.
     *
     * @return a simulated value from the distribution
     */
    public double simulate() {
        double r = Math.sqrt(-2 * Math.log(Math.random()));
        double theta = 2 * Math.PI * Math.random();

        return location + (scale * r * Math.cos(theta));
    }

    /**
     * This method returns the location parameter.
     *
     * @return the location parameter
     */
    public double getLocation() {
        return location;
    }

    /**
     * This method sets the location parameter.
     *
     * @param m the location parameter
     */
    public void setLocation(double m) {
        setParameters(m, scale);
    }

    /**
     * This method gets the scale parameter.
     *
     * @return the scale parameter
     */
    public double getScale() {
        return scale;
    }

    /**
     * This method sets the scale parameter.
     *
     * @param s the scale parameter
     */
    public void setScale(double s) {
        setParameters(location, s);
    }

    /**
     * This method computes the cumulative distribution function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the cumulative probability at x
     */
    public double getCDF(double x) {
        double z = (x - location) / scale;

        if (z >= 0) {
            return 0.5 + (0.5 * StatisticsUtils.gammaCDF((z * z) / 2, 0.5));
        } else {
            return 0.5 - (0.5 * StatisticsUtils.gammaCDF((z * z) / 2, 0.5));
        }
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Normal distribution [location = " + location + ", scale = " +
        scale + "]";
    }
}
