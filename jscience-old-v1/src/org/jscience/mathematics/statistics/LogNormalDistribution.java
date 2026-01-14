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
 * This class models the lognormal distribution with specified parameters.
 * This is the distribution of Y = exp(X) where X has a normal distribution
 * with the parameters.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class LogNormalDistribution extends Distribution implements Serializable {
    //variables
    /** DOCUMENT ME! */
    public final static double C = Math.sqrt(2 * Math.PI);

    /** DOCUMENT ME! */
    private double location;

    /** DOCUMENT ME! */
    private double scale;

/**
     * This general constructor creates a new lognormal distribution with
     * specified parameters.
     *
     * @param m the location parameter of ln(Y)
     * @param s the scale parameter of ln(Y)
     */
    public LogNormalDistribution(double m, double s) {
        setParameters(m, s);
    }

/**
     * This default constructor creates the standard lognormal distribution.
     */
    public LogNormalDistribution() {
        this(0, 1);
    }

    /**
     * This method sets the parameters, computes the default domain.
     *
     * @param m the location parameter of ln(Y)
     * @param s the scale parameter of ln(Y)
     */
    public void setParameters(double m, double s) {
        if (s <= 0) {
            s = 1;
        }

        location = m;
        scale = s;

        double upper = getMean() + (3 * getSD());
        setDomain(0, upper, 0.01 * upper, CONTINUOUS);
    }

    /**
     * This method computes the probability density function.
     *
     * @param x a number in the domain of the distribution [0, &infin;)
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        double z = (Math.log(x) - location) / scale;

        return Math.exp((-z * z) / 2) / (x * C * scale);
    }

    /**
     * This method computes the maximum value of the density function.
     *
     * @return the maximum value of the probability density function
     */
    public double getMaxDensity() {
        double mode = Math.exp(location - (scale * scale));

        return getDensity(mode);
    }

    /**
     * This method computes the mean of the distribution.
     *
     * @return the mean
     */
    public double getMean() {
        return Math.exp(location + ((scale * scale) / 2));
    }

    /**
     * This method computes the variance of the distribution.
     *
     * @return the variance
     */
    public double getVariance() {
        double a = location + (scale * scale);

        return Math.exp(2 * a) - Math.exp(location + a);
    }

    /**
     * This method computes the moment of a specified order.
     *
     * @param n the order
     *
     * @return the moment of order n about 0
     */
    public double getMoment(int n) {
        return Math.exp((n * location) + ((n * n * scale * scale) / 2));
    }

    /**
     * This method returns the moment of a specified order about a
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
                (StatisticsUtils.combinations(n, k) * getMoment(k) * Math.pow(-a,
                    n - k));

        return sum;
    }

    /**
     * This method computes the moment generating function.
     *
     * @param t a real number
     *
     * @return the moment genrating function at t
     */
    public double getMGF(double t) {
        if (t == 0) {
            return 1;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * This method simulates a value from the distribution.
     *
     * @return a simulated value from the distribuiton
     */
    public double simulate() {
        double r = Math.sqrt(-2 * Math.log(Math.random()));
        double theta = 2 * Math.PI * Math.random();

        return Math.exp(location + (scale * r * Math.cos(theta)));
    }

    /**
     * This method returns the location parameter of ln(Y).
     *
     * @return the location parameter
     */
    public double getLocation() {
        return location;
    }

    /**
     * This method sets the location parameter of ln(Y).
     *
     * @param m the location parameter
     */
    public void setLocation(double m) {
        setParameters(m, scale);
    }

    /**
     * This method gets the scale parameter of ln(Y).
     *
     * @return the scale parameter
     */
    public double getScale() {
        return scale;
    }

    /**
     * This method sets the scale parameter of ln(Y).
     *
     * @param s the scale parameter
     */
    public void setScale(double s) {
        setParameters(location, s);
    }

    /**
     * This method computes the cumulative distribution function.
     *
     * @param x a value in the domain of the distribution
     *
     * @return the cumulative probability at x
     */
    public double getCDF(double x) {
        double z = (Math.log(x) - location) / scale;

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
        return "Log normal distribution [location = " + location +
        ", scale = " + scale + "]";
    }
}
