package org.jscience.swing;

import org.jscience.awt.Graph2D;
import org.jscience.awt.Graph2DModel;
import org.jscience.awt.GraphDataEvent;
import org.jscience.awt.GraphDataListener;

import org.jscience.mathematics.MathUtils;

import java.awt.*;
import java.awt.geom.Point2D;

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * The JGraph2D superclass provides an abstract encapsulation of 2D graphs.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class JGraph2D extends JDoubleBufferedComponent
    implements GraphDataListener {
    /** DOCUMENT ME! */
    public final static int LINEAR_SCALE = 0;

    /** DOCUMENT ME! */
    public final static int LOG_SCALE = 1;

    /** Data model. */
    protected Graph2DModel model;

    /** Origin. */
    protected Point origin = new Point();

    /** DOCUMENT ME! */
    protected Graph2D.DataMarker dataMarker = Graph2D.DataMarker.NONE;

    /** Series colors. */
    protected Color[] seriesColor = {
            Color.black, Color.blue, Color.green, Color.red, Color.yellow,
            Color.cyan, Color.lightGray, Color.magenta, Color.orange, Color.pink
        };

    /** Axis numbering. */
    protected boolean numbering = true;

    /** DOCUMENT ME! */
    protected NumberFormat xNumberFormat = new DecimalFormat("##0.0");

    /** DOCUMENT ME! */
    protected NumberFormat yNumberFormat = new DecimalFormat("##0.0");

    /** DOCUMENT ME! */
    protected boolean gridLines = false;

    /** DOCUMENT ME! */
    private final Color gridLineColor = Color.lightGray;

    /** Axis scaling. */
    private float xScale;

    /** Axis scaling. */
    private float yScale;

    /** Axis scaling type. */
    private int xScaleType;

    /** Axis scaling type. */
    private int yScaleType;

    /** Axis extrema. */
    private float minX;

    /** Axis extrema. */
    private float minY;

    /** Axis extrema. */
    private float maxX;

    /** Axis extrema. */
    private float maxY;

    /** DOCUMENT ME! */
    private float scaledMinX;

    /** DOCUMENT ME! */
    private float scaledMinY;

    /** DOCUMENT ME! */
    private float scaledMaxX;

    /** DOCUMENT ME! */
    private float scaledMaxY;

    /** DOCUMENT ME! */
    private boolean autoXExtrema = true;

    /** DOCUMENT ME! */
    private boolean autoYExtrema = true;

    /** DOCUMENT ME! */
    private float xGrowth;

    /** DOCUMENT ME! */
    private float yGrowth;

    /** Axis numbering increment. */
    private final float xIncPixels = 40.0f;

    /** DOCUMENT ME! */
    private final float yIncPixels = 40.0f;

    /** DOCUMENT ME! */
    private float xInc;

    /** DOCUMENT ME! */
    private float yInc;

    /** DOCUMENT ME! */
    private boolean autoXInc = true;

    /** DOCUMENT ME! */
    private boolean autoYInc = true;

    /** Padding. */
    protected final int scalePad = 5;

    /** DOCUMENT ME! */
    protected final int axisPad = 25;

    /** DOCUMENT ME! */
    protected int leftAxisPad;

/**
     * Constructs a 2D graph.
     *
     * @param gm DOCUMENT ME!
     */
    public JGraph2D(Graph2DModel gm) {
        model = gm;
        model.addGraphDataListener(this);
        dataChanged(new GraphDataEvent(model));
    }

    /**
     * Sets the data plotted by this graph to the specified data.
     *
     * @param gm DOCUMENT ME!
     */
    public final void setModel(Graph2DModel gm) {
        model.removeGraphDataListener(this);
        model = gm;
        model.addGraphDataListener(this);
        dataChanged(new GraphDataEvent(model));
    }

    /**
     * Returns the model used by this graph.
     *
     * @return DOCUMENT ME!
     */
    public final Graph2DModel getModel() {
        return model;
    }

    /**
     * Implementation of GraphDataListener. Application code will not
     * use this method explicitly, it is used internally.
     *
     * @param e DOCUMENT ME!
     */
    public void dataChanged(GraphDataEvent e) {
        if (e.isIncremental()) {
            Graphics g = getOffscreenGraphics();

            if (g == null) {
                return;
            }

            final int series = e.getSeries();

            if (series == GraphDataEvent.ALL_SERIES) {
                model.firstSeries();

                int n = 0;

                do {
                    final int i = model.seriesLength() - 1;
                    incrementalRescale(model.getXCoord(i), model.getYCoord(i));
                    g.setColor(seriesColor[n]);
                    drawDataPoint(g, i);
                    n++;
                } while (model.nextSeries());
            } else {
                model.firstSeries();

                int n = 0;

                for (; n < series; n++)
                    model.nextSeries();

                final int i = model.seriesLength() - 1;
                incrementalRescale(model.getXCoord(i), model.getYCoord(i));
                g.setColor(seriesColor[n]);
                drawDataPoint(g, i);
            }

            repaint();
        } else {
            // ensure there are enough colors
            int n = 1;
            model.firstSeries();

            while (model.nextSeries())
                n++;

            if (n > seriesColor.length) {
                Color[] tmp = seriesColor;
                seriesColor = new Color[n];
                System.arraycopy(tmp, 0, seriesColor, 0, tmp.length);

                for (int i = tmp.length; i < n; i++)
                    seriesColor[i] = seriesColor[i - tmp.length];
            }

            if (autoXExtrema) {
                setXExtrema(0.0f, 0.0f);
            }

            if (autoYExtrema) {
                setYExtrema(0.0f, 0.0f);
            }

            redraw();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    private void incrementalRescale(final float x, final float y) {
        float min;
        float max;

        if (x < minX) {
            min = autoXExtrema ? Math.min(x, minX - xGrowth) : (minX - xGrowth);
        } else {
            min = minX;
        }

        if (x > maxX) {
            max = autoXExtrema ? Math.max(x, maxX + xGrowth) : (maxX + xGrowth);
        } else {
            max = maxX;
        }

        rescaleX(min, max);

        if (y < minY) {
            min = autoYExtrema ? Math.min(y, minY - yGrowth) : (minY - yGrowth);
        } else {
            min = minY;
        }

        if (y > maxY) {
            max = autoYExtrema ? Math.max(y, maxY + yGrowth) : (maxY + yGrowth);
        } else {
            max = maxY;
        }

        rescaleY(min, max);
    }

    /**
     * Turns axis numbering on/off. Default is on.
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

            if (scaledMinX < 0.0f) {
                final int negXLen = (int) (((Math.max(getSize().width,
                        getMinimumSize().width) - (2 * (axisPad + scalePad))) * scaledMinX) / (scaledMinX -
                    scaledMaxX));
                yNumPad = Math.max(yNumPad - negXLen, 0);
            }

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
     * Turns grid lines on/off. Default is off.
     *
     * @param flag DOCUMENT ME!
     */
    public final void setGridLines(boolean flag) {
        gridLines = flag;
        redraw();
    }

    /**
     * Sets the x-axis scale type.
     *
     * @param t a _SCALE constant.
     */
    public final void setXScale(int t) {
        if (xScaleType != t) {
            xScaleType = t;
            dataChanged(new GraphDataEvent(model));
        }
    }

    /**
     * Sets the y-axis scale type.
     *
     * @param t a _SCALE constant.
     */
    public final void setYScale(int t) {
        if (yScaleType != t) {
            yScaleType = t;
            dataChanged(new GraphDataEvent(model));
        }
    }

    /**
     * Sets the x-axis numbering increment.
     *
     * @param dx use 0.0f for auto-adjusting (default).
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void setXIncrement(float dx) {
        if (dx < 0.0f) {
            throw new IllegalArgumentException("Increment should be positive.");
        } else if (dx == 0.0f) {
            if (!autoXInc) {
                autoXInc = true;
                rescale();
            }
        } else {
            autoXInc = false;

            if (dx != xInc) {
                xInc = dx;
                rescale();
            }
        }
    }

    /**
     * Sets the y-axis numbering increment.
     *
     * @param dy use 0.0f for auto-adjusting (default).
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void setYIncrement(float dy) {
        if (dy < 0.0f) {
            throw new IllegalArgumentException("Increment should be positive.");
        } else if (dy == 0.0f) {
            if (!autoYInc) {
                autoYInc = true;
                rescale();
            }
        } else {
            autoYInc = false;

            if (dy != yInc) {
                yInc = dy;
                rescale();
            }
        }
    }

    /**
     * Sets the minimum/maximum values on the x-axis. Set both min and
     * max to 0.0f for auto-adjusting (default).
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void setXExtrema(float min, float max) {
        if ((min == 0.0f) && (max == 0.0f)) {
            autoXExtrema = true;

            // determine min and max from model
            min = Float.POSITIVE_INFINITY;
            max = Float.NEGATIVE_INFINITY;

            float tmp;
            model.firstSeries();

            do {
                for (int i = 0; i < model.seriesLength(); i++) {
                    tmp = model.getXCoord(i);
                    min = Math.min(tmp, min);
                    max = Math.max(tmp, max);
                }
            } while (model.nextSeries());

            if (min == max) {
                // default values if no variation in data
                if (yScaleType == LOG_SCALE) {
                    min /= 10.0f;
                    max *= 10.0f;
                } else {
                    min -= 0.5f;
                    max += 0.5f;
                }
            }

            if ((min == Float.POSITIVE_INFINITY) ||
                    (max == Float.NEGATIVE_INFINITY)) {
                // default values if no data
                if (xScaleType == LOG_SCALE) {
                    min = 1.0f;
                    max = 100.0f;
                } else {
                    min = -5.0f;
                    max = 5.0f;
                }
            }
        } else if (max <= min) {
            throw new IllegalArgumentException(
                "Maximum should be greater than minimum; max = " + max +
                " and min = " + min);
        } else {
            autoXExtrema = false;
        }

        rescaleX(min, max);
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param growth DOCUMENT ME!
     */
    public final void setXExtrema(float min, float max, float growth) {
        setXExtrema(min, max);
        xGrowth = growth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    private void rescaleX(final float min, final float max) {
        if ((min != minX) || (max != maxX)) {
            minX = min;
            maxX = max;

            if (xScaleType == LOG_SCALE) {
                scaledMinX = (float) Math.log(minX);
                scaledMaxX = (float) Math.log(maxX);
            } else {
                scaledMinX = minX;
                scaledMaxX = maxX;
            }

            setNumbering(numbering);
        }
    }

    /**
     * Sets the minimum/maximum values on the y-axis. Set both min and
     * max to 0.0f for auto-adjusting (default).
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void setYExtrema(float min, float max) {
        if ((min == 0.0f) && (max == 0.0f)) {
            autoYExtrema = true;

            // determine min and max from model
            min = Float.POSITIVE_INFINITY;
            max = Float.NEGATIVE_INFINITY;

            float tmp;
            model.firstSeries();

            do {
                for (int i = 0; i < model.seriesLength(); i++) {
                    tmp = model.getYCoord(i);
                    min = Math.min(tmp, min);
                    max = Math.max(tmp, max);
                }
            } while (model.nextSeries());

            if (min == max) {
                // default values if no variation in data
                if (yScaleType == LOG_SCALE) {
                    min /= 10.0f;
                    max *= 10.0f;
                } else {
                    min -= 0.5f;
                    max += 0.5f;
                }
            }

            if ((min == Float.POSITIVE_INFINITY) ||
                    (max == Float.NEGATIVE_INFINITY)) {
                // default values if no data
                if (yScaleType == LOG_SCALE) {
                    min = 1.0f;
                    max = 100.0f;
                } else {
                    min = -5.0f;
                    max = 5.0f;
                }
            }
        } else if (max <= min) {
            throw new IllegalArgumentException(
                "Maximum should be greater than minimum; max = " + max +
                " and min = " + min);
        } else {
            autoYExtrema = false;
        }

        rescaleY(min, max);
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param growth DOCUMENT ME!
     */
    public final void setYExtrema(float min, float max, float growth) {
        setYExtrema(min, max);
        yGrowth = growth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    private void rescaleY(final float min, final float max) {
        if ((min != minY) || (max != maxY)) {
            minY = min;
            maxY = max;

            if (yScaleType == LOG_SCALE) {
                scaledMinY = (float) Math.log(minY);
                scaledMaxY = (float) Math.log(maxY);
            } else {
                scaledMinY = minY;
                scaledMaxY = maxY;
            }

            setNumbering(numbering);
        }
    }

    /**
     * Sets the data marker used to draw data points.
     *
     * @param marker DOCUMENT ME!
     */
    public final void setMarker(Graph2D.DataMarker marker) {
        dataMarker = marker;
        redraw();
    }

    /**
     * Sets the color of the nth y-series.
     *
     * @param n the index of the y-series.
     * @param c the line color.
     */
    public final void setColor(int n, Color c) {
        seriesColor[n] = c;
        redraw();
    }

    /**
     * Gets the color of the nth y-series.
     *
     * @param n the index of the y-series.
     *
     * @return DOCUMENT ME!
     */
    public final Color getColor(int n) {
        return seriesColor[n];
    }

    /**
     * Reshapes this graph to the specified bounding box.
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
     * Rescales this graph.
     */
    protected final void rescale() {
        final Dimension minSize = getMinimumSize();

        // Swing optimised
        final int thisWidth = Math.max(getWidth(), minSize.width);
        final int thisHeight = Math.max(getHeight(), minSize.height);
        xScale = (thisWidth - (leftAxisPad + axisPad)) / (scaledMaxX -
            scaledMinX);
        yScale = (thisHeight - (2 * axisPad)) / (scaledMaxY - scaledMinY);

        if (autoXInc) {
            xInc = (float) MathUtils.round(xIncPixels / xScale, 1);

            if (xInc == 0.0f) {
                xInc = Float.MIN_VALUE;
            }
        }

        //assert xInc != 0.0f;
        if (autoYInc) {
            yInc = (float) MathUtils.round(yIncPixels / yScale, 1);

            if (yInc == 0.0f) {
                yInc = Float.MIN_VALUE;
            }
        }

        //assert yInc != 0.0f;
        origin.x = leftAxisPad - Math.round(scaledMinX * xScale);
        origin.y = thisHeight - axisPad + Math.round(scaledMinY * yScale);
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
        if (xScaleType == LOG_SCALE) {
            x = (float) Math.log(x);
        }

        if (yScaleType == LOG_SCALE) {
            y = (float) Math.log(y);
        }

        return scaledDataToScreen(x, y);
    }

    /**
     * Converts a scaled data point to screen coordinates.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected final Point scaledDataToScreen(float x, float y) {
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
        float x = (p.x - origin.x) / xScale;
        float y = (origin.y - p.y) / yScale;

        if (xScaleType == LOG_SCALE) {
            x = (float) Math.exp(x);
        }

        if (yScaleType == LOG_SCALE) {
            y = (float) Math.exp(y);
        }

        return new Point2D.Float(x, y);
    }

    /**
     * Draws the graph axes.
     *
     * @param g DOCUMENT ME!
     */
    protected final void drawAxes(Graphics g) {
        // Swing optimised
        final int width = getWidth();
        final int height = getHeight();
        g.setColor(getForeground());

        // grid lines and numbering
        if (gridLines || numbering) {
            final FontMetrics metrics = g.getFontMetrics();
            final int strHeight = metrics.getHeight();

            // x-axis numbering and vertical grid lines
            float xAxisY;

            if (scaledMinY > 0.0f) {
                xAxisY = scaledMinY;
            } else if (scaledMaxY < 0.0f) {
                xAxisY = scaledMaxY;
            } else {
                xAxisY = 0.0f;
            }

            for (float x = (scaledMinX > 0.0f) ? scaledMinX : xInc;
                    x <= scaledMaxX; x += xInc) {
                Point p = scaledDataToScreen(x, xAxisY);

                if (gridLines) {
                    g.setColor(gridLineColor);
                    g.drawLine(p.x, axisPad - scalePad, p.x,
                        height - (axisPad - scalePad));
                    g.setColor(getForeground());
                }

                if (numbering) {
                    double scaledX;

                    if (xScaleType == LOG_SCALE) {
                        scaledX = Math.exp(x);
                    } else {
                        scaledX = x;
                    }

                    String str = xNumberFormat.format(scaledX);
                    int strWidth = metrics.stringWidth(str);
                    g.drawLine(p.x, p.y, p.x, p.y + 5);
                    g.drawString(str, p.x - (strWidth / 2), p.y + 5 +
                        strHeight);
                }
            }

            for (float x = -xInc; x >= scaledMinX; x -= xInc) {
                Point p = scaledDataToScreen(x, xAxisY);

                if (gridLines) {
                    g.setColor(gridLineColor);
                    g.drawLine(p.x, axisPad - scalePad, p.x,
                        height - (axisPad - scalePad));
                    g.setColor(getForeground());
                }

                if (numbering) {
                    double scaledX;

                    if (xScaleType == LOG_SCALE) {
                        scaledX = Math.exp(x);
                    } else {
                        scaledX = x;
                    }

                    String str = xNumberFormat.format(scaledX);
                    int strWidth = metrics.stringWidth(str);
                    g.drawLine(p.x, p.y, p.x, p.y + 5);
                    g.drawString(str, p.x - (strWidth / 2), p.y + 5 +
                        strHeight);
                }
            }

            // y-axis numbering and horizontal grid lines
            float yAxisX;

            if (scaledMinX > 0.0f) {
                yAxisX = scaledMinX;
            } else if (scaledMaxX < 0.0f) {
                yAxisX = scaledMaxX;
            } else {
                yAxisX = 0.0f;
            }

            for (float y = (scaledMinY > 0.0f) ? scaledMinY : yInc;
                    y <= scaledMaxY; y += yInc) {
                Point p = scaledDataToScreen(yAxisX, y);

                if (gridLines) {
                    g.setColor(gridLineColor);
                    g.drawLine(leftAxisPad - scalePad, p.y,
                        width - (axisPad - scalePad), p.y);
                    g.setColor(getForeground());
                }

                if (numbering) {
                    double scaledY;

                    if (yScaleType == LOG_SCALE) {
                        scaledY = Math.exp(y);
                    } else {
                        scaledY = y;
                    }

                    String str = yNumberFormat.format(scaledY);
                    int strWidth = metrics.stringWidth(str);
                    g.drawLine(p.x, p.y, p.x - 5, p.y);
                    g.drawString(str, p.x - 8 - strWidth, p.y +
                        (strHeight / 3));
                }
            }

            for (float y = -yInc; y >= scaledMinY; y -= yInc) {
                Point p = scaledDataToScreen(yAxisX, y);

                if (gridLines) {
                    g.setColor(gridLineColor);
                    g.drawLine(leftAxisPad - scalePad, p.y,
                        width - (axisPad - scalePad), p.y);
                    g.setColor(getForeground());
                }

                if (numbering) {
                    double scaledY;

                    if (yScaleType == LOG_SCALE) {
                        scaledY = Math.exp(y);
                    } else {
                        scaledY = y;
                    }

                    String str = yNumberFormat.format(scaledY);
                    int strWidth = metrics.stringWidth(str);
                    g.drawLine(p.x, p.y, p.x - 5, p.y);
                    g.drawString(str, p.x - 8 - strWidth, p.y +
                        (strHeight / 3));
                }
            }
        }

        // axis lines
        // horizontal axis
        if (scaledMinY > 0.0f) {
            // draw at bottom
            g.drawLine(leftAxisPad - scalePad, height - axisPad,
                width - (axisPad - scalePad), height - axisPad);
        } else if (scaledMaxY < 0.0f) {
            // draw at top
            g.drawLine(leftAxisPad - scalePad, axisPad,
                width - (axisPad - scalePad), axisPad);
        } else {
            // draw through y origin
            g.drawLine(leftAxisPad - scalePad, origin.y,
                width - (axisPad - scalePad), origin.y);
        }

        // vertical axis
        if (scaledMinX > 0.0f) {
            // draw at left
            g.drawLine(leftAxisPad, axisPad - scalePad, leftAxisPad,
                height - (axisPad - scalePad));
        } else if (scaledMaxX < 0.0f) {
            // draw at right
            g.drawLine(width - axisPad, axisPad - scalePad, width - axisPad,
                height - (axisPad - scalePad));
        } else {
            // draw through x origin
            g.drawLine(origin.x, axisPad - scalePad, origin.x,
                height - (axisPad - scalePad));
        }
    }

    /**
     * Paints the graph.
     *
     * @param g DOCUMENT ME!
     */
    protected void offscreenPaint(Graphics g) {
        drawAxes(g);

        // points
        model.firstSeries();
        g.setColor(seriesColor[0]);

        int i;

        for (i = 0; i < model.seriesLength(); i++)
            drawDataPoint(g, i);

        for (int n = 1; model.nextSeries(); n++) {
            g.setColor(seriesColor[n]);

            for (i = 0; i < model.seriesLength(); i++)
                drawDataPoint(g, i);
        }
    }

    /**
     * Draws a single data point in the current series.
     *
     * @param g DOCUMENT ME!
     * @param i index of the data point.
     */
    private void drawDataPoint(Graphics g, int i) {
        Point p = dataToScreen(model.getXCoord(i), model.getYCoord(i));
        dataMarker.paint(g, p.x, p.y);
    }
}
