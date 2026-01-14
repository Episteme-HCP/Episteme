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

package org.jscience.architecture.traffic.tools;

import org.jscience.architecture.traffic.View;
import org.jscience.architecture.traffic.ViewScroller;

import java.awt.*;


/**
 * This implements scroll user action (like in acrobat reader)
 *
 * @author Group GUI
 * @version 1.0
 */
public class ScrollAction implements ToolAction {
    /** DOCUMENT ME! */
    protected Point startPoint = null;

    /** DOCUMENT ME! */
    protected Point startScrollPoint = null;

    /** DOCUMENT ME! */
    protected ViewScroller viewScroller = null;

    /** DOCUMENT ME! */
    protected boolean skipnext = false;

/**
     * Creates a new ScrollAction object.
     *
     * @param vs DOCUMENT ME!
     */
    public ScrollAction(ViewScroller vs) {
        viewScroller = vs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean beingUsed() {
        return startPoint != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean startScroll(View view, Point p) {
        startPoint = p;
        startScrollPoint = viewScroller.getScrollPosition();

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void doScroll(View view, Point p) {
        if (startPoint.distance(p) > 5) {
            if (skipnext) {
                skipnext = false;

                return;
            }

            int offx = p.x - startPoint.x;
            int offy = p.y - startPoint.y;
            Point to = new Point(startScrollPoint.x - offx,
                    startScrollPoint.y - offy);
            startScrollPoint = to;
            viewScroller.setScrollPosition(to);
            skipnext = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void endScroll(View view, Point p) {
        Point to = new Point((startScrollPoint.x + p.x) - startPoint.x,
                (startScrollPoint.y + p.y) - startPoint.y);
        viewScroller.setScrollPosition(to);
        startPoint = null;
        startScrollPoint = null;
    }
}
