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

import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.util.ArrayUtils;
import org.jscience.architecture.traffic.util.ListEnumeration;
import org.jscience.architecture.traffic.xml.*;

import java.awt.*;

import java.io.IOException;

import java.util.Dictionary;
import java.util.LinkedList;


/**
 * Class with common code for NetTunnels and EdgeNodes
 */
public abstract class SpecialNode extends Node implements XMLSerializable,
    TwoStageLoader {
    /** The road this SpecialNode is connected to */
    protected Road road;

    /** True if the connected road is an alpha road */
    protected boolean isAlpha;

    /** The connection-position of the connected road */
    protected int roadPos;

    /**
     * The queue with all road users which have not entered the road.
     * For example because it's already full
     */
    protected LinkedList waitingQueue = new LinkedList();

    /**
     * Temporary data structure to tranfer info from the first stage
     * loader to the second stage loader
     */
    protected TwoStageLoaderData loadData = new TwoStageLoaderData();

/**
     * Creates a new SpecialNode object.
     */
    public SpecialNode() {
    }

/**
     * Creates a new SpecialNode object.
     *
     * @param _coord DOCUMENT ME!
     */
    public SpecialNode(Point _coord) {
        super(_coord);
    }

    /*============================================*/
    /* LOAD and SAVE                              */
    /*============================================*/
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException {
        super.load(myElement, loader);
        isAlpha = myElement.getAttribute("road-is-alpha").getBoolValue();
        roadPos = myElement.getAttribute("position").getIntValue();

        if (isAlpha) {
            road = new Road();
            loader.load(this, road);
        }

        loadData.roadId = myElement.getAttribute("road-id").getIntValue();
        waitingQueue = (LinkedList) (XMLArray.loadArray(this, loader));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = super.saveSelf();
        result.setName("node-special");
        result.addAttribute(new XMLAttribute("road-is-alpha", isAlpha));
        result.addAttribute(new XMLAttribute("road-id", road.getId()));
        result.addAttribute(new XMLAttribute("position", roadPos));

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
        throws XMLTreeException, IOException, XMLCannotSaveException {
        super.saveChilds(saver);

        if (isAlpha) {
            saver.saveObject(road);
        }

        XMLUtils.setParentName(new ListEnumeration(waitingQueue), getXMLName());
        XMLArray.saveArray(waitingQueue, this, saver, "queue");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".node-special";
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
        super.loadSecondStage(dictionaries);

        if (!isAlpha) {
            Dictionary roadDictionary = (Dictionary) (dictionaries.get("road"));
            road = (Road) (roadDictionary.get(new Integer(loadData.roadId)));
        }

        road.loadSecondStage(dictionaries);

        try {
            updateLanes();
        } catch (InfraException e) {
            throw new XMLInvalidInputException(
                "Cannot initialize lanes of node " + nodeId);
        }

        XMLUtils.loadSecondStage(new ListEnumeration(waitingQueue), dictionaries);
    }

    /*============================================*/
    /* Basic GET and SET methods                  */
    /*============================================*/
    /**
     * Returns the road this SpecialNode is connected to
     *
     * @return DOCUMENT ME!
     */
    public Road getRoad() {
        return road;
    }

    /**
     * Sets the road this SpecialNode is connected to
     *
     * @param r DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void setRoad(Road r) throws InfraException {
        road = r;
        updateLanes();
    }

    /**
     * Returns the position of the road
     *
     * @return DOCUMENT ME!
     */
    public int getRoadPos() {
        return roadPos;
    }

    /**
     * Sets the position of the road
     *
     * @param pos DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void setRoadPos(int pos) throws InfraException {
        roadPos = pos;
        updateLanes();
    }

    /**
     * Returns true if the road is an alpha road
     *
     * @return DOCUMENT ME!
     */
    public boolean getAlpha() {
        return isAlpha;
    }

    /**
     * Sets the isAlpha flag
     *
     * @param f DOCUMENT ME!
     */
    public void setAlpha(boolean f) {
        isAlpha = f;
    }

    /**
     * Returns all roads connected to this node
     *
     * @return DOCUMENT ME!
     */
    public Road[] getAllRoads() {
        Road[] r = { road };

        return r;
    }

    /**
     * Returns the alpha roads connected to this node
     *
     * @return DOCUMENT ME!
     */
    public Road[] getAlphaRoads() {
        if (isAlpha) {
            return getAllRoads();
        }

        return new Road[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        if (road != null) {
            int w = road.getWidth();

            if (w < 4) {
                return 4;
            }

            return w;
        }

        return 4;
    }

    /*============================================*/
    /* RU over node methods                              */
    /*============================================*/
    /**
     * Place a roaduser in one of the outbound queues
     *
     * @param ru DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void placeRoaduser(Roaduser ru) throws InfraException {
        Drivelane[] lanes = (Drivelane[]) getShortestPaths(ru.getDestNode()
                                                             .getId(),
                ru.getType()).clone();
        ArrayUtils.randomizeArray(lanes);

        // The next person who outcomments this code will
        // be chopped into little pieces, burned, hanged, chainsawed, 
        // shredded, killed, /toaded and then ported to Microsoft Visual Lisp.
        // You were warned.
        //							Was signed,
        //												Siets El Snel
        if (lanes.length == 0) {
            throw new InfraException(
                "Cannot find shortest path for new Roaduser in EdgeNode");
        }

        lanes[0].addRoaduserAtEnd(ru);
    }

    /**
     * Returns the queue with waiting road users for this node
     *
     * @return DOCUMENT ME!
     */
    public LinkedList getWaitingQueue() {
        return waitingQueue;
    }

    /**
     * Sets a new queue with waiting road users
     *
     * @param l DOCUMENT ME!
     */
    public void setWaitingQueue(LinkedList l) {
        waitingQueue = l;
    }

    /**
     * Get the number of waiting road users, i.e. the length of the
     * waitingQueue
     *
     * @return DOCUMENT ME!
     */
    public int getWaitingQueueLength() {
        return waitingQueue.size();
    }

    /**
     * Place a roaduser in the waitingQueue
     *
     * @param ru DOCUMENT ME!
     */
    public void enqueueRoaduser(Roaduser ru) {
        waitingQueue.addLast(ru);
    }

    /**
     * Remove a roaduser from the waitingQueue
     *
     * @return DOCUMENT ME!
     */
    public Roaduser dequeueRoaduser() {
        return (Roaduser) waitingQueue.removeFirst();
    }

    /*============================================*/
    /* STATISTICS                                 */
    /*============================================*/
    protected int calcDelay(Roaduser ru, int stop, int distance) {
        // first, add the delay for the drivelane leading to this EdgeNode
        int start = ru.getDrivelaneStartTime();
        int speed = ru.getSpeed();
        ru.addDelay((stop - start) - (distance / speed));

        // then, return the total delay of the full trip	
        return ru.getDelay();
    }

    /*============================================*/
    /* MODIFYING DATA                             */
    /*============================================*/
    public void reset() {
        super.reset();

        if (isAlpha) {
            road.reset();
        }

        waitingQueue = new LinkedList();
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void addRoad(Road r, int pos) throws InfraException {
        if (r == null) {
            throw new InfraException("Parameter r is null");
        }

        if ((pos > 3) || (pos < 0)) {
            throw new InfraException("Position out of range: " + pos);
        }

        if (road != null) {
            throw new InfraException("Road already exists");
        }

        Node other = r.getOtherNode(this);

        if ((other == null) || !other.isAlphaRoad(r)) {
            isAlpha = true;
        } else {
            isAlpha = false;
        }

        roadPos = pos;
        road = r;
        updateLanes();
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void setAlphaRoad(int pos) throws InfraException {
        if ((pos > 3) || (pos < 0)) {
            throw new InfraException("Position out of range");
        }

        if ((road == null) || (pos != roadPos)) {
            throw new InfraException("Road at position " + pos +
                " does not exist");
        }

        isAlpha = true;
        updateLanes();
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void remRoad(int pos) throws InfraException {
        if ((pos > 3) || (pos < 0)) {
            throw new InfraException("Position out of range");
        }

        if ((road == null) || (pos != roadPos)) {
            throw new InfraException("Road at position " + pos +
                " does not exist");
        }

        road = null;
        isAlpha = false;
        updateLanes();
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void remRoad(Road r) throws InfraException {
        if (r == null) {
            throw new InfraException("Parameter r is null");
        }

        if (road == null) {
            throw new InfraException("No road is connected to this node");
        }

        if (r != road) {
            throw new InfraException("Road not found on this node");
        }

        road = null;
        isAlpha = false;
        updateLanes();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void remAllRoads() throws InfraException {
        road = null;
        isAlpha = false;
        updateLanes();
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void setSigns(Sign[] s) throws InfraException {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public int getDesiredSignType() throws InfraException {
        return Sign.NO_SIGN;
    }

    /*============================================*/
    /* COMPLEX GET                                */
    /*============================================*/
    public boolean isAlphaRoad(Road r) throws InfraException {
        if (r == null) {
            throw new InfraException("Parameter r is null");
        }

        return (r == road) && isAlpha;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public boolean isConnected(Road r) throws InfraException {
        if (r == null) {
            throw new InfraException("Parameter r is null");
        }

        return r == road;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public int isConnectedAt(Road r) throws InfraException {
        if (r == null) {
            throw new InfraException("Parameter r is null");
        }

        if (r != road) {
            throw new InfraException("Road is not connected to this node");
        }

        return roadPos;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public boolean isConnectionPosFree(int pos) throws InfraException {
        if ((pos > 3) || (pos < 0)) {
            throw new InfraException("Position out of range");
        }

        return (road == null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumRoads() {
        return (road != null) ? 1 : 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumAlphaRoads() {
        return ((road != null) && isAlpha) ? 1 : 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public int getNumInboundLanes() throws InfraException {
        return road.getNumInboundLanes(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public int getNumOutboundLanes() throws InfraException {
        return road.getNumOutboundLanes(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumAllLanes() {
        return road.getNumAllLanes();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumSigns() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumRealSigns() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lane DOCUMENT ME!
     * @param ruType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public Drivelane[] getLanesLeadingTo(Drivelane lane, int ruType)
        throws InfraException {
        return new Drivelane[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param lane DOCUMENT ME!
     * @param ruType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public Drivelane[] getLanesLeadingFrom(Drivelane lane, int ruType)
        throws InfraException {
        return new Drivelane[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public Drivelane[] getOutboundLanes() throws InfraException {
        return (road != null) ? road.getOutboundLanes(this) : new Drivelane[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public Drivelane[] getInboundLanes() throws InfraException {
        return (road != null) ? road.getInboundLanes(this) : new Drivelane[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public Drivelane[] getAllLanes() throws InfraException {
        return (Drivelane[]) ArrayUtils.addArray(getInboundLanes(),
            getOutboundLanes());
    }

    /*============================================*/
    /* Hook methods                               */
    /*============================================*/
    /* Hook method for stuff that has to be done every step in the simulation */
    public void doStep(SimModel model) {
    }

    /**
     * Hook method for stuff that has to be done when the simulation is
     * started
     */
    public void start() {
    }

    /**
     * Hook method for stuff that has to be done when the simulation is
     * stopped
     */
    public void stop() {
    }

    /**
     * Hook method that is called by the infra when a roaduser reaches
     * this node
     *
     * @param ru DOCUMENT ME!
     */
    public void enter(Roaduser ru) {
        if (ru instanceof CustomRoaduser) {
            CustomFactory.removeCustom((CustomRoaduser) ru);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class TwoStageLoaderData {
        /** DOCUMENT ME! */
        int roadId;
    }
}
