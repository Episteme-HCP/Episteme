/**
 * TriCubicSpline.java Class for performing an interpolation on the tabulated
 * function y = f(x1,x2,x3) using a natural bicubic spline Assumes second
 * derivatives at end points = 0 (natural spine) WRITTEN BY: Dr Michael Thomas
 * Flanagan DATE:    May 2002 UPDATE: 20 May 2003, 17 February 2006
 * DOCUMENTATION: See Michael Thomas Flanagan's Java library on-line web page:
 * TriCubicSpline.html Copyright (c) May 2003, February 2006   Michael Thomas
 * Flanagan PERMISSION TO COPY: Permission to use, copy and modify this
 * software and its documentation for NON-COMMERCIAL purposes is granted,
 * without fee, provided that an acknowledgement to the author, Michael Thomas
 * Flanagan at www.ee.ucl.ac.uk/~mflanaga, appears in all copies. Dr Michael
 * Thomas Flanagan makes no representations about the suitability or fitness
 * of the software for any or for a particular purpose. Michael Thomas
 * Flanagan shall not be liable for any damages suffered as a result of using,
 * modifying or distributing this software or its derivatives.
 */
package org.jscience.mathematics.analysis.interpolation;

import org.jscience.mathematics.analysis.ValuedPair;


//The author agreed that we reuse his code under GPL, the above license is the original license
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class DoubleCubicSplineInterpolator3D implements PrimitiveInterpolatorND {
    /** DOCUMENT ME! */
    private int nPoints = 0; // no. of x1 tabulated points

    /** DOCUMENT ME! */
    private int mPoints = 0; // no. of x2 tabulated points

    /** DOCUMENT ME! */
    private int lPoints = 0; // no. of x3 tabulated points

    /** DOCUMENT ME! */
    private double[][][] y = null; // y=f(x1,x2) tabulated function

    /** DOCUMENT ME! */
    private double[] x1 = null; // x1 in tabulated function f(x1,x2,x3)

    /** DOCUMENT ME! */
    private double[] x2 = null; // x2 in tabulated function f(x1,x2,x3)

    /** DOCUMENT ME! */
    private double[] x3 = null; // x3 in tabulated function f(x1,x2,x3)

    /** DOCUMENT ME! */
    private DoubleCubicSplineInterpolator2D[] bcsn = null; // nPoints array of BiCubicSpline instances

    /** DOCUMENT ME! */
    private DoubleCubicSplineInterpolator csm = null; // CubicSpline instance

    /** DOCUMENT ME! */
    private double[][][] d2ydx2 = null; // inner matrix of second derivatives

    /** DOCUMENT ME! */
    private boolean derivCalculated = false; // = true when the called bicubic spline derivatives have been calculated

    // Constructor
    /**
     * Creates a new DoubleCubicSplineInterpolator3D object.
     *
     * @param x1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param x3 DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public DoubleCubicSplineInterpolator3D(double[] x1, double[] x2,
        double[] x3, double[][][] y) {
        this.nPoints = x1.length;
        this.mPoints = x2.length;
        this.lPoints = x3.length;

        if (this.nPoints != y.length) {
            throw new IllegalArgumentException(
                "Arrays x1 and y-row are of different length" + this.nPoints +
                " " + y.length);
        }

        if (this.mPoints != y[0].length) {
            throw new IllegalArgumentException(
                "Arrays x2 and y-column are of different length" +
                this.mPoints + " " + y[0].length);
        }

        if (this.lPoints != y[0][0].length) {
            throw new IllegalArgumentException(
                "Arrays x3 and y-column are of different length" +
                this.mPoints + " " + y[0].length);
        }

        if ((this.nPoints < 3) || (this.mPoints < 3) || (this.lPoints < 3)) {
            throw new IllegalArgumentException(
                "The tabulated 3D array must have a minimum size of 3 X 3 X 3");
        }

        this.csm = new DoubleCubicSplineInterpolator(this.nPoints);
        this.bcsn = DoubleCubicSplineInterpolator2D.oneDarray(this.nPoints,
                this.mPoints, this.lPoints);
        this.x1 = new double[this.nPoints];
        this.x2 = new double[this.mPoints];
        this.x3 = new double[this.lPoints];
        this.y = new double[this.nPoints][this.mPoints][this.lPoints];
        this.d2ydx2 = new double[this.nPoints][this.mPoints][this.lPoints];

        for (int i = 0; i < this.nPoints; i++) {
            this.x1[i] = x1[i];
        }

        for (int j = 0; j < this.mPoints; j++) {
            this.x2[j] = x2[j];
        }

        for (int j = 0; j < this.lPoints; j++) {
            this.x3[j] = x3[j];
        }

        for (int i = 0; i < this.nPoints; i++) {
            for (int j = 0; j < this.mPoints; j++) {
                for (int k = 0; k < this.lPoints; k++) {
                    this.y[i][j][k] = y[i][j][k];
                }
            }
        }
    }

    //  METHODS
    /**
     * The dimension of variable parameter. Should be a strictly
     * positive integer.
     *
     * @return DOCUMENT ME!
     */
    public int numInputDimensions() {
        return 3;
    }

    /**
     * The dimension of the result values. Should be inferior or equal
     * to numInputDimensions(). Should be a strictly positive integer.
     *
     * @return DOCUMENT ME!
     */
    public int numOutputDimensions() {
        return 1;
    }

    /**
     * Get the number of points in the sample.
     *
     * @return number of points in the sample
     */
    public int size() {
        return nPoints * mPoints * lPoints;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double[] map(int[] x) {
        if (x.length == 3) {
            double[] result;

            result = new double[1];
            result[0] = map((int) x[0], (int) x[1], (int) x[2]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 3D mappings.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double[] map(long[] x) {
        if (x.length == 3) {
            double[] result;

            result = new double[1];
            result[0] = map((long) x[0], (long) x[1], (long) x[2]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 3D mappings.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double[] map(float[] x) {
        if (x.length == 3) {
            double[] result;

            result = new double[1];
            result[0] = map((float) x[0], (float) x[1], (float) x[2]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 3D mappings.");
        }
    }

    //we do check that x[] is of dimension 3.
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(double[] x) {
        if (x.length == 3) {
            double[] result;

            result = new double[1];
            result[0] = map(x[0], x[1], x[2]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 3D mappings.");
        }
    }

    //	Returns an interpolated value of y for values of x1, x2 and x3
    /**
     * DOCUMENT ME!
     *
     * @param xx1 DOCUMENT ME!
     * @param xx2 DOCUMENT ME!
     * @param xx3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(double xx1, double xx2, double xx3) {
        double[][] yTempml = new double[this.mPoints][this.lPoints];

        for (int i = 0; i < this.nPoints; i++) {
            for (int j = 0; j < this.mPoints; j++) {
                for (int k = 0; k < this.lPoints; k++) {
                    yTempml[j][k] = y[i][j][k];
                }
            }

            this.bcsn[i].resetData(x2, x3, yTempml);
        }

        double[] yTempm = new double[nPoints];

        for (int i = 0; i < nPoints; i++) {
            if (this.derivCalculated) {
                this.bcsn[i].setDeriv(d2ydx2[i]);
            }

            yTempm[i] = this.bcsn[i].map(xx2, xx3);

            if (!this.derivCalculated) {
                d2ydx2[i] = this.bcsn[i].getDeriv();
            }
        }

        derivCalculated = true;

        this.csm.resetData(x1, yTempm);

        return this.csm.map(xx1);
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
        Number[] xnumbers;
        Number[] ynumbers;

        xnumbers = new Number[3];
        ynumbers = new Number[1];

        //possible mistake here
        xnumbers[0] = x1[(index % (mPoints * nPoints)) % nPoints];
        xnumbers[1] = x2[(index % (mPoints * nPoints)) / nPoints];
        xnumbers[2] = x2[index / (mPoints * nPoints)];
        ynumbers[0] = y[(index % (mPoints * nPoints)) % nPoints][(index % (mPoints * nPoints)) / nPoints][index / (mPoints * nPoints)];

        if ((index >= 0) && (index < size())) {
            return new ValuedPair(xnumbers, ynumbers);
        } else {
            throw new ArrayIndexOutOfBoundsException(
                "Sample point must be between 0 and size()-1.");
        }
    }
}
