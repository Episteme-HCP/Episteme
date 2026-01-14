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

import java.io.File;
import java.io.IOException;

import java.util.Enumeration;


// TODO : - write Junit test
/**
 * This class takes care of saving the state of the program to a XML file.
 * It also provides methods which XMLSerializable objects can use to save
 * their child objects.
 */
public class XMLSaver {
    /** DOCUMENT ME! */
    protected XMLWriter writer;

    /** DOCUMENT ME! */
    protected XMLStack stack;

/**
     * Make a new XMLSaver
     */
    protected XMLSaver() {
        stack = new XMLStack();
    }

/**
     * Makes a new XMLSaver which saves to a file
     *
     * @param file The file to save to
     * @throws IOException If the XMLSaver cannot open the XML file because of
     *                     an IO error.
     */
    public XMLSaver(File file) throws IOException {
        this();
        writer = new XMLFileWriter(file);
    }

/**
     * Makes a new XMLSaver which saves to a socket
     *
     * @param hostname The hostname to connect to
     * @param port     The port to connect to
     * @throws IOException If the XMLSaver cannot open the socket connection
     *                     because of an IO error.
     */
    public XMLSaver(String hostname, int port) throws IOException {
        this();
        writer = new XMLNetWriter(hostname, port);
    }

    /**
     * A crude indication if we can write to this XMLSaver
     *
     * @return DOCUMENT ME!
     */
    public boolean hasStream() {
        return writer != null;
    }

    /**
     * Close this XMLSaver
     */
    public void close() {
        writer.flush();
        writer.close();
        writer = null;
    }

    /**
     * Save a XMLSerializable object to the XML file
     *
     * @param object The object to save
     *
     * @throws IOException If the XMLSaver cannot write to the XML file because
     *         of an IO error.
     * @throws XMLTreeException If something goes wrong while the parser is
     *         building the XML tree structure for this object
     * @throws XMLCannotSaveException If this object cannot save itself
     */
    public void saveObject(XMLSerializable object)
        throws IOException, XMLTreeException, XMLCannotSaveException { //Initialize various names of the object

        String fullName = object.getXMLName();
        String parentName = XMLUtils.getParentName(object);
        String firstName = XMLUtils.getLastName(object);

        // Check if the object can be saved.
        stack.assertIsCurrentParent(parentName);

        // Save element of object
        stack.push(firstName);

        XMLElement element = object.saveSelf();
        writer.writeOpenTag(element, indent());

        // Let object save its child objects
        object.saveChilds(this);

        // Finish element
        writer.writeCloseTag(element, indent());
        stack.pop();

        // Check if the stack is OK
        if ((!stack.getBranchName().equals(parentName)) &&
                (!XMLUtils.getGenericName(stack.getBranchName())
                              .equals(XMLUtils.getGenericName(parentName)))) {
            throw new XMLTreeException("The XMLSerializable " + fullName +
                " (or possibly one of its child objects) left the XML tree " +
                "in an invalid state. Terminating save.\n" +
                "Expected branchName :" + stack.getBranchName() + "/" +
                XMLUtils.getGenericName(stack.getBranchName()) + "\n" +
                "but branchname was  :" + parentName + "/" +
                XMLUtils.getGenericName(parentName));
        }
    }

    /**
     * Save an enumeration of XML Serializables to file
     *
     * @param e The enumeration of XML Serializables
     *
     * @throws ClassCastException If the enumeration contains an object that is
     *         not XMLSerializable
     * @throws XMLTreeException If something goes wrong while the parser is
     *         building the XML tree structure for one of the objects.
     * @throws IOException If the XMLSaver cannot write to the XML file because
     *         of an IO error.
     * @throws XMLCannotSaveException If one of the objects cannot save itself
     *         for one reason or another.
     */
    public void saveEnumerationObjects(Enumeration e)
        throws ClassCastException, XMLTreeException, IOException,
            XMLCannotSaveException {
        while (e.hasMoreElements())
            saveObject((XMLSerializable) (e.nextElement()));
    }

    /**
     * Save an atomary XML element
     *
     * @param parent DOCUMENT ME!
     * @param el The XMLElement to save
     *
     * @throws IOException If the XMLSaver cannot write to the XML file because
     *         of an IO error.
     * @throws XMLTreeException If there is a parser problem with writing the
     *         element.
     */
    public void saveAtomaryElement(XMLSerializable parent, XMLElement el)
        throws IOException, XMLTreeException {
        stack.assertIsCurrentParent(parent);
        writer.writeAtomaryElement(el, indent());
    }

    /**
     * DOCUMENT ME!
     *
     * @return An internal indentation string
     */
    protected int indent() {
        return stack.size();
    }
}
