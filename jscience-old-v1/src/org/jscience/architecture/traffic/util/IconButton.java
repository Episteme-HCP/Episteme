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

package org.jscience.architecture.traffic.util;

import java.awt.*;


/**
 * 
 */
public class IconButton extends Button {
    /** The image on this IconButton */
    protected Image img = null;

    /** DOCUMENT ME! */
    protected int id = 0;

/**
     * Create a new IconButton without an Image
     */
    public IconButton() {
        super();
    }

/**
     * Create a new IconButton with a given Image
     *
     * @param i DOCUMENT ME!
     */
    public IconButton(Image i) {
        super();
        img = i;
    }

/**
     * Create a new IconButton with a given Image and Id
     *
     * @param i   DOCUMENT ME!
     * @param idn DOCUMENT ME!
     */
    public IconButton(Image i, int idn) {
        super();
        img = i;
        id = idn;
    }

    /**
     * Draw this IconButton
     *
     * @param g The Graphics object to draw onto
     */
    public void paint(Graphics g) {
        super.paint(g);

        if (img != null) {
            g.drawImage(img,
                (int) (this.getWidth() / 2) - (int) (img.getWidth(this) / 2),
                (int) (this.getHeight() / 2) - (int) (img.getHeight(this) / 2),
                this);
        }
    }

    /**
     * Get the image for this IconButton
     *
     * @return DOCUMENT ME!
     */
    public Image getImage() {
        return img;
    }

    /**
     * Set the image for this IconButton
     *
     * @param i DOCUMENT ME!
     */
    public void setImage(Image i) {
        img = i;
    }

    /**
     * Returns the id of this button
     *
     * @return DOCUMENT ME!
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Id of this button
     *
     * @param i DOCUMENT ME!
     */
    public void setId(int i) {
        id = i;
    }
}
