/**
 * Class CubicSpline Class for performing an interpolation using a cubic spline
 * setTabulatedArrays and interpolate adapted from Numerical Recipes in C
 * WRITTEN BY: Dr Michael Thomas Flanagan DATE:    May 2002 UPDATE: 29 April
 * 2005 DOCUMENTATION: See Michael Thomas Flanagan's Java library on-line web
 * page: CubicSpline.html Copyright (c) May 2003  Michael Thomas Flanagan
 * PERMISSION TO COPY: Permission to use, copy and modify this software and
 * its documentation for NON-COMMERCIAL purposes is granted, without fee,
 * provided that an acknowledgement to the author, Michael Thomas Flanagan at
 * www.ee.ucl.ac.uk/~mflanaga, appears in all copies. Dr Michael Thomas
 * Flanagan makes no representations about the suitability or fitness of the
 * software for any or for a particular purpose. Michael Thomas Flanagan shall
 * not be liable for any damages suffered as a result of using, modifying or
 * distributing this software or its derivatives.
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
