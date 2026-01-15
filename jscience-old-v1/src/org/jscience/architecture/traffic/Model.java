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
package org.jscience.architecture.traffic;

import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.Observable;


/**
 * The heart of the simulation.
 *
 * @author Group Model
 * @version 1.0
 */
public class Model extends Observable implements XMLSerializable {
    /** Determines whether statistics are saved and loaded or not. */
    public static boolean SAVE_STATS = false;

    /** The infrastructure used by the model */
    protected Infrastructure infra;

    /** DOCUMENT ME! */
    protected String parentName = "";

/**
     * Creates a model with an empty infrastructure
     */
    public Model() {
        infra = new Infrastructure();
    }

/**
     * Creates a model with the given infrastructure
     *
     * @param i DOCUMENT ME!
     */
    public Model(Infrastructure i) {
        infra = i;
    }

    /**
     * Returns the current infrastructure
     *
     * @return DOCUMENT ME!
     */
    public Infrastructure getInfrastructure() {
        return infra;
    }

    /**
     * Sets the current infrastructure
     *
     * @param i DOCUMENT ME!
     */
    public void setInfrastructure(Infrastructure i) {
        infra = i;
    }

    // XMLSerializable implementation
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
        infra = new Infrastructure();
        SAVE_STATS = myElement.getAttribute("save-stats").getBoolValue();
        loader.load(this, infra);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XMLElement saveSelf() {
        XMLElement result = new XMLElement("model");
        result.addAttribute(new XMLAttribute("save-stats", SAVE_STATS));

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
        saver.saveObject(infra);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return "model";
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentName DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void setParentName(String parentName) throws XMLTreeException {
        throw new XMLTreeException(
            "XML root class Model does not support different parent names");
    }
}
