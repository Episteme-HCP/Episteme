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

package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;


/**
 * 
 */
public class PMRDOMImplementationImpl extends PMRNodeImpl
    implements DOMImplementation {
    /** DOCUMENT ME! */
    protected DOMImplementation domImplementation;

/**
     * Creates a new PMRDOMImplementationImpl object.
     */
    protected PMRDOMImplementationImpl() {
        super();
    }

/**
     * Creates a new PMRDOMImplementationImpl object.
     *
     * @param di DOCUMENT ME!
     */
    protected PMRDOMImplementationImpl(DOMImplementation di) {
        this.domImplementation = di;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Document createDocument(String s, String t, DocumentType dt) {
        Document doc = domImplementation.createDocument(s, t, dt);

        return new PMRDocumentImpl(doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param version DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasFeature(String feature, String version) {
        return domImplementation.hasFeature(feature, version);
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param version DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getFeature(String feature, String version) {
        return domImplementation.getFeature(feature, version);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DocumentType createDocumentType(String s, String t, String v) {
        DocumentType dt = domImplementation.createDocumentType(s, t, v);
        Document doc = domImplementation.createDocument(s, t, dt);

        return new PMRDocumentTypeImpl(dt, new PMRDocumentImpl(doc));
    }
}
