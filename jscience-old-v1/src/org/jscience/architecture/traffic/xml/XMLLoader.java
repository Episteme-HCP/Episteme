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

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.NoSuchElementException;

// TODO : - Make JUnit tests (together with XMLInputReader)

/**
 * This class can restore the state of the program from an XML file. It also
 * provides methods which XMLSerializable objects can use to retrieve
 * their child objects.
 */

public class XMLLoader {
    protected XMLReader reader;
    protected XMLElement nextElement;
    protected XMLStack stack;
    protected boolean atEOF;

    /**
     * Simple constructor
     */
    protected XMLLoader() {
        stack = new XMLStack();
        atEOF = false;
    }

    /**
     * Make a new XMLLoader which loads from a file
     *
     * @param file The file to load from
     * @throws IOException              If the XMLLoader cannot read from the XML file because
     *                                  of an IO error.
     * @throws XMLTreeException         If something goes wrong while the parser is
     *                                  initializing the XML tree structure.
     * @throws XMLInvalidInputException If the XML file contains invalid
     *                                  values.
     */
    public XMLLoader(File file) throws IOException, XMLTreeException, XMLInvalidInputException {
        this();
        reader = new XMLFileReader(file);
        lookAhead();
    }

    /**
     * Make a new XMLLoader which load from a socket
     *
     * @param file The file to load from
     * @throws IOException              If the XMLLoader cannot read from the XML file because
     *                                  of an IO error.
     * @throws XMLTreeException         If something goes wrong while the parser is
     *                                  initializing the XML tree structure.
     * @throws XMLInvalidInputException If the XML file contains invalid
     *                                  values.
     */
    public XMLLoader(Socket socket) throws IOException, XMLTreeException, XMLInvalidInputException {
        this();
        reader = new XMLNetReader(socket);
        lookAhead();
    }

    /**
     * Load an XMLSerializable from the XML file
     *
     * @param parent The parent of this XMLSerializable
     * @param object The XMLSerializable object itself
     * @throws IOException              If the XMLLoader cannot read from the XML file because
     *                                  of an IO error.
     * @throws XMLTreeException         If something goes wrong while the parser is
     *                                  building the XML tree structure.
     * @throws XMLInvalidInputException If this XMLSerializable
     *                                  encounters invalid input or cannot load itself for another
     *                                  reason.
     */
    public void load(XMLSerializable parent, XMLSerializable object) throws XMLTreeException, XMLInvalidInputException, IOException {
        object.load(getNextElement(parent,
                XMLUtils.getLastName(object)), this);
    }


    /**
     * Close this XMLLoader
     */
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Warning : could not close XMLLoader : " + e);
            // Ignore it if we don't succeed. This should not be fatal
        }
    }

    /**
     * Load an enumeration of XMLSerializables from the XML file
     *
     * @param parent The parent of the XMLSerializables
     * @param o      The enumeration of XML serializables
     * @throws IOException              If the XMLLoader cannot read from the XML file because
     *                                  of an IO error.
     * @throws XMLTreeException         If something goes wrong while the parser is
     *                                  building the XML tree structure.
     * @throws XMLInvalidInputException If one of the XMLSerializables
     *                                  encounters invalid input or cannot load itself for another
     *                                  reason.
     * @throws ClassCastException       If the enumeration contains an object that
     *                                  is not XMLSerializable
     */
    public void loadEnumeration(XMLSerializable parent, Enumeration o) throws XMLTreeException, XMLInvalidInputException, IOException, ClassCastException {
        while (o.hasMoreElements())
            load(parent, (XMLSerializable) (o.nextElement()));
    }

    /**
     * Fill the nextElement buffer if it is empty
     *
     * @throws IOException      If the XMLLoader cannot read from the XML file because
     *                          of an IO error.
     * @throws XMLTreeException If something goes wrong while the parser is
     *                          building the XML tree structure.
     */
    protected void lookAhead() throws XMLTreeException, IOException {
        String tag, ref;
        // Read an open tag , eventually preceded by closing tags
        while (nextElement == null && !atEOF) { // Read tag
            try {
                tag = reader.readNextTag();

            }
            catch (EOFException e) {
                if (!stack.isEmpty()) {
                    throw new XMLTreeException
                            ("EOF at non-terminated tree. File is probably truncated.");
                } else {
                    return; // End of tree. We don't have to look ahead.
                }
            }
            // Process Closing tags
            if (tag.startsWith("</")) {
                ref = (String) (stack.pop());
                tag = tag.substring(2, tag.length() - 1);
                if (!tag.equals(ref)) {
                    throw new XMLTreeException
                            ("Unbalanced tags while loading XML file :" + ref + "/" + tag);
                }
            }
            // And begin tag
            else {
                nextElement = XMLElement.parse(tag);
            }
        }
    }

    /**
     * Get the next element from the file
     *
     * @param parent The XMLSerializable which is asking for the next element
     * @return The next XMLElement
     * @throws IOException      If the XMLLoader cannot read from the XML file because
     *                          of an IO error.
     * @throws XMLTreeException If something goes wrong while the parser is
     *                          building the XML tree structure or if the next element has
     *                          another parent.
     */
    public XMLElement getNextElement(XMLSerializable parent) throws XMLTreeException, IOException {
        stack.assertIsCurrentParent(parent);
        if (nextElement == null) {
            throw new XMLTreeException
                    ("Can't find more XMLElements inside " + parent.getXMLName());
        }
        return pullNextElement();
    }

    /**
     * Checks if an XMLSerializable can get another element from the file
     *
     * @param parent The XMLSerializable that wants to request the next element
     * @return A boolean value which indicates if another element can be fetched.
     */
    public boolean hasNextElement(XMLSerializable parent) {
        return stack.isCurrentParent(parent) && nextElement != null;
    }

    /**
     * Get the next element from the file if it has a specific name
     *
     * @param parent The XMLSerializable which is asking for the next element
     * @param name   The name which the next element has to have
     * @return The next XMLElement
     * @throws IOException      If the XMLLoader cannot read from the XML file because
     *                          of an IO error.
     * @throws XMLTreeException If something goes wrong while the parser is
     *                          building the XML tree structure or if the next element in the
     *                          file has another name/parent.
     */
    public XMLElement getNextElement(XMLSerializable parent, String name) throws XMLTreeException, IOException {
        stack.assertIsCurrentParent(parent);
        if (nextElement == null) {
            throw new XMLTreeException
                    ("Can't find more XMLElements inside " + parent.getXMLName());
        }
        if (name == null || !name.equals(nextElement.getName())) {
            throw new XMLTreeException
                    ("Can't find next element named " + name + " in parent " +
                            XMLUtils.getLastName(parent.getXMLName()));
        }
        return pullNextElement();
    }

    /**
     * Checks if an XMLSerializable can get an element with a specific
     * name from the file.
     *
     * @param parent The XMLSerializable that wants to request the next element
     * @param name   The requested name
     * @return A boolean value which indicates if an element with such a name
     *         can be fetched.
     */
    public boolean hasNextElement(XMLSerializable parent, String name) {
        return hasNextElement(parent) && nextElement.getName().equals(name);
    }

    /**
     * Tranfer the contents of the nextElement buffer to a XMLSerializable
     * and fill the buffer.
     *
     * @return The XMLElement in the buffer
     * @throws IOException      If the XMLLoader cannot read from the XML file because
     *                          of an IO error.
     * @throws XMLTreeException If something goes wrong while the parser is
     *                          building the XML tree structure.
     */
    protected XMLElement pullNextElement() throws IOException, XMLTreeException {
        XMLElement result = nextElement;
        nextElement = null;
        stack.push(result.getName());
        lookAhead();
        return result;
    }

    /**
     * @return The name of the XMLElement in the buffer, regardless of its
     *         parent. If the buffer is empty, then the return value is null.
     */
    public String getNextElementName() {
        if (nextElement == null) {
            return null;
        } else {
            return nextElement.getName();
        }
    }

    /**
     * Ignores the further child objects of this XMLSerializable
     * <p/>
     * /** Returns an enumeration of XML elements which can be loaded. More or
     * less the enumeration form of getNextElement(parent).
     * WARNING : using more than one of these enumerations simultaneously
     * may result in unpredictable behaviour.
     *
     * @param parent The object which is requesting the enumeration
     * @return An enumeration of XMLElements
     */
    public Enumeration getEnumeration(XMLSerializable parent) {
        return new XMLLoadEnumeration(parent);
    }

    /**
     * Returns an enumeration of XML elements with a specific name which can be
     * loaded. More or less the enumeration form of
     * getNextElement(parent,name).
     * WARNING : using more than one of these enumerations simultaneously
     * may result in unpredictable behaviour.
     *
     * @param parent The object which is requesting the enumeration
     * @param name   The requested name
     * @return An enumeration of XMLElements
     */
    public Enumeration getEnumeration(XMLSerializable parent, String tagName) {
        return new XMLLoadEnumeration(parent, tagName);
    }

    /**
     * This method is just for debugging
     */
    public void doStackDump() {
        System.out.println("XML stack dump " + stack.getBranchName());
    }

    /**
     * The enumeration which the getEnumeration methods return. See the
     * documentation of java.util.Enumeration for more info.
     */
    class XMLLoadEnumeration implements Enumeration {
        boolean hasName;
        String name;
        XMLSerializable parent;

        public XMLLoadEnumeration(XMLSerializable parent) {
            this.parent = parent;
            hasName = false;
        }

        public XMLLoadEnumeration(XMLSerializable parent, String name) {
            this.parent = parent;
            hasName = true;
            this.name = name;
        }

        public boolean hasMoreElements() {
            if (hasName) {
                return hasNextElement(parent, name);
            } else {
                return hasNextElement(parent);
            }
        }

        public Object nextElement() throws NoSuchElementException {
            try {
                if (hasName) {
                    return getNextElement(parent, name);
                } else {
                    return getNextElement(parent);
                }
            }
            catch (Exception e) {
                throw new NoSuchElementException();
            }
        }
    }

}


