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

//The author agreed that we reuse his code under GPL, the above license is the original license
package org.jscience.mathematics.analysis.interpolation;

import org.jscience.mathematics.analysis.ValuedPair;


//a linear interpolation
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class DoubleLinearSplineInterpolator implements PrimitiveInterpolator {
    /** DOCUMENT ME! */
    private int nPoints = 0; // no. of tabulated points

    /** DOCUMENT ME! */
    private double[] y = null; // y=f(x) tabulated function

    /** DOCUMENT ME! */
    private double[] x = null; // x in tabulated function f(x)

    // Constructors
    /**
     * Creates a new DoubleLinearSplineInterpolator object.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public DoubleLinearSplineInterpolator(double[] x, double[] y) {
        this.nPoints = x.length;

        if (this.nPoints != y.length) {
            throw new IllegalArgumentException(
                "Arrays x and y are of different length" + this.nPoints + " " +
                y.length);
        }

        if (this.nPoints < 2) {
            throw new IllegalArgumentException(
                "A minimum of three data points is needed");
        }

        this.x = new double[nPoints];
        this.y = new double[nPoints];

        for (int i = 0; i < this.nPoints; i++) {
            this.x[i] = x[i];
            this.y[i] = y[i];
        }
    }

    /**
     * Get the number of points in the sample.
     *
     * @return number of points in the sample
     */
    public int size() {
        return nPoints;
    }

    /**
     * Get the dimension of the input values of the function.
     *
     * @return dimension
     */
    public int numInputDimensions() {
        return 1;
    }

    /**
     * Get the dimension of the output values of the function.
     *
     * @return dimension
     */
    public int numOutputDimensions() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(int x) {
        return map((double) x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(long x) {
        return map((double) x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(float x) {
        return map((double) x);
    }

    //  INTERPOLATE
    /**
     * DOCUMENT ME!
     *
     * @param xx DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(double xx) {
        if ((xx < this.x[0]) || (xx > this.x[this.nPoints - 1])) {
            throw new IllegalArgumentException("x (" + x +
                ") is outside the range of data points (" + this.x[0] + " to " +
                this.x[this.nPoints - 1]);
        }

        double h = 0.0D;
        double yy = 0.0D;
        int k = 0;
        int klo = 0;
        int khi = this.nPoints - 1;

        while ((khi - klo) > 1) {
            k = (khi + klo) >> 1;

            if (this.x[k] > xx) {
                khi = k;
            } else {
                klo = k;
            }
        }

        h = this.x[khi] - this.x[klo];

        if (h == 0.0) {
            throw new IllegalArgumentException(
                "Two values of x are identical: point " + klo + " (" +
                this.x[klo] + ") and point " + khi + " (" + this.x[khi] + ")");
        } else {
            //yy=((this.x[khi]-xx)*this.y[klo]+(xx-this.x[klo])*this.y[khi])/h;
            yy = y[klo] +
                (((xx - this.x[klo]) * (this.y[khi] - this.y[klo])) / h);
        }

        return yy;
    }

    /**
     * Get the abscissa and value of the sample at the specified index.
     *
     * @param index index in the sample, should be between 0 and {@link #size}
     *        - 1
     *
     * @return abscissa and value of the sample at the specified index
     *
     * @throws ArrayIndexOutOfBoundsException if the index is wrong
     */
    public ValuedPair samplePointAt(int index) {
        if ((index >= 0) && (index < size())) {
            return new ValuedPair(x[index], y[index]);
        } else {
            throw new ArrayIndexOutOfBoundsException(
                "Sample point must be between 0 and size()-1.");
        }
    }
}
