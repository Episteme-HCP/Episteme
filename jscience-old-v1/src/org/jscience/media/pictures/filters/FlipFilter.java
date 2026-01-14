/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.media.pictures.filters;

import java.awt.image.ColorModel;
import java.awt.image.ImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class FlipFilter extends ImageFilter {
    /** DOCUMENT ME! */
    public static final int FLIP_H = 1;

    /** DOCUMENT ME! */
    public static final int FLIP_V = 2;

    /** DOCUMENT ME! */
    public static final int FLIP_HV = 3;

    /** DOCUMENT ME! */
    public static final int FLIP_90CW = 4;

    /** DOCUMENT ME! */
    public static final int FLIP_90CCW = 5;

    /** DOCUMENT ME! */
    public static final int FLIP_180 = 6;

    /** DOCUMENT ME! */
    private int operation;

    /** DOCUMENT ME! */
    private int width;

    /** DOCUMENT ME! */
    private int height;

    /** DOCUMENT ME! */
    private int newWidth;

    /** DOCUMENT ME! */
    private int newHeight;

/**
     * Creates a new FlipFilter object.
     */
    public FlipFilter() {
        this(FLIP_HV);
    }

/**
     * Creates a new FlipFilter object.
     *
     * @param operation DOCUMENT ME!
     */
    public FlipFilter(int operation) {
        this.operation = operation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hintflags DOCUMENT ME!
     */
    public void setHints(int hintflags) {
        hintflags &= ~TOPDOWNLEFTRIGHT;
        consumer.setHints(hintflags);
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;

        switch (operation) {
        case FLIP_H:
        case FLIP_V:
        case FLIP_180:
            newWidth = width;
            newHeight = height;

            break;

        case FLIP_HV:
        case FLIP_90CW:
        case FLIP_90CCW:
            newWidth = height;
            newHeight = width;

            break;
        }

        consumer.setDimensions(newWidth, newHeight);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param model DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, ColorModel model,
        byte[] pixels, int off, int scansize) {
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

        case FLIP_90CW:
            newW = h;
            newH = w;
            newX = height - (y + h);
            newY = x;

            break;

        case FLIP_90CCW:
            newW = h;
            newH = w;
            newX = y;
            newY = width - (x + w);

            break;

        case FLIP_180:
            newX = width - (x + w);
            newY = height - (y + h);

            break;
        }

        byte[] newPixels = new byte[newW * newH];

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

                case FLIP_90CW:
                    newRow = col;
                    newCol = h - row - 1;
                    ;

                    break;

                case FLIP_90CCW:
                    newRow = w - col - 1;
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

        consumer.setPixels(newX, newY, newW, newH, model, newPixels, 0, newW);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param model DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, ColorModel model,
        int[] pixels, int off, int scansize) {
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

        case FLIP_90CW:
            newW = h;
            newH = w;
            newX = height - (y + h);
            newY = x;

            break;

        case FLIP_90CCW:
            newW = h;
            newH = w;
            newX = y;
            newY = width - (x + w);

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

                case FLIP_90CW:
                    newRow = col;
                    newCol = h - row - 1;
                    ;

                    break;

                case FLIP_90CCW:
                    newRow = w - col - 1;
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

        consumer.setPixels(newX, newY, newW, newH, model, newPixels, 0, newW);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        switch (operation) {
        case FLIP_H:
            return "Flip Horizontal";

        case FLIP_V:
            return "Flip Vertical";

        case FLIP_HV:
            return "Flip Diagonal";

        case FLIP_90CW:
            return "Rotate 90";

        case FLIP_90CCW:
            return "Rotate -90";

        case FLIP_180:
            return "Rotate 180";
        }

        return "Flip";
    }
}
