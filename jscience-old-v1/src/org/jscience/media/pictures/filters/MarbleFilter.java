/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import org.jscience.media.pictures.filters.math.*;


/**
 * This filter applies a marbling effect to an image, displacing pixels by
 * random amounts.
 */
public class MarbleFilter extends TransformFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -3658170437130333021L;

    /** DOCUMENT ME! */
    public float[] sinTable;

    /** DOCUMENT ME! */
    public float[] cosTable;

    /** DOCUMENT ME! */
    public float xScale = 4;

    /** DOCUMENT ME! */
    public float yScale = 4;

    /** DOCUMENT ME! */
    public float amount = 1;

    /** DOCUMENT ME! */
    public float turbulence = 1;

/**
     * Creates a new MarbleFilter object.
     */
    public MarbleFilter() {
        setEdgeAction(CLAMP);
    }

    /**
     * DOCUMENT ME!
     *
     * @param xScale DOCUMENT ME!
     */
    public void setXScale(float xScale) {
        this.xScale = xScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getXScale() {
        return xScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param yScale DOCUMENT ME!
     */
    public void setYScale(float yScale) {
        this.yScale = yScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getYScale() {
        return yScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param turbulence DOCUMENT ME!
     */
    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getTurbulence() {
        return turbulence;
    }

    /**
     * DOCUMENT ME!
     */
    private void initialize() {
        sinTable = new float[256];
        cosTable = new float[256];

        for (int i = 0; i < 256; i++) {
            float angle = (ImageMath.TWO_PI * i) / 256f * turbulence;
            sinTable[i] = (float) (-yScale * Math.sin(angle));
            cosTable[i] = (float) (yScale * Math.cos(angle));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int displacementMap(int x, int y) {
        return PixelUtils.clamp((int) (127 * (1 +
            Noise.noise2(x / xScale, y / xScale))));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        int displacement = displacementMap(x, y);
        out[0] = x + sinTable[displacement];
        out[1] = y + cosTable[displacement];
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status) {
        initialize();
        super.imageComplete(status);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Marble...";
    }
}
