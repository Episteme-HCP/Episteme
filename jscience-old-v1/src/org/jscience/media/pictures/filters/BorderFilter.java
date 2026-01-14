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
public class BorderFilter extends ImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 670926158411088373L;

    /** DOCUMENT ME! */
    private int width;

    /** DOCUMENT ME! */
    private int height;

    /** DOCUMENT ME! */
    private int leftBorder;

    /** DOCUMENT ME! */
    private int rightBorder;

    /** DOCUMENT ME! */
    private int topBorder;

    /** DOCUMENT ME! */
    private int bottomBorder;

/**
     * Creates a new BorderFilter object.
     */
    public BorderFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param leftBorder DOCUMENT ME!
     */
    public void setLeftBorder(int leftBorder) {
        this.leftBorder = leftBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLeftBorder() {
        return leftBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rightBorder DOCUMENT ME!
     */
    public void setRightBorder(int rightBorder) {
        this.rightBorder = rightBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRightBorder() {
        return rightBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param topBorder DOCUMENT ME!
     */
    public void setTopBorder(int topBorder) {
        this.topBorder = topBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTopBorder() {
        return topBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bottomBorder DOCUMENT ME!
     */
    public void setBottomBorder(int bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBottomBorder() {
        return bottomBorder;
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
        consumer.setDimensions(width + leftBorder + rightBorder,
            height + topBorder + bottomBorder);
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
        consumer.setPixels(x + leftBorder, y + topBorder, width, height, model,
            pixels, 0, scansize);
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
     * @param offset DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, ColorModel model,
        int[] pixels, int offset, int scansize) {
        consumer.setPixels(x + leftBorder, y + topBorder, width, height, model,
            pixels, 0, scansize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Border...";
    }
}
