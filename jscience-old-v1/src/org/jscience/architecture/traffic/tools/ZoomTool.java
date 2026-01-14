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

import org.jscience.architecture.traffic.Controller;
import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.View;

import java.awt.*;


/**
 * Left-click to zoom in, right-click to zoom out. That is, when this
 * <code>Tool</code> is the currently selected <code>Tool</code>.
 *
 * @author Group GUI
 * @version 1.0
 */
public class ZoomTool implements Tool {
    /** DOCUMENT ME! */
    protected ZoomAction za;

/**
     * Creates a <code>ZoomTool</code>.
     *
     * @param c The <code>Controller</code> controlling this <code>Tool</code>.
     */
    public ZoomTool(Controller c) {
        za = new ZoomAction(c);
    }

    /**
     * Invoked when the user releases a mouse button. Zoom in on
     * left-click, zoom out on right-click.
     *
     * @param view The <code>View</code> that the event originates from.
     * @param p The coordinates in the infrastructure the mouse cursor was at
     *        when the event was generated.
     * @param mask Identifies which button was pressed, as well as any
     *        aditional sytem keys
     */
    public void mousePressed(View view, Point p, Tool.Mask mask) {
        if (mask.isLeft()) {
            za.doZoom(view, p, ZoomAction.IN);
        } else if (mask.isRight()) {
            za.doZoom(view, p, ZoomAction.OUT);
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
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mouseMoved(View view, Point p, Tool.Mask mask) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int overlayType() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g) throws TrafficException {
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
