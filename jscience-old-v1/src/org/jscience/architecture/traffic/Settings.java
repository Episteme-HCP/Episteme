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

import org.jscience.architecture.traffic.xml.XMLCannotSaveException;
import org.jscience.architecture.traffic.xml.XMLInvalidInputException;
import org.jscience.architecture.traffic.xml.XMLSerializable;
import org.jscience.architecture.traffic.xml.XMLTreeException;

import java.io.IOException;

import java.util.NoSuchElementException;


/**
 * General interface for classes which hold settings
 */
public interface Settings extends XMLSerializable {
    /**
     * Load settings from settings file
     *
     * @throws IOException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     * @throws XMLInvalidInputException DOCUMENT ME!
     */
    public void doLoad()
        throws IOException, XMLTreeException, XMLInvalidInputException;

    /**
     * Save settings to settings file
     *
     * @throws IOException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void doSave()
        throws IOException, XMLTreeException, XMLCannotSaveException;

    /**
     * Indicates if this Settings are predefined. If so, then the user
     * cannot add new properties to this object. So, in that case, it is not
     * possible to do a setProperty with a name that does not exist yet. So it
     * is OK to change existing properties of a predefined settings object,
     * but you cannot add new properties.
     *
     * @return DOCUMENT ME!
     */
    public boolean isPredefined();

    /**
     * Set a string property to a certain value
     *
     * @param name The name of the property
     * @param value The new value of the property
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public void setProperty(String name, String value)
        throws NoSuchElementException;

    /**
     * Set an int property to a certain value
     *
     * @param name The name of the property
     * @param value The new value of the property
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public void setProperty(String name, int value)
        throws NoSuchElementException;

    /**
     * Set a boolean property to a certain value
     *
     * @param name The name of the property
     * @param value The new value of the property
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public void setProperty(String name, boolean value)
        throws NoSuchElementException;

    /**
     * Gets the String value of a certain property
     *
     * @param name The name of the property
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if there is no property with that name
     */
    public String getPropertyStringValue(String name)
        throws NoSuchElementException;

    /**
     * Gets the value of an int property
     *
     * @param name The name of the property
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if there is no property with that name
     */
    public int getPropertyIntValue(String name) throws NoSuchElementException;

    /**
     * Gets the value of a boolean property
     *
     * @param name The name of the property
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if there is no property with that name
     */
    public boolean getPropertyBooleanValue(String name)
        throws NoSuchElementException;

    /**
     * Gets the value of a float property
     *
     * @param name The name of the property
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if there is no property with that name
     */
    public float getPropertyFloatValue(String name)
        throws NoSuchElementException;
}
