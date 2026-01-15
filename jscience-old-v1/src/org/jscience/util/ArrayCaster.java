package org.jscience.util;

/**
 * A simple convenience class for casting arrays.
 *
 * @author Daniel Lemire
 */
public final class ArrayCaster extends Object {
/**
     * Creates a new ArrayCaster object.
     */
    private ArrayCaster() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float[] toFloat(double[] v) {
        final float[] ans = new float[v.length];

        for (int k = 0; k < v.length; k++)
            ans[k] = (float) v[k];

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float[] toFloat(int[] v) {
        final float[] ans = new float[v.length];

        for (int k = 0; k < v.length; k++)
            ans[k] = (float) v[k];

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float[][] toFloat(double[][] v) {
        final float[][] ans = new float[v.length][];

        for (int k = 0; k < v.length; k++)
            ans[k] = toFloat(v[k]);

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float[][] toFloat(int[][] v) {
        final float[][] ans = new float[v.length][];

        for (int k = 0; k < v.length; k++)
            ans[k] = toFloat(v[k]);

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double[] toDouble(float[] v) {
        final double[] ans = new double[v.length];

        for (int k = 0; k < v.length; k++)
            ans[k] = (double) v[k];

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double[] toDouble(int[] v) {
        final double[] ans = new double[v.length];

        for (int k = 0; k < v.length; k++)
            ans[k] = (double) v[k];

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double[][] toDouble(float[][] v) {
        final double[][] ans = new double[v.length][];

        for (int k = 0; k < v.length; k++)
            ans[k] = toDouble(v[k]);

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double[][] toDouble(int[][] v) {
        final double[][] ans = new double[v.length][];

        for (int k = 0; k < v.length; k++)
            ans[k] = toDouble(v[k]);

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[] toInt(double[] v) {
        final int[] ans = new int[v.length];

        for (int k = 0; k < v.length; k++)
            ans[k] = (int) v[k];

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[] toInt(float[] v) {
        final int[] ans = new int[v.length];

        for (int k = 0; k < v.length; k++)
            ans[k] = (int) v[k];

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] toInt(float[][] v) {
        final int[][] ans = new int[v.length][];

        for (int k = 0; k < v.length; k++)
            ans[k] = toInt(v[k]);

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] toInt(double[][] v) {
        final int[][] ans = new int[v.length][];

        for (int k = 0; k < v.length; k++)
            ans[k] = toInt(v[k]);

        return (ans);
    }
}
