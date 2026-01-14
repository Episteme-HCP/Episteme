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

package org.jscience.swing;

import org.jscience.awt.CategoryGraph2DModel;
import org.jscience.awt.GraphDataEvent;

import org.jscience.mathematics.MathUtils;

import java.awt.*;
import java.awt.geom.Point2D;

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * A bar graph Swing component. Multiple series are side-by-side.
 *
 * @author Ismael Orenstein
 * @version 1.0
 */
public class JBarGraph extends JCategoryGraph2D {
    /** Bar colors. */
    protected Color[] barColor = {
            Color.blue, Color.green, Color.red, Color.yellow, Color.cyan,
            Color.lightGray, Color.magenta, Color.orange, Color.pink
        };

    /** Min and max data points. */
    protected float minY;

    /** Min and max data points. */
    protected float maxY;

    /** Axis scaling. */
    private final float yIncPixels = 40.0f;

    /** DOCUMENT ME! */
    private float xScale;

    /** DOCUMENT ME! */
    private float yScale;

    /** DOCUMENT ME! */
    protected int barWidth;

    /** Padding. */
    protected final int barPad = 0;

    /** Axis numbering. */
    protected boolean numbering = true;

    /** DOCUMENT ME! */
    protected NumberFormat xNumberFormat = new DecimalFormat("##0.0");

    /** DOCUMENT ME! */
    protected NumberFormat yNumberFormat = new DecimalFormat("##0.0");

/**
     * Constructs a bar graph.
     *
     * @param cgm DOCUMENT ME!
     */
    public JBarGraph(CategoryGraph2DModel cgm) {
        super(cgm);
        dataChanged(new GraphDataEvent(model));
    }

    /**
     * Implementation of GraphDataListener. Application code will not
     * use this method explicitly, it is used internally.
     *
     * @param e DOCUMENT ME!
     */
    public void dataChanged(GraphDataEvent e) {
        minY = 0.0f;
        maxY = Float.NEGATIVE_INFINITY;
        model.firstSeries();

        do {
            for (int i = 0; i < model.seriesLength(); i++) {
                float tmp = model.getValue(i);
                minY = Math.min(tmp, minY);
                maxY = Math.max(tmp, maxY);
            }
        } while (model.nextSeries());

        if (minY == maxY) {
            minY -= 0.5f;
            maxY += 0.5f;
        }

        setNumbering(numbering);
    }

    /**
     * Sets the bar color of the nth series.
     *
     * @param n the index of the series.
     * @param c the line color.
     */
    public final void setColor(int n, Color c) {
        barColor[n] = c;
        redraw();
    }

    /**
     * Gets the bar color of the nth series.
     *
     * @param n the index of the series.
     *
     * @return DOCUMENT ME!
     */
    public final Color getColor(int n) {
        return barColor[n];
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
            final int yNumPad = 8 * Math.max(yNumberFormat.format(maxY).length(),
                    yNumberFormat.format(minY).length());
            leftAxisPad += yNumPad;
        }

        rescale();
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
     * Draws the graph axes.
     *
     * @param g DOCUMENT ME!
     */
    protected final void drawAxes(Graphics g) {
        g.setColor(getForeground());

        // axis - Swing optimised
        if (minY > 0.0f) {
            g.drawLine(leftAxisPad - scalePad, getHeight() - axisPad,
                getWidth() - (axisPad - scalePad), getHeight() - axisPad);
        } else {
            g.drawLine(leftAxisPad - scalePad, origin.y,
                getWidth() - (axisPad - scalePad), origin.y);
        }

        g.drawLine(origin.x, axisPad - scalePad, origin.x,
            getHeight() - (axisPad - scalePad));

        // x-axis labels
        final FontMetrics metrics = g.getFontMetrics();
        final int strHeight = metrics.getHeight();

        for (int x = 0; x < model.seriesLength(); x++) {
            int strWidth = metrics.stringWidth(model.getCategory(x));
            g.drawString(model.getCategory(x),
                dataToScreen((x + 0.5f) - ((0.5f * strWidth) / xScale), 0.0f).x,
                origin.y + strHeight);
        }

        // numbering
        if (numbering) {
            // y-axis numbering
            float dy = (float) MathUtils.round(yIncPixels / yScale, 1);

            if (dy == 0.0f) {
                dy = Float.MIN_VALUE;
            }

            for (float y = dy; y <= maxY; y += dy) {
                Point p = dataToScreen(0.0f, y);
                String str = yNumberFormat.format(y);
                int strWidth = metrics.stringWidth(str);
                g.drawLine(p.x, p.y, p.x - 5, p.y);
                g.drawString(str, p.x - 8 - strWidth, p.y + (strHeight / 3));
            }

            for (float y = -dy; y >= minY; y -= dy) {
                Point p = dataToScreen(0.0f, y);
                String str = yNumberFormat.format(y);
                int strWidth = metrics.stringWidth(str);
                g.drawLine(p.x, p.y, p.x - 5, p.y);
                g.drawString(str, p.x - 8 - strWidth, p.y + (strHeight / 3));
            }
        }
    }

    /**
     * Draws the graph bars.
     *
     * @param g DOCUMENT ME!
     */
    protected void drawBars(Graphics g) {
        // bars
        int numSeries = 1;
        model.firstSeries();

        while (model.nextSeries())
            numSeries++;

        if (numSeries == 1) {
            for (int i = 0; i < model.seriesLength(); i++)
                drawBar(g, i, model.getValue(i), barColor[0], barWidth, 0);
        } else {
            final int subBarWidth = barWidth / numSeries;

            for (int i = 0; i < model.seriesLength(); i++) {
                // draw
                model.firstSeries();

                for (int j = 0; j < numSeries; j++) {
                    drawBar(g, i, model.getValue(i), barColor[j], subBarWidth,
                        j * subBarWidth);
                    model.nextSeries();
                }
            }
        }
    }

    /**
     * Draws a bar.
     *
     * @param g DOCUMENT ME!
     * @param pos DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param xoffset DOCUMENT ME!
     */
    private void drawBar(Graphics g, int pos, float value, Color color,
        int width, int xoffset) {
        Point p;

        if (value < 0.0f) {
            p = dataToScreen(pos, 0.0f);
        } else {
            p = dataToScreen(pos, value);
        }

        g.setColor(color);

        final int dy = Math.abs(p.y - origin.y);
        g.fillRect(p.x + barPad + xoffset, p.y, width, dy);
        g.setColor(Color.black);
        g.drawRect(p.x + barPad + xoffset, p.y, width, dy);
    }

    /**
     * Paint the graph.
     *
     * @param g DOCUMENT ME!
     */
    protected final void offscreenPaint(Graphics g) {
        drawAxes(g);
        drawBars(g);
    }

    /**
     * Reshapes the bar graph to the specified bounding box.
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
     * Rescales the bar graph.
     */
    protected final void rescale() {
        final Dimension minSize = getMinimumSize();

        // Swing optimised
        final int thisWidth = Math.max(getWidth(), minSize.width);
        final int thisHeight = Math.max(getHeight(), minSize.height);
        xScale = (thisWidth - (leftAxisPad + axisPad)) / model.seriesLength();
        yScale = (thisHeight - (2 * axisPad)) / (maxY - minY);
        barWidth = Math.round(xScale - (2 * barPad));
        origin.x = leftAxisPad;
        origin.y = thisHeight - axisPad + Math.round(minY * yScale);
        redraw();
    }

    /**
     * Converts a data point to screen coordinates.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected final Point dataToScreen(float x, float y) {
        return new Point(origin.x + Math.round(xScale * x),
            origin.y - Math.round(yScale * y));
    }

    /**
     * Converts a screen point to data coordinates.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected final Point2D.Float screenToData(Point p) {
        return new Point2D.Float((p.x - origin.x) / xScale,
            (origin.y - p.y) / yScale);
    }
}
