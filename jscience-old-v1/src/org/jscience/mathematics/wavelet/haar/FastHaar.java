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

package org.jscience.mathematics.wavelet.haar;

import org.jscience.mathematics.wavelet.FWT;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class FastHaar extends FWT {
    /** DOCUMENT ME! */
    static final float[] scale = {
            (float) (1.0 / Math.sqrt(2)), (float) (1.0 / Math.sqrt(2))
        };

    /** DOCUMENT ME! */
    static final float[] wavelet = { -scale[1], scale[0] };

/**
     * Creates a new FastHaar object.
     */
    public FastHaar() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     * @param last DOCUMENT ME!
     */
    private static void transform(float[] v, int last) {
        float[] ans = new float[last];
        int half = last / 2;

        try {
            for (int k = 0; /*k<half*/; k++) {
                ans[k + half] = (v[((2 * k) + 0)] * wavelet[0]) +
                    (v[((2 * k) + 1)] * wavelet[1]);
                ans[k] = (v[((2 * k) + 0)] * scale[0]) +
                    (v[((2 * k) + 1)] * scale[1]);
            }
        } catch (IndexOutOfBoundsException e) {
        }

        System.arraycopy(ans, 0, v, 0, last);
    }

    /**
     * Standard (Haar) transform
     *
     * @param v DOCUMENT ME!
     */
    public void transform(float[] v) {
        int last;

        for (last = v.length; last > 2; last /= 2) {
            transform(v, last);
        }

        if (last != 2) {
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

        for (last = 2; (2 * last) <= v.length; last *= 2) {
            invTransform(v, last);
        }

        if (last != v.length) {
            System.err.println("Careful! this should be a power of 2 : " +
                v.length);
        }
    }

    /**
     * Standard (Haar) transform
     *
     * @param v DOCUMENT ME!
     * @param last DOCUMENT ME!
     */
    private static void invTransform(float[] v, int last) {
        int ResultingLength = 2 * last;
        float[] ans = new float[ResultingLength];

        try {
            for (int k = 0; /*k<last*/; k++) {
                ans[((2 * k) + 1)] += ((scale[1] * v[k]) +
                (wavelet[1] * v[k + last]));
                ans[((2 * k) + 0)] += ((scale[0] * v[k]) +
                (wavelet[0] * v[k + last]));
            }
        } catch (IndexOutOfBoundsException e) {
        }

        System.arraycopy(ans, 0, v, 0, ans.length);
    }
}
