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

import org.jscience.mathematics.MathUtils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A line trace AWT component.
 *
 * @author Mark Hale
 * @version 1.4
 */
public final class LineTrace extends DoubleBufferedCanvas {
    /** Data points. */
    private final List dataPoints = new ArrayList();

    /** Sampling interval. */
    private float samplingInterval;

    /** Axis numbering. */
    private boolean numbering = true;

    /** DOCUMENT ME! */
    private NumberFormat xNumberFormat = new DecimalFormat("##0.0");

    /** DOCUMENT ME! */
    private NumberFormat yNumberFormat = new DecimalFormat("##0.0");

    /** Origin. */
    private Point origin = new Point();

    /** Min and max data points. */
    private float minX;

    /** Min and max data points. */
    private float minY;

    /** Min and max data points. */
    private float maxX;

    /** Min and max data points. */
    private float maxY;

    /** Axis scaling. */
    private float xScale;

    /** Axis scaling. */
    private float yScale;

    /** DOCUMENT ME! */
    private final float xIncPixels = 40.0f;

    /** DOCUMENT ME! */
    private final float yIncPixels = 40.0f;

    /** Padding. */
    private final int scalePad = 5;

    /** DOCUMENT ME! */
    private final int axisPad = 25;

    /** DOCUMENT ME! */
    private int leftAxisPad;

/**
     * Constructs a line trace.
     *
     * @param minx DOCUMENT ME!
     * @param maxx DOCUMENT ME!
     * @param miny DOCUMENT ME!
     * @param maxy DOCUMENT ME!
     */
    public LineTrace(float minx, float maxx, float miny, float maxy) {
        addMouseMotionListener(new MouseLineAdapter());
        setXExtrema(minx, maxx);
        setYExtrema(miny, maxy);
        setSamplingInterval(0.2f);
    }

    /**
     * Gets the data sampled by this line trace.
     *
     * @return DOCUMENT ME!
     */
    public Graph2DModel getModel() {
        final Point2DListModel model = new Point2DListModel();
        model.setData(dataPoints);

        return model;
    }

    /**
     * Turns axis numbering on/off.
     *
     * @param flag DOCUMENT ME!
     */
    public final void setNumbering(boolean flag) {
        numbering = flag;
        leftAxisPad = axisPad;

        if (numbering) {
            final int maxYNumLen = yNumberFormat.format(maxY).length();
            final int minYNumLen = yNumberFormat.format(minY).length();
            int yNumPad = 8 * Math.max(minYNumLen, maxYNumLen);

            if (minX < 0.0f) {
                final int negXLen = (int) (((Math.max(getSize().width,
                        getMinimumSize().width) - (2 * (axisPad + scalePad))) * minX) / (minX -
                    maxX));
                yNumPad = Math.max(yNumPad - negXLen, 0);
            }

            leftAxisPad += yNumPad;
        }
    }

    /**
     * Sets the display format used for axis numbering. Convenience
     * method.
     *
     * @param format DOCUMENT ME!
     *
     * @see #setXNumberFormat(NumberFormat)
     * @see #setYNumberFormat(NumberFormat)
     */
    public final void setNumberFormat(NumberFormat format) {
        xNumberFormat = format;
        yNumberFormat = format;
        setNumbering(numbering);
    }

    /**
     * Sets the display format used for x-axis numbering.
     *
     * @param format DOCUMENT ME!
     */
    public final void setXNumberFormat(NumberFormat format) {
        xNumberFormat = format;
        setNumbering(numbering);
    }

    /**
     * Sets the display format used for y-axis numbering.
     *
     * @param format DOCUMENT ME!
     */
    public final void setYNumberFormat(NumberFormat format) {
        yNumberFormat = format;
        setNumbering(numbering);
    }

    /**
     * Sets the minimum/maximum values on the x-axis.
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setXExtrema(float min, float max) {
        if (max < min) {
            throw new IllegalArgumentException(
                "Maximum should be greater than minimum; max = " + max +
                " and min = " + min);
        }

        minX = min;
        maxX = max;
        rescale();
    }

    /**
     * Sets the minimum/maximum values on the y-axis.
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setYExtrema(float min, float max) {
        if (max < min) {
            throw new IllegalArgumentException(
                "Maximum should be greater than minimum; max = " + max +
                " and min = " + min);
        }

        minY = min;
        maxY = max;
        rescale();
    }

    /**
     * Sets the sampling interval. Smaller values give a more accurate
     * trace, but more susceptible to mouse noise.
     *
     * @param interval DOCUMENT ME!
     */
    public void setSamplingInterval(float interval) {
        samplingInterval = interval;
    }

    /**
     * Clears the current trace.
     */
    public void clear() {
        dataPoints.clear();
        redraw();
    }

    /**
     * Reshapes the line trace to the specified bounding box.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public final void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        rescale();
    }

    /**
     * Rescales the line trace.
     */
    private void rescale() {
        final Dimension s = getMinimumSize();
        final int thisWidth = Math.max(getSize().width, s.width);
        final int thisHeight = Math.max(getSize().height, s.height);
        xScale = (thisWidth - (leftAxisPad + axisPad)) / (maxX - minX);
        yScale = (thisHeight - (2 * axisPad)) / (maxY - minY);
        origin.x = leftAxisPad - Math.round(minX * xScale);
        origin.y = thisHeight - axisPad + Math.round(minY * yScale);
        redraw();
    }

    /**
     * Returns the preferred size of this component.
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    /**
     * Returns the minimum size of this component.
     *
     * @return DOCUMENT ME!
     */
    public Dimension getMinimumSize() {
        return new Dimension(200, 200);
    }

    /**
     * Converts a screen point to data coordinates.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Point2D.Float screenToData(Point p) {
        return new Point2D.Float((p.x - origin.x) / xScale,
            (origin.y - p.y) / yScale);
    }

    /**
     * Converts a data point to screen coordinates.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Point dataToScreen(Point2D.Float p) {
        return new Point(origin.x + Math.round(xScale * p.x),
            origin.y - Math.round(yScale * p.y));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Point dataToScreen(float x, float y) {
        return new Point(origin.x + Math.round(xScale * x),
            origin.y - Math.round(yScale * y));
    }

    /**
     * Paint the trace.
     *
     * @param g DOCUMENT ME!
     */
    protected void offscreenPaint(Graphics g) {
        drawAxes(g);

        // lines
        Point p1;

        // lines
        Point p2;
        Iterator iter = dataPoints.iterator();

        if (iter.hasNext()) {
            p1 = dataToScreen((Point2D.Float) iter.next());

            while (iter.hasNext()) {
                p2 = dataToScreen((Point2D.Float) iter.next());
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                p1 = p2;
            }
        }
    }

    /**
     * Draws the graph axes.
     *
     * @param g DOCUMENT ME!
     */
    private void drawAxes(Graphics g) {
        // axis
        g.setColor(getForeground());

        if (minY > 0.0f) {
            g.drawLine(leftAxisPad - scalePad, getSize().height - axisPad,
                getSize().width - (axisPad - scalePad),
                getSize().height - axisPad);
        } else {
            g.drawLine(leftAxisPad - scalePad, origin.y,
                getSize().width - (axisPad - scalePad), origin.y);
        }

        if (minX > 0.0f) {
            g.drawLine(leftAxisPad, axisPad - scalePad, leftAxisPad,
                getSize().height - (axisPad - scalePad));
        } else {
            g.drawLine(origin.x, axisPad - scalePad, origin.x,
                getSize().height - (axisPad - scalePad));
        }

        // numbering
        if (numbering) {
            final FontMetrics metrics = g.getFontMetrics();
            final int strHeight = metrics.getHeight();

            // x-axis numbering
            float dx = (float) MathUtils.round(xIncPixels / xScale, 1);

            if (dx == 0.0f) {
                dx = Float.MIN_VALUE;
            }

            for (float x = (minX > 0.0f) ? minX : dx; x <= maxX; x += dx) {
                String str = xNumberFormat.format(x);

                // add a + prefix to compensate for - prefix in negative number strings when calculating length
                int strWidth = metrics.stringWidth('+' + str);
                Point p = dataToScreen(x, (minY > 0.0f) ? minY : 0.0f);
                g.drawLine(p.x, p.y, p.x, p.y + 5);
                g.drawString(str, p.x - (strWidth / 2), p.y + 5 + strHeight);
            }

            for (float x = -dx; x >= minX; x -= dx) {
                String str = xNumberFormat.format(x);
                int strWidth = metrics.stringWidth(str);
                Point p = dataToScreen(x, (minY > 0.0f) ? minY : 0.0f);
                g.drawLine(p.x, p.y, p.x, p.y + 5);
                g.drawString(str, p.x - (strWidth / 2), p.y + 5 + strHeight);
            }

            // y-axis numbering
            float dy = (float) MathUtils.round(yIncPixels / yScale, 1);

            if (dy == 0.0f) {
                dy = Float.MIN_VALUE;
            }

            for (float y = (minY > 0.0f) ? minY : dy; y <= maxY; y += dy) {
                String str = yNumberFormat.format(y);
                int strWidth = metrics.stringWidth(str);
                Point p = dataToScreen((minX > 0.0f) ? minX : 0.0f, y);
                g.drawLine(p.x, p.y, p.x - 5, p.y);
                g.drawString(str, p.x - 8 - strWidth, p.y + (strHeight / 4));
            }

            for (float y = -dy; y >= minY; y -= dy) {
                String str = yNumberFormat.format(y);
                int strWidth = metrics.stringWidth(str);
                Point p = dataToScreen((minX > 0.0f) ? minX : 0.0f, y);
                g.drawLine(p.x, p.y, p.x - 5, p.y);
                g.drawString(str, p.x - 8 - strWidth, p.y + (strHeight / 4));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class MouseLineAdapter extends MouseMotionAdapter {
        /**
         * DOCUMENT ME!
         *
         * @param evt DOCUMENT ME!
         */
        public void mouseDragged(MouseEvent evt) {
            Point2D.Float p = screenToData(evt.getPoint());
            final int i = dataPoints.size() - 1;

            if ((p.x >= ((i * samplingInterval) + minX)) && (p.x <= maxX)) {
                dataPoints.add(p);
            }

            redraw();
        }
    }
}
