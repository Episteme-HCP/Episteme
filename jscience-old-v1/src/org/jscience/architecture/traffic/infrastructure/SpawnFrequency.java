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


/**
 * Containts a spawning frequency for a certain roaduser type.
 *
 * @author Group Datastructures
 * @version 1.0
 */
public class SpawnFrequency implements XMLSerializable {
    /** DOCUMENT ME! */
    public int ruType;

    /** DOCUMENT ME! */
    public float freq;

    /** DOCUMENT ME! */
    protected String parentName = "model.infrastructure.node";

/**
     * Creates an empty instance.
     */
    public SpawnFrequency() { // For loading
    }

/**
     * Creates an instance initiated with given parameters.
     *
     * @param _ruType Roaduser type.
     * @param _freq   Initial frequency.
     */
    public SpawnFrequency(int _ruType, float _freq) {
        ruType = _ruType;
        freq = _freq;
    }

    // XML Serializable implementation
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
        ruType = myElement.getAttribute("ru-type").getIntValue();
        freq = myElement.getAttribute("freq").getFloatValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = new XMLElement("spawnfreq");
        result.addAttribute(new XMLAttribute("ru-type", ruType));
        result.addAttribute(new XMLAttribute("freq", freq));

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
        throws XMLTreeException, IOException, XMLCannotSaveException { // A spawnfrequency has no child objects
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".spawnfreq";
    }

    /**
     * DOCUMENT ME!
     *
     * @param newParentName DOCUMENT ME!
     */
    public void setParentName(String newParentName) {
        this.parentName = parentName;
    }
}
