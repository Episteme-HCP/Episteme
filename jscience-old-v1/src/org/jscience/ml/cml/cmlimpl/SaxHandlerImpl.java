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

package org.jscience.ml.cml.cmlimpl;

import org.jscience.ml.cml.AbstractBase;
import org.jscience.ml.cml.AbstractCMLDocument;
import org.jscience.ml.cml.CMLException;
import org.jscience.ml.cml.SaxHandler;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;

import java.util.Stack;
import java.util.Vector;


/**
 * manages callbacks from SAX2 handler
 */
public class SaxHandlerImpl extends org.xml.sax.helpers.DefaultHandler
    implements SaxHandler {
    /** DOCUMENT ME! */
    AbstractCMLDocument doc;

    /** DOCUMENT ME! */
    Stack stack;

    // the node which determines the data type; always mirrors the input
    /** DOCUMENT ME! */
    Node currentNode;

    // the Node to add new elements to; mirrors the output data structure
    //    Node currentParent = null;
    /** DOCUMENT ME! */
    Vector errorVector;

    /** DOCUMENT ME! */
    boolean debug = false;

    /** DOCUMENT ME! */
    Element oldRootElement = null;

    /** DOCUMENT ME! */
    Element newRootElement = null;

    /** DOCUMENT ME! */
    boolean ignoreWhite = false;

/**
     * Creates a new SaxHandlerImpl object.
     *
     * @param d     DOCUMENT ME!
     * @param debug DOCUMENT ME!
     */
    public SaxHandlerImpl(AbstractCMLDocument d, boolean debug) {
        this.doc = d;
        stack = new Stack();
        stack.push(doc);
        errorVector = new Vector();
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
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    void addError(String s) {
        errorVector.addElement(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ignoreWhite DOCUMENT ME!
     */
    public void setIgnoreWhite(boolean ignoreWhite) {
        this.ignoreWhite = ignoreWhite;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasErrors() {
        return errorVector.size() > 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getErrorVector() {
        return errorVector;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Element getNewRootElement() {
        return newRootElement;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Stack getStack() {
        return stack;
    }

    /**
     * DOCUMENT ME!
     *
     * @param debug DOCUMENT ME!
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getDebug() {
        return this.debug;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    protected void debug(String s) {
        if (debug) {
            System.out.println("SAX> " + s);
        }
    }

    // ============================== SAX CALLBACKS ====================== //
    /**
     * DOCUMENT ME!
     *
     * @param ch DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param length DOCUMENT ME!
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
     * DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param uri DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param qName DOCUMENT ME!
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
        currentNode = (Node) stack.pop();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void error(SAXParseException e) {
        System.err.println("SAX Error: " + e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void fatalError(SAXParseException e) {
        String msg = "" + e + " at " + e.getLineNumber() + ":" +
            e.getColumnNumber() + " in " + e.getSystemId();
        System.err.println("SAX FatalError: " + msg);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ch DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     * @param data DOCUMENT ME!
     */
    public void processingInstruction(String target, String data) {
    }

    /**
     * DOCUMENT ME!
     */
    public void startDocument() {
        debug("startDOCUMENT");
        debug("Debug on: Started doc");
        currentNode = doc;

        //        currentParent = doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param uri DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param qName DOCUMENT ME!
     * @param attributes DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param prefix DOCUMENT ME!
     * @param uri DOCUMENT ME!
     */
    public void startPrefixMapping(String prefix, String uri) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param publicId DOCUMENT ME!
     * @param systemId DOCUMENT ME!
     * @param notationName DOCUMENT ME!
     */
    public void unparsedEntityDecl(String name, String publicId,
        String systemId, String notationName) {
        System.err.println("unparsed entity: " + name + "/" + systemId);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
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
