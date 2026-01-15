/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import java.awt.*;


/**
 * A Filter to pixellate images.
 */
public class BlockFilter extends TransformFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 8077109551486196569L;

    /** DOCUMENT ME! */
    private int blockSize = 2;

/**
     * Creates a new BlockFilter object.
     */
    public BlockFilter() {
    }

    /**
     * Set the pixel block size
     *
     * @param blockSize the number of pixels along each block edge
     */
    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    /**
     * Get the pixel block size
     *
     * @return the number of pixels along each block edge
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transform(int x, int y, Point out) {
        out.x = (x / blockSize) * blockSize;
        out.y = (y / blockSize) * blockSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        out[0] = (x / blockSize) * blockSize;
        out[1] = (y / blockSize) * blockSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Stylize/Mosaic...";
    }
}
