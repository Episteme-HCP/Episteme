/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import java.awt.image.ColorModel;
import java.awt.image.ImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class BorderFilter extends ImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 670926158411088373L;

    /** DOCUMENT ME! */
    private int width;

    /** DOCUMENT ME! */
    private int height;

    /** DOCUMENT ME! */
    private int leftBorder;

    /** DOCUMENT ME! */
    private int rightBorder;

    /** DOCUMENT ME! */
    private int topBorder;

    /** DOCUMENT ME! */
    private int bottomBorder;

/**
     * Creates a new BorderFilter object.
     */
    public BorderFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param leftBorder DOCUMENT ME!
     */
    public void setLeftBorder(int leftBorder) {
        this.leftBorder = leftBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLeftBorder() {
        return leftBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rightBorder DOCUMENT ME!
     */
    public void setRightBorder(int rightBorder) {
        this.rightBorder = rightBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRightBorder() {
        return rightBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param topBorder DOCUMENT ME!
     */
    public void setTopBorder(int topBorder) {
        this.topBorder = topBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTopBorder() {
        return topBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bottomBorder DOCUMENT ME!
     */
    public void setBottomBorder(int bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBottomBorder() {
        return bottomBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hintflags DOCUMENT ME!
     */
    public void setHints(int hintflags) {
        hintflags &= ~TOPDOWNLEFTRIGHT;
        consumer.setHints(hintflags);
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        consumer.setDimensions(width + leftBorder + rightBorder,
            height + topBorder + bottomBorder);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param model DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, ColorModel model,
        byte[] pixels, int off, int scansize) {
        consumer.setPixels(x + leftBorder, y + topBorder, width, height, model,
            pixels, 0, scansize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param model DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, ColorModel model,
        int[] pixels, int offset, int scansize) {
        consumer.setPixels(x + leftBorder, y + topBorder, width, height, model,
            pixels, 0, scansize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Border...";
    }
}
