/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class ContrastFilter extends TransferFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2724874183243154495L;

    /** DOCUMENT ME! */
    private float gain = 0.5f;

    /** DOCUMENT ME! */
    private float bias = 0.5f;

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int transferFunction(int v) {
        float f = (float) v / 255;
        f = ImageMath.gain(f, gain);
        f = ImageMath.bias(f, bias);

        return PixelUtils.clamp((int) (f * 255));
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void setGain(float gain) {
        this.gain = gain;
        initialized = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getGain() {
        return gain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bias DOCUMENT ME!
     */
    public void setBias(float bias) {
        this.bias = bias;
        initialized = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getBias() {
        return bias;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Colors/Contrast...";
    }
}
