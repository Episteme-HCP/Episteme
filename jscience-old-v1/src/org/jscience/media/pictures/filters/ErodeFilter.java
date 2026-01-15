/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

/**
 * Given a binary image, this filter performs binary erosion, setting all
 * removed pixels to the given 'new' color.
 */
public class ErodeFilter extends BinaryFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1791577333635724103L;

    /** DOCUMENT ME! */
    protected int threshold = 2;

/**
     * Creates a new ErodeFilter object.
     */
    public ErodeFilter() {
        newColor = 0xffffffff;
    }

    /**
     * Set the threshold - the number of neighbouring pixels for
     * dilation to occur.
     *
     * @param threshold the new threshold
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Return the threshold - the number of neighbouring pixels for
     * dilation to occur.
     *
     * @return the current threshold
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status) {
        if ((status == IMAGEERROR) || (status == IMAGEABORTED)) {
            consumer.imageComplete(status);

            return;
        }

        try {
            int width = originalSpace.width;
            int height = originalSpace.height;
            int[] outPixels = new int[width * height];

            for (int i = 0; i < iterations; i++) {
                int index = 0;

                if (i > 0) {
                    int[] t = inPixels;
                    inPixels = outPixels;
                    outPixels = t;
                }

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int pixel = inPixels[(y * width) + x];

                        if (blackFunction.isBlack(pixel)) {
                            int neighbours = 0;

                            for (int dy = -1; dy <= 1; dy++) {
                                int iy = y + dy;
                                int ioffset;

                                if ((0 <= iy) && (iy < height)) {
                                    ioffset = iy * width;

                                    for (int dx = -1; dx <= 1; dx++) {
                                        int ix = x + dx;

                                        if (!((dy == 0) && (dx == 0)) &&
                                                (0 <= ix) && (ix < width)) {
                                            int rgb = inPixels[ioffset + ix];

                                            if (!blackFunction.isBlack(rgb)) {
                                                neighbours++;
                                            }
                                        }
                                    }
                                }
                            }

                            if (neighbours >= threshold) {
                                if (colormap != null) {
                                    pixel = colormap.getColor((float) i / iterations);
                                } else {
                                    pixel = newColor;
                                }
                            }
                        }

                        outPixels[index++] = pixel;
                    }
                }
            }

            consumer.setPixels(0, 0, width, height, defaultRGBModel, outPixels,
                0, width);
            consumer.imageComplete(status);
            inPixels = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Binary/Erode...";
    }
}
