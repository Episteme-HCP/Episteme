/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.chemistry.loaders.cml.cmlimpl;

import org.episteme.natural.chemistry.loaders.cml.AbstractBase;
import org.episteme.natural.chemistry.loaders.cml.AbstractCMLDocument;
import org.episteme.natural.chemistry.loaders.cml.CMLException;
import org.episteme.natural.chemistry.loaders.cml.SaxHandler;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;

import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;
import java.util.Vector;
import java.util.logging.LogRecord;


/**
 * manages callbacks from SAX2 handler
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SaxHandlerImpl extends DefaultHandler
    implements SaxHandler {
    /** The CML document being populated by the handler. */
    AbstractCMLDocument doc;

    /** Stack to manage the hierarchical structure of CML nodes during parsing. */
    Stack<Node> stack;

    // the node which determines the data type; always mirrors the input
    /** The current node being processed in the input document. */
    Node currentNode;

    // the Node to add new elements to; mirrors the output data structure
    //    Node currentParent = null;
    /** Vector to store error messages encountered during parsing. */
    Vector<String> errorVector;

    /** Flag to enable or disable debugging output. */
    boolean debug = false;

    /** The original root element of the document, if it existed. */
    Element oldRootElement = null;

    /** The new root element created during the parsing process. */
    Element newRootElement = null;

    /** Flag to indicate whether ignorable whitespace should be skipped. */
    boolean ignoreWhite = false;

    /**
     * Creates a new SaxHandlerImpl object.
     *
     * @param d     the document to populate
     * @param debug true to enable debugging output
     */
    public SaxHandlerImpl(AbstractCMLDocument d, boolean debug) {
        this.doc = d;
        stack = new Stack<>();
        stack.push(doc);
        errorVector = new Vector<>();
        this.debug = debug;
        debug("Sax debugging on");

        // if doc already has a root element, detach it and build new one.
        // but keep the old one and replace it at end
        oldRootElement = doc.getDocumentElement();

        // in most instances the new element is unrelated to the old one (except maybe as a sibling)
        // however sometimes a new document root is created and the old element is then part of its
        // tree. If so, check it can be removed
        if (oldRootElement != null) {
            debug("removing old root");

            try {
                //if (oldRootElement.getParentNode().equals(doc)) {
                doc.removeChild(oldRootElement);

                //}
            } catch (NullPointerException npe) {
                //npe.printStackTrace();
                System.err.println("BUG: " + npe);
            }
        }
    }


    /**
     * Adds an error message to the internal error vector.
     *
     * @param s the error message string
     */
    void addError(String s) {
        errorVector.addElement(s);
    }

    /**
     * Sets whether to ignore ignorable whitespace during parsing.
     *
     * @param ignoreWhite true to ignore whitespace, false otherwise
     */
    public void setIgnoreWhite(boolean ignoreWhite) {
        this.ignoreWhite = ignoreWhite;
    }

    /**
     * Returns true if any errors were encountered during parsing.
     *
     * @return true if errors occurred, false otherwise
     */
    public boolean hasErrors() {
        return errorVector.size() > 0;
    }

    /**
     * Returns a vector of error messages encountered during parsing.
     *
     * @return a vector of error strings
     */
    public Vector<String> getErrorVector() {
        return errorVector;
    }

    /**
     * Returns the newly created root element.
     *
     * @return the new root element
     */
    Element getNewRootElement() {
        return newRootElement;
    }

    /**
     * Returns the current parsing stack.
     *
     * @return the stack of nodes
     */
    Stack<Node> getStack() {
        return stack;
    }

    /**
     * Enables or disables debugging output.
     *
     * @param debug true to enable, false to disable
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Returns the current debug status.
     *
     * @return true if debug is on
     */
    public boolean getDebug() {
        return this.debug;
    }

    /**
     * Publishes a log record. Currently commented out but originally intended to
     * print level and message.
     *
     * @param record the log record to publish
     */
    public void publish(LogRecord record) {
        //System.out.println(record.getLevel()+"/"+record.getMessage());
    }

    /**
     * Flushes any buffered output.
     */
    public void flush() {
        System.out.flush();
    }

    /**
     * Closes the handler and releases all resources.
     */
    public void close() {
        //        System.out.println("CLOSECONSOLE");
    }

    /**
     * Outputs a debug message if debugging is enabled.
     *
     * @param s the debug message
     */
    protected void debug(String s) {
        if (debug) {
            System.out.println("SAX> " + s);
        }
    }

    /**
     * SAX callback for character data.
     *
     * @param ch     the character array
     * @param start  the start position
     * @param length the number of characters
     */
    public void characters(char[] ch, int start, int length) {
        String content = new String(ch, start, length);
        content = content.trim();

        if (ignoreWhite && content.equals("")) {
        } else {
            try {
                if (currentNode instanceof AbstractBase) {
                    ((AbstractBase) currentNode).characters(this, content);
                } else if (currentNode instanceof CMLBaseImpl) {
                    ((CMLBaseImpl) currentNode).setContentValue(content);
                } else {
                    currentNode.appendChild(currentNode.getOwnerDocument()
                                                       .createTextNode(content));
                }
            } catch (CMLException e) {
                e.printStackTrace();
                addError("E2: " + e + "/" + content);
            }
        }
    }

    /**
     * SAX callback for the end of the document.
     */
    public void endDocument() {
        debug("End doc: ");

        if (oldRootElement != null) {
            debug("resetting docs: ");
            newRootElement = doc.getDocumentElement();

            try {
                //if (oldRootElement.getParentNode().equals(doc)) {
                doc.replaceChild(oldRootElement, newRootElement);

                //}
            } catch (NullPointerException npe) {
                //npe.printStackTrace();
                System.err.println("BUG: " + npe);
            }
        } else {
            debug("NEW ROOT " + newRootElement);
        }

        debug("Debug: Ended doc");
    }

    /**
     * SAX callback for the end of an element.
     *
     * @param uri       the namespace URI
     * @param localName the local name
     * @param qName     the qualified name
     */
    public void endElement(String uri, String localName, String qName) {
        debug("endElement " + localName);

        try {
            if (currentNode instanceof AbstractBase) {
                ((AbstractBase) currentNode).endElement(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            addError("" + e);
        }

        // this is called for all elements
        currentNode = stack.pop();
    }

    /**
     * SAX callback for recoverable parse errors.
     *
     * @param e the SAX parse exception
     */
    public void error(SAXParseException e) {
        System.err.println("SAX Error: " + e);
    }

    /**
     * SAX callback for fatal parse errors.
     *
     * @param e the fatal SAX parse exception
     */
    public void fatalError(SAXParseException e) {
        String msg = "" + e + " at " + e.getLineNumber() + ":" +
            e.getColumnNumber() + " in " + e.getSystemId();
        System.err.println("SAX FatalError: " + msg);
    }

    /**
     * SAX callback for ignorable whitespace.
     *
     * @param ch     the character array
     * @param start  the start position
     * @param length the number of characters
     */
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    /**
     * SAX callback for processing instructions.
     *
     * @param target the target string
     * @param data   the data string
     */
    public void processingInstruction(String target, String data) {
    }

    /**
     * SAX callback for the start of the document.
     */
    public void startDocument() {
        debug("startDOCUMENT");
        debug("Debug on: Started doc");
        currentNode = doc;

        //        currentParent = doc;
    }

    /**
     * SAX callback for the start of an element.
     *
     * @param uri        the namespace URI
     * @param localName  the local name
     * @param qName      the qualified name
     * @param attributes the element attributes
     */
    public void startElement(String uri, String localName, String qName,
        Attributes attributes) {
        if (localName.equals("")) {
            System.out.println(
                "CLASSPATH BUG in SAX parser. You will have to try different libraries for XML parsing or reorder them. In ANT use fork='true'");
            System.out.println("StartElement: [" + localName + "][" + qName +
                "][" + uri + "]");
            new Exception().printStackTrace();
            System.exit(0);
        }

        String elemName = localName.trim();
        debug("startElement: " + elemName);

        Node newNode = null;

        try {
            newNode = doc.createSubclassedElement(elemName);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("CANNOT MAKE SUBCLASS: " + e.getCause());
        }

        if (newNode == null) {
            System.err.println("Cannot create node: " + elemName);

            return;
        }

        try {
            if (currentNode instanceof AbstractCMLDocument) {
                ((AbstractCMLDocument) doc).appendChild(newNode);
            } else if (currentNode instanceof CMLBaseImpl) {
                ((CMLBaseImpl) currentNode).appendChild((Element) newNode);
            } else if (currentNode instanceof Element) {
                ((Element) currentNode).appendChild((Element) newNode);
            }
        } catch (ClassCastException cce) {
            System.err.println("ClassCastException in startElement current: " +
                currentNode.getClass() + "/new: " + newNode.getClass());
        } catch (Exception e) {
            System.err.println("Exception" + e);
            e.printStackTrace();
        }

        // generic action; may be undone in element-specific routines
        //        currentParent.appendChild(newNode);
        // this is always called, regardless of the output DOM
        stack.push(currentNode);

        // current node is always determined by input document
        currentNode = newNode;

        // current parent may be reset in element-specific codes
        //        currentParent = newNode;
        // process element (mainly attributes at this stage
        if (newNode instanceof AbstractBase) {
            try {
                ((AbstractBase) currentNode).startElement(this, attributes);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("AB fail" + e);
                addError("" + e);
            }
        } else {
            //            System.out.println("StartElementAttributes: "+attributes.getLength());
            // process attributes
            for (int i = 0; i < attributes.getLength(); i++) {
                ((Element) currentNode).setAttribute(attributes.getLocalName(i),
                    attributes.getValue(i));
            }
        }

        if (newRootElement == null) {
            newRootElement = (Element) newNode;
            debug("new root element");
        }
    }

    /**
     * SAX callback for the start of a prefix mapping.
     *
     * @param prefix the namespace prefix
     * @param uri    the namespace URI
     */
    public void startPrefixMapping(String prefix, String uri) {
    }

    /**
     * SAX callback for unparsed entity declarations.
     *
     * @param name         the entity name
     * @param publicId     the public identifier
     * @param systemId     the system identifier
     * @param notationName the name of the notation
     */
    public void unparsedEntityDecl(String name, String publicId,
        String systemId, String notationName) {
        System.err.println("unparsed entity: " + name + "/" + systemId);
    }

    /**
     * SAX callback for parse warnings.
     *
     * @param e the SAX parse warning
     */
    public void warning(SAXParseException e) {
        String s = "" + e + " at " + e.getLineNumber() + ":" +
            e.getColumnNumber() + " in " + e.getSystemId();

        //        if (s.indexOf("URI was not reported to parser for entity [document]") != -1) {
        //     URI was not reported to parser for entity [document]
        //        } else {
        System.err.println("SAX Warning: " + s);

        //        }
    }

    /**
     * Test main method for parsing experiments.
     *
     * @param args command line arguments
     *
     * @throws Exception if a test error occurs
     */
    public static void main(String[] args) throws Exception {
        /*
        boolean debug = true;
        String a1 = "<foo1><bar1>junk1</bar1></foo1>";
        AbstractCMLDocument doc = new AbstractCMLDocumentImpl();
        Element exampleElem = DocumentFactoryImpl.newInstance().parseSAX(new InputSource(new StringReader(a1)), doc, debug);
        String a2 = "<foo2><bar2>junk2</bar2></foo2>";
        exampleElem = DocumentFactoryImpl.newInstance().parseSAX(new InputSource(new StringReader(a2)), doc, debug);
        String a3 = "<foo3><bar3>junk3</bar3></foo3>";
        exampleElem = DocumentFactoryImpl.newInstance().parseSAX(new InputSource(new StringReader(a3)), doc, debug);
        */
    }
}


