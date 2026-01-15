/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters.math;

import org.jscience.media.pictures.filters.ImageMath;
import org.jscience.media.pictures.filters.PixelUtils;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class ImageFunction2D implements Function2D {
    /** DOCUMENT ME! */
    public final static int ZERO = 0;

    /** DOCUMENT ME! */
    public final static int CLAMP = 1;

    /** DOCUMENT ME! */
    public final static int WRAP = 2;

    /** DOCUMENT ME! */
    protected int[] pixels;

    /** DOCUMENT ME! */
    protected int width;

    /** DOCUMENT ME! */
    protected int height;

    /** DOCUMENT ME! */
    protected int edgeAction = ZERO;

/**
     * Creates a new ImageFunction2D object.
     *
     * @param image DOCUMENT ME!
     */
    public ImageFunction2D(Image image) {
        this(image, ZERO);
    }

/**
     * Creates a new ImageFunction2D object.
     *
     * @param image      DOCUMENT ME!
     * @param edgeAction DOCUMENT ME!
     * @throws RuntimeException DOCUMENT ME!
     */
    public ImageFunction2D(Image image, int edgeAction) {
        PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, null, 0, -1);

        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException("interrupted waiting for pixels!");
        }

        if ((pg.status() & ImageObserver.ABORT) != 0) {
            throw new RuntimeException("image fetch aborted");
        }

        init((int[]) pg.getPixels(), pg.getWidth(), pg.getHeight(), edgeAction);
    }

/**
     * Creates a new ImageFunction2D object.
     *
     * @param pixels     DOCUMENT ME!
     * @param width      DOCUMENT ME!
     * @param height     DOCUMENT ME!
     * @param edgeAction DOCUMENT ME!
     */
    public ImageFunction2D(int[] pixels, int width, int height, int edgeAction) {
        init(pixels, width, height, edgeAction);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param edgeAction DOCUMENT ME!
     */
    public void init(int[] pixels, int width, int height, int edgeAction) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.edgeAction = edgeAction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float evaluate(float x, float y) {
        int ix = (int) x;
        int iy = (int) y;

        if (edgeAction == WRAP) {
            ix = ImageMath.mod(ix, width);
            iy = ImageMath.mod(iy, height);
        } else if ((ix < 0) || (iy < 0) || (ix >= width) || (iy >= height)) {
            if (edgeAction == ZERO) {
                return 0;
            }

            if (ix < 0) {
                ix = 0;
            } else if (ix >= width) {
                ix = width - 1;
            }

            if (iy < 0) {
                iy = 0;
            } else if (iy >= height) {
                iy = height - 1;
            }
        }

        return PixelUtils.brightness(pixels[(iy * width) + ix]) / 255.0f;
    }

    /**
     * DOCUMENT ME!
     *
     * @param edgeAction DOCUMENT ME!
     */
    public void setEdgeAction(int edgeAction) {
        this.edgeAction = edgeAction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getEdgeAction() {
        return edgeAction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return height;
    }
}
