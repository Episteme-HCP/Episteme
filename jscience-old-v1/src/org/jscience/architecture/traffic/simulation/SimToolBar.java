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
package org.jscience.architecture.traffic.simulation;

import org.jscience.architecture.traffic.TrafficToolBar;
import org.jscience.architecture.traffic.util.IconButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;


/**
 * The ToolBar for the simulator
 *
 * @author Group GUI
 * @version 1.0
 */
public class SimToolBar extends TrafficToolBar {
    /** DOCUMENT ME! */
    protected static final int STEP = APPBUTTON;

    /** DOCUMENT ME! */
    protected static final int RUN = APPBUTTON + 1;

    /** DOCUMENT ME! */
    protected static final int PAUSE = APPBUTTON + 2;

    /** DOCUMENT ME! */
    protected static final int STOP = APPBUTTON + 3;

    /** DOCUMENT ME! */
    protected Choice speed;

/**
     * Creates a new SimToolBar object.
     *
     * @param sc DOCUMENT ME!
     */
    public SimToolBar(SimController sc) {
        super(sc, false);

        addSeparator();
    }

    /**
     * DOCUMENT ME!
     */
    protected void addTools() {
        addButton("org/jscience/architecture/traffic/images/edgenode.gif",
            this, EDGENODE);

        addSeparator();

        addButton("org/jscience/architecture/traffic/images/step.gif", this,
            STEP);
        addButton("org/jscience/architecture/traffic/images/run.gif", this, RUN);
        addButton("org/jscience/architecture/traffic/images/pause.gif", this,
            PAUSE);
        addButton("org/jscience/architecture/traffic/images/stop.gif", this,
            STOP);

        addSeparator();

        speed = new Choice();

        for (int i = 0; i < SimController.speedTexts.length; i++)
            speed.add(SimController.speedTexts[i]);

        speed.addItemListener(this);
        speed.setSize(100, 20);
        addComponent(speed);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Choice getSpeed() {
        return speed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        SimController sc = (SimController) controller;
        int Id = ((IconButton) e.getSource()).getId();

        switch (Id) {
        case STEP: {
            sc.doStep();

            break;
        }

        case RUN: {
            sc.unpause();

            break;
        }

        case PAUSE: {
            sc.pause();

            break;
        }

        case STOP: {
            sc.stop();

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
        SimController sc = (SimController) controller;
        Choice s = (Choice) e.getItemSelectable();

        if (s == speed) {
            sc.setSpeed(speed.getSelectedIndex());
        } else {
            super.itemStateChanged(e);
        }
    }
}
