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
 * This class defines a partition of an interval into subintervals of equal
 * width. These objects are used to define default domains.  A finite domain
 * can be modeled by the values (midpoints) of the partition.  The boundary
 * points are a + iw for i = 0..., n, where n is the size of the partition, a
 * is the lower bound and w the width. The values (midpoints) are a + (i +
 * 1/2)w, for i = 0..., n &minus; 1. The type variable indicates whether the
 * upper and lower bounds are specified (which is usally the case for
 * continuous domains) or whether the upper and lower values are specified
 * (which is usually the case for discrete domains).
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class DistributionDomain implements Serializable {
    /** DOCUMENT ME! */
    public final static int DISCRETE = 0;

    /** DOCUMENT ME! */
    public final static int CONTINUOUS = 1;

    //Variables
    /** DOCUMENT ME! */
    private double lowerBound;

    //Variables
    /** DOCUMENT ME! */
    private double upperBound;

    //Variables
    /** DOCUMENT ME! */
    private double width;

    //Variables
    /** DOCUMENT ME! */
    private double lowerValue;

    //Variables
    /** DOCUMENT ME! */
    private double upperValue;

    /** DOCUMENT ME! */
    private int size;

    /** DOCUMENT ME! */
    private int type;

/**
     * This general constructor creates a new partition of a specified interval
     * [a, b] into subintervals of width w. The underlying variable has a
     * specified name and symbol.
     *
     * @param a the lower bound or value
     * @param b the upper bound or value
     * @param w the step size
     * @param t the type of domain
     */
    public DistributionDomain(double a, double b, double w, int t) {
        if (w <= 0) {
            w = 1;
        }

        width = w;

        if (t < 0) {
            t = 0;
        } else if (t > 1) {
            t = 1;
        }

        type = t;

        if (type == DISCRETE) {
            if (b < a) {
                b = a;
            }

            lowerBound = a - (0.5 * width);
            upperBound = b + (0.5 * width);
        } else {
            if (b < (a + w)) {
                b = a + w;
            }

            lowerBound = a;
            upperBound = b;
        }

        lowerValue = lowerBound + (0.5 * width);
        upperValue = upperBound - (0.5 * width);
        size = (int) Math.rint((upperBound - lowerBound) / width);
    }

/**
     * This default constructor creates a new domain on (0, 1) with step size
     * 0.1
     */
    public DistributionDomain() {
        this(0, 1, 0.1, 1);
    }

    /**
     * This method returns the type of the domain (DISCRETE or
     * CONTINUOUS).
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * This method returns the index of the interval containing a given
     * value.
     *
     * @param x a number in the domain
     *
     * @return the index of x
     */
    public int getIndex(double x) {
        if (x < lowerBound) {
            return -1;
        }

        if (x > upperBound) {
            return size;
        } else {
            return (int) Math.rint((x - lowerValue) / width);
        }
    }

    /**
     * This method returns the boundary point corresponding to a given
     * index.
     *
     * @param i the index
     *
     * @return DOCUMENT ME!
     */
    public double getBound(int i) {
        return lowerBound + (i * width);
    }

    /**
     * This method return the midpoint of the interval corresponding to
     * a given index.
     *
     * @param i the index
     *
     * @return the midpoint of the interval corresponding to the index
     */
    public double getValue(int i) {
        return lowerValue + (i * width);
    }

    /**
     * This method returns the lower bound of the domain.
     *
     * @return the lower bound
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * This method returns the upper bound of the domain.
     *
     * @return the upper bound
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * This method returns the lower midpoint of the domain.
     *
     * @return the lower value
     */
    public double getLowerValue() {
        return lowerValue;
    }

    /**
     * This method returns the upper midpoint of the domain.
     *
     * @return the upper value
     */
    public double getUpperValue() {
        return upperValue;
    }

    /**
     * This method returns the width (step size) of the domain.
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * This method returns the size of the partition (the number of
     * subintervals).
     *
     * @return the size of the domain
     */
    public int getSize() {
        return size;
    }

    /**
     * This method returns the value (midpoint) that is closest to a
     * given value.
     *
     * @param x a number in the domain
     *
     * @return the midpoint closest to x
     */
    public double getNearestValue(double x) {
        return getValue(getIndex(x));
    }
}
