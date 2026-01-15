/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

/**
 * A filter which solarizes an image.
 */
public class SolarizeFilter extends TransferFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 2284566165608004967L;

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int transferFunction(int v) {
        return (v > 127) ? (2 * (v - 128)) : (2 * (127 - v));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Colors/Solarize";
    }
}
