package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The JuliaSet class provides an object that encapsulates Julia sets.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class JuliaSet extends Object {
    /** DOCUMENT ME! */
    public final static Complex RABBIT = new Complex(-0.123, 0.745);

    /** DOCUMENT ME! */
    public final static Complex SAN_MARCO = new Complex(-0.75, 0.0);

    /** DOCUMENT ME! */
    public final static Complex SIEGEL_DISK = new Complex(-0.391, -0.587);

    /** DOCUMENT ME! */
    private final MandelbrotMap mbrot;

/**
     * Constructs a Julia set.
     *
     * @param c DOCUMENT ME!
     */
    public JuliaSet(Complex c) {
        mbrot = new MandelbrotMap(c);
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
    public int isMember(Complex z, final int maxIter) {
        for (int i = 0; i < maxIter; i++) {
            z = mbrot.map(z);

            if (z.mod() > MandelbrotMap.CONVERGENT_BOUND) {
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
    public int isMember(double zRe, double zIm, final int maxIter) {
        final double cRe = mbrot.getConstant().real();
        final double cIm = mbrot.getConstant().imag();
        double tmp;

        for (int i = 0; i < maxIter; i++) {
            tmp = (2.0 * zRe * zIm) + cIm;
            zRe = (zRe * zRe) - (zIm * zIm) + cRe;
            zIm = tmp;

            if (((zRe * zRe) + (zIm * zIm)) > (MandelbrotMap.CONVERGENT_BOUND * MandelbrotMap.CONVERGENT_BOUND)) {
                return i + 1;
            }
        }

        return 0;
    }
}
