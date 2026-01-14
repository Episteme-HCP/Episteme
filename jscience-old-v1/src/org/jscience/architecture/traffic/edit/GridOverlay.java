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

package org.jscience.architecture.traffic.edit;

import org.jscience.architecture.traffic.Overlay;

import java.awt.*;


/**
 * Overlay for <code>gld.View</code>. Shows a grid.
 *
 * @author Group GUI
 * @version 1.0
 */
public class GridOverlay implements Overlay {
    /** DOCUMENT ME! */
    protected final static int GRID_SPACING = 20;

    /** DOCUMENT ME! */
    Dimension gridSize;

/**
     * Creates a default <code>GridOverlay</code>.
     *
     * @param size DOCUMENT ME!
     */
    public GridOverlay(Dimension size) {
        gridSize = size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int overlayType() {
        return 2;
    }

    /**
     * Paints the grid.
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.setPaintMode();
        g.setColor(new Color(128, 128, 128, 128));

        int top = -(int) (gridSize.height / 2);
        int left = -(int) (gridSize.width / 2);
        top -= (top % GRID_SPACING);
        left -= (left % GRID_SPACING);

        int cellSize = GRID_SPACING;

        for (int x = 0; x < gridSize.width; x += cellSize)
            g.drawLine(x + left, top, x + left, top + gridSize.height);

        for (int y = 0; y < gridSize.height; y += cellSize)
            g.drawLine(left, y + top, left + gridSize.width, y + top);
    }
}
