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
