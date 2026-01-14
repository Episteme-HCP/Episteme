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

package org.jscience.architecture.traffic;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author Group Datastructures
 * @version 1.0
 */
public interface Selectable extends SelectionStarter {
    /**
     * Returns the bounding box of this Selectable
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getBounds();

    /**
     * Returns a complex bounding box of this Selectable
     *
     * @return DOCUMENT ME!
     */
    public Shape getComplexBounds();

    /**
     * Returns the center point of this Selectable
     *
     * @return DOCUMENT ME!
     */
    public Point getSelectionPoint();

    /**
     * Returns the selection points of this Selectable
     *
     * @return DOCUMENT ME!
     */
    public Point[] getSelectionPoints();

    /**
     * Returns the center point of this selectable
     *
     * @return DOCUMENT ME!
     */
    public Point getCenterPoint();

    /**
     * Returns the distance of given point to this Selectable
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDistance(Point p);

    /**
     * Returns true if this Selectable should be selectable
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelectable();
}
