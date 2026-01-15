/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

/**
 * A warp grid.
 *
 * @author Jerry Huxtable
 */
public class WarpGrid implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4312410199770201968L;

    /** DOCUMENT ME! */
    public float[] xGrid = null;

    /** DOCUMENT ME! */
    public float[] yGrid = null;

    /** DOCUMENT ME! */
    public int rows;

    /** DOCUMENT ME! */
    public int cols;

/**
     * Creates a new WarpGrid object.
     *
     * @param rows DOCUMENT ME!
     * @param cols DOCUMENT ME!
     * @param w    DOCUMENT ME!
     * @param h    DOCUMENT ME!
     */
    public WarpGrid(int rows, int cols, int w, int h) {
        this.rows = rows;
        this.cols = cols;
        xGrid = new float[rows * cols];
        yGrid = new float[rows * cols];

        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                xGrid[index] = ((float) col * w) / (cols - 1);
                yGrid[index] = ((float) row * h) / (rows - 1);
                index++;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     * @param destination DOCUMENT ME!
     * @param intermediate DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void lerp(float t, WarpGrid destination, WarpGrid intermediate) {
        if ((rows != destination.rows) || (cols != destination.cols)) {
            throw new IllegalArgumentException(
                "source and destination are different sizes");
        }

        if ((rows != intermediate.rows) || (cols != intermediate.cols)) {
            throw new IllegalArgumentException(
                "source and intermediate are different sizes");
        }

        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                intermediate.xGrid[index] = (float) ImageMath.lerp(t,
                        xGrid[index], destination.xGrid[index]);
                intermediate.yGrid[index] = (float) ImageMath.lerp(t,
                        yGrid[index], destination.yGrid[index]);
                index++;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param inPixels DOCUMENT ME!
     * @param cols DOCUMENT ME!
     * @param rows DOCUMENT ME!
     * @param sourceGrid DOCUMENT ME!
     * @param destGrid DOCUMENT ME!
     * @param outPixels DOCUMENT ME!
     */
    public void warp(int[] inPixels, int cols, int rows, WarpGrid sourceGrid,
        WarpGrid destGrid, int[] outPixels) {
        try {
            int x;
            int y;
            int u;
            int v;
            int[] intermediate;
            WarpGrid splines;

            if ((sourceGrid.rows != destGrid.rows) ||
                    (sourceGrid.cols != destGrid.cols)) {
                throw new IllegalArgumentException(
                    "source and destination grids are different sizes");
            }

            int size = Math.max(cols, rows);
            float[] xrow = new float[size];
            float[] yrow = new float[size];
            float[] scale = new float[size + 1];
            float[] interpolated = new float[size + 1];

            int gridCols = sourceGrid.cols;
            int gridRows = sourceGrid.rows;

            splines = new WarpGrid(rows, gridCols, 1, 1);

            for (u = 0; u < gridCols; u++) {
                int i = u;

                for (v = 0; v < gridRows; v++) {
                    xrow[v] = sourceGrid.xGrid[i];
                    yrow[v] = sourceGrid.yGrid[i];
                    i += gridCols;
                }

                interpolate(yrow, xrow, 0, interpolated, 0, rows);

                i = u;

                for (y = 0; y < rows; y++) {
                    splines.xGrid[i] = interpolated[y];
                    i += gridCols;
                }
            }

            for (u = 0; u < gridCols; u++) {
                int i = u;

                for (v = 0; v < gridRows; v++) {
                    xrow[v] = destGrid.xGrid[i];
                    yrow[v] = destGrid.yGrid[i];
                    i += gridCols;
                }

                interpolate(yrow, xrow, 0, interpolated, 0, rows);

                i = u;

                for (y = 0; y < rows; y++) {
                    splines.yGrid[i] = interpolated[y];
                    i += gridCols;
                }
            }

            /* first pass: warp x using splines */
            intermediate = new int[rows * cols];

            int offset = 0;

            for (y = 0; y < rows; y++) {
                /* fit spline to x-intercepts; resample over all cols */
                interpolate(splines.xGrid, splines.yGrid, offset, scale, 0, cols);
                scale[cols] = cols;
                ImageMath.resample(inPixels, intermediate, cols, y * cols, 1,
                    scale);
                offset += gridCols;
            }

            /* create table of y-intercepts for intermediate mesh's hor splines */
            splines = new WarpGrid(gridRows, cols, 1, 1);

            offset = 0;

            int offset2 = 0;

            for (v = 0; v < gridRows; v++) {
                interpolate(sourceGrid.xGrid, sourceGrid.yGrid, offset,
                    splines.xGrid, offset2, cols);
                offset += gridCols;
                offset2 += cols;
            }

            offset = 0;
            offset2 = 0;

            for (v = 0; v < gridRows; v++) {
                interpolate(destGrid.xGrid, destGrid.yGrid, offset,
                    splines.yGrid, offset2, cols);
                offset += gridCols;
                offset2 += cols;
            }

            /* second pass: warp y */
            for (x = 0; x < cols; x++) {
                int i = x;

                for (v = 0; v < gridRows; v++) {
                    xrow[v] = splines.xGrid[i];
                    ;
                    yrow[v] = splines.yGrid[i];
                    ;
                    i += cols;
                }

                interpolate(xrow, yrow, 0, scale, 0, rows);
                scale[rows] = rows;
                ImageMath.resample(intermediate, outPixels, rows, x, cols, scale);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param xKnots DOCUMENT ME!
     * @param yKnots DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param splineY DOCUMENT ME!
     * @param splineOffset DOCUMENT ME!
     * @param splineLength DOCUMENT ME!
     */
    protected void interpolate(float[] xKnots, float[] yKnots, int offset,
        float[] splineY, int splineOffset, int splineLength) {
        int index = offset;
        float leftX;
        float rightX;
        float leftY;
        float rightY;

        leftX = xKnots[index];
        leftY = yKnots[index];
        rightX = xKnots[index + 1];
        rightY = yKnots[index + 1];

        for (int i = 0; i < splineLength; i++) {
            if (i > xKnots[index]) {
                leftX = xKnots[index];
                leftY = yKnots[index];
                index++;
                rightX = xKnots[index];
                rightY = yKnots[index];
            }

            float f = (i - leftX) / (rightX - leftX);
            splineY[splineOffset + i] = leftY + (f * (rightY - leftY));
        }
    }
}
