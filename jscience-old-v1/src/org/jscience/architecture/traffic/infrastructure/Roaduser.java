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
package org.jscience.architecture.traffic.infrastructure;

import org.jscience.architecture.traffic.Selectable;
import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.xml.*;

import java.awt.*;

import java.io.IOException;

import java.util.Dictionary;
import java.util.Enumeration;


/**
 * Basic Roaduser
 *
 * @author Group Datastructures
 * @version 1.0 Todo: Fix Save/Load
 */
abstract public class Roaduser implements Selectable, XMLSerializable,
    TwoStageLoader, Cloneable {
    /** The node this Roaduser spawned at */
    protected Node startNode;

    /** The node that is the destination of this Roaduser */
    protected Node destNode;

    /** The last cycle this Roaduser moved */
    protected int cycleMoved;

    /** The last cycle this Roaduser was asked when it had last moved */
    protected int cycleAsked;

    /** The position of this Roaduser on the drivelane. Zero based. */
    protected int position;

    /** The SignID of the lane this Roaduser came from */
    protected int prevSign = -1;

    /** The starttime on this lane */
    protected int drivelaneStartTime;

    /** The delay experienced so far */
    protected int delay;

    /** Stuff to transfer between the first and second stage loader */
    protected TwoStageLoaderData loadData = new TwoStageLoaderData();

    /** The color of this Roaduser */
    protected Color color = new Color(0, 0, 0);

    /** The name of the parent of this Roaduser */
    protected String parentName = "model.infrastructure.lane";

    /** The id of the sign this roaduser last passed */
    protected int waitTl = -1;

    /** The id of the sign this roaduser last passed */
    protected int waitPos = -1;

    /** The id of the sign this roaduser last passed */
    protected boolean waitTlColor = false;

    /** The id of the sign this roaduser last passed */
    protected int prevWaitTl = -1;

    /** The id of the sign this roaduser last passed */
    protected int prevWaitPos = -1;

    /** The id of the sign this roaduser last passed */
    protected boolean prevWaitTlColor = false;

    /** DOCUMENT ME! */
    protected boolean voted = false;

    /** DOCUMENT ME! */
    protected boolean inQueueForSign = false;

/**
     * Creates a new Roaduser object.
     *
     * @param _startNode DOCUMENT ME!
     * @param _destNode  DOCUMENT ME!
     * @param pos        DOCUMENT ME!
     */
    public Roaduser(Node _startNode, Node _destNode, int pos) {
        this();
        startNode = _startNode;
        destNode = _destNode;
        position = pos;
    }

/**
     * Creates a new Roaduser object.
     */
    public Roaduser() {
        resetStats();
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setInQueueForSign(boolean b) {
        inQueueForSign = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getInQueueForSign() {
        return inQueueForSign;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetStats() {
        cycleMoved = -1;
        cycleAsked = -1;
        drivelaneStartTime = -1;
        delay = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    /*============================================*/
    /* Basic GET and SET methods                  */
    /*============================================*/
    /**
     * Returns the start Node of this Roaduser
     *
     * @return DOCUMENT ME!
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * Sets the start Node of this Roaduser
     *
     * @param n DOCUMENT ME!
     */
    public void setStartNode(Node n) {
        startNode = n;
    }

    /**
     * Returns the destination Node of this Roaduser
     *
     * @return DOCUMENT ME!
     */
    public Node getDestNode() {
        return destNode;
    }

    /**
     * Sets the destination Node of this Roaduser
     *
     * @param n DOCUMENT ME!
     */
    public void setDestNode(Node n) {
        destNode = n;
    }

    /**
     * Returns the position of this Roaduser on the current Drivelane
     *
     * @return DOCUMENT ME!
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position of this Roaduser on the current Drivelane
     *
     * @param pos DOCUMENT ME!
     */
    public void setPosition(int pos) { /*System.out.println("newPos:"+pos);*/
        position = pos;
    }

    /**
     * Returns the last cycle this Roaduser moved
     *
     * @return DOCUMENT ME!
     */
    public int getCycleMoved() {
        return cycleMoved;
    }

    /**
     * Sets the last cycle this Roaduser moved
     *
     * @param cycle DOCUMENT ME!
     */
    public void setCycleMoved(int cycle) {
        cycleMoved = cycle;
    }

    /**
     * Returns the last cycle this Roaduser was asked about its
     * movements
     *
     * @return DOCUMENT ME!
     */
    public int getCycleAsked() {
        return cycleAsked;
    }

    /**
     * Sets the last cycle this Roaduser was asked its movements
     *
     * @param cycle DOCUMENT ME!
     */
    public void setCycleAsked(int cycle) {
        cycleAsked = cycle;
    }

    /**
     * Returns the start time of this Roaduser in the current drivelane
     *
     * @return DOCUMENT ME!
     */
    public int getDrivelaneStartTime() {
        return drivelaneStartTime;
    }

    /**
     * Sets the start time of this Roaduser on the current Drivelane
     *
     * @param time DOCUMENT ME!
     */
    public void setDrivelaneStartTime(int time) {
        drivelaneStartTime = time;
    }

    /**
     * Returns the distance experienced so far
     *
     * @return DOCUMENT ME!
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Add a given delay to the total delay already experienced
     *
     * @param d DOCUMENT ME!
     */
    public void addDelay(int d) {
        delay += d;
    }

    /**
     * Sets a new delay
     *
     * @param delay DOCUMENT ME!
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Returns the Id of the previous lane this Roaduser hit
     *
     * @return DOCUMENT ME!
     */
    public int getPrevSign() {
        return prevSign;
    }

    /**
     * Sets the Id of the previous lane this Roaduser hit
     *
     * @param _prevSign DOCUMENT ME!
     */
    public void setPrevSign(int _prevSign) {
        prevSign = _prevSign;
    }

    /**
     * Returns the color of this Roaduser
     *
     * @return DOCUMENT ME!
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of this Roaduser
     *
     * @param c DOCUMENT ME!
     */
    public void setColor(Color c) {
        color = c;
    }

    /**
     * Sets the last waiting point's position relative to the sign
     *
     * @param tlId DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param pos DOCUMENT ME!
     */
    public void setWaitPos(int tlId, boolean b, int pos) {
        prevWaitTl = waitTl;
        prevWaitTlColor = waitTlColor;
        prevWaitPos = waitPos;
        waitTl = tlId;
        waitTlColor = b;
        waitPos = pos;
    }

    /**
     * Gets the last waiting point's position relative to the sign
     *
     * @return DOCUMENT ME!
     */
    public int getPrevWaitPos() {
        return prevWaitPos;
    }

    /**
     * Gets the last waiting point's sign
     *
     * @return DOCUMENT ME!
     */
    public int getPrevWaitTl() {
        return prevWaitTl;
    }

    /**
     * Gets the last waiting point's sign
     *
     * @return DOCUMENT ME!
     */
    public boolean getPrevWaitTlColor() {
        return prevWaitTlColor;
    }

    /**
     * Gets the current waiting point's sign
     *
     * @return DOCUMENT ME!
     */
    public int getCurrentWaitTl() {
        return waitTl;
    }

    /**
     * Gets the current waiting point's position relative to the sign
     *
     * @return DOCUMENT ME!
     */
    public int getCurrentWaitPos() {
        return waitPos;
    }

    /**
     * Gets the current waiting point's sign
     *
     * @return DOCUMENT ME!
     */
    public boolean getCurrentWaitTlColor() {
        return waitTlColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean didVote() {
        return voted;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public void setVoted(boolean v) {
        voted = v;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cycleNow DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean didMove(int cycleNow) {
        cycleAsked = cycleNow;

        return cycleMoved == cycleNow;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String getName();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getNumPassengers();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getLength();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getSpeed();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getType();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVehicleName() {
        return "unknown";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDriverName() {
        return "unknown";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return "no description available.";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPicture() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSound() {
        return null;
    }

    /*============================================*/
    /* Selectable                                 */
    /*============================================*/
    public Rectangle getBounds() {
        return new Rectangle(0, 0, 0, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Shape getComplexBounds() {
        return getBounds();
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDistance(Point p) {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getSelectionPoint() {
        return new Point(0, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point[] getSelectionPoints() {
        return new Point[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getCenterPoint() {
        return new Point(0, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelectable() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChildren() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getChildren() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g) throws TrafficException {
        paint(g, 0, 0, 1.0f);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     * @param angle DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g, int dx, int dy, double angle)
        throws TrafficException {
        paint(g, dx, dy, 1.0f, angle);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     * @param zf DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public abstract void paint(Graphics g, int dx, int dy, float zf)
        throws TrafficException;

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     * @param zf DOCUMENT ME!
     * @param angle DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public abstract void paint(Graphics g, int dx, int dy, float zf,
        double angle) throws TrafficException;

    /*============================================*/
    /* Load/save                                  */
    /*============================================*/
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException {
        loadData.startNodeId = myElement.getAttribute("start-node-id")
                                        .getIntValue();
        loadData.destNodeId = myElement.getAttribute("dest-node-id")
                                       .getIntValue();
        cycleMoved = myElement.getAttribute("cycle-moved").getIntValue();
        cycleAsked = myElement.getAttribute("cycle-asked").getIntValue();
        position = myElement.getAttribute("position").getIntValue();
        drivelaneStartTime = myElement.getAttribute("lane-start-time")
                                      .getIntValue();
        delay = myElement.getAttribute("delay").getIntValue();
        prevSign = myElement.getAttribute("prevSign").getIntValue();
        color = new Color(myElement.getAttribute("color-red").getIntValue(),
                myElement.getAttribute("color-green").getIntValue(),
                myElement.getAttribute("color-blue").getIntValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = new XMLElement(XMLUtils.getLastName(getXMLName()));
        result.addAttribute(new XMLAttribute("start-node-id", startNode.getId()));
        result.addAttribute(new XMLAttribute("dest-node-id", destNode.getId()));
        result.addAttribute(new XMLAttribute("cycle-moved", cycleMoved));
        result.addAttribute(new XMLAttribute("cycle-asked", cycleAsked));
        result.addAttribute(new XMLAttribute("position", position));
        result.addAttribute(new XMLAttribute("type", getType())); // For tunnels
        result.addAttribute(new XMLAttribute("lane-start-time",
                drivelaneStartTime));
        result.addAttribute(new XMLAttribute("delay", delay));
        result.addAttribute(new XMLAttribute("prevSign", prevSign));
        result.addAttribute(new XMLAttribute("color-green", color.getGreen()));
        result.addAttribute(new XMLAttribute("color-blue", color.getBlue()));
        result.addAttribute(new XMLAttribute("color-red", color.getRed()));

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param saver DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void saveChilds(XMLSaver saver)
        throws XMLTreeException, IOException, XMLCannotSaveException { // Roadusers don't have child objects
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentName DOCUMENT ME!
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dictionaries DOCUMENT ME!
     *
     * @throws XMLInvalidInputException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void loadSecondStage(Dictionary dictionaries)
        throws XMLInvalidInputException, XMLTreeException {
        Dictionary nodeDictionary = (Dictionary) (dictionaries.get("node"));
        startNode = (Node) (nodeDictionary.get(new Integer(loadData.startNodeId)));
        destNode = (Node) (nodeDictionary.get(new Integer(loadData.destNodeId)));
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class TwoStageLoaderData {
        /** DOCUMENT ME! */
        int startNodeId;

        /** DOCUMENT ME! */
        int destNodeId;
    }
}
