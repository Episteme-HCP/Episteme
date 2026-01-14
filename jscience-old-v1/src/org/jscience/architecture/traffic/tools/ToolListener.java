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
import org.jscience.architecture.traffic.View;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 * Serves as MouseListener and MouseMotionListener for  View. Asks
 * Controller for current Tool  and invokes a method of this Tool when
 * necessary. Created and set by Controller.
 *
 * @author Group GUI
 * @version 1.0
 */
public class ToolListener implements MouseListener, MouseMotionListener {
    /** DOCUMENT ME! */
    Controller controller;

    /** DOCUMENT ME! */
    View view;

/**
     * Creates a ToolListener.
     *
     * @param con The Controller maintaining the currently selected Tool.
     * @param con The View.
     */
    public ToolListener(Controller con, View v) {
        controller = con;
        view = v;
    }

    /**
     * Invoked when a mouse button is pressed on the View.
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
        controller.getCurrentTool()
                  .mousePressed(view, view.toInfra(e.getPoint()),
            new Tool.Mask(e.getModifiers()));
    }

    /**
     * Invoked when a mouse button is released on the View.
     *
     * @param e DOCUMENT ME!
     */
    public void mouseReleased(MouseEvent e) {
        controller.getCurrentTool()
                  .mouseReleased(view, view.toInfra(e.getPoint()),
            new Tool.Mask(e.getModifiers()));
    }

    /**
     * Invoked when the mouse cursor is moved over the View.
     *
     * @param e DOCUMENT ME!
     */
    public void mouseMoved(MouseEvent e) {
        try {
            controller.getCurrentTool()
                      .mouseMoved(view, view.toInfra(e.getPoint()),
                new Tool.Mask(e.getModifiers()));
        } catch (Exception x) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseDragged(MouseEvent e) {
        controller.getCurrentTool()
                  .mouseMoved(view, view.toInfra(e.getPoint()),
            new Tool.Mask(e.getModifiers()));
    }

    /**
     * Empty implementation, required by the MouseListener interface.
     *
     * @param e DOCUMENT ME!
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Empty implementation, required by the MouseListener interface.
     *
     * @param e DOCUMENT ME!
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Empty implementation, required by the MouseListener interface.
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
    }
}
