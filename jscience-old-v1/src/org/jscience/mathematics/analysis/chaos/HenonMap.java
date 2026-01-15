package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.analysis.PrimitiveMappingND;


/**
 * The HenonMap class provides an object that encapsulates the Henon map.
 * x<sub>n+1</sub> = 1 - a x<sub>n</sub><sup>2</sup> + y<sub>n</sub>
 * y<sub>n+1</sub> = b x<sub>n</sub>
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class HenonMap extends Object implements PrimitiveMappingND {
    /** Chaotic a parameter value. */
    public final static double A_CHAOS = 1.4;

    /** Chaotic b parameter value. */
    public final static double B_CHAOS = 0.3;

    /** DOCUMENT ME! */
    private final double a;

    /** DOCUMENT ME! */
    private final double b;

/**
     * Constructs a Henon map.
     *
     * @param aval the value of the a parameter
     * @param bval the value of the b parameter
     */
    public HenonMap(double aval, double bval) {
        a = aval;
        b = bval;
    }

    /**
     * Performs the mapping.
     *
     * @param x a 2-D double array
     *
     * @return a 2-D double array
     */
    public double[] map(double[] x) {
        double[] ans = new double[2];
        ans[0] = 1.0 - (a * x[0] * x[0]) + x[1];
        ans[1] = b * x[0];

        return ans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(float[] x) {
        double[] result;

        result = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = (double) x[i];
        }

        return map(result);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(long[] x) {
        long[] result;

        result = new long[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = (long) x[i];
        }

        return map(result);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(int[] x) {
        int[] result;

        result = new int[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = (int) x[i];
        }

        return map(result);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int numInputDimensions() {
        return 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int numOutputDimensions() {
        return 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double hausdorffDimension() {
        return 1.26;
    }

    /**
     * Iterates the map.
     *
     * @param n the number of iterations
     * @param x the initial values (2-D)
     *
     * @return a 2-D double array
     */
    public double[] iterate(int n, double[] x) {
        double[] xn = map(x);

        for (int i = 1; i < n; i++)
            xn = map(xn);

        return xn;
    }
}
