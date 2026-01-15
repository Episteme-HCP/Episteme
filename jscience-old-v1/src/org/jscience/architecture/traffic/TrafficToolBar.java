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
package org.jscience.architecture.traffic;

import org.jscience.architecture.traffic.tools.EdgeNodeTool;
import org.jscience.architecture.traffic.tools.ScrollTool;
import org.jscience.architecture.traffic.tools.SelectTool;
import org.jscience.architecture.traffic.tools.ZoomTool;
import org.jscience.architecture.traffic.util.IconButton;
import org.jscience.architecture.traffic.util.ToolBar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * Abstract ToolBar for simulator and editor contains common elements
 *
 * @author Group GUI
 * @version 1.0
 */
public abstract class TrafficToolBar extends ToolBar implements ActionListener,
    ItemListener {
    /** DOCUMENT ME! */
    protected static final int NEW = 1;

    /** DOCUMENT ME! */
    protected static final int OPEN = 2;

    /** DOCUMENT ME! */
    protected static final int SAVE = 3;

    /** DOCUMENT ME! */
    protected static final int CENTER = 4;

    /** DOCUMENT ME! */
    protected static final int SCROLL = 5;

    /** DOCUMENT ME! */
    protected static final int ZOOM = 6;

    /** DOCUMENT ME! */
    protected static final int SELECT = 7;

    /** DOCUMENT ME! */
    protected static final int EDGENODE = 9;

    /** DOCUMENT ME! */
    protected static final int CONFIG = 10;

    /** DOCUMENT ME! */
    protected static final int APPBUTTON = 100;

    /** DOCUMENT ME! */
    protected static final int HELP = 911;

    /** DOCUMENT ME! */
    protected Controller controller;

    /** DOCUMENT ME! */
    protected Choice zoom;

/**
     * Creates a new TrafficToolBar object.
     *
     * @param c       DOCUMENT ME!
     * @param newicon DOCUMENT ME!
     */
    public TrafficToolBar(Controller c, boolean newicon) {
        super();
        controller = c;

        if (newicon) {
            addButton("org/jscience/architecture/traffic/images/new.gif", this,
                NEW);
        }

        addButton("org/jscience/architecture/traffic/images/open.gif", this,
            OPEN);
        addButton("org/jscience/architecture/traffic/images/save.gif", this,
            SAVE);

        addSeparator();

        zoom = new Choice();
        zoom.add("25%");
        zoom.add("50%");
        zoom.add("75%");
        zoom.add("100%");
        zoom.add("150%");
        zoom.add("200%");
        zoom.add("250%");
        zoom.addItemListener(this);
        zoom.setSize(75, 15);
        zoom.select(3);
        addComponent(zoom);

        addSeparator();
        addButton("org/jscience/architecture/traffic/images/center.gif", this,
            CENTER);
        addSeparator();

        addButton("org/jscience/architecture/traffic/images/scroll.gif", this,
            SCROLL);
        addButton("org/jscience/architecture/traffic/images/zoom.gif", this,
            ZOOM);
        addButton("org/jscience/architecture/traffic/images/select.gif", this,
            SELECT);

        addSeparator();

        addTools();

        addSeparator();

        addButton("org/jscience/architecture/traffic/images/config.gif", this,
            CONFIG);

        addSeparator();

        addButton("org/jscience/architecture/traffic/images/help.gif", this,
            HELP);

        addSeparator();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void addTools();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Choice getZoom() {
        return zoom;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        int Id = ((IconButton) e.getSource()).getId();

        switch (Id) {
        case NEW: {
            controller.newFile();

            break;
        }

        case OPEN: {
            controller.openFile();

            break;
        }

        case SAVE: {
            controller.saveFile();

            break;
        }

        case CENTER: {
            controller.getViewScroller().center();

            break;
        }

        case SCROLL: {
            controller.changeTool(new ScrollTool(controller));

            break;
        }

        case ZOOM: {
            controller.changeTool(new ZoomTool(controller));

            break;
        }

        case SELECT: {
            controller.changeTool(new SelectTool(controller));

            break;
        }

        case EDGENODE: {
            controller.changeTool(new EdgeNodeTool(controller));

            break;
        }

        case CONFIG: {
            controller.switchConfigDialog();

            break;
        }

        case HELP: {
            controller.showHelp(HelpViewer.HELP_INDEX);

            break;
        }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void itemStateChanged(ItemEvent e) {
        controller.zoomTo(zoom.getSelectedIndex());
    }
}
