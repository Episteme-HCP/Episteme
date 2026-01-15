/**
 * PolyCubicSpline.java Class for performing an interpolation on the tabulated
 * function y = f(x1,x2, x3 .... xn) using a natural cubic splines Assumes
 * second derivatives at end points = 0 (natural spines) WRITTEN BY: Dr
 * Michael Thomas Flanagan DATE:    15 February 2006 DOCUMENTATION: See
 * Michael Thomas Flanagan's Java library on-line web page:
 * PolyCubicSpline.html Copyright (c) February 2006   Michael Thomas Flanagan
 * PERMISSION TO COPY: Permission to use, copy and modify this software and
 * its documentation for NON-COMMERCIAL purposes is granted, without fee,
 * provided that an acknowledgement to the author, Michael Thomas Flanagan at
 * www.ee.ucl.ac.uk/~mflanaga, appears in all copies. Dr Michael Thomas
 * Flanagan makes no representations about the suitability or fitness of the
 * software for any or for a particular purpose. Michael Thomas Flanagan shall
 * not be liable for any damages suffered as a result of using, modifying or
 * distributing this software or its derivatives.
 */
package org.jscience.mathematics.analysis.interpolation;


//The author agreed that we reuse his code under GPL, the above license is the original license
import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.analysis.ValuedPair;

import java.lang.reflect.Array;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class DoubleCubicSplineInterpolatorND implements PrimitiveInterpolatorND {
    /** DOCUMENT ME! */
    private int nDimensions = 0; // number of the dimensions of the tabulated points array, y=f(x1,x2,x3 . . xn), i.e. n

    /** DOCUMENT ME! */
    private Object fOfX = null; // tabulated values of y = f(x1,x2,x3 . . fn)

    // as a multidemensional array of double [x1 length][x2 length] ... [xn length]
    /** DOCUMENT ME! */
    private Object xArrays = null; // The variable arrays x1, x2, x3 . . . xn

    // packed as a multidemensional array of double [][]
    // where xArrays[0] = array of x1 values, xArrays[1] = array of x2 values etc
    /** DOCUMENT ME! */
    private double yValue = 0.0D; // returned interpolated value

    /** DOCUMENT ME! */
    private int size;

    // Constructor
    /**
     * Creates a new DoubleCubicSplineInterpolatorND object.
     *
     * @param xArrays DOCUMENT ME!
     * @param fOfX DOCUMENT ME!
     */
    public DoubleCubicSplineInterpolatorND(Object xArrays, Object fOfX) {
        this.fOfX = fOfX;
        this.xArrays = xArrays;

        // Calculate fOfX array dimension number
        Object internalArray = fOfX;
        this.nDimensions = 1;
        this.size = Array.getLength(internalArray);

        while (!((internalArray = Array.get(internalArray, 0)) instanceof Double)) {
            nDimensions++;
            this.size *= Array.getLength(internalArray);
        }

        // Repack xArrays as 2 dimensional array if entered a single dimensioned array for a simple cubic spline
        if (this.xArrays instanceof double[] && (this.nDimensions == 1)) {
            double[][] xArraysTemp = new double[1][];
            xArraysTemp[0] = (double[]) xArrays;
            this.xArrays = (Object) xArraysTemp;
        } else {
            if (!(xArrays instanceof double[][])) {
                throw new IllegalArgumentException(
                    "xArrays should be a two dimensional array of doubles");
            }
        }
    }

    /**
     * The dimension of variable parameter. Should be a strictly
     * positive integer.
     *
     * @return DOCUMENT ME!
     */
    public int numInputDimensions() {
        return this.nDimensions;
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
        return this.size;
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
        int nUnknown = x.length;

        if (nUnknown != this.nDimensions) {
            throw new IllegalArgumentException(
                "Number of unknown value coordinates, " + nUnknown +
                ", does not equal the number of tabulated data dimensions, " +
                this.nDimensions);
        }

        return map(ArrayMathUtils.toDouble(x));
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
        int nUnknown = x.length;

        if (nUnknown != this.nDimensions) {
            throw new IllegalArgumentException(
                "Number of unknown value coordinates, " + nUnknown +
                ", does not equal the number of tabulated data dimensions, " +
                this.nDimensions);
        }

        return map(ArrayMathUtils.toDouble(x));
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
        int nUnknown = x.length;

        if (nUnknown != this.nDimensions) {
            throw new IllegalArgumentException(
                "Number of unknown value coordinates, " + nUnknown +
                ", does not equal the number of tabulated data dimensions, " +
                this.nDimensions);
        }

        return map(ArrayMathUtils.toDouble(x));
    }

    //  Interpolation method
    /**
     * DOCUMENT ME!
     *
     * @param unknownCoord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(double[] unknownCoord) {
        int nUnknown = unknownCoord.length;

        if (nUnknown != this.nDimensions) {
            throw new IllegalArgumentException(
                "Number of unknown value coordinates, " + nUnknown +
                ", does not equal the number of tabulated data dimensions, " +
                this.nDimensions);
        }

        int kk = 0;
        double[][] xArray = (double[][]) this.xArrays;

        switch (this.nDimensions) {
        case 0:
            throw new IllegalArgumentException(
                "data array must have at least one dimension");

        case 1: // If fOfX is one dimensional perform simple cubic spline

            DoubleCubicSplineInterpolator cs = new DoubleCubicSplineInterpolator(xArray[0],
                    (double[]) this.fOfX);
            this.yValue = cs.map(unknownCoord[0]);

            break;

        case 2: // If fOfX is two dimensional perform bicubic spline

            DoubleCubicSplineInterpolator2D bcs = new DoubleCubicSplineInterpolator2D(xArray[0],
                    xArray[1], (double[][]) this.fOfX);
            this.yValue = bcs.map(unknownCoord[0], unknownCoord[1]);

            break;

        case 3: // If fOfX is three dimensional perform tricubic spline

            DoubleCubicSplineInterpolator3D tcs = new DoubleCubicSplineInterpolator3D(xArray[0],
                    xArray[1], xArray[2], (double[][][]) this.fOfX);
            this.yValue = tcs.map(unknownCoord[0], unknownCoord[1],
                    unknownCoord[2]);

            break;

        default: // If fOfX is greater than three dimensional, recursively call PolyCubicSpline

            //  with, as arguments, the n1 fOfX sub-arrays, each of (number of dimensions - 1) dimensions,
            //  where n1 is the number of x1 variables.
            Object obj = fOfX;
            int dimOne = Array.getLength(obj);
            double[] csArray = new double[dimOne];
            double[][] newXarrays = new double[this.nDimensions - 1][];
            double[] newCoord = new double[this.nDimensions - 1];

            for (int i = 0; i < (this.nDimensions - 1); i++) {
                newXarrays[i] = xArray[i + 1];
                newCoord[i] = unknownCoord[i + 1];
            }

            for (int i = 0; i < dimOne; i++) {
                Object objT = (Object) Array.get(obj, i);
                DoubleCubicSplineInterpolatorND pcs = new DoubleCubicSplineInterpolatorND(newXarrays,
                        objT);
                csArray[i] = pcs.map(newCoord)[0];
            }

            // Perform simple cubic spline on the array of above returned interpolates
            DoubleCubicSplineInterpolator ncs = new DoubleCubicSplineInterpolator(xArray[0],
                    csArray);
            this.yValue = ncs.map(unknownCoord[0]);
        }

        double[] result;

        result = new double[1];
        result[0] = this.yValue;

        return result;
    }

    /**
     * Get the abscissa and value of the sample at the specified index.
     *
     * @param index index in the sample, should be between 0 and {@link #size}
     *        - 1
     *
     * @return abscissa and value of the sample at the specified index
     *
     * @throws UnsupportedOperationException if the index is wrong
     */

    //unsupported operation as it seems to me (SMM) this would add no functionality
    //especially considering the fact that the array is probably very large and you would really have hard time figuring out the order of the points returned by this method
    //and considering the fact that you already have the values in a convenient form at construction time
    //let us know if you really want this method implemented (TODO)
    public ValuedPair samplePointAt(int index) {
        throw new UnsupportedOperationException();
    }
}
