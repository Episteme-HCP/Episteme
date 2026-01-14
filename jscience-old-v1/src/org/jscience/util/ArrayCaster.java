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
