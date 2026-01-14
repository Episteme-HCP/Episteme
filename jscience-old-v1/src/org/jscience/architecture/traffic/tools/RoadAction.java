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
import org.jscience.architecture.traffic.infrastructure.*;

import java.awt.*;

import java.util.Vector;


/**
 * This implements create road user action
 *
 * @author Group GUI
 * @version 1.0
 */
public class RoadAction implements ToolAction {
    /** DOCUMENT ME! */
    protected static final int MIN_TURN_DISTANCE = 50;

    /** DOCUMENT ME! */
    protected EditModel model;

    /** DOCUMENT ME! */
    protected Node alphaNode = null;

    /** DOCUMENT ME! */
    protected Point startPoint = null;

    /** DOCUMENT ME! */
    protected Point mousePoint = null;

    /** DOCUMENT ME! */
    protected Point alphaPoint = null;

    /** DOCUMENT ME! */
    protected Vector turnPoints = null;

    /** DOCUMENT ME! */
    protected int alphaConPos = -1;

    /** DOCUMENT ME! */
    protected int betaConPos = -1;

/**
     * Creates a new RoadAction object.
     *
     * @param em DOCUMENT ME!
     */
    public RoadAction(EditModel em) {
        model = em;
        turnPoints = new Vector();
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
        alphaPoint = null;
        turnPoints.clear();
        alphaConPos = -1;
        betaConPos = -1;
    }

    /**
     * Starts creating a road at given point
     *
     * @param view The main view. Needed to convert coordinates
     * @param p The point to start the new road at
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
            startPoint = alphaPoint = alphaNode.getCoord();
            mousePoint = p;

            return true;
        }

        return false;
    }

    /**
     * Performs a step in creating a road at given point. If a node was
     * found at the given point, the road is created and the action ends.
     * Otherwise a turn is added using the given point.
     *
     * @param view DOCUMENT ME!
     * @param p The point
     *
     * @return False if the action was ended without creating a road.
     */
    public boolean nextAction(View view, Point p) {
        Class[] sf = { Node.class };
        Node clicked = (Node) Selection.selectObject(sf,
                model.getInfrastructure(), p);

        try {
            if (clicked == alphaNode) {
                return true; // click the alpha node again does not end the action
            }

            if (clicked == null) {
                // Add a turn
                Point prevp = alphaPoint;

                if (!turnPoints.isEmpty()) {
                    prevp = (Point) turnPoints.lastElement();
                }

                if (prevp.distance(p) < MIN_TURN_DISTANCE) {
                    return true; // new turn to close to previous
                }

                if (alphaConPos == -1) {
                    try {
                        alphaConPos = getBestConPosition(alphaNode, alphaPoint,
                                p);
                    } catch (CannotConnectException e) {
                        reset();

                        return false;
                    }
                }

                turnPoints.add(p);
            } else {
                // Connect road and end action
                Node betaNode = clicked;
                Point betaPoint = betaNode.getCoord();

                try {
                    if (alphaConPos == -1) {
                        alphaConPos = getBestConPosition(alphaNode, alphaPoint,
                                betaPoint);
                    }

                    Point cp = alphaPoint;

                    if (!turnPoints.isEmpty()) {
                        cp = (Point) turnPoints.lastElement();
                    }

                    betaConPos = getBestConPosition(betaNode, betaPoint, cp);
                } catch (CannotConnectException e) {
                    reset();

                    return false;
                }

                model.addRoad(createRoad(alphaNode, betaNode), alphaNode,
                    alphaConPos, betaNode, betaConPos, turnPoints);

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

        Point p1 = alphaPoint;
        Point p2 = mousePoint;

        for (int i = 0; i < turnPoints.size(); i++) {
            p2 = (Point) turnPoints.get(i);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
            p1 = p2;
        }

        g.drawLine(p1.x, p1.y, mousePoint.x, mousePoint.y);
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param pa DOCUMENT ME!
     * @param pb DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CannotConnectException DOCUMENT ME!
     * @throws InfraException DOCUMENT ME!
     */
    protected int getBestConPosition(Node node, Point pa, Point pb)
        throws CannotConnectException, InfraException {
        // TODO: add old RoadTool code to allow more flexible road connecting
        int dx = pb.x - pa.x;
        int dy = pb.y - pa.y;
        int adx = Math.abs(dx);
        int ady = Math.abs(dy);
        int pos = -1;

        if ((dx < 0) && (ady <= adx)) {
            pos = 3;
        }

        if ((dx >= 0) && (ady <= adx)) {
            pos = 1;
        }

        if ((dy < 0) && (adx < ady)) {
            pos = 0;
        }

        if ((dy >= 0) && (adx < ady)) {
            pos = 2;
        }

        if (node.isConnectionPosFree(pos)) {
            return pos;
        }

        throw new CannotConnectException("Cannot connect to: " + pos);
    }

    /**
     * DOCUMENT ME!
     *
     * @param alpha DOCUMENT ME!
     * @param beta DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    protected Road createRoad(Node alpha, Node beta) throws InfraException {
        int dx = alpha.getCoord().x - beta.getCoord().x;
        int dy = alpha.getCoord().y - beta.getCoord().y;

        Road road = new Road(alpha, beta,
                (int) (Math.sqrt((dx * dx) + (dy * dy)) / Infrastructure.blockLength));
        int ruType = RoaduserFactory.getTypeByDesc("Automobiles");

        Drivelane lane0 = new Drivelane(road);
        lane0.setType(ruType);
        lane0.setTarget(0, true);
        lane0.setTarget(1, false);
        lane0.setTarget(2, false);
        road.addLane(lane0, alpha);

        Drivelane lane1 = new Drivelane(road);
        lane1.setType(ruType);
        lane1.setTarget(0, false);
        lane1.setTarget(1, true);
        lane1.setTarget(2, true);
        road.addLane(lane1, alpha);

        Drivelane lane2 = new Drivelane(road);
        lane2.setType(ruType);
        lane2.setTarget(0, true);
        lane2.setTarget(1, false);
        lane2.setTarget(2, false);
        road.addLane(lane2, beta);

        Drivelane lane3 = new Drivelane(road);
        lane3.setType(ruType);
        lane3.setTarget(0, false);
        lane3.setTarget(1, true);
        lane3.setTarget(2, true);
        road.addLane(lane3, beta);

        return road;
    }
}
