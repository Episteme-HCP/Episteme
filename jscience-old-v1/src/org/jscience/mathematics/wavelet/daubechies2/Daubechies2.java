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

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.MathConstants;
import org.jscience.mathematics.wavelet.*;


/**
 * Daubechies wavelets adapted to the interval by Meyer. Thanks to Pierre
 * Vial for the filters.
 *
 * @author Daniel Lemire
 */
public final class Daubechies2 extends Multiresolution implements Filter {
    /** DOCUMENT ME! */
    protected final static int filtretype = 2;

    /** DOCUMENT ME! */
    protected final static int minlength = 4;

    /** DOCUMENT ME! */
    final static double[] vgtemp = {
            0.482962913145, 0.836516303738, 0.224143868042, -0.129409522551
        };

    /** DOCUMENT ME! */
    final static double[] v0temp = { 0.848528137424, -0.529150262213 };

    /** DOCUMENT ME! */
    final static double[] v1temp = {
            0.132287565553, 0.212132034356, 0.838525491562, -0.484122918276
        };

    /** DOCUMENT ME! */
    final static double[] vd0temp = { 0.848528137424, 0.529150262213 };

    /** DOCUMENT ME! */
    final static double[] vd1temp = {
            -0.132287565553, 0.212132034356, 0.838525491562, 0.484122918276
        };

    /** DOCUMENT ME! */
    final static double[] vg = ArrayMathUtils.scalarMultiply(MathConstants.SQRT2,
            vgtemp);

    /** DOCUMENT ME! */
    final static double[] vd0 = ArrayMathUtils.scalarMultiply(MathConstants.SQRT2,
            ArrayMathUtils.invert(vd0temp));

    /** DOCUMENT ME! */
    final static double[] vd1 = ArrayMathUtils.scalarMultiply(MathConstants.SQRT2,
            ArrayMathUtils.invert(vd1temp));

    /** DOCUMENT ME! */
    final static double[] v0 = ArrayMathUtils.scalarMultiply(MathConstants.SQRT2,
            v0temp);

    /** DOCUMENT ME! */
    final static double[] v1 = ArrayMathUtils.scalarMultiply(MathConstants.SQRT2,
            v1temp);

    /**
     * On d�finit ici le filtre comme tel par le vecteur phvg (filtre
     * passe-haut).
     */
    final static double[] phvg = WaveletMathUtils.lowToHigh(vgtemp);

    /** DOCUMENT ME! */
    final static double[] phv0 = {
            -0.512347538298, -0.821583836258, 0.216506350947, -0.125
        };

    /** DOCUMENT ME! */
    final static double[] phvd0temp = {
            0.512347538298, -0.821583836258, 0.216506350946, 0.125
        };

    /** DOCUMENT ME! */
    final static double[] phvd0 = ArrayMathUtils.invert(phvd0temp);

/**
     * Creates a new Daubechies2 object.
     */
    public Daubechies2() {
    }

    /**
     * This method is used to compute how the number of scaling
     * functions changes from on scale to the other. Basically, if you have k
     * scaling function and a Filter of type t, you'll have 2k+t scaling
     * functions at the next scale (dyadic case). Notice that this method
     * assumes that one is working with the dyadic grid while the method
     * "previousDimension" define in the interface "Filter" doesn't.
     *
     * @return DOCUMENT ME!
     */
    public int getFilterType() {
        return (filtretype);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MultiscaleFunction primaryScaling(int n0, int k) {
        return (new Scaling2(n0, k));
    }

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MultiscaleFunction dualScaling(int n0, int k) {
        return (new Scaling2(n0, k));
    }

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MultiscaleFunction primaryWavelet(int n0, int k) {
        return (new Wavelet2(n0, k));
    }

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MultiscaleFunction dualWavelet(int n0, int k) {
        return (new Wavelet2(n0, k));
    }

    /**
     * This method return the number of "scaling" functions at the
     * previous scale given a number of scaling functions. The answer is
     * always smaller than the provided value (about half since this is a
     * dyadic implementation). This relates to the same idea as the "Filter
     * type". It is used by the interface "Filter".
     *
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int previousDimension(int k) {
        return (Cascades.previousDimension(filtretype, k));
    }

    /**
     * This is the implementation of the lowpass Filter. It is used by
     * the interface "Filter". Lowpass filters are normalized so that they
     * preserve constants away from the boundaries.
     *
     * @param v DOCUMENT ME!
     * @param param DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] lowpass(double[] v, double[] param) {
        return (lowpass(v));
    }

    /**
     * This is the implementation of the highpass Filter. It is used by
     * the interface "Filter". Highpass filters are normalized in order to get
     * L2 orthonormality of the resulting wavelets (when it applies). See the
     * class DiscreteHilbertSpace for an implementation of the L2 integration.
     *
     * @param v DOCUMENT ME!
     * @param param DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] highpass(double[] v, double[] param) {
        return (highpass(v));
    }

    /**
     * This is the implementation of the lowpass Filter. It is used by
     * the interface "Filter". Lowpass filters are normalized so that they
     * preserve constants away from the boundaries.
     *
     * @param gete DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalScalingException DOCUMENT ME!
     */
    public double[] lowpass(double[] gete) {
        if (gete.length < minlength) {
            throw new IllegalScalingException("The array is not long enough: " +
                gete.length + " < " + minlength);
        }

        double[] sortie = new double[(2 * gete.length) - 2];
        int dl0 = gete.length - 1;

        for (int k = 2; k <= (dl0 - 2); k++) {
            for (int L = -2; L < 2; L++) {
                sortie[(2 * k) + L] += (vg[L + 2] * gete[k]);
            }
        }

        sortie = ArrayMathUtils.add(sortie, gete[0], v0, 0);
        sortie = ArrayMathUtils.add(sortie, gete[1], v1, 0);

        int p0 = sortie.length - vd0.length;
        int p1 = sortie.length - vd1.length;
        sortie = ArrayMathUtils.add(sortie, gete[dl0], vd0, p0);
        sortie = ArrayMathUtils.add(sortie, gete[dl0 - 1], vd1, p1);

        return (sortie);
    }

    /**
     * This is the implementation of the highpass Filter. It is used by
     * the interface "Filter". Highpass filters are normalized in order to get
     * L2 orthonormality of the resulting wavelets (when it applies). See the
     * class DiscreteHilbertSpace for an implementation of the L2 integration.
     *
     * @param gete DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] highpass(double[] gete) {
        double[] sortie = new double[(2 * gete.length) + 2];
        int dl0 = gete.length - 1;

        for (int k = 1; k <= (dl0 - 1); k++) {
            for (int L = -2; L < 2; L++) {
                sortie[(2 * k) + L + 2] += (phvg[L + 2] * gete[k]);
            }
        }

        sortie = ArrayMathUtils.add(sortie, gete[0], phv0, 0);

        int p0 = sortie.length - phvd0.length;
        sortie = ArrayMathUtils.add(sortie, gete[dl0], phvd0, p0);

        return (sortie);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     * @param j1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] evalScaling(int n0, int k, int j1) {
        return (Cascades.evalScaling(this, n0, j1, k));
    }

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     * @param j1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] evalWavelet(int n0, int k, int j1) {
        return (Cascades.evalWavelet(this, filtretype, n0, j1, k));

        //return(Cascades.evalWavelet(this,n0,j1,k));
    }
}
