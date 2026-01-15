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
public class FillFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 3545801679342659341L;

    /** DOCUMENT ME! */
    private int fillColor;

/**
     * Creates a new FillFilter object.
     */
    public FillFilter() {
        this(0xff000000);
    }

/**
     * Creates a new FillFilter object.
     *
     * @param color DOCUMENT ME!
     */
    public FillFilter(int color) {
        this.fillColor = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fillColor DOCUMENT ME!
     */
    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFillColor() {
        return fillColor;
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
        return fillColor;
    }
}
