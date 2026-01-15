/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class ShadowFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 6310370419462785691L;

    /** DOCUMENT ME! */
    private int radius = 5;

    /** DOCUMENT ME! */
    private int xOffset = 5;

    /** DOCUMENT ME! */
    private int yOffset = 5;

    /** DOCUMENT ME! */
    private int opacity = 255;

    /** DOCUMENT ME! */
    private int shadowColor = 0xff000000;

    /** DOCUMENT ME! */
    private int backgroundColor = 0x00000000;

/**
     * Creates a new ShadowFilter object.
     */
    public ShadowFilter() {
    }

/**
     * Creates a new ShadowFilter object.
     *
     * @param radius  DOCUMENT ME!
     * @param xOffset DOCUMENT ME!
     * @param yOffset DOCUMENT ME!
     * @param opacity DOCUMENT ME!
     */
    public ShadowFilter(int radius, int xOffset, int yOffset, int opacity) {
        this.radius = radius;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.opacity = opacity;
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
     * Set the radius of the kernel, and hence the amount of blur. The
     * bigger the radius, the longer this filter will take.
     *
     * @param radius the radius of the blur in pixels.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Get the radius of the kernel.
     *
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param opacity DOCUMENT ME!
     */
    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOpacity() {
        return opacity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param shadowColor DOCUMENT ME!
     */
    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getShadowColor() {
        return shadowColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param backgroundColor DOCUMENT ME!
     */
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    protected void transformSpace(Rectangle r) {
        r.width += (Math.abs(xOffset) + (2 * radius));
        r.height += (Math.abs(yOffset) + (2 * radius));
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status) {
        try {
            if ((status == IMAGEERROR) || (status == IMAGEABORTED)) {
                consumer.imageComplete(status);

                return;
            }

            int width = originalSpace.width;
            int height = originalSpace.height;

            int outWidth = transformedSpace.width;
            int outHeight = transformedSpace.height;
            int[] outPixels = new int[outWidth * outHeight];

            int iIn = 0;
            int iOut = 0;

            int shadow = (opacity << 24) & 0xff000000;
            shadow |= (shadowColor & 0xffffff);

            int radius2 = radius / 2;

            int topShadow = Math.max(yOffset, radius2) + radius2;
            int leftShadow = Math.max(xOffset, radius2) + radius2;

            iIn = 0;

            for (int y = 0; y < height; y++) {
                iOut = ((topShadow + y) * outWidth) + leftShadow;

                for (int x = 0; x < width; x++) {
                    int v = (inPixels[iIn++] >> 24) & 0xff;
                    outPixels[iOut++] = (v != 0) ? shadow : (shadow & 0xffffff);
                }
            }

            if (radius > 0) {
                Kernel[] kernels = GaussianFilter.separatedKernels(radius);
                float[] matrix;
                int rows = kernels[1].rows;
                int cols = kernels[0].cols;
                int rows2 = rows / 2;
                int cols2 = cols / 2;
                int index;

                byte[] shadowPixels = new byte[outWidth * outHeight];

                matrix = kernels[1].matrix;
                index = 0;

                for (int y = 0; y < outHeight; y++) {
                    int ioffset = y * outWidth;

                    for (int x = 0; x < outWidth; x++) {
                        float r = 0;
                        float g = 0;
                        float b = 0;
                        float a = 0;

                        for (int col = -cols2; col <= cols2; col++) {
                            int ix = x + col;

                            if (!((0 <= ix) && (ix < outWidth))) {
                                ix = x;
                            }

                            int rgb = outPixels[ioffset + ix];
                            float f = matrix[cols2 + col];

                            if (f != 0) {
                                a += (f * ((rgb >> 24) & 0xff));
                            }
                        }

                        if (a > 255f) {
                            a = 255f;
                        }

                        shadowPixels[index] = (byte) a;
                        index++;
                    }
                }

                matrix = kernels[1].matrix;
                index = 0;

                for (int y = 0; y < outHeight; y++) {
                    for (int x = 0; x < outWidth; x++) {
                        float r = 0;
                        float g = 0;
                        float b = 0;
                        float a = 0;

                        for (int row = -rows2; row <= rows2; row++) {
                            int iy = y + row;
                            int ioffset;

                            if ((0 <= iy) && (iy < outHeight)) {
                                ioffset = iy * outWidth;
                            } else {
                                ioffset = y * outWidth;
                            }

                            int s = shadowPixels[ioffset + x] & 0xff;
                            float f = matrix[row + rows2];

                            if (f != 0) {
                                a += (f * s);
                            }
                        }

                        if (a > 255f) {
                            a = 255f;
                        }

                        if (backgroundColor != 0) {
                            outPixels[index] = PixelUtils.combinePixels(shadowColor,
                                    backgroundColor, PixelUtils.NORMAL, (int) a);
                        } else {
                            outPixels[index] = (((int) a) << 24) |
                                (shadowColor & 0xffffff);
                        }

                        index++;
                    }
                }
            }

            iIn = 0;

            for (int y = 0; y < height; y++) {
                iOut = ((((topShadow + y) - yOffset) * outWidth) + leftShadow) -
                    xOffset;

                for (int x = 0; x < width; x++) {
                    outPixels[iOut] = PixelUtils.combinePixels(inPixels[iIn],
                            outPixels[iOut], PixelUtils.NORMAL);
                    iIn++;
                    iOut++;
                }
            }

            consumer.setPixels(0, 0, outWidth, outHeight, defaultRGBModel,
                outPixels, 0, outWidth);
            consumer.imageComplete(status);
            inPixels = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Stylize/Drop Shadow...";
    }
}
