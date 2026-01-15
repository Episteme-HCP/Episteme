// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.monitors;

/**
 * This is a Java implementation of the fast Fourier transform written by
 * Jef Poskanzer. The copyright appears above.
 */

/* libfft.c - fast Fourier transform library
 **
 ** Copyright (C) 1989 by Jef Poskanzer.
 **
 ** Permission to use, copy, modify, and distribute this software and its
 ** documentation for any purpose and without fee is hereby granted, provided
 ** that the above copyright notice appear in all copies and that both that
 ** copyright notice and this permission notice appear in supporting
 ** documentation.  This software is provided "as is" without express or
 ** implied warranty.
 */
public class FFT {
    /** DOCUMENT ME! */
    private static final double TWOPI = 2.0 * Math.PI;

    // Limits on the number of bits this algorithm can utilize
    /** DOCUMENT ME! */
    private static final int LOG2_MAXFFTSIZE = 15;

    /** DOCUMENT ME! */
    private static final int MAXFFTSIZE = 1 << LOG2_MAXFFTSIZE;

    // Private class data
    /** DOCUMENT ME! */
    private int bits;

    /** DOCUMENT ME! */
    private int[] bitreverse = new int[MAXFFTSIZE];

/**
     * FFT class constructor Initializes code for doing a fast Fourier
     * transform
     *
     * @param bits bits is a power of two such that 2^b is the number of
     *             samples.
     */
    public FFT(int bits) {
        this.bits = bits;

        if (bits > LOG2_MAXFFTSIZE) {
            System.out.println("" + bits + " is too big");
            System.exit(1);
        }

        for (int i = (1 << bits) - 1; i >= 0; --i) {
            int k = 0;

            for (int j = 0; j < bits; ++j) {
                k *= 2;

                if ((i & (1 << j)) != 0) {
                    k++;
                }
            }

            bitreverse[i] = k;
        }
    }

    /**
     * A fast Fourier transform routine
     *
     * @param xr [] xr is the real part of the data to be transformed
     * @param xi [] xi is the imaginary part of the data to be transformed
     *        (normally zero unless inverse transofrm is effect).
     * @param invFlag invFlag which is true if inverse transform is being
     *        applied. false for a forward transform.
     */
    public void doFFT(double[] xr, double[] xi, boolean invFlag) {
        int n;
        int n2;
        int i;
        int k;
        int kn2;
        int l;
        int p;
        double ang;
        double s;
        double c;
        double tr;
        double ti;

        n2 = (n = (1 << bits)) / 2;

        for (l = 0; l < bits; ++l) {
            for (k = 0; k < n; k += n2) {
                for (i = 0; i < n2; ++i, ++k) {
                    p = bitreverse[k / n2];
                    ang = (TWOPI * p) / n;
                    c = Math.cos(ang);
                    s = Math.sin(ang);
                    kn2 = k + n2;

                    if (invFlag) {
                        s = -s;
                    }

                    tr = (xr[kn2] * c) + (xi[kn2] * s);
                    ti = (xi[kn2] * c) - (xr[kn2] * s);

                    xr[kn2] = xr[k] - tr;
                    xi[kn2] = xi[k] - ti;
                    xr[k] += tr;
                    xi[k] += ti;
                }
            }

            n2 /= 2;
        }

        for (k = 0; k < n; k++) {
            if ((i = bitreverse[k]) <= k) {
                continue;
            }

            tr = xr[k];
            ti = xi[k];
            xr[k] = xr[i];
            xi[k] = xi[i];
            xr[i] = tr;
            xi[i] = ti;
        }

        // Finally, multiply each value by 1/n, if this is the forward
        // transform.
        if (!invFlag) {
            double f = 1.0 / n;

            for (i = 0; i < n; i++) {
                xr[i] *= f;
                xi[i] *= f;
            }
        }
    }
}
