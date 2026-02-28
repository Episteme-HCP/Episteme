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

package org.episteme.natural.chemistry.loaders.cml.dom.pmr;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;


/**
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PMRDOMImplementationImpl extends PMRNodeImpl
    implements DOMImplementation {
    /** The underlying generic W3C DOM implementation. */
    protected DOMImplementation domImplementation;

/**
     * Creates a new PMRDOMImplementationImpl object.
     */
    protected PMRDOMImplementationImpl() {
        super();
    }

    /**
     * Creates a new PMRDOMImplementationImpl that wraps a base implementation.
     *
     * @param di the base DOM implementation to wrap
     */
    protected PMRDOMImplementationImpl(DOMImplementation di) {
        this.domImplementation = di;
    }

    /**
     * Creates a new DOM Document of the specified type with its document element.
     *
     * @param s  the namespace URI of the document element to create
     * @param t  the qualified name of the document element to be created
     * @param dt the type of document to be created or null
     *
     * @return a new PMRDocument object with its document element
     */
    public Document createDocument(String s, String t, DocumentType dt) {
        Document doc = domImplementation.createDocument(s, t, dt);

        return new PMRDocumentImpl(doc);
    }

    /**
     * Test if the DOM implementation implements a specific feature.
     *
     * @param feature the name of the feature to test
     * @param version the version number of the feature to test
     *
     * @return true if the feature is implemented, false otherwise
     */
    public boolean hasFeature(String feature, String version) {
        return domImplementation.hasFeature(feature, version);
    }

    /**
     * Returns an object which implements the specialized APIs of the specified feature.
     *
     * @param feature the name of the feature
     * @param version the version number of the feature
     *
     * @return the specialized object or null
     */
    public Object getFeature(String feature, String version) {
        return domImplementation.getFeature(feature, version);
    }

    /**
     * Creates an empty DocumentType node.
     *
     * @param s the qualified name of the document type to be created
     * @param t the public identifier of the external subset
     * @param v the system identifier of the external subset
     *
     * @return a new DocumentType node with ownerDocument set to null
     */
    public DocumentType createDocumentType(String s, String t, String v) {
        DocumentType dt = domImplementation.createDocumentType(s, t, v);
        Document doc = domImplementation.createDocument(s, t, dt);

        return new PMRDocumentTypeImpl(dt, new PMRDocumentImpl(doc));
    }
}

