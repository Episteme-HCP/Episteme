// JTEM - Java Tools for Experimental Mathematics
// Copyright (C) 2001 JTEM-Group
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
package org.jscience.swing.spinner.icons;

import java.awt.*;

import javax.swing.*;


/**
 * An <code>Icon</code> showing two vertical rectangles. For example you
 * can use it as the Icon for a pause button of a timer.
 *
 * @see ArrowIcon
 * @see SquareIcon
 */
public class RectanglesIcon implements Icon {
    /** DOCUMENT ME! */
    final int iconSize;

    /** DOCUMENT ME! */
    Color shadow = UIManager.getColor("controlShadow");

    /** DOCUMENT ME! */
    Color highlight = UIManager.getColor("controlLtHighlight");

/**
     * Creates a new SquareIcon with the default size of 8 pixels.
     */
    public RectanglesIcon() {
        this(8);
    }

/**
     * Creates a new RectanglesIcon with the specified size. Note that the size
     * of the Icon is its width AND height, the two rectangles displayed by
     * this Icon lie within that square but have not that size.
     *
     * @param iconSize DOCUMENT ME!
     */
    public RectanglesIcon(int iconSize) {
        this.iconSize = iconSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIconHeight() {
        return iconSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIconWidth() {
        return iconSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color fg = c.getForeground();

        if (!c.isEnabled()) {
            fg = shadow;
        }

        int visibleSize = (int) ((iconSize / Math.sqrt(2.)) + 0.5);
        int offset = (iconSize - visibleSize) / 2;
        paintTriangle(g, x + offset, y + offset, visibleSize, c.isEnabled(), fg);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param size DOCUMENT ME!
     * @param isEnabled DOCUMENT ME!
     * @param foreground DOCUMENT ME!
     */
    public void paintTriangle(Graphics g, int x, int y, int size,
        boolean isEnabled, Color foreground) {
        Color oldColor = g.getColor();
        size = Math.max(size, 2);
        g.translate(x, y);
        g.setColor(foreground);

        int rectWidth = size / 3;
        g.fillRect(0, 0, rectWidth, size);
        g.fillRect(2 * rectWidth, 0, rectWidth, size);

        if (!isEnabled) {
            g.setColor(highlight);
            g.drawLine(1, size, rectWidth, size);
            g.drawLine(rectWidth, 1, rectWidth, size);
            g.drawLine((2 * rectWidth) + 1, size, 3 * rectWidth, size);
            g.drawLine(3 * rectWidth, 1, 3 * rectWidth, size);
        }

        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}
