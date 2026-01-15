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
public class OffsetFilter extends ImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 8123120922961090736L;

    /** DOCUMENT ME! */
    private int width;

    /** DOCUMENT ME! */
    private int height;

    /** DOCUMENT ME! */
    private int xOffset;

    /** DOCUMENT ME! */
    private int yOffset;

    /** DOCUMENT ME! */
    private boolean wrap;

/**
     * Creates a new OffsetFilter object.
     */
    public OffsetFilter() {
        this(0, 0, true);
    }

/**
     * Creates a new OffsetFilter object.
     *
     * @param xOffset DOCUMENT ME!
     * @param yOffset DOCUMENT ME!
     * @param wrap    DOCUMENT ME!
     */
    public OffsetFilter(int xOffset, int yOffset, boolean wrap) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.wrap = wrap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xOffset DOCUMENT ME!
     */
    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getXOffset() {
        return xOffset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param yOffset DOCUMENT ME!
     */
    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getYOffset() {
        return yOffset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param wrap DOCUMENT ME!
     */
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getWrap() {
        return wrap;
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

        while (xOffset < 0)
            xOffset += width;

        while (yOffset < 0)
            yOffset += height;

        xOffset %= width;
        yOffset %= height;
        consumer.setDimensions(width, height);
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
        consumer.setPixels(x + xOffset, y + yOffset, width - xOffset,
            height - yOffset, model, pixels, 0, scansize);

        if (wrap) {
            if ((x + w + xOffset) > width) {
                consumer.setPixels(0, y + yOffset, xOffset - (width - x - w),
                    height - yOffset, model, pixels, w - xOffset, scansize);

                if ((y + h + yOffset) > height) {
                    consumer.setPixels(0, 0, xOffset - (width - x - w),
                        yOffset - (height - y - h), model, pixels,
                        w - xOffset + ((h - yOffset) * scansize), scansize);
                }
            }

            if ((y + h + yOffset) > height) {
                consumer.setPixels(x + xOffset, 0, width - xOffset,
                    yOffset - (height - y - h), model, pixels,
                    (h - yOffset) * scansize, scansize);
            }
        }
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
        consumer.setPixels(x + xOffset, y + yOffset, width - xOffset,
            height - yOffset, model, pixels, 0, scansize);

        if (wrap) {
            if ((x + w + xOffset) > width) {
                consumer.setPixels(0, y + yOffset, xOffset - (width - x - w),
                    height - yOffset, model, pixels, w - xOffset, scansize);

                if ((y + h + yOffset) > height) {
                    consumer.setPixels(0, 0, xOffset - (width - x - w),
                        yOffset - (height - y - h), model, pixels,
                        w - xOffset + ((h - yOffset) * scansize), scansize);
                }
            }

            if ((y + h + yOffset) > height) {
                consumer.setPixels(x + xOffset, 0, width - xOffset,
                    yOffset - (height - y - h), model, pixels,
                    (h - yOffset) * scansize, scansize);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Offset...";
    }
}
