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
import org.jscience.architecture.traffic.Selection;
import org.jscience.architecture.traffic.View;
import org.jscience.architecture.traffic.edit.EditModel;
import org.jscience.architecture.traffic.infrastructure.Drivelane;
import org.jscience.architecture.traffic.infrastructure.InfraException;
import org.jscience.architecture.traffic.infrastructure.Node;
import org.jscience.architecture.traffic.infrastructure.Road;

import java.awt.*;


/**
 * This implements the add lane user action
 *
 * @author Group GUI
 * @version 1.0
 */
public class LaneAction implements ToolAction {
    /** DOCUMENT ME! */
    protected EditModel model;

    /** DOCUMENT ME! */
    protected Node alphaNode = null;

    /** DOCUMENT ME! */
    protected Point startPoint = null;

    /** DOCUMENT ME! */
    protected Point mousePoint = null;

/**
     * Creates a new LaneAction object.
     *
     * @param em DOCUMENT ME!
     */
    public LaneAction(EditModel em) {
        model = em;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean beingUsed() {
        return alphaNode != null;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        alphaNode = null;
        startPoint = null;
        mousePoint = null;
    }

    /**
     * Starts the action by finding the alpha node of the road
     *
     * @param view The main view. Needed to convert coordinates
     * @param p The point to search for the first node
     *
     * @return False if no node was found at the given point (and the action
     *         could not be started consequently)
     */
    public boolean startAction(View view, Point p) {
        Class[] sf = { Node.class };
        Node clicked = (Node) Selection.selectObject(sf,
                model.getInfrastructure(), p);

        if (clicked != null) {
            alphaNode = clicked;
            startPoint = alphaNode.getCoord();
            mousePoint = p;

            return true;
        }

        return false;
    }

    /**
     * Looks for the second node at given point. If it is found, ends
     * the action by adding a drivelane to the road connecting the first and
     * second node.
     *
     * @param view DOCUMENT ME!
     * @param p The point
     *
     * @return False if the action was ended without adding a drivelane.
     */
    public boolean endAction(View view, Point p) {
        Class[] sf = { Node.class };
        Node clicked = (Node) Selection.selectObject(sf,
                model.getInfrastructure(), p);

        try {
            if (clicked == alphaNode) {
                return true; // clicking the alpha node again does not end the action
            }

            if (clicked == null) {
                reset();

                return false;
            } else {
                // add drivelane
                Node betaNode = clicked;
                Road[] alphaRoads = alphaNode.getAllRoads();
                Road[] betaRoads = betaNode.getAllRoads();
                Road road = null;

                for (int i = 0; i < alphaRoads.length; i++)
                    if (alphaRoads[i] != null) {
                        for (int j = 0; j < betaRoads.length; j++)
                            if (alphaRoads[i] == betaRoads[j]) {
                                road = alphaRoads[i];
                            }
                    }

                if ((road == null) || (road.getNumInboundLanes(betaNode) >= 4)) {
                    reset();

                    return false;
                }

                model.addLane(new Drivelane(road), road, betaNode);

                reset();
            }
        } catch (InfraException e) {
            reset();
            Controller.reportError(e);

            return false;
        }

        return true;
    }

    /**
     * Moves the current mouse point
     *
     * @param view DOCUMENT ME!
     * @param p The new position of the mouse cursor
     */
    public void moveAction(View view, Point p) {
        mousePoint = p;
    }

    /**
     * Paints a graphical representation of the status of this action
     *
     * @param g The Graphics object to paint on
     */
    public void paint(Graphics g) {
        g.setXORMode(Color.darkGray);
        g.setColor(Color.lightGray);
        g.drawLine(startPoint.x, startPoint.y, mousePoint.x, mousePoint.y);
    }
}
