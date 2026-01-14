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

import java.awt.*;


/**
 * A graph layout arranges components in the style of a graph. Available
 * regions are:
 * <code>Title</code><code>Graph</code><code>X-axis</code><code>Y-axis</code>
 *
 * @author Mark Hale
 * @version 0.5
 */
public final class GraphLayout implements LayoutManager2 {
    /** DOCUMENT ME! */
    private final static Dimension zeroDim = new Dimension();

    /** DOCUMENT ME! */
    private Component title;

    /** DOCUMENT ME! */
    private Component graph;

    /** DOCUMENT ME! */
    private Component xaxis;

    /** DOCUMENT ME! */
    private Component yaxis;

/**
     * Creates a new GraphLayout object.
     */
    public GraphLayout() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param comp DOCUMENT ME!
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     * @param constraint DOCUMENT ME!
     */
    public void addLayoutComponent(Component comp, Object constraint) {
        if (constraint.equals("Title")) {
            title = comp;
        } else if (constraint.equals("Graph")) {
            graph = comp;
        } else if (constraint.equals("X-axis")) {
            xaxis = comp;
        } else if (constraint.equals("Y-axis")) {
            yaxis = comp;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     */
    public void removeLayoutComponent(Component comp) {
        if (comp.equals(title)) {
            title = null;
        }

        if (comp.equals(graph)) {
            graph = null;
        }

        if (comp.equals(xaxis)) {
            xaxis = null;
        }

        if (comp.equals(yaxis)) {
            yaxis = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Dimension size = parent.getSize();
            Insets insets = parent.getInsets();
            int width = size.width - insets.left - insets.right;
            int height = size.height - insets.top - insets.bottom;
            int graphLeftPad = 0;
            int graphAxisPad = 0;

            if (graph instanceof Graph2D) {
                graphLeftPad = ((Graph2D) graph).leftAxisPad;
                graphAxisPad = ((Graph2D) graph).axisPad;
            } else if (graph instanceof CategoryGraph2D) {
                graphLeftPad = ((CategoryGraph2D) graph).leftAxisPad;
                graphAxisPad = ((CategoryGraph2D) graph).axisPad;
            }

            int yaxisWidth = getMinimumSize(yaxis).width;
            int graphWidth = width - yaxisWidth;
            int titleWidth = graphWidth - graphLeftPad - graphAxisPad;
            int xaxisWidth = titleWidth;
            int titleHeight = getMinimumSize(title).height;
            int xaxisHeight = getMinimumSize(xaxis).height;
            int graphHeight = height - titleHeight - xaxisHeight;
            int yaxisHeight = graphHeight - (2 * graphAxisPad);

            if (title != null) {
                title.setBounds(insets.left + yaxisWidth + graphLeftPad,
                    insets.top, titleWidth, titleHeight);
            }

            if (graph != null) {
                graph.setBounds(insets.left + yaxisWidth,
                    insets.top + titleHeight, graphWidth, graphHeight);
            }

            if (yaxis != null) {
                yaxis.setBounds(insets.left,
                    insets.top + titleHeight + graphAxisPad, yaxisWidth,
                    yaxisHeight);
            }

            if (xaxis != null) {
                xaxis.setBounds(insets.left + yaxisWidth + graphLeftPad,
                    height - xaxisHeight, xaxisWidth, xaxisHeight);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public void invalidateLayout(Container parent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getLayoutAlignmentX(Container parent) {
        return 0.5f;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getLayoutAlignmentY(Container parent) {
        return 0.5f;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension minimumLayoutSize(Container parent) {
        return calculateLayoutSize(parent.getInsets(), getMinimumSize(title),
            getMinimumSize(graph), getMinimumSize(xaxis), getMinimumSize(yaxis));
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Dimension getMinimumSize(Component comp) {
        if (comp == null) {
            return zeroDim;
        } else {
            return comp.getMinimumSize();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension preferredLayoutSize(Container parent) {
        return calculateLayoutSize(parent.getInsets(), getMinimumSize(title),
            getPreferredSize(graph), getMinimumSize(xaxis),
            getMinimumSize(yaxis));
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Dimension getPreferredSize(Component comp) {
        if (comp == null) {
            return zeroDim;
        } else {
            return comp.getPreferredSize();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension maximumLayoutSize(Container parent) {
        return calculateLayoutSize(parent.getInsets(), getMaximumSize(title),
            getMaximumSize(graph), getMaximumSize(xaxis), getMaximumSize(yaxis));
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Dimension getMaximumSize(Component comp) {
        if (comp == null) {
            return zeroDim;
        } else {
            return comp.getMaximumSize();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param insets DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param graph DOCUMENT ME!
     * @param xaxis DOCUMENT ME!
     * @param yaxis DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Dimension calculateLayoutSize(Insets insets,
        Dimension title, Dimension graph, Dimension xaxis, Dimension yaxis) {
        int width = insets.left + insets.right;
        int height = insets.top + insets.bottom;

        if (title.width > (yaxis.width + graph.width)) {
            width += title.width;
        } else {
            width += (yaxis.width + graph.width);
        }

        height += (title.height + xaxis.height);

        if (yaxis.height > graph.height) {
            height += yaxis.height;
        } else {
            height += graph.height;
        }

        return new Dimension(width, height);
    }
}
