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

package org.jscience.chemistry.vapor.util.chart;

import java.awt.*;
import java.awt.geom.Point2D;


/**
 * Dataset class for charting tool.
 */
public class Dataset {
    /**
     * DOCUMENT ME!
     */
    private Color lineColor = Color.green;

    /**
     * DOCUMENT ME!
     */
    private Point2D.Double[] ptArray = null;

    /**
     * DOCUMENT ME!
     */
    private double minX = +1E10;

    /**
     * DOCUMENT ME!
     */
    private double minY = +1E10;

    /**
     * DOCUMENT ME!
     */
    private double maxX = -1E10;

    /**
     * DOCUMENT ME!
     */
    private double maxY = -1E10;

    /**
     * Creates a new Dataset object.
     *
     * @param numOfPoints DOCUMENT ME!
     */
    public Dataset(int numOfPoints) {
        ptArray = new Point2D.Double[numOfPoints];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumOfPoints() {
        return ptArray.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point2D.Double getValue(int index) {
        return ptArray[index];
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void setValue(int index, double x, double y) {
        if (ptArray[index] == null) {
            ptArray[index] = new Point2D.Double();
        }

        ptArray[index].setLocation(x, y);

        if (x < minX) {
            minX = x;
        }

        if (x > maxX) {
            maxX = x;
        }

        if (y < minY) {
            minY = y;
        }

        if (y > maxY) {
            maxY = y;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lineColor DOCUMENT ME!
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMinX() {
        return minX;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMinY() {
        return minY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMaxY() {
        return maxY;
    }
}
