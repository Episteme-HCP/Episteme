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
