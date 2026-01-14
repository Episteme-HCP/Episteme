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
