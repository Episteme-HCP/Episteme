/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import java.awt.image.RGBImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class RGBAdjustFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 3509907597266563800L;

    /** DOCUMENT ME! */
    public float rFactor;

    /** DOCUMENT ME! */
    public float gFactor;

    /** DOCUMENT ME! */
    public float bFactor;

/**
     * Creates a new RGBAdjustFilter object.
     */
    public RGBAdjustFilter() {
        this(0, 0, 0);
    }

/**
     * Creates a new RGBAdjustFilter object.
     *
     * @param r DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    public RGBAdjustFilter(float r, float g, float b) {
        rFactor = 1 + r;
        gFactor = 1 + g;
        bFactor = 1 + b;
        canFilterIndexColorModel = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param rgb DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r = PixelUtils.clamp((int) (r * rFactor));
        g = PixelUtils.clamp((int) (g * gFactor));
        b = PixelUtils.clamp((int) (b * bFactor));

        return a | (r << 16) | (g << 8) | b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Colors/Adjust RGB...";
    }
}
