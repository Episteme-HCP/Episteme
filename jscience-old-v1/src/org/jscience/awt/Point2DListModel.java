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

package org.jscience.awt;

import java.awt.geom.Point2D;

import java.util.List;


/**
 * The Point2DListModel provides a convenient implementation of the
 * Graph2DModel interface based upon a List of Point2D objects.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class Point2DListModel extends AbstractGraphModel
    implements Graph2DModel {
    /** DOCUMENT ME! */
    private List data;

/**
     * Creates a new Point2DListModel object.
     */
    public Point2DListModel() {
    }

    /**
     * Sets the list of points to be plotted.
     *
     * @param points DOCUMENT ME!
     */
    public void setData(List points) {
        data = points;
        fireGraphDataChanged();
    }

    // Graph2DModel interface
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getXCoord(int i) {
        Point2D p = (Point2D) data.get(i);

        return (float) p.getX();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getYCoord(int i) {
        Point2D p = (Point2D) data.get(i);

        return (float) p.getY();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int seriesLength() {
        return data.size();
    }

    /**
     * DOCUMENT ME!
     */
    public void firstSeries() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean nextSeries() {
        return false;
    }
}
