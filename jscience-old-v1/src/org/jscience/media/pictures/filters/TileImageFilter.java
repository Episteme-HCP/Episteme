package org.jscience.media.pictures.filters;

import java.awt.image.ColorModel;
import java.awt.image.ImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class TileImageFilter extends ImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4926390225069192478L;

    /** DOCUMENT ME! */
    public static final int FLIP_NONE = 0;

    /** DOCUMENT ME! */
    public static final int FLIP_H = 1;

    /** DOCUMENT ME! */
    public static final int FLIP_V = 2;

    /** DOCUMENT ME! */
    public static final int FLIP_HV = 3;

    /** DOCUMENT ME! */
    public static final int FLIP_180 = 4;

    /** DOCUMENT ME! */
    private int width;

    /** DOCUMENT ME! */
    private int height;

    /** DOCUMENT ME! */
    private int tileWidth;

    /** DOCUMENT ME! */
    private int tileHeight;

    /** DOCUMENT ME! */
    private int edge = 0;

    /** DOCUMENT ME! */
    private int cols;

    /** DOCUMENT ME! */
    private int rows;

    /** DOCUMENT ME! */
    private int[][] symmetryMatrix = null;

    /*
            private int[][] symmetryMatrix = {
                    { FLIP_NONE, FLIP_H },
                    { FLIP_V, FLIP_HV }
            };
    */
    /** DOCUMENT ME! */
    private int symmetryRows = 2;

    /*
            private int[][] symmetryMatrix = {
                    { FLIP_NONE, FLIP_H },
                    { FLIP_V, FLIP_HV }
            };
    */
    /** DOCUMENT ME! */
    private int symmetryCols = 2;

/**
     * Creates a new TileImageFilter object.
     */
    public TileImageFilter() {
        this(32, 32);
    }

/**
     * Creates a new TileImageFilter object.
     *
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public TileImageFilter(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        consumer.setDimensions(this.width, this.height);

        tileWidth = width - edge;
        tileHeight = height - edge;

        cols = ((this.width + tileWidth) - 1) / tileWidth;
        rows = ((this.height + tileHeight) - 1) / tileHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hints DOCUMENT ME!
     */
    public void setHints(int hints) {
        hints &= ~TOPDOWNLEFTRIGHT;
        hints &= ~COMPLETESCANLINES;
        hints |= RANDOMPIXELORDER;
        consumer.setHints(hints);
    }

    /**
     * DOCUMENT ME!
     *
     * @param sx DOCUMENT ME!
     * @param sy DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param model DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param scan DOCUMENT ME!
     */
    public void setPixels(int sx, int sy, int w, int h, ColorModel model,
        byte[] pixels, int offset, int scan) {
        for (int y = 0; y < rows; y++) {
            int clippedHeight = Math.min(h, height - sy);

            if (clippedHeight > 0) {
                int tx = sx;

                for (int x = 0; x < cols; x++) {
                    int clippedWidth = Math.min(w, width - sx);

                    if (clippedWidth > 0) {
                        consumer.setPixels(tx, sy, clippedWidth, clippedHeight,
                            model, pixels, offset, scan);
                    }

                    tx += tileWidth;
                }
            }

            sy += tileHeight;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param sx DOCUMENT ME!
     * @param sy DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param model DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param scan DOCUMENT ME!
     */
    public void setPixels(int sx, int sy, int w, int h, ColorModel model,
        int[] pixels, int offset, int scan) {
        if (edge > 0) {
            pixels = blendPixels(sx, sy, w, h, pixels, offset, scan);
        }

        for (int y = 0; y < rows; y++) {
            int clippedHeight = Math.min(h, height - sy);

            if (clippedHeight > 0) {
                int tx = sx;

                for (int x = 0; x < cols; x++) {
                    int clippedWidth = Math.min(w, width - sx);

                    if (clippedWidth > 0) {
                        if (symmetryMatrix != null) {
                            consumer.setPixels(tx, sy, clippedWidth,
                                clippedHeight, model,
                                flipPixels(sx, sy, w, h, pixels, offset, scan,
                                    symmetryMatrix[x % symmetryCols][y % symmetryRows]),
                                offset, scan);
                        } else {
                            consumer.setPixels(tx, sy, clippedWidth,
                                clippedHeight, model, pixels, offset, scan);
                        }
                    }

                    tx += tileWidth;
                }
            }

            sy += tileHeight;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param stride DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] blendPixels(int x, int y, int w, int h, int[] pixels, int off,
        int stride) {
        int[] newPixels = new int[w * h];
        int edge = 8;
        int i = 0;

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                if ((row < edge) || (col < edge)) {
                    int row2 = (row < edge) ? (h - edge - 1 + row) : row;
                    int col2 = (col < edge) ? (w - edge - 1 + col) : col;

                    if ((row < edge) && (col < edge)) {
                        float frow = (float) row / (float) edge;
                        float fcol = (float) col / (float) edge;
                        int i2 = (row2 * w) + col2;
                        int i3 = (row * w) + col2;
                        int i4 = (row2 * w) + col;
                        int left = ImageMath.mixColors(frow, pixels[i4],
                                pixels[i]);
                        int right = ImageMath.mixColors(frow, pixels[i2],
                                pixels[i3]);
                        newPixels[i] = ImageMath.mixColors(fcol, right, left);
                    } else {
                        float f = (float) Math.min(row, col) / (float) edge;
                        int i2 = (row2 * w) + col2;
                        newPixels[i] = ImageMath.mixColors(f, pixels[i2],
                                pixels[i]);
                    }
                } else {
                    newPixels[i] = pixels[i];
                }

                i++;
            }
        }

        return newPixels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param symmetryMatrix DOCUMENT ME!
     */
    public void setSymmetryMatrix(int[][] symmetryMatrix) {
        this.symmetryMatrix = symmetryMatrix;
        symmetryRows = symmetryMatrix.length;
        symmetryCols = symmetryMatrix[0].length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[][] getSymmetryMatrix() {
        return symmetryMatrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     * @param operation DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] flipPixels(int x, int y, int w, int h, int[] pixels, int off,
        int scansize, int operation) {
        int newX = x;
        int newY = y;
        int newW = w;
        int newH = h;

        switch (operation) {
        case FLIP_H:
            newX = width - (x + w);

            break;

        case FLIP_V:
            newY = height - (y + h);

            break;

        case FLIP_HV:
            newW = h;
            newH = w;
            newX = y;
            newY = x;

            break;

        case FLIP_180:
            newX = width - (x + w);
            newY = height - (y + h);

            break;
        }

        int[] newPixels = new int[newW * newH];

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                int index = (row * scansize) + off + col;
                int newRow = row;
                int newCol = col;

                switch (operation) {
                case FLIP_H:
                    newCol = w - col - 1;

                    break;

                case FLIP_V:
                    newRow = h - row - 1;

                    break;

                case FLIP_HV:
                    newRow = col;
                    newCol = row;

                    break;

                case FLIP_180:
                    newRow = h - row - 1;
                    newCol = w - col - 1;

                    break;
                }

                int newIndex = (newRow * newW) + newCol;
                newPixels[newIndex] = pixels[index];
            }
        }

        return newPixels;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Tile";
    }
}
