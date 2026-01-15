package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The MandelbrotSet class provides an object that encapsulates the
 * Mandelbrot set.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class MandelbrotSet extends Object {
    /** DOCUMENT ME! */
    private final MandelbrotMap mbrot = new MandelbrotMap(Complex.ZERO);

/**
     * Constructs a Mandelbrot set.
     */
    public MandelbrotSet() {
    }

    /**
     * Returns 0 if z is a member of this set, else the number of
     * iterations it took for z to diverge to infinity.
     *
     * @param z DOCUMENT ME!
     * @param maxIter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int isMember(final Complex z, final int maxIter) {
        mbrot.setConstant(z);

        Complex w = Complex.ZERO;

        for (int i = 0; i < maxIter; i++) {
            w = mbrot.map(w);

            if (w.mod() > MandelbrotMap.CONVERGENT_BOUND) {
                return i + 1;
            }
        }

        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param zRe DOCUMENT ME!
     * @param zIm DOCUMENT ME!
     * @param maxIter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int isMember(final double zRe, final double zIm, final int maxIter) {
        double re = 0.0;
        double im = 0.0;
        double tmp;

        for (int i = 0; i < maxIter; i++) {
            tmp = (2.0 * re * im) + zIm;
            re = (re * re) - (im * im) + zRe;
            im = tmp;

            if (((re * re) + (im * im)) > (MandelbrotMap.CONVERGENT_BOUND * MandelbrotMap.CONVERGENT_BOUND)) {
                return i + 1;
            }
        }

        return 0;
    }
}
