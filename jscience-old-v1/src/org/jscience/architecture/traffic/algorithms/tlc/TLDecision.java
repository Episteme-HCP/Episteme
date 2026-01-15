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
package org.jscience.architecture.traffic.algorithms.tlc;

import org.jscience.architecture.traffic.infrastructure.TrafficLight;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.Dictionary;


/**
 * This class holds a tuple of a TrafficLight and a float value to
 * represent the reward (Q) for the TrafficLight to be kept. Note :
 * TLControllers typically don't use the XMLSerializable interface of this
 * class, because they can load it faster themselves.
 *
 * @author Group Algorithms
 * @version 1.0
 */
public class TLDecision implements XMLSerializable, TwoStageLoader {
    /** DOCUMENT ME! */
    private TrafficLight trafficLight;

    /** DOCUMENT ME! */
    private float gain;

    /** DOCUMENT ME! */
    protected TwoStageLoaderData loadData = new TwoStageLoaderData();

    /** DOCUMENT ME! */
    protected String parentName = "model.tlc";

/**
     * Empty constructor for loading
     */
    public TLDecision() {
    }

/**
     * The constructor for TLDecision.
     *
     * @param tl The Trafficlight. The reward value (Q value).
     * @param f  DOCUMENT ME!
     */
    public TLDecision(TrafficLight tl, float f) {
        trafficLight = tl;
        gain = f;
    }

    /**
     * Returns the TrafficLight.
     *
     * @return The TrafficLight.
     *
     * @see gld.infra.TrafficLight
     */
    public TrafficLight getTL() {
        return trafficLight;
    }

    /**
     * Returns the Q value.
     *
     * @return The qValue.
     */
    public float getGain() {
        return gain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param _g DOCUMENT ME!
     */
    public void setGain(float _g) {
        gain = _g;
    }

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
        gain = myElement.getAttribute("q").getFloatValue();
        loadData.tlId = myElement.getAttribute("tl-id").getIntValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = new XMLElement("decision");
        result.addAttribute(new XMLAttribute("q", gain));
        result.addAttribute(new XMLAttribute("tl-id", trafficLight.getId()));

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
        throws XMLTreeException, IOException, XMLCannotSaveException { // A TLDecision has no child objects
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".decision";
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
        trafficLight = (TrafficLight) ((Dictionary) (dictionaries.get("sign"))).get(new Integer(
                    loadData.tlId));
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class TwoStageLoaderData {
        /** DOCUMENT ME! */
        int tlId;
    }
}
