/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
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
