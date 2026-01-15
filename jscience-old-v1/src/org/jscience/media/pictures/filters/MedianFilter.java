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
public class MedianFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 2970344826880168137L;

/**
     * Creates a new MedianFilter object.
     */
    public MedianFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param array DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int median(int[] array) {
        int max;
        int maxIndex;

        for (int i = 0; i < 4; i++) {
            max = 0;
            maxIndex = 0;

            for (int j = 0; j < 9; j++) {
                if (array[j] > max) {
                    max = array[j];
                    maxIndex = j;
                }
            }

            array[maxIndex] = 0;
        }

        max = 0;

        for (int i = 0; i < 9; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int rgbMedian(int[] r, int[] g, int[] b) {
        int sum;
        int index = 0;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            sum = 0;

            for (int j = 0; j < 9; j++) {
                sum += Math.abs(r[i] - r[j]);
                sum += Math.abs(g[i] - g[j]);
                sum += Math.abs(b[i] - b[j]);
            }

            if (sum < min) {
                min = sum;
                index = i;
            }
        }

        return index;
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
        int index = 0;
        int[] argb = new int[9];
        int[] r = new int[9];
        int[] g = new int[9];
        int[] b = new int[9];
        int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int k = 0;

                for (int dy = -1; dy <= 1; dy++) {
                    int iy = y + dy;

                    if ((0 <= iy) && (iy < height)) {
                        int ioffset = iy * width;

                        for (int dx = -1; dx <= 1; dx++) {
                            int ix = x + dx;

                            if ((0 <= ix) && (ix < width)) {
                                int rgb = inPixels[ioffset + ix];
                                argb[k] = rgb;
                                r[k] = (rgb >> 16) & 0xff;
                                g[k] = (rgb >> 8) & 0xff;
                                b[k] = rgb & 0xff;
                                k++;
                            }
                        }
                    }
                }

                while (k < 9) {
                    argb[k] = 0xff000000;
                    r[k] = g[k] = b[k] = 0;
                    k++;
                }

                outPixels[index++] = argb[rgbMedian(r, g, b)];
            }
        }

        consumer.setPixels(0, 0, width, height, defaultRGBModel, outPixels, 0,
            width);
        consumer.imageComplete(status);
        inPixels = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Blur/Median";
    }
}
