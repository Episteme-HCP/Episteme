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


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class DoubleCubicSplineInterpolator implements PrimitiveInterpolator {
    /** DOCUMENT ME! */
    private int nPoints = 0; // no. of tabulated points

    /** DOCUMENT ME! */
    private double[] y = null; // y=f(x) tabulated function

    /** DOCUMENT ME! */
    private double[] x = null; // x in tabulated function f(x)

    /** DOCUMENT ME! */
    private double[] d2ydx2 = null; // second derivatives of y

    /** DOCUMENT ME! */
    private double yp1 = Double.NaN; // first derivative at point one

    // default value = NaN (natural spline)
    /** DOCUMENT ME! */
    private double ypn = Double.NaN; // first derivative at point n

    // default value = NaN (natural spline)
    /** DOCUMENT ME! */
    private boolean derivCalculated = false; // = true when the derivatives have been calculated

    // Constructors
    /**
     * Creates a new DoubleCubicSplineInterpolator object.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public DoubleCubicSplineInterpolator(double[] x, double[] y) {
        this.nPoints = x.length;

        if (this.nPoints != y.length) {
            throw new IllegalArgumentException(
                "Arrays x and y are of different length" + this.nPoints + " " +
                y.length);
        }

        if (this.nPoints < 3) {
            throw new IllegalArgumentException(
                "A minimum of three data points is needed");
        }

        this.x = new double[nPoints];
        this.y = new double[nPoints];
        this.d2ydx2 = new double[nPoints];

        for (int i = 0; i < this.nPoints; i++) {
            this.x[i] = x[i];
            this.y[i] = y[i];
        }
    }

    // Constructor with data arrays initialised to zero
    /**
     * Creates a new DoubleCubicSplineInterpolator object.
     *
     * @param nPoints DOCUMENT ME!
     */
    protected DoubleCubicSplineInterpolator(int nPoints) {
        this.nPoints = nPoints;

        if (this.nPoints < 3) {
            throw new IllegalArgumentException(
                "A minimum of three data points is needed");
        }

        this.x = new double[nPoints];
        this.y = new double[nPoints];
        this.d2ydx2 = new double[nPoints];
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

    //  METHODS
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    protected void resetData(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException(
                "Arrays x and y are of different length");
        }

        if (this.nPoints != x.length) {
            throw new IllegalArgumentException(
                "Original array length not matched by new array length");
        }

        for (int i = 0; i < this.nPoints; i++) {
            this.x[i] = x[i];
            this.y[i] = y[i];
        }
    }

    // Returns a new CubicSpline setting array lengths to n and all array values to zero with natural spline default
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static DoubleCubicSplineInterpolator zero(int n) {
        if (n < 3) {
            throw new IllegalArgumentException(
                "A minimum of three data points is needed");
        }

        DoubleCubicSplineInterpolator aa = new DoubleCubicSplineInterpolator(n);

        return aa;
    }

    // Create a one dimensional array of cubic spline objects of length n each of array length m
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static DoubleCubicSplineInterpolator[] oneDarray(int n, int m) {
        if (m < 3) {
            throw new IllegalArgumentException(
                "A minimum of three data points is needed");
        }

        DoubleCubicSplineInterpolator[] a = new DoubleCubicSplineInterpolator[n];

        for (int i = 0; i < n; i++) {
            a[i] = DoubleCubicSplineInterpolator.zero(m);
        }

        return a;
    }

    // Enters the first derivatives of the cubic spline at
    /**
     * DOCUMENT ME!
     *
     * @param yp1 DOCUMENT ME!
     * @param ypn DOCUMENT ME!
     */
    protected void setDerivLimits(double yp1, double ypn) {
        this.yp1 = yp1;
        this.ypn = ypn;
    }

    // Resets a natural spline
    /**
     * DOCUMENT ME!
     */
    protected void setDerivLimits() {
        this.yp1 = Double.NaN;
        this.ypn = Double.NaN;
    }

    // Enters the first derivatives of the cubic spline at
    /**
     * DOCUMENT ME!
     *
     * @param yp1 DOCUMENT ME!
     * @param ypn DOCUMENT ME!
     */
    protected void setDeriv(double yp1, double ypn) {
        this.yp1 = yp1;
        this.ypn = ypn;
    }

    // Returns the internal array of second derivatives
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double[] getDeriv() {
        if (!this.derivCalculated) {
            this.calcDeriv();
        }

        return this.d2ydx2;
    }

    // Sets the internal array of second derivatives
    /**
     * DOCUMENT ME!
     *
     * @param deriv DOCUMENT ME!
     */
    protected void setDeriv(double[] deriv) {
        this.d2ydx2 = deriv;
        this.derivCalculated = true;
    }

    //  Calculates the second derivatives of the tabulated function
    /**
     * DOCUMENT ME!
     */
    protected void calcDeriv() {
        double p = 0.0D;
        double qn = 0.0D;
        double sig = 0.0D;
        double un = 0.0D;
        double[] u = new double[nPoints];

        if (this.yp1 != this.yp1) {
            d2ydx2[0] = u[0] = 0.0;
        } else {
            this.d2ydx2[0] = -0.5;
            u[0] = (3.0 / (this.x[1] - this.x[0])) * (((this.y[1] - this.y[0]) / (this.x[1] -
                this.x[0])) - this.yp1);
        }

        for (int i = 1; i <= (this.nPoints - 2); i++) {
            sig = (this.x[i] - this.x[i - 1]) / (this.x[i + 1] - this.x[i - 1]);
            p = (sig * this.d2ydx2[i - 1]) + 2.0;
            this.d2ydx2[i] = (sig - 1.0) / p;
            u[i] = ((this.y[i + 1] - this.y[i]) / (this.x[i + 1] - this.x[i])) -
                ((this.y[i] - this.y[i - 1]) / (this.x[i] - this.x[i - 1]));
            u[i] = (((6.0 * u[i]) / (this.x[i + 1] - this.x[i - 1])) -
                (sig * u[i - 1])) / p;
        }

        if (this.ypn != this.ypn) {
            qn = un = 0.0;
        } else {
            qn = 0.5;
            un = (3.0 / (this.x[nPoints - 1] - this.x[this.nPoints - 2])) * (this.ypn -
                ((this.y[this.nPoints - 1] - this.y[this.nPoints - 2]) / (this.x[this.nPoints -
                1] - x[this.nPoints - 2])));
        }

        this.d2ydx2[this.nPoints - 1] = (un - (qn * u[this.nPoints - 2])) / ((qn * this.d2ydx2[this.nPoints -
            2]) + 1.0);

        for (int k = this.nPoints - 2; k >= 0; k--) {
            this.d2ydx2[k] = (this.d2ydx2[k] * this.d2ydx2[k + 1]) + u[k];
        }

        this.derivCalculated = true;
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
            throw new IllegalArgumentException("x (" + xx +
                ") is outside the range of data points (" + x[0] + " to " +
                x[this.nPoints - 1]);
        }

        if (!this.derivCalculated) {
            this.calcDeriv();
        }

        double h = 0.0D;
        double b = 0.0D;
        double a = 0.0D;
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
            a = (this.x[khi] - xx) / h;
            b = (xx - this.x[klo]) / h;
            yy = (a * this.y[klo]) + (b * this.y[khi]) +
                ((((((a * a * a) - a) * this.d2ydx2[klo]) +
                (((b * b * b) - b) * this.d2ydx2[khi])) * (h * h)) / 6.0);
        }

        return yy;
    }

    //  Returns an interpolated value of y for a value of x (xx) from a tabulated function y=f(x)
    /**
     * DOCUMENT ME!
     *
     * @param xx DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deriv DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static double map(double xx, double[] x, double[] y,
        double[] deriv) {
        if (((x.length != y.length) || (x.length != deriv.length)) ||
                (y.length != deriv.length)) {
            throw new IllegalArgumentException(
                "array lengths are not all equal");
        }

        int n = x.length;
        double h = 0.0D;
        double b = 0.0D;
        double a = 0.0D;
        double yy = 0.0D;

        int k = 0;
        int klo = 0;
        int khi = n - 1;

        while ((khi - klo) > 1) {
            k = (khi + klo) >> 1;

            if (x[k] > xx) {
                khi = k;
            } else {
                klo = k;
            }
        }

        h = x[khi] - x[klo];

        if (h == 0.0) {
            throw new IllegalArgumentException("Two values of x are identical");
        } else {
            a = (x[khi] - xx) / h;
            b = (xx - x[klo]) / h;
            yy = (a * y[klo]) + (b * y[khi]) +
                ((((((a * a * a) - a) * deriv[klo]) +
                (((b * b * b) - b) * deriv[khi])) * (h * h)) / 6.0);
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
