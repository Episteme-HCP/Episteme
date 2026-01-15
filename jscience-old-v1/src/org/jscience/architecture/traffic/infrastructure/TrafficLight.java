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

import org.jscience.architecture.traffic.xml.XMLCannotSaveException;
import org.jscience.architecture.traffic.xml.XMLElement;


/**
 * DOCUMENT ME!
 *
 * @author Group Datastructures
 * @version 1.0 The Red Doom of all Speeding. If the state is false, the colour of the sign is red.
 */
public class TrafficLight extends Sign {
    /** DOCUMENT ME! */
    protected final static int type = Sign.TRAFFICLIGHT;

/**
     * Creates a new TrafficLight object.
     *
     * @param _node DOCUMENT ME!
     * @param _lane DOCUMENT ME!
     */
    public TrafficLight(Node _node, Drivelane _lane) {
        super(_node, _lane);
    }

/**
     * Creates a new TrafficLight object.
     */
    public TrafficLight() {
    }

    /**
     * Returns the type of this Sign
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean needsExternalAlgorithm() {
        return true;
    }

    // Specific XMLSerializable implementation 
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".sign-tl";
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
        result.setName("sign-tl");

        return result;
    }
}
