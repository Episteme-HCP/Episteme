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

import org.jscience.architecture.traffic.Selection;
import org.jscience.architecture.traffic.View;

import java.awt.*;


/**
 * This implements the PopupMenu user action
 *
 * @author Group GUI
 * @version 1.0
 */
public class SelectAction implements ToolAction {
    /** DOCUMENT ME! */
    public static final int INVERT = 1;

    /** DOCUMENT ME! */
    public static final int ADD = 2;

    /** DOCUMENT ME! */
    public static final int NEW = 3;

    /** DOCUMENT ME! */
    Selection currentSelection;

    /** DOCUMENT ME! */
    Point startPoint = null;

    /** DOCUMENT ME! */
    Point endPoint = null;

    /** DOCUMENT ME! */
    Point prevPoint = null;

    /** DOCUMENT ME! */
    boolean isDragging = false;

/**
     * Creates a new SelectAction object.
     *
     * @param s DOCUMENT ME!
     */
    public SelectAction(Selection s) {
        currentSelection = s;
    }

    /**
     * Returns the enclosing rectangle of the last (current) drag
     * operation
     *
     * @return DOCUMENT ME!
     */
    private Rectangle getEnclosingRectangle() {
        Rectangle r = new Rectangle(startPoint);

        if (endPoint == null) {
            r.add(prevPoint);
        } else {
            r.add(endPoint);
        }

        return r;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean beingUsed() {
        return isDragging;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void doStart(View view, Point p) {
        startPoint = p;
        prevPoint = p;
        isDragging = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void doDrag(View view, Point p) {
        prevPoint = endPoint;
        endPoint = p;

        if (startPoint != null) {
            Rectangle r = new Rectangle(view.toView(startPoint));

            if (endPoint != null) {
                r.add(view.toView(endPoint));
            }

            if (prevPoint != null) {
                r.add(view.toView(prevPoint));
            }

            r.grow(1, 1);
            view.repaint(r.x, r.y, r.width, r.height);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public void endDrag(View view, Point p, int type) {
        endPoint = p;
        isDragging = false;

        Rectangle r = getEnclosingRectangle();

        if ((r.width < 5) && (r.height < 5)) {
            Point p2 = new Point(r.x + 2, r.y + 2);

            if (type == INVERT) {
                currentSelection.invertWithSelection(p2);
            } else if (type == ADD) {
                currentSelection.addToSelection(p2);
            } else {
                currentSelection.newSelection(p2);
            }
        } else {
            if ((type == ADD) || (type == INVERT)) {
                currentSelection.addToSelection(r);
            } else {
                currentSelection.newSelection(r);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.setXORMode(Color.darkGray);
        g.setColor(Color.lightGray);

        Rectangle r = getEnclosingRectangle();
        g.drawRect(r.x, r.y, r.width, r.height);
    }
}
