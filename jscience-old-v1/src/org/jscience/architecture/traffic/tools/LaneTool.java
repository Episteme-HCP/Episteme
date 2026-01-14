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

import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.View;
import org.jscience.architecture.traffic.edit.EditController;

import java.awt.*;


/**
 * This tool implements the LaneAction
 *
 * @author Group GUI
 * @version 1.0
 */
public class LaneTool extends PopupMenuTool {
    /** DOCUMENT ME! */
    protected LaneAction la;

/**
     * Creates a new LaneTool object.
     *
     * @param c DOCUMENT ME!
     */
    public LaneTool(EditController c) {
        super(c);
        la = new LaneAction(c.getEditModel());
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mousePressed(View view, Point p, Tool.Mask mask) {
        if (la.beingUsed()) {
            if (mask.isRight()) {
                la.reset();
                view.repaint();
            }

            return;
        }

        super.mousePressed(view, p, mask);

        if (!mask.isLeft()) {
            return;
        }

        if (la.startAction(view, p)) {
            view.repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mouseReleased(View view, Point p, Tool.Mask mask) {
        if (!mask.isLeft() && !la.beingUsed()) {
            return;
        }

        la.endAction(view, p);
        view.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mouseMoved(View view, Point p, Tool.Mask mask) {
        if (la.beingUsed()) {
            la.moveAction(view, p);
            view.repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int overlayType() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g) throws TrafficException {
        if (la.beingUsed()) {
            la.paint(g);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel getPanel() {
        return new Panel(null);
    }
}
