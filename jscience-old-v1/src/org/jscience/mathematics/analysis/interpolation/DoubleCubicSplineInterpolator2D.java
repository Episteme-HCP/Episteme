/**
 * BiCubicSpline.java Class for performing an interpolation on the tabulated
 * function y = f(x1,x2) using a natural bicubic spline Assumes second
 * derivatives at end points = 0 (natural spine) WRITTEN BY: Dr Michael Thomas
 * Flanagan DATE:    May 2002 UPDATE: 20 May 2003 DOCUMENTATION: See Michael
 * Thomas Flanagan's Java library on-line web page: BiCubicSpline.html
 * Copyright (c) May 2003   Michael Thomas Flanagan PERMISSION TO COPY:
 * Permission to use, copy and modify this software and its documentation for
 * NON-COMMERCIAL purposes is granted, without fee, provided that an
 * acknowledgement to the author, Michael Thomas Flanagan at
 * www.ee.ucl.ac.uk/~mflanaga, appears in all copies. Dr Michael Thomas
 * Flanagan makes no representations about the suitability or fitness of the
 * software for any or for a particular purpose. Michael Thomas Flanagan shall
 * not be liable for any damages suffered as a result of using, modifying or
 * distributing this software or its derivatives.
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
public class DoubleCubicSplineInterpolator2D implements PrimitiveInterpolatorND {
    /** DOCUMENT ME! */
    private int nPoints = 0; // no. of x1 tabulated points

    /** DOCUMENT ME! */
    private int mPoints = 0; // no. of x2 tabulated points

    /** DOCUMENT ME! */
    private double[][] y = null; // y=f(x1,x2) tabulated function

    /** DOCUMENT ME! */
    private double[] x1 = null; // x1 in tabulated function f(x1,x2)

    /** DOCUMENT ME! */
    private double[] x2 = null; // x2 in tabulated function f(x1,x2)

    /** DOCUMENT ME! */
    private double[][] d2ydx2inner = null; // second derivatives of first called array of cubic splines

    /** DOCUMENT ME! */
    private DoubleCubicSplineInterpolator[] csn = null; // nPoints array of CubicSpline instances

    /** DOCUMENT ME! */
    private DoubleCubicSplineInterpolator csm = null; // CubicSpline instance

    /** DOCUMENT ME! */
    private boolean derivCalculated1 = false; // = true when the first called cubic spline derivatives have been calculated

    // Constructor
    /**
     * Creates a new DoubleCubicSplineInterpolator2D object.
     *
     * @param x1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public DoubleCubicSplineInterpolator2D(double[] x1, double[] x2,
        double[][] y) {
        this.nPoints = x1.length;
        this.mPoints = x2.length;

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

        if ((this.nPoints < 3) || (this.mPoints < 3)) {
            throw new IllegalArgumentException(
                "The data matrix must have a minimum size of 3 X 3");
        }

        this.csm = new DoubleCubicSplineInterpolator(this.nPoints);
        this.csn = DoubleCubicSplineInterpolator.oneDarray(this.nPoints,
                this.mPoints);
        this.x1 = new double[this.nPoints];
        this.x2 = new double[this.mPoints];
        this.y = new double[this.nPoints][this.mPoints];
        this.d2ydx2inner = new double[this.nPoints][this.mPoints];

        for (int i = 0; i < this.nPoints; i++) {
            this.x1[i] = x1[i];
        }

        for (int j = 0; j < this.mPoints; j++) {
            this.x2[j] = x2[j];
        }

        for (int i = 0; i < this.nPoints; i++) {
            for (int j = 0; j < this.mPoints; j++) {
                this.y[i][j] = y[i][j];
            }
        }
    }

    // Constructor with data arrays initialised to zero
    /**
     * Creates a new DoubleCubicSplineInterpolator2D object.
     *
     * @param nP DOCUMENT ME!
     * @param mP DOCUMENT ME!
     */
    protected DoubleCubicSplineInterpolator2D(int nP, int mP) {
        this.nPoints = nP;
        this.mPoints = mP;

        if ((this.nPoints < 3) || (this.mPoints < 3)) {
            throw new IllegalArgumentException(
                "The data matrix must have a minimum size of 3 X 3");
        }

        this.csm = new DoubleCubicSplineInterpolator(this.nPoints);
        this.csn = DoubleCubicSplineInterpolator.oneDarray(this.nPoints,
                this.mPoints);
        this.x1 = new double[this.nPoints];
        this.x2 = new double[this.mPoints];
        this.y = new double[this.nPoints][this.mPoints];
        this.d2ydx2inner = new double[this.nPoints][this.mPoints];
    }

    //  METHODS
    /**
     * The dimension of variable parameter. Should be a strictly
     * positive integer.
     *
     * @return DOCUMENT ME!
     */
    public int numInputDimensions() {
        return 2;
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
        return nPoints * mPoints;
    }

    // Resets the x1, x2, y data arrays
    /**
     * DOCUMENT ME!
     *
     * @param x1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    protected void resetData(double[] x1, double[] x2, double[][] y) {
        if (x1.length != y.length) {
            throw new IllegalArgumentException(
                "Arrays x1 and y row are of different length");
        }

        if (x2.length != y[0].length) {
            throw new IllegalArgumentException(
                "Arrays x2 and y column are of different length");
        }

        if (this.nPoints != x1.length) {
            throw new IllegalArgumentException(
                "Original array length not matched by new array length");
        }

        if (this.mPoints != x2.length) {
            throw new IllegalArgumentException(
                "Original array length not matched by new array length");
        }

        for (int i = 0; i < this.nPoints; i++) {
            this.x1[i] = x1[i];
        }

        for (int i = 0; i < this.mPoints; i++) {
            this.x2[i] = x2[i];
        }

        for (int i = 0; i < this.nPoints; i++) {
            for (int j = 0; j < this.mPoints; j++) {
                this.y[i][j] = y[i][j];
            }
        }
    }

    // Returns a new BiCubicSpline setting internal array size to nP x mP and all array values to zero with natural spline default
    /**
     * DOCUMENT ME!
     *
     * @param nP DOCUMENT ME!
     * @param mP DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static DoubleCubicSplineInterpolator2D zero(int nP, int mP) {
        if ((nP < 3) || (mP < 3)) {
            throw new IllegalArgumentException(
                "A minimum of three x three data points is needed");
        }

        DoubleCubicSplineInterpolator2D aa = new DoubleCubicSplineInterpolator2D(nP,
                mP);

        return aa;
    }

    // Create a one dimensional array of BiCubicSpline objects of length nP each of internal array size mP x lP
    /**
     * DOCUMENT ME!
     *
     * @param nP DOCUMENT ME!
     * @param mP DOCUMENT ME!
     * @param lP DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static DoubleCubicSplineInterpolator2D[] oneDarray(int nP,
        int mP, int lP) {
        if ((mP < 3) || (lP < 3)) {
            throw new IllegalArgumentException(
                "A minimum of three x three data points is needed");
        }

        DoubleCubicSplineInterpolator2D[] a = new DoubleCubicSplineInterpolator2D[nP];

        for (int i = 0; i < nP; i++) {
            a[i] = DoubleCubicSplineInterpolator2D.zero(mP, lP);
        }

        return a;
    }

    //	Calculates the second derivatives of the tabulated function for
    /**
     * DOCUMENT ME!
     */
    protected void calcDeriv() {
        double[] yTempn = new double[mPoints];

        for (int i = 0; i < this.nPoints; i++) {
            for (int j = 0; j < mPoints; j++)
                yTempn[j] = y[i][j];

            this.csn[i].resetData(x2, yTempn);
            this.csn[i].calcDeriv();
            this.d2ydx2inner[i] = this.csn[i].getDeriv();
        }

        this.derivCalculated1 = true;
    }

    // Get inner matrix of derivatives
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double[][] getDeriv() {
        if (!this.derivCalculated1) {
            this.calcDeriv();
        }

        return this.d2ydx2inner;
    }

    // Set inner matrix of derivatives
    /**
     * DOCUMENT ME!
     *
     * @param d2ydx2 DOCUMENT ME!
     */
    public void setDeriv(double[][] d2ydx2) {
        this.d2ydx2inner = d2ydx2;
        this.derivCalculated1 = true;
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
        if (x.length == 2) {
            double[] result;

            result = new double[1];
            result[0] = map((int) x[0], (int) x[1]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 2D mappings.");
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
        if (x.length == 2) {
            double[] result;

            result = new double[1];
            result[0] = map((long) x[0], (long) x[1]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 2D mappings.");
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
        if (x.length == 2) {
            double[] result;

            result = new double[1];
            result[0] = map((float) x[0], (float) x[1]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 2D mappings.");
        }
    }

    //we do check that x[] is of dimension 2.
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(double[] x) {
        if (x.length == 2) {
            double[] result;

            result = new double[1];
            result[0] = map(x[0], x[1]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 2D mappings.");
        }
    }

    //	Returns an interpolated value of y for a value of x
    /**
     * DOCUMENT ME!
     *
     * @param xx1 DOCUMENT ME!
     * @param xx2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(double xx1, double xx2) {
        if (!this.derivCalculated1) {
            this.calcDeriv();
        }

        double[] yTempm = new double[this.nPoints];

        for (int i = 0; i < this.nPoints; i++) {
            this.csn[i].setDeriv(this.d2ydx2inner[i]);
            yTempm[i] = this.csn[i].map(xx2);
        }

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

        if ((index >= 0) && (index < size())) {
            xnumbers = new Number[2];
            ynumbers = new Number[1];
            xnumbers[0] = x1[index % nPoints];
            xnumbers[1] = x2[index / nPoints];
            ynumbers[0] = y[index % nPoints][index / nPoints];

            return new ValuedPair(xnumbers, ynumbers);
        } else {
            throw new ArrayIndexOutOfBoundsException(
                "Sample point must be between 0 and size()-1.");
        }
    }
}
