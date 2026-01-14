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

package org.jscience.ml.cml;

import org.w3c.dom.Element;

import org.xml.sax.InputSource;

import java.io.IOException;


/**
 * Creates DocumentFactory of appropriate subclass
 *
 * @author P.Murray-Rust, 2001
 */
public interface CMLDocumentFactory {
    /**
     * set the document class name
     *
     * @param documentClassName the classname with which to create new
     *        Documents
     */
    void setDocumentClassName(String documentClassName);

    /**
     * create document with current documentClassName
     *
     * @return AbstractCMLDocument the document
     */
    AbstractCMLDocument createDocument();

    /**
     * convenience method; parses a string representing a well-formed
     * XML document
     *
     * @param xmlString the string
     *
     * @return Element
     */
    AbstractCMLDocument parseString(String xmlString)
        throws org.jscience.ml.cml.CMLException;

    // restored, PMR
    /**
     * parses XML document into existing DOM The new document must be
     * well-formewd with a single root element the application will probably
     * wish to relocate this in the DOM.
     *
     * @param is the input
     * @param doc the existing document
     * @param debug
     *
     * @return Element element formed by parsing NOT attached to tree
     *
     * @throws IOException specific or IO
     */
    Element parseSAX(InputSource is, AbstractCMLDocument doc, boolean debug)
        throws IOException, org.jscience.ml.cml.CMLException;

    //*/
    // restored, PMR /*
    /**
     * parses XML document into existing DOM The new document must be
     * well-formed with a single root element the application will probably
     * wish to relocate this in the DOM. debug is set to false
     *
     * @param is - the input
     * @param doc - the existing document
     *
     * @return Element - element formed by parsing NOT attached to tree
     *
     * @throws IOException - domain or IO
     */
    Element parseSAX(InputSource is, AbstractCMLDocument doc)
        throws IOException, org.jscience.ml.cml.CMLException;

    //*/
    /**
     * parses XML document into DOM, creating a new ownerDocument
     *
     * @param is - the input
     * @param debug
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException - domain or IO
     */
    AbstractCMLDocument parseSAX(InputSource is, boolean debug)
        throws IOException, org.jscience.ml.cml.CMLException;

    /**
     * as above but debug is false
     *
     * @param is - the input
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException - domain or IO
     */
    AbstractCMLDocument parseSAX(InputSource is)
        throws IOException, org.jscience.ml.cml.CMLException;
}
