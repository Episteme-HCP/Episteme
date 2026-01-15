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
import org.jscience.architecture.traffic.Selection;
import org.jscience.architecture.traffic.View;
import org.jscience.architecture.traffic.edit.EditModel;
import org.jscience.architecture.traffic.infrastructure.InfraException;
import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.infrastructure.Node;

import java.awt.*;


/**
 * This implements zoom user action
 *
 * @author Group GUI
 * @version 1.0
 */
public class NodeAction implements ToolAction {
    /** DOCUMENT ME! */
    EditModel model;

/**
     * Creates a new NodeAction object.
     *
     * @param m DOCUMENT ME!
     */
    public NodeAction(EditModel m) {
        model = m;
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
     * @param type DOCUMENT ME!
     */
    public void doCreateNode(View view, Point p, int type) {
        Class[] sf = { Node.class };
        Selection s = new Selection(view, sf, model.getInfrastructure());
        Rectangle r = new Rectangle(p);
        int growsize = (Infrastructure.blockLength * 2) +
            (Infrastructure.blockWidth * 8) + 2;
        r.grow(growsize, growsize);
        s.newSelection(r);

        if (s.getNumSelectedObjects() > 0) {
            return;
        }

        try {
            model.addNode(p, type);
        } catch (InfraException e) {
            Controller.reportError(e);
        }
    }
}
