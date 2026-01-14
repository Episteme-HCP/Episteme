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
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0 A <code>ViewScroller</code> is a component that lets the user scroll over a <code>View</code> using scrollbars.
 */
public class ViewScroller extends ScrollPane implements AdjustmentListener,
    ComponentListener {
    /** DOCUMENT ME! */
    protected View view;

/**
     * Create a new <code>ViewScroller</code> for a given
     * <code>Container</code> and <code>View</code>.
     *
     * @param view the <code>View</code> for this <code>ViewScroller</code>
     */
    public ViewScroller(View view) {
        super(ScrollPane.SCROLLBARS_ALWAYS);
        setup(view);
    }

/**
     * Create a new <code>ViewScroller</code> for a given
     * <code>Container</code> and <code>View</code>.
     *
     * @param view       the <code>View</code> for this <code>ViewScroller</code>
     * @param scrollbars the parent<code>Container</code> for this
     *                   <code>ViewScroller</code>
     */
    public ViewScroller(View view, boolean scrollbars) {
        super(scrollbars ? ScrollPane.SCROLLBARS_ALWAYS
                         : ScrollPane.SCROLLBARS_NEVER);
        setup(view);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    private void setup(View v) {
        view = v;
        getHAdjustable().addAdjustmentListener(this);
        getVAdjustable().addAdjustmentListener(this);
        add(view);
        addComponentListener(this);
    }

    /**
     * Called when infrastructure is resized
     *
     * @param infras DOCUMENT ME!
     */
    public void resizeInfra(Dimension infras) {
        Point p = view.toInfra(view.getViewportPosition());
        view.resizeInfra(infras);
        scrollTo(p);
        doLayout();
        scrollTo(p);
    }

    /**
     * Centers the view
     */
    public void center() {
        setScrollPosition(new Point(
                (int) ((view.getWidth() / 2) -
                (view.getViewportSize().width / 2)),
                (int) ((view.getHeight() / 2) -
                (view.getViewportSize().height / 2))));
    }

    /**
     * Scrolls the view to the specified infrastructure point
     *
     * @param p DOCUMENT ME!
     */
    public void scrollTo(Point p) {
        Point vp = view.toView(p);
        setScrollPosition(vp);
    }

    /**
     * Centers the view on the specified infrastructure point
     *
     * @param p DOCUMENT ME!
     */
    public void center(Point p) {
        p = view.toView(p);
        p.x -= (int) (view.getViewportSize().width / 2);
        p.y -= (int) (view.getViewportSize().height / 2);
        setScrollPosition(p);
    }

    /**
     * Returns the current center point in infrastructure coordinate
     * space
     *
     * @return DOCUMENT ME!
     */
    public Point getCurrentCenter() {
        Point p = getScrollPosition();
        p.x += (int) (getViewportSize().width / 2);
        p.y += (int) (getViewportSize().height / 2);

        return view.toInfra(p);
    }

    /**
     * Zooms the view to the specified index
     *
     * @param index DOCUMENT ME!
     */
    public void zoomTo(int index) {
        Point cc = getCurrentCenter();
        view.zoomTo(index);
        center(cc);
        doLayout();
        center(cc);
    }

    /**
     * Zooms the view in, centering on the given point
     *
     * @param p DOCUMENT ME!
     */
    public void zoomIn(Point p) {
        view.zoomIn();
        center(p);
        doLayout();
        center(p);
    }

    /**
     * Zooms the view out, centering on the given point
     *
     * @param p DOCUMENT ME!
     */
    public void zoomOut(Point p) {
        view.zoomOut();
        center(p);
        doLayout();
        center(p);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
        view.scrollViewport(getScrollPosition());
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentHidden(ComponentEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentMoved(ComponentEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentResized(ComponentEvent e) {
        view.resizeViewport(getViewportSize());
        doLayout();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentShown(ComponentEvent e) {
    }
}
