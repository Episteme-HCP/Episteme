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
 * This class models the triangle distribution on a specified interval. If
 * (X, Y) is uniformly distributed on a triangular region, then X and Y have
 * triangular distribuitons.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class TriangleDistribution extends Distribution implements Serializable {
    /** DOCUMENT ME! */
    public final static int UP = 0;

    /** DOCUMENT ME! */
    public final static int DOWN = 1;

    /** DOCUMENT ME! */
    private int orientation;

    /** DOCUMENT ME! */
    private double c;

    /** DOCUMENT ME! */
    private double lowerBound;

    /** DOCUMENT ME! */
    private double upperBound;

/**
     * This general constructor creates a new triangle distribution on a
     * specified interval and with a specified orientation.
     *
     * @param a the left endpoint of the domain
     * @param b the right endpoint of the domain param i the orientation (UP or
     *          DOWN)
     * @param i DOCUMENT ME!
     */
    public TriangleDistribution(double a, double b, int i) {
        setParameters(a, b, i);
    }

/**
     * This default constructor creates a new triangle distribution on the
     * interval (0, 1) with positive slope.
     */
    public TriangleDistribution() {
        this(0, 1, UP);
    }

    /**
     * This method sets the parameters: the minimum value, maximum
     * value, and orientation.
     *
     * @param a the left endpoint of the domain
     * @param b the right endpoint of the domain
     * @param i the orientation (UP or DOWN)
     */
    public void setParameters(double a, double b, int i) {
        if (a >= b) {
            b = a + 1;
        }

        if (i < 0) {
            i = 0;
        } else if (i > 1) {
            i = 1;
        }

        lowerBound = a;
        upperBound = b;
        orientation = i;

        double stepSize = (upperBound - lowerBound) / 100;
        setDomain(lowerBound, upperBound, stepSize, CONTINUOUS);

        //Compute normalizing constant
        c = (upperBound - lowerBound) * (upperBound - lowerBound);
    }

    /**
     * This method computes the density.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        if ((lowerBound <= x) & (x <= upperBound)) {
            if (orientation == UP) {
                return (2 * (x - lowerBound)) / c;
            } else {
                return (2 * (upperBound - x)) / c;
            }
        } else {
            return 0;
        }
    }

    /**
     * This method computes the maximum value of the density function.
     *
     * @return the maximum value of the probability density function
     */
    public double getMaxDensity() {
        double mode;

        if (orientation == UP) {
            mode = upperBound;
        } else {
            mode = lowerBound;
        }

        return getDensity(mode);
    }

    /**
     * This method computes the mean.
     *
     * @return the mean of the distribution
     */
    public double getMean() {
        if (orientation == UP) {
            return (lowerBound / 3) + ((2 * upperBound) / 3);
        } else {
            return ((2 * lowerBound) / 3) + (upperBound / 3);
        }
    }

    /**
     * This method computes the variance.
     *
     * @return the variance of the distribution
     */
    public double getVariance() {
        return ((upperBound - lowerBound) * (upperBound - lowerBound)) / 18;
    }

    /**
     * This method sets the lower bound
     *
     * @param a the lower bound
     */
    public void setLowerBound(double a) {
        setParameters(a, upperBound, orientation);
    }

    /**
     * This method returns the lower bound
     *
     * @return the left endpoint of the domain
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * This method sets the upper bound.
     *
     * @param b the upper bound
     */
    public void setUpperBound(double b) {
        setParameters(lowerBound, b, orientation);
    }

    /**
     * This method returns the upper bound.
     *
     * @return the right endpoint of the domain
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * This method sets the orientation.
     *
     * @param i the orientation (UP or DOWN)
     */
    public void setOrientation(int i) {
        setParameters(lowerBound, upperBound, i);
    }

    /**
     * This method returns the orientation.
     *
     * @return the orientation (UP or DOWN)
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * This method simulates a value from the distribution.
     *
     * @return a simulated value from the distribution
     */
    public double simulate() {
        double u = lowerBound + ((upperBound - lowerBound) * Math.random());
        double v = lowerBound + ((upperBound - lowerBound) * Math.random());

        if (orientation == UP) {
            return Math.max(u, v);
        } else {
            return Math.min(u, v);
        }
    }

    /**
     * This method computes the cumulative distribution function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the cumulative distribution at x
     */
    public double getCDF(double x) {
        if (orientation == UP) {
            return ((x - lowerBound) * (x - lowerBound)) / c;
        } else {
            return 1 - (((upperBound - x) * (upperBound - x)) / c);
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
        return "Triangle distribution [lower bound = " + lowerBound +
        ", upper bound = " + upperBound + "orientation = " + orientation + "]";
    }
}
