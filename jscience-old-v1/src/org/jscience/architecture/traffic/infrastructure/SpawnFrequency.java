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
