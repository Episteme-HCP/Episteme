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
public class RescaleFilter extends TransferFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2724874183243154495L;

    /** DOCUMENT ME! */
    private float scale = 1.0f;

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int transferFunction(int v) {
        return PixelUtils.clamp((int) (v * scale));
    }

    /**
     * DOCUMENT ME!
     *
     * @param scale DOCUMENT ME!
     */
    public void setScale(float scale) {
        this.scale = scale;
        initialized = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getScale() {
        return scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Colors/Rescale...";
    }
}
