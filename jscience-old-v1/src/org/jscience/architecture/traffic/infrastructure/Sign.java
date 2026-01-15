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

import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.Dictionary;


/**
 * Sign class.
 *
 * @author Group Datastructures
 * @version 1.0
 */
public abstract class Sign implements XMLSerializable, TwoStageLoader {
    /** DOCUMENT ME! */
    public static final int SIGN = 0;

    /** DOCUMENT ME! */
    public static final int TRAFFICLIGHT = 1;

    /** DOCUMENT ME! */
    public static final int NO_SIGN = 2;

    /** DOCUMENT ME! */
    protected boolean state = false;

    /** DOCUMENT ME! */
    protected Node node;

    /** DOCUMENT ME! */
    protected Drivelane lane;

    /** Data for loading the second stage */
    protected TwoStageLoaderData loadData = new TwoStageLoaderData();

    /** DOCUMENT ME! */
    protected String parentName = "model.infrastructure.lane";

/**
     * Creates a new Sign object.
     *
     * @param _node DOCUMENT ME!
     * @param _lane DOCUMENT ME!
     */
    public Sign(Node _node, Drivelane _lane) {
        node = _node;
        lane = _lane;
        state = false;
    }

/**
     * Creates a new Sign object.
     */
    public Sign() {
    }

    /**
     * Smallest factory ever
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public static Sign getInstance(int type) throws InfraException {
        if (type == TRAFFICLIGHT) {
            return new TrafficLight();
        }

        if (type == NO_SIGN) {
            return new NoSign();
        }

        throw new InfraException("Unknown sign type: " + type);
    }

    /**
     * This will reset the sign to its default state (false)
     */
    public void reset() {
        state = false;
    }

    /**
     * Returns true if this Sign should be handled by an external
     * algorithm (TC-3 for example)
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean needsExternalAlgorithm();

    /**
     * Returns true if the Roaduser at the start of the Drivelane may
     * cross the Node. Default behavior is implemented to return state. Any
     * Sign using an external algorithm does not need to override this method.
     *
     * @return DOCUMENT ME!
     */
    public boolean mayDrive() {
        return state;
    }

    /**
     * Returns the Id of this sign. It is the same as the Id of the
     * drivelane this sign is on.
     *
     * @return DOCUMENT ME!
     */
    public int getId() {
        return lane.getId();
    }

    /**
     * Returns the current state
     *
     * @return DOCUMENT ME!
     */
    public boolean getState() {
        return state;
    }

    /**
     * Sets the current state
     *
     * @param b DOCUMENT ME!
     */
    public void setState(boolean b) {
        state = b;
    }

    /**
     * Returns the Drivelane this Sign is on
     *
     * @return DOCUMENT ME!
     */
    public Drivelane getLane() {
        return lane;
    }

    /**
     * Sets the Drivelane this Sign is on
     *
     * @param l DOCUMENT ME!
     */
    public void setLane(Drivelane l) {
        lane = l;
    }

    /**
     * Returns the Node this Sign is on
     *
     * @return DOCUMENT ME!
     */
    public Node getNode() {
        return node;
    }

    /**
     * Sets the Node this Sign is on
     *
     * @param n DOCUMENT ME!
     */
    public void setNode(Node n) {
        node = n;
    }

    /**
     * Returns the type of this Sign
     *
     * @return DOCUMENT ME!
     */
    public abstract int getType();

    // Common XMLSerializable implementation for subclasses
    /**
     * DOCUMENT ME!
     *
     * @param myElement DOCUMENT ME!
     * @param loader DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLInvalidInputException DOCUMENT ME!
     */
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException {
        state = myElement.getAttribute("state").getBoolValue();
        loadData.nodeId = myElement.getAttribute("node-id").getIntValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = new XMLElement("sign");
        result.addAttribute(new XMLAttribute("type", getType()));
        result.addAttribute(new XMLAttribute("state", state));
        result.addAttribute(new XMLAttribute("node-id", node.getId()));

        // Lane id doesn't have to be saved, because it is set by the parent
        // lane on loading
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
        node = (Node) (((Dictionary) (dictionaries.get("node"))).get(new Integer(
                    loadData.nodeId)));

        //System.out.println("Node gotten:"+node);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class TwoStageLoaderData {
        /** DOCUMENT ME! */
        int nodeId;
    }
}
