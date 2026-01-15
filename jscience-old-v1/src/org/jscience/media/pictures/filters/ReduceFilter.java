/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import java.awt.image.RGBImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class ReduceFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    private int numLevels;

    /** DOCUMENT ME! */
    private int[] levels;

    /** DOCUMENT ME! */
    private boolean initialized = false;

/**
     * Creates a new ReduceFilter object.
     */
    public ReduceFilter() {
        setNumLevels(6);
    }

    /**
     * DOCUMENT ME!
     *
     * @param numLevels DOCUMENT ME!
     */
    public void setNumLevels(int numLevels) {
        this.numLevels = numLevels;
        initialized = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumLevels() {
        return numLevels;
    }

    /**
     * DOCUMENT ME!
     */
    protected void initialize() {
        levels = new int[256];

        if (numLevels != 1) {
            for (int i = 0; i < 256; i++)
                levels[i] = (255 * ((numLevels * i) / 256)) / (numLevels - 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param rgb DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int filterRGB(int x, int y, int rgb) {
        if (!initialized) {
            initialized = true;
            initialize();
        }

        int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r = levels[r];
        g = levels[g];
        b = levels[b];

        return a | (r << 16) | (g << 8) | b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Colors/Posterize...";
    }
}
