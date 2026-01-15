package org.jscience.mathematics.wavelet;

/**
 * Abstract class for using very fast, in-place, implementations of the
 * Fast Wavelet Transform. See for example the FastDaubechies2 or FastSymmlet8
 * classes.
 *
 * @author Daniel Lemire
 */
public abstract class FWT {
    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public abstract void transform(float[] v);

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public abstract void invTransform(float[] v);
}
