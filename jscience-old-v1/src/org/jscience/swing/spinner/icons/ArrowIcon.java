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
 * An <code>Icon</code> showing an arrow with a specifiable direction and
 * size. For example you can use it as the Icon for a start button of a timer.
 *
 * @see SquareIcon
 */
public class ArrowIcon implements Icon {
    /** DOCUMENT ME! */
    final int direction;

    /** DOCUMENT ME! */
    final int iconSize;

    /** DOCUMENT ME! */
    Color shadow = UIManager.getColor("controlShadow");

    /** DOCUMENT ME! */
    Color highlight = UIManager.getColor("controlLtHighlight");

/**
     * Creates a new ArrowIcon with the specified direction and
     * the default size of 8 pixels.
     * For the direction you choose one of the following {@link SwingConstants}
     * final fields:
     * <p/>
     * {@link SwingConstants#WEST}<br>
     * {@link SwingConstants#EAST}<br>
     * {@link SwingConstants#NORTH}<br>
     * {@link SwingConstants#SOUTH}<br>
     * </p>
     */
    public ArrowIcon(int direction) {
        this(direction, 8);
    }

/**
     * Creates a new ArrowIcon with the specified direction and
     * size. Note that the size of the Icon is its width AND height,
     * the arrow displayed by this Icon lies within that square.
     * For the direction you choose one of the following {@link SwingConstants}
     * final fields:
     * <p/>
     * {@link SwingConstants#WEST}<br>
     * {@link SwingConstants#EAST}<br>
     * {@link SwingConstants#NORTH}<br>
     * {@link SwingConstants#SOUTH}<br>
     * </p>
     */
    public ArrowIcon(int direction, int iconSize) {
        switch (direction) {
        default:
            throw new IllegalArgumentException("direction " + direction);

        case SwingConstants.WEST:
        case SwingConstants.EAST:
        case SwingConstants.NORTH:
        case SwingConstants.SOUTH:
        }

        this.direction = direction;
        this.iconSize = iconSize;
    }

    /**
     * Returns the specified or default size depending on the chosen
     * constructor.
     *
     * @return DOCUMENT ME!
     */
    public int getIconHeight() {
        return iconSize;
    }

    /**
     * Returns the specified or default size depending on the chosen
     * constructor.
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

        paintTriangle(g, x + (iconSize / 4), y + (iconSize / 4), iconSize / 2,
            c.isEnabled(), fg);
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
        int mid;
        int i;
        int j;

        j = 0;
        size = Math.max(size, 2);
        mid = (size / 2); // - 1;

        g.translate(x, y);
        g.setColor(foreground);

        switch (direction) {
        case SwingConstants.NORTH:

            for (i = 0; i < size; i++) {
                g.drawLine(mid - i, i, mid + i, i);
            }

            if (!isEnabled) {
                g.setColor(highlight);
                g.drawLine(mid - i + 2, i, mid + i, i);
            }

            break;

        case SwingConstants.SOUTH:

            if (!isEnabled) {
                g.translate(1, 1);
                g.setColor(highlight);

                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(mid - i, j, mid + i, j);
                    j++;
                }

                g.translate(-1, -1);
                g.setColor(foreground);
            }

            j = 0;

            for (i = size - 1; i >= 0; i--) {
                g.drawLine(mid - i, j, mid + i, j);
                j++;
            }

            break;

        case SwingConstants.WEST:

            for (i = 0; i < size; i++) {
                g.drawLine(i, mid - i, i, mid + i);
            }

            if (!isEnabled) {
                g.setColor(highlight);
                g.drawLine(i, mid - i + 2, i, mid + i);
            }

            break;

        case SwingConstants.EAST:

            if (!isEnabled) {
                g.translate(1, 1);
                g.setColor(highlight);

                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(j, mid - i, j, mid + i);
                    j++;
                }

                g.translate(-1, -1);
                g.setColor(foreground);
            }

            j = 0;

            for (i = size - 1; i >= 0; i--) {
                g.drawLine(j, mid - i, j, mid + i);
                j++;
            }

            break;
        }

        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}
