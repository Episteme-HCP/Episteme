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
package org.jscience.architecture.traffic.edit;

import org.jscience.architecture.traffic.TrafficToolBar;
import org.jscience.architecture.traffic.tools.*;
import org.jscience.architecture.traffic.util.IconButton;

import java.awt.event.ActionEvent;


/**
 * The ToolBar for the editor
 *
 * @author Group GUI
 * @version 1.0
 */
public class EditToolBar extends TrafficToolBar {
    /** DOCUMENT ME! */
    protected static final int TOTAL = APPBUTTON;

    /** DOCUMENT ME! */
    protected static final int MOVE = APPBUTTON + 1;

    /** DOCUMENT ME! */
    protected static final int NODE = APPBUTTON + 2;

    /** DOCUMENT ME! */
    protected static final int ROAD = APPBUTTON + 3;

    /** DOCUMENT ME! */
    protected static final int LANE = APPBUTTON + 4;

/**
     * Creates a new EditToolBar object.
     *
     * @param ec DOCUMENT ME!
     */
    public EditToolBar(EditController ec) {
        super(ec, true);
    }

    /**
     * DOCUMENT ME!
     */
    protected void addTools() {
        //  	addButton("org/jscience/architecture/traffic/images/total.gif", this, TOTAL);
        //		addButton("org/jscience/architecture/traffic/images/move.gif", this, MOVE);
        addButton("org/jscience/architecture/traffic/images/node.gif", this,
            NODE);
        addButton("org/jscience/architecture/traffic/images/road.gif", this,
            ROAD);
        addButton("org/jscience/architecture/traffic/images/drivelane.gif",
            this, LANE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        int Id = ((IconButton) e.getSource()).getId();

        switch (Id) {
        case TOTAL: {
            controller.changeTool(new TotalEditTool((EditController) controller));

            break;
        }

        case MOVE: {
            controller.changeTool(new MoveTool((EditController) controller));

            break;
        }

        case NODE: {
            controller.changeTool(new NodeTool((EditController) controller));

            break;
        }

        case ROAD: {
            controller.changeTool(new RoadTool((EditController) controller));

            break;
        }

        case LANE: {
            controller.changeTool(new LaneTool((EditController) controller));

            break;
        }
        }
    }
}
