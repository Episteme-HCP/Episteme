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
