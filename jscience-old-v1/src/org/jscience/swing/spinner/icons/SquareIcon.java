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

import javax.swing.*;
import java.awt.*;

/**
 * An <code>Icon</code> showing a square. For example you can use it as the
 * Icon for a stop button of a timer.
 * <p/>
 * The size of the Icon is not the size of the actual square the Icon is
 * showing. The square size fits to the arrow size of an ArrowIcon object.
 * </p>
 *
 * @see ArrowIcon
 */
public class SquareIcon implements Icon {
    final int iconSize;
    Color shadow = UIManager.getColor("controlShadow"),
            highlight = UIManager.getColor("controlLtHighlight");

    /**
     * Creates a new SquareIcon with
     * the default size of 8 pixels.
     */
    public SquareIcon() {
        this(8);
    }

    /**
     * Creates a new SquareIcon with the specified
     * size. Note that the size of the Icon is its width AND height,
     * the square displayed by this Icon lies within that square but
     * is not that size - it fits to an appropriate sized arrow of an
     * ArrowIcon.
     */
    public SquareIcon(int iconSize) {
        this.iconSize = iconSize;
    }

    public int getIconHeight() {
        return iconSize;
    }

    public int getIconWidth() {
        return iconSize;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color fg = c.getForeground();
        if (!c.isEnabled()) fg = shadow;

        int visibleSize = (int) (iconSize / Math.sqrt(2.) + 0.5);
        int offset = (iconSize - visibleSize) / 2;
        paintTriangle(g, x + offset, y + offset, visibleSize, c.isEnabled(), fg);
    }

    public void paintTriangle(Graphics g, int x, int y, int size,
                              boolean isEnabled, Color foreground) {
        Color oldColor = g.getColor();

        size = Math.max(size, 2);

        g.translate(x, y);
        g.setColor(foreground);

        g.fillRect(0, 0, size, size);
        if (!isEnabled) {
            g.setColor(highlight);
            g.drawLine(1, size, size, size);
            g.drawLine(size, 1, size, size);
        }

        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}
