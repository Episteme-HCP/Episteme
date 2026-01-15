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
import org.jscience.architecture.traffic.View;

import java.awt.*;


/**
 * This implements the right-click PopupMenu
 *
 * @author Group GUI
 * @version 1.0
 */
public abstract class PopupMenuTool implements Tool {
    /** DOCUMENT ME! */
    protected PopupMenuAction pma;

/**
     * Creates a new PopupMenuTool object.
     *
     * @param con DOCUMENT ME!
     */
    PopupMenuTool(Controller con) {
        pma = new PopupMenuAction(con);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PopupMenuAction getPopupMenuAction() {
        return pma;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mousePressed(View view, Point p, Tool.Mask mask) {
        if (mask.isRight()) {
            pma.doPopupMenu(view, p);
        }
    }
}
