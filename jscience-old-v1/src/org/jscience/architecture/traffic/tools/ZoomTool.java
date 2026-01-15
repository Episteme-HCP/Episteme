/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
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
