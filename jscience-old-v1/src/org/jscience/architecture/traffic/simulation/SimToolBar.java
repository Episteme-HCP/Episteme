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
