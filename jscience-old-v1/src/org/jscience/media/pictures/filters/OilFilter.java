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
public class OilFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1722613531684653826L;

    /** DOCUMENT ME! */
    public int range = 3;

/**
     * Creates a new OilFilter object.
     */
    public OilFilter() {
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
        int[] rHistogram = new int[256];
        int[] gHistogram = new int[256];
        int[] bHistogram = new int[256];
        int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int i = 0; i < 256; i++)
                    rHistogram[i] = gHistogram[i] = bHistogram[i] = 0;

                for (int row = -range; row <= range; row++) {
                    int iy = y + row;
                    int ioffset;

                    if ((0 <= iy) && (iy < height)) {
                        ioffset = iy * width;

                        for (int col = -range; col <= range; col++) {
                            int ix = x + col;

                            if ((0 <= ix) && (ix < width)) {
                                int rgb = inPixels[ioffset + ix];
                                rHistogram[(rgb >> 16) & 0xff]++;
                                gHistogram[(rgb >> 8) & 0xff]++;
                                bHistogram[rgb & 0xff]++;
                            }
                        }
                    }
                }

                int r = 0;
                int g = 0;
                int b = 0;

                for (int i = 1; i < 256; i++) {
                    if (rHistogram[i] > rHistogram[r]) {
                        r = i;
                    }

                    if (gHistogram[i] > gHistogram[g]) {
                        g = i;
                    }

                    if (bHistogram[i] > bHistogram[b]) {
                        b = i;
                    }
                }

                outPixels[index++] = 0xff000000 | (r << 16) | (g << 8) | b;
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
        return "Stylize/Oil...";
    }
}
