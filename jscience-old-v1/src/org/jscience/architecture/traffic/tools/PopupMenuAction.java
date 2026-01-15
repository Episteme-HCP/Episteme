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

import org.jscience.architecture.traffic.*;

import java.awt.*;

import java.util.LinkedList;


/**
 * This implements the PopupMenu user action
 *
 * @author Group GUI
 * @version 1.0
 */
public class PopupMenuAction implements ToolAction {
    /** DOCUMENT ME! */
    Controller controller;

/**
     * Creates a new PopupMenuAction object.
     *
     * @param con DOCUMENT ME!
     */
    public PopupMenuAction(Controller con) {
        controller = con;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean beingUsed() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void doPopupMenu(View view, Point p) {
        Selection cs = controller.getCurrentSelection();
        Selection s = new Selection(cs);

        s.newSelection(p);

        if (s.getNumSelectedObjects() > 0) {
            cs.setSelectedObjects(s.getSelectedObjects());
        }

        LinkedList list = cs.getSelectedObjects();

        if (list.size() > 0) {
            try {
                Selectable o = (Selectable) list.getFirst();
                PopupMenu menu = controller.getPopupMenuFor(o);
                view.add(menu);

                Point p2 = view.toView(p);
                menu.show(view, p2.x, p2.y);
            } catch (PopupException e) {
                Controller.reportError(e);
            }
        }
    }
}
