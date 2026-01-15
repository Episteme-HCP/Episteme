/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class DistanceFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    private final static int one = 41;

    /** DOCUMENT ME! */
    private final static int sqrt2 = 58;

    /** DOCUMENT ME! */
    private final static int sqrt5 = 92;

    /** DOCUMENT ME! */
    private float factor = 1.0f;

    /** DOCUMENT ME! */
    protected Colormap colormap;

    /** DOCUMENT ME! */
    private boolean useAlpha = true;

/**
     * Creates a new DistanceFilter object.
     */
    public DistanceFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param factor DOCUMENT ME!
     */
    public void setFactor(float factor) {
        this.factor = factor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getFactor() {
        return factor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colormap DOCUMENT ME!
     */
    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Colormap getColormap() {
        return colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param useAlpha DOCUMENT ME!
     */
    public void setUseAlpha(boolean useAlpha) {
        this.useAlpha = useAlpha;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getUseAlpha() {
        return useAlpha;
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
            convertToIntegers(inPixels, outPixels, width, height);

            int max = distanceMap(outPixels, width, height);
            convertToPixels(outPixels, width, height, max);

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
     * @param pixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int distanceMap(int[] pixels, int width, int height) {
        int xmax = width - 3;
        int ymax = height - 3;
        int max = 0;
        int v;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = x + (y * width);

                if (pixels[offset] > 0) {
                    if ((x < 2) || (x > xmax) || (y < 2) || (y > ymax)) {
                        v = setEdgeValue(x, y, pixels, width, offset, xmax, ymax);
                    } else {
                        v = setValue(pixels, width, offset);
                    }

                    if (v > max) {
                        max = v;
                    }
                }
            }
        }

        for (int y = height - 1; y >= 0; y--) {
            for (int x = width - 1; x >= 0; x--) {
                int offset = x + (y * width);

                if (pixels[offset] > 0) {
                    if ((x < 2) || (x > xmax) || (y < 2) || (y > ymax)) {
                        v = setEdgeValue(x, y, pixels, width, offset, xmax, ymax);
                    } else {
                        v = setValue(pixels, width, offset);
                    }

                    if (v > max) {
                        max = v;
                    }
                }
            }
        }

        return max;
    }

    /**
     * DOCUMENT ME!
     *
     * @param inPixels DOCUMENT ME!
     * @param outPixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void convertToIntegers(int[] inPixels, int[] outPixels, int width,
        int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = x + (y * width);
                int b = useAlpha ? ((inPixels[offset] >> 24) & 0xff)
                                 : ((PixelUtils.brightness(inPixels[offset]) > 127)
                    ? 255 : 0);
                outPixels[offset] = b * one;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    private void convertToPixels(int[] pixels, int width, int height, int max) {
        int round = one / 2;

        if (max == 0) {
            max = 1;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = x + (y * width);

                if (colormap == null) {
                    int v;
                    v = (int) ((factor * (pixels[offset] + round)) / one);

                    if (v > 255) {
                        v = 255;
                    }

                    pixels[offset] = 0xff000000 | (v << 16) | (v << 8) | v;
                } else {
                    float v;
                    v = (factor * (float) (pixels[offset] + round)) / one / 255.0f;
                    pixels[offset] = colormap.getColor(v);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param xmax DOCUMENT ME!
     * @param ymax DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int setEdgeValue(int x, int y, int[] pixels, int width, int offset,
        int xmax, int ymax) {
        int min;
        int inc;
        int v;
        int r1;
        int r2;
        int r3;
        int r4;
        int r5;
        int offimage;

        r1 = offset - width - width - 2;
        r2 = r1 + width;
        r3 = r2 + width;
        r4 = r3 + width;
        r5 = r4 + width;

        offimage = pixels[r3 + 2];

        if (y < 2) {
            v = offimage + one;
        } else {
            v = pixels[r2 + 2] + one;
        }

        min = v;

        if (x < 2) {
            v = offimage + one;
        } else {
            v = pixels[r3 + 1] + one;
        }

        if (v < min) {
            min = v;
        }

        if (x > xmax) {
            v = offimage + one;
        } else {
            v = pixels[r3 + 3] + one;
        }

        if (v < min) {
            min = v;
        }

        if (y > ymax) {
            v = offimage + one;
        } else {
            v = pixels[r4 + 2] + one;
        }

        if (v < min) {
            min = v;
        }

        if ((x < 2) || (y < 2)) {
            v = offimage + sqrt2;
        } else {
            v = pixels[r2 + 1] + sqrt2;
        }

        if (v < min) {
            min = v;
        }

        if ((x > xmax) || (y < 2)) {
            v = offimage + sqrt2;
        } else {
            v = pixels[r2 + 3] + sqrt2;
        }

        if (v < min) {
            min = v;
        }

        if ((x < 2) || (y > ymax)) {
            v = offimage + sqrt2;
        } else {
            v = pixels[r4 + 1] + sqrt2;
        }

        if (v < min) {
            min = v;
        }

        if ((x > xmax) || (y > ymax)) {
            v = offimage + sqrt2;
        } else {
            v = pixels[r4 + 3] + sqrt2;
        }

        if (v < min) {
            min = v;
        }

        if ((x < 2) || (y < 2)) {
            v = offimage + sqrt5;
        } else {
            v = pixels[r1 + 1] + sqrt5;
        }

        if (v < min) {
            min = v;
        }

        if ((x > xmax) || (y < 2)) {
            v = offimage + sqrt5;
        } else {
            v = pixels[r1 + 3] + sqrt5;
        }

        if (v < min) {
            min = v;
        }

        if ((x > xmax) || (y < 2)) {
            v = offimage + sqrt5;
        } else {
            v = pixels[r2 + 4] + sqrt5;
        }

        if (v < min) {
            min = v;
        }

        if ((x > xmax) || (y > ymax)) {
            v = offimage + sqrt5;
        } else {
            v = pixels[r4 + 4] + sqrt5;
        }

        if (v < min) {
            min = v;
        }

        if ((x > xmax) || (y > ymax)) {
            v = offimage + sqrt5;
        } else {
            v = pixels[r5 + 3] + sqrt5;
        }

        if (v < min) {
            min = v;
        }

        if ((x < 2) || (y > ymax)) {
            v = offimage + sqrt5;
        } else {
            v = pixels[r5 + 1] + sqrt5;
        }

        if (v < min) {
            min = v;
        }

        if ((x < 2) || (y > ymax)) {
            v = offimage + sqrt5;
        } else {
            v = pixels[r4] + sqrt5;
        }

        if (v < min) {
            min = v;
        }

        if ((x < 2) || (y < 2)) {
            v = offimage + sqrt5;
        } else {
            v = pixels[r2] + sqrt5;
        }

        if (v < min) {
            min = v;
        }

        return pixels[offset] = min;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param offset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int setValue(int[] pixels, int width, int offset) {
        int min;
        int inc;
        int v;
        int r1;
        int r2;
        int r3;
        int r4;
        int r5;

        r1 = offset - width - width - 2;
        r2 = r1 + width;
        r3 = r2 + width;
        r4 = r3 + width;
        r5 = r4 + width;

        v = pixels[r2 + 2] + one;
        min = v;
        v = pixels[r3 + 1] + one;

        if (v < min) {
            min = v;
        }

        v = pixels[r3 + 3] + one;

        if (v < min) {
            min = v;
        }

        v = pixels[r4 + 2] + one;

        if (v < min) {
            min = v;
        }

        v = pixels[r2 + 1] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = pixels[r2 + 3] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = pixels[r4 + 1] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = pixels[r4 + 3] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = pixels[r1 + 1] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = pixels[r1 + 3] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = pixels[r2 + 4] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = pixels[r4 + 4] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = pixels[r5 + 3] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = pixels[r5 + 1] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = pixels[r4] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = pixels[r2] + sqrt5;

        if (v < min) {
            min = v;
        }

        return pixels[offset] = min;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Binary/Distance Map";
    }
}
