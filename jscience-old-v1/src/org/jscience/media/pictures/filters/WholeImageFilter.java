/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.ImageFilter;
import java.awt.image.IndexColorModel;


/**
 * A filter which acts as a superclass for filters which need to have the
 * whole image in memory to do their stuff.
 */
public abstract class WholeImageFilter extends ImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    protected Rectangle transformedSpace;

    /** DOCUMENT ME! */
    protected Rectangle originalSpace;

    /** DOCUMENT ME! */
    protected ColorModel defaultRGBModel;

    /** DOCUMENT ME! */
    protected int[] inPixels;

    /** DOCUMENT ME! */
    protected byte[] inBytePixels;

    /**
     * If true, then image pixels for images with an IndexColorModel
     * ndex will be accumulated as bytes in inBytePixels. If false, they will
     * be converted to the default RGB color model and accumulated in
     * inPixels.
     */
    protected boolean canFilterIndexColorModel = false;

/**
     * Construct a WholeImageFilter
     */
    public WholeImageFilter() {
        defaultRGBModel = ColorModel.getRGBdefault();
    }

    /**
     * DOCUMENT ME!
     *
     * @param rect DOCUMENT ME!
     */
    protected void transformSpace(Rectangle rect) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        originalSpace = new Rectangle(0, 0, width, height);
        transformedSpace = new Rectangle(0, 0, width, height);
        transformSpace(transformedSpace);
        consumer.setDimensions(transformedSpace.width, transformedSpace.height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     */
    public void setColorModel(ColorModel model) {
        if (canFilterIndexColorModel && model instanceof IndexColorModel) {
            consumer.setColorModel(model);
        } else {
            consumer.setColorModel(defaultRGBModel);
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
     * @param off DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, ColorModel model,
        byte[] pixels, int off, int scansize) {
        int index = (y * originalSpace.width) + x;
        int srcindex = off;
        int srcinc = scansize - w;
        int indexinc = originalSpace.width - w;

        if (canFilterIndexColorModel) {
            if (inBytePixels == null) {
                inBytePixels = new byte[originalSpace.width * originalSpace.height];
            }

            for (int dy = 0; dy < h; dy++) {
                for (int dx = 0; dx < w; dx++)
                    inBytePixels[index++] = pixels[srcindex++];

                srcindex += srcinc;
                index += indexinc;
            }
        } else {
            if (inPixels == null) {
                inPixels = new int[originalSpace.width * originalSpace.height];
            }

            for (int dy = 0; dy < h; dy++) {
                for (int dx = 0; dx < w; dx++)
                    inPixels[index++] = model.getRGB(pixels[srcindex++] & 0xff);

                srcindex += srcinc;
                index += indexinc;
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
     * @param off DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, ColorModel model,
        int[] pixels, int off, int scansize) {
        int index = (y * originalSpace.width) + x;
        int srcindex = off;
        int srcinc = scansize - w;
        int indexinc = originalSpace.width - w;

        if (inPixels == null) {
            inPixels = new int[originalSpace.width * originalSpace.height];
        }

        for (int dy = 0; dy < h; dy++) {
            for (int dx = 0; dx < w; dx++)
                inPixels[index++] = model.getRGB(pixels[srcindex++]);

            srcindex += srcinc;
            index += indexinc;
        }
    }
}
