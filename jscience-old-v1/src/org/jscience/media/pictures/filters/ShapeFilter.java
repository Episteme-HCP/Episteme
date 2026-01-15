/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;


// original code Copyright (C) Jerry Huxtable 1998
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class ShapeFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    public final static int LINEAR = 0;

    /** DOCUMENT ME! */
    public final static int CIRCLE_UP = 1;

    /** DOCUMENT ME! */
    public final static int CIRCLE_DOWN = 2;

    /** DOCUMENT ME! */
    public final static int SMOOTH = 3;

    /** DOCUMENT ME! */
    private final static int one = 41;

    /** DOCUMENT ME! */
    private final static int sqrt2 = (int) (41 * Math.sqrt(2));

    /** DOCUMENT ME! */
    private final static int sqrt5 = (int) (41 * Math.sqrt(5));

    /** DOCUMENT ME! */
    private float factor = 1.0f;

    /** DOCUMENT ME! */
    protected Colormap colormap;

    /** DOCUMENT ME! */
    private boolean useAlpha = true;

    /** DOCUMENT ME! */
    private boolean invert = false;

    /** DOCUMENT ME! */
    private boolean merge = false;

    /** DOCUMENT ME! */
    private int type;

/**
     * Creates a new ShapeFilter object.
     */
    public ShapeFilter() {
        colormap = new LinearColormap();
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
     * @param type DOCUMENT ME!
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param invert DOCUMENT ME!
     */
    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getInvert() {
        return invert;
    }

    /**
     * DOCUMENT ME!
     *
     * @param merge DOCUMENT ME!
     */
    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getMerge() {
        return merge;
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

        int width = originalSpace.width;
        int height = originalSpace.height;

        if (false) {
            float[] map = new float[width * height];
            float max = gddistanceMap(inPixels, map, width, height);
            applyMap(map, inPixels, width, height, max);
        } else {
            int[] map = new int[width * height];
            makeMap(inPixels, map, width, height);

            int max = distanceMap(map, width, height);
            applyMap(map, inPixels, width, height, max);
        }

        consumer.setPixels(0, 0, width, height, defaultRGBModel, inPixels, 0,
            width);
        consumer.imageComplete(status);
    }

    /**
     * DOCUMENT ME!
     *
     * @param map DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int distanceMap(int[] map, int width, int height) {
        int xmax = width - 3;
        int ymax = height - 3;
        int max = 0;
        int v;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = x + (y * width);

                if (map[offset] > 0) {
                    if ((x < 2) || (x > xmax) || (y < 2) || (y > ymax)) {
                        v = setEdgeValue(x, y, map, width, offset, xmax, ymax);
                    } else {
                        v = setValue(map, width, offset);
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

                if (map[offset] > 0) {
                    if ((x < 2) || (x > xmax) || (y < 2) || (y > ymax)) {
                        v = setEdgeValue(x, y, map, width, offset, xmax, ymax);
                    } else {
                        v = setValue(map, width, offset);
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
     * @param pixels DOCUMENT ME!
     * @param map DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void makeMap(int[] pixels, int[] map, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = x + (y * width);
                int b = useAlpha ? ((pixels[offset] >> 24) & 0xff)
                                 : PixelUtils.brightness(pixels[offset]);
                map[offset] = b * one;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param map DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    private void applyMap(int[] map, int[] pixels, int width, int height,
        int max) {
        if (max == 0) {
            max = 1;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = x + (y * width);
                int m = map[offset];
                float v = 0;
                int sa = 0;
                int sr = 0;
                int sg = 0;
                int sb = 0;

                if (m == 0) {
                    // default color
                    sa = sr = sg = sb = 0;
                } else {
                    // get V from map
                    v = ImageMath.clamp((factor * m) / max, 0, 1);

                    switch (type) {
                    case CIRCLE_UP:
                        v = (ImageMath.circleUp(v));

                        break;

                    case CIRCLE_DOWN:
                        v = (ImageMath.circleDown(v));

                        break;

                    case SMOOTH:
                        v = (ImageMath.smoothStep(0, 1, v));

                        break;
                    }

                    if (colormap == null) {
                        sr = sg = sb = (int) (v * 255);
                    } else {
                        int c = (colormap.getColor(v));

                        sr = (c >> 16) & 0xFF;
                        sg = (c >> 8) & 0xFF;
                        sb = (c) & 0xFF;
                    }

                    sa = useAlpha ? ((pixels[offset] >> 24) & 0xff)
                                  : PixelUtils.brightness(pixels[offset]);

                    // invert v if necessary
                    if (invert) {
                        sr = 255 - sr;
                        sg = 255 - sg;
                        sb = 255 - sb;
                    }
                }

                // write results
                if (merge) {
                    // merge with source
                    int transp = 255;
                    int col = pixels[offset];

                    int a = (col & 0xFF000000) >> 24;
                    int r = (col & 0xFF0000) >> 16;
                    int g = (col & 0xFF00) >> 8;
                    int b = (col & 0xFF);

                    r = (int) (((sr * r) / transp));
                    g = (int) (((sg * g) / transp));
                    b = (int) (((sb * b) / transp));

                    // clip colors
                    if (r < 0) {
                        r = 0;
                    }

                    if (r > 255) {
                        r = 255;
                    }

                    if (g < 0) {
                        g = 0;
                    }

                    if (g > 255) {
                        g = 255;
                    }

                    if (b < 0) {
                        b = 0;
                    }

                    if (b > 255) {
                        b = 255;
                    }

                    pixels[offset] = (a << 24) | (r << 16) | (g << 8) | b;
                } else {
                    // write gray shades
                    pixels[offset] = (sa << 24) | (sr << 16) | (sg << 8) | sb;
                }
            }
        }

        //System.out.println();
        //System.out.println();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param map DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param xmax DOCUMENT ME!
     * @param ymax DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int setEdgeValue(int x, int y, int[] map, int width, int offset,
        int xmax, int ymax) {
        int min;
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

        if ((y == 0) || (x == 0) || (y == (ymax + 2)) || (x == (xmax + 2))) {
            return map[offset] = one;
        }

        v = map[r2 + 2] + one;
        min = v;

        v = map[r3 + 1] + one;

        if (v < min) {
            min = v;
        }

        v = map[r3 + 3] + one;

        if (v < min) {
            min = v;
        }

        v = map[r4 + 2] + one;

        if (v < min) {
            min = v;
        }

        v = map[r2 + 1] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = map[r2 + 3] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = map[r4 + 1] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = map[r4 + 3] + sqrt2;

        if (v < min) {
            min = v;
        }

        if ((y == 1) || (x == 1) || (y == (ymax + 1)) || (x == (xmax + 1))) {
            return map[offset] = min;
        }

        v = map[r1 + 1] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r1 + 3] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r2 + 4] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r4 + 4] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r5 + 3] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r5 + 1] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r4] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r2] + sqrt5;

        if (v < min) {
            min = v;
        }

        return map[offset] = min;
    }

    /**
     * DOCUMENT ME!
     *
     * @param map DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param offset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int setValue(int[] map, int width, int offset) {
        int min;
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

        v = map[r2 + 2] + one;
        min = v;
        v = map[r3 + 1] + one;

        if (v < min) {
            min = v;
        }

        v = map[r3 + 3] + one;

        if (v < min) {
            min = v;
        }

        v = map[r4 + 2] + one;

        if (v < min) {
            min = v;
        }

        v = map[r2 + 1] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = map[r2 + 3] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = map[r4 + 1] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = map[r4 + 3] + sqrt2;

        if (v < min) {
            min = v;
        }

        v = map[r1 + 1] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r1 + 3] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r2 + 4] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r4 + 4] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r5 + 3] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r5 + 1] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r4] + sqrt5;

        if (v < min) {
            min = v;
        }

        v = map[r2] + sqrt5;

        if (v < min) {
            min = v;
        }

        return map[offset] = min;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param map DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float gddistanceMap(int[] pixels, float[] map, int width, int height) {
        float max_iterations;
        float[] distp_cur;
        float[] distp_prev;
        float min_prev;
        float float_tmp;
        int minimum;
        int min_left;
        int length;
        int i;
        int j;
        int k;
        int fraction;
        int prev_frac;
        int x;
        int y;
        int end;
        int inc;

        max_iterations = 0.0f;

        length = width + 1;
        distp_prev = new float[length];

        for (i = 0; i < length; i++)
            distp_prev[i] = 0.0f;

        distp_cur = new float[length];

        for (i = 0; i < length; i++)
            distp_cur[i] = 0.0f;

        for (i = 0; i < height; i++) {
            for (int m = 0; m < length; m++)
                distp_cur[m] = 0.0f;

            for (j = 0; j < width; j++) {
                min_prev = Math.min(distp_cur[j], distp_prev[j + 1]);
                min_left = Math.min((width - j - 1), (height - i - 1));
                minimum = (int) Math.min(min_left, min_prev);
                fraction = 255;

                int src = 0;

                //	This might need to be changed to 0 instead of k = (minimum) ? (minimum - 1) : 0
                for (k = 0 /*(minimum != 0) ? (minimum - 1) : 0*/;
                        k <= minimum; k++) {
                    x = j;
                    y = i + k;
                    end = y - k;

                    while (y >= end) {
                        int offset = (y * width) + x;
                        int boundary = Math.min(width, height);
                        boundary = Math.min(boundary, (y - end)) + 1;
                        inc = 1 - width;

                        while (boundary-- != 0) {
                            src = useAlpha ? ((pixels[offset] >> 24) & 0xff)
                                           : PixelUtils.brightness(pixels[offset]);

                            if (src == 0) {
                                minimum = k;
                                y = -1;

                                break;
                            }

                            if (src < fraction) {
                                fraction = src;
                            }

                            x++;
                            y--;
                            offset += inc;
                        }
                    }
                }

                if (src != 0) {
                    //	If min_left != min_prev use the previous fraction
                    //	 if it is less than the one found
                    if (min_left != minimum) {
                        prev_frac = (int) (255 * (min_prev - minimum));

                        if (prev_frac == 255) {
                            prev_frac = 0;
                        }

                        fraction = Math.min(fraction, prev_frac);
                    }

                    minimum++;
                }

                float_tmp = distp_cur[j + 1] = minimum + (fraction / 256.0f);

                if (float_tmp > max_iterations) {
                    max_iterations = float_tmp;
                }
            }

            int index = i * width;

            for (int m = 1; m <= width; m++)
                map[index++] = distp_cur[m];

            float[] tmp = distp_prev;
            distp_prev = distp_cur;
            distp_cur = tmp;
        }

        return max_iterations;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param map DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float ddistanceMap(int[] pixels, float[] map, int width, int height) {
        float max = 0;

        for (int y = 0; y < height; y++) {
            float d = 0;
            int offset = y * width;

            for (int x = 0; x < width; x++) {
                int v = useAlpha ? ((pixels[offset] >> 24) & 0xff)
                                 : PixelUtils.brightness(pixels[offset]);

                if (v != 0) {
                    map[offset] = Math.min(d + 255, v * 255);
                    d += 255;
                } else {
                    d = 0;
                }

                //				map[offset] = x;
                max = Math.max(max, map[offset]);
                offset++;
            }
        }

        return max;
    }

    /**
     * DOCUMENT ME!
     *
     * @param map DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    private void applyMap(float[] map, int[] pixels, int width, int height,
        float max) {
        if (max == 0) {
            max = 1;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = x + (y * width);
                int m = (int) map[offset];
                float v = 0;
                int sa = 0;
                int sr = 0;
                int sg = 0;
                int sb = 0;

                if (m == 0) {
                    // default color
                    sa = sr = sg = sb = 0;
                } else {
                    // get V from map
                    v = ImageMath.clamp((factor * m) / max, 0, 1);

                    switch (type) {
                    case CIRCLE_UP:
                        v = (ImageMath.circleUp(v));

                        break;

                    case CIRCLE_DOWN:
                        v = (ImageMath.circleDown(v));

                        break;

                    case SMOOTH:
                        v = (ImageMath.smoothStep(0, 1, v));

                        break;
                    }

                    if (colormap == null) {
                        sr = sg = sb = (int) (v * 255);
                    } else {
                        int c = (colormap.getColor(v));

                        sr = (c >> 16) & 0xFF;
                        sg = (c >> 8) & 0xFF;
                        sb = (c) & 0xFF;
                    }

                    sa = useAlpha ? ((pixels[offset] >> 24) & 0xff)
                                  : PixelUtils.brightness(pixels[offset]);

                    // invert v if necessary
                    if (invert) {
                        sr = 255 - sr;
                        sg = 255 - sg;
                        sb = 255 - sb;
                    }
                }

                // write results
                if (merge) {
                    // merge with source
                    int transp = 255;
                    int col = pixels[offset];

                    int a = (col & 0xFF000000) >> 24;
                    int r = (col & 0xFF0000) >> 16;
                    int g = (col & 0xFF00) >> 8;
                    int b = (col & 0xFF);

                    r = (int) (((sr * r) / transp));
                    g = (int) (((sg * g) / transp));
                    b = (int) (((sb * b) / transp));

                    // clip colors
                    if (r < 0) {
                        r = 0;
                    }

                    if (r > 255) {
                        r = 255;
                    }

                    if (g < 0) {
                        g = 0;
                    }

                    if (g > 255) {
                        g = 255;
                    }

                    if (b < 0) {
                        b = 0;
                    }

                    if (b > 255) {
                        b = 255;
                    }

                    pixels[offset] = (a << 24) | (r << 16) | (g << 8) | b;
                } else {
                    // write gray shades
                    pixels[offset] = (sa << 24) | (sr << 16) | (sg << 8) | sb;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Stylize/Shapeburst...";
    }
}
