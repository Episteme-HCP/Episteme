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

package org.jscience.mathematics.wavelet.daubechies2;

import org.jscience.mathematics.wavelet.FWT;


/**
 * This is a very fast implementation of the Fast Wavelet Transform. It
 * uses in-place computations for less memory usage. Data length should be a
 * power of 2 a be at least of length 4. Handles boundaries by assuming
 * periodicity. Ideal for image processing or processing large amount of data.
 * Uses floats for more performance. Safety is minimal, so be careful!
 *
 * @author Daniel Lemire
 */
public final class FastDaubechies2 extends FWT {
    /** DOCUMENT ME! */
    static final private float root3 = (float) (Math.sqrt(3.0));

    /** DOCUMENT ME! */
    static final private float normalizer = (float) (Math.pow(2d, -.5d));

    /** DOCUMENT ME! */
    static final float[] scale = {
            ((1f + (root3)) * normalizer) / 4f,
            ((3f + (root3)) * normalizer) / 4f,
            ((3f + (-1 * (root3))) * normalizer) / 4f,
            ((1f + (-1 * (root3))) * normalizer) / 4f
        };

    /** DOCUMENT ME! */
    static final float[] wavelet = {
            (-(1f + (-1 * (root3))) * normalizer) / 4f,
            ((3f + (-1 * (root3))) * normalizer) / 4f,
            (-(3f + (root3)) * normalizer) / 4f,
            ((1f + (root3)) * normalizer) / 4f
        };

/**
     * Creates a new FastDaubechies2 object.
     */
    public FastDaubechies2() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     * @param last DOCUMENT ME!
     */
    public static void transform(float[] v, int last) {
        float[] ans = new float[last];
        final int half = last / 2;

        try {
            for (int k = 0; /*k<half*/; k++) {
                ans[k + half] = (v[((2 * k) + 0)] * wavelet[0]) +
                    (v[((2 * k) + 1)] * wavelet[1]) +
                    (v[((2 * k) + 2)] * wavelet[2]) +
                    (v[((2 * k) + 3)] * wavelet[3]);
                ans[k] = (v[((2 * k) + 0)] * scale[0]) +
                    (v[((2 * k) + 1)] * scale[1]) +
                    (v[((2 * k) + 2)] * scale[2]) +
                    (v[((2 * k) + 3)] * scale[3]);
            }
        } catch (IndexOutOfBoundsException e) {
        }

        ans[last - 1] = (v[last - 2] * wavelet[0]) +
            (v[last - 1] * wavelet[1]) + (v[0] * wavelet[2]) +
            (v[1] * wavelet[3]);
        ans[half - 1] = (v[last - 2] * scale[0]) + (v[last - 1] * scale[1]) +
            (v[0] * scale[2]) + (v[1] * scale[3]);

        System.arraycopy(ans, 0, v, 0, last);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public void transform(float[] v) {
        int last;

        for (last = v.length; last > 4; last /= 2) {
            transform(v, last);
        }

        if (last != 4) {
            System.err.println("Careful! this should be a power of 2 : " +
                v.length);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public void invTransform(float[] v) {
        int last;

        for (last = 4; (2 * last) <= v.length; last *= 2) {
            invTransform(v, last);
        }

        if (last != v.length) {
            System.err.println("Careful! this should be a power of 2 : " +
                v.length);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     * @param last DOCUMENT ME!
     */
    public static void invTransform(float[] v, int last) {
        int ResultingLength = 2 * last;
        float[] ans = new float[ResultingLength];

        try {
            for (int k = 0; /*k<last*/; k++) {
                ans[((2 * k) + 3)] += ((scale[3] * v[k]) +
                (wavelet[3] * v[k + last]));
                ans[((2 * k) + 2)] += ((scale[2] * v[k]) +
                (wavelet[2] * v[k + last]));
                ans[((2 * k) + 1)] += ((scale[1] * v[k]) +
                (wavelet[1] * v[k + last]));
                ans[((2 * k) + 0)] += ((scale[0] * v[k]) +
                (wavelet[0] * v[k + last]));
            }
        } catch (IndexOutOfBoundsException e) {
        }

        ans[ResultingLength - 2] += ((scale[0] * v[last - 1]) +
        (wavelet[0] * v[ResultingLength - 1]));
        ans[ResultingLength - 1] += ((scale[1] * v[last - 1]) +
        (wavelet[1] * v[ResultingLength - 1]));
        ans[0] += ((scale[2] * v[last - 1]) +
        (wavelet[2] * v[ResultingLength - 1]));
        ans[1] += ((scale[3] * v[last - 1]) +
        (wavelet[3] * v[ResultingLength - 1]));
        System.arraycopy(ans, 0, v, 0, ans.length);
    }
}
