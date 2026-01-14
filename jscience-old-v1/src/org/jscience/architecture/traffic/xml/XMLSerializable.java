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

package org.jscience.architecture.traffic.xml;

import java.io.IOException;


/**
 * This interface has to be implemented by objects that want to  be loaded and
 * saved via the parser.
 */
public interface XMLSerializable {
    /**
     * Load this XMLSerializable
     *
     * @param myself The XMLElement which represents this object in the XML
     *        tree. It can contain attributes which hold information about the
     *        object.
     * @param loader The XMLLoader which this XMLSerializable can use to load
     *        child objects.
     *
     * @throws XMLTreeException The parser can throw this exception if it is
     *         called by the XMLSerializable. The XMLSerializable should NOT
     *         throw this exception by itself and preferrably not catch it
     *         too.
     * @throws IOException Thrown in case of an read error in the XML file.
     * @throws XMLInvalidInputException The XMLSerializable can throw this
     *         exception if it cannot load itself or one of its child objects
     *         for whatever reason.
     */
    void load(XMLElement myself, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException;

    /**
     * DOCUMENT ME!
     *
     * @return The XMLElement which represents the internal datastate. This
     *         function is not meant to save child objects. That is done in
     *         void saveChilds()
     *
     * @throws XMLCannotSaveException The XMLSerializable can throw this
     *         exception if it cannot save itself or one of its child objects
     *         for whatever reason.
     */
    XMLElement saveSelf() throws XMLCannotSaveException;

    /**
     * This method gives an XMLSerializable the opportunity to save its
     * child objects.
     *
     * @param saver The XMLSaver that the XMLSerializable can use to save its
     *        child objects.
     *
     * @throws XMLTreeException The parser can throw this exception if it is
     *         called by the XMLSerializable. The XMLSerializable should NOT
     *         throw this exception by itself and preferrably also not catch
     *         it too.
     * @throws IOException Thrown in case the parser cannot write to the file
     * @throws XMLCannotSaveException The XMLSerializable can throw this
     *         exception if it cannot save itself or one of its child objects
     *         for whatever reason.
     */
    void saveChilds(XMLSaver saver)
        throws XMLTreeException, IOException, XMLCannotSaveException;

    /**
     * DOCUMENT ME!
     *
     * @return The tagname of the XML element in which information about this
     *         object is stored prepended by a dot separated list  of the
     *         tagnames of the parents of this object. (e.g.
     *         "model.infrastructure.node")
     */
    String getXMLName();

    /**
     * Sets a new parent name for this object
     *
     * @param parentName The new parent name
     *
     * @throws XMLTreeException If this object does not support setting other
     *         parent names.
     */
    void setParentName(String parentName) throws XMLTreeException;
}
