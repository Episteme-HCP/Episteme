package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.analysis.PrimitiveMappingND;


/**
 * The GingerbreadManMap class provides an object that encapsulates the
 * gingerbread man map. x<sub>n+1</sub> = 1 - y<sub>n</sub> + |x<sub>n</sub>|
 * y<sub>n+1</sub> = x<sub>n</sub> (Devaney).
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class GingerbreadManMap extends Object
    implements PrimitiveMappingND {
    /** Chaotic x value. */
    public final static double[] X_CHAOS = { -0.1, 0.0 };

/**
     * Constructs a gingerbread man map.
     */
    public GingerbreadManMap() {
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
        ans[0] = 1.0 - x[1] + Math.abs(x[0]);
        ans[1] = x[0];

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
