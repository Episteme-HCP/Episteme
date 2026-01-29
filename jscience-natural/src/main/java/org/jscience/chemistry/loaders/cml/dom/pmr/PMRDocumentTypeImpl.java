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

package org.jscience.chemistry.loaders.cml.dom.pmr;

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;


/**
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PMRDocumentTypeImpl extends PMRNodeImpl implements DocumentType {
/**
     * Creates a new PMRDocumentTypeImpl object.
     */
    public PMRDocumentTypeImpl() {
        super();
    }

/**
     * Creates a new PMRDocumentTypeImpl object.
     *
     * @param dt  the generic W3C document type to delegate to
     * @param doc the owner CML document
     */
    public PMRDocumentTypeImpl(DocumentType dt, PMRDocument doc) {
        super(dt, doc);
    }

    /**
     * Returns the internal subset as a string.
     *
     * @return the internal subset
     */
    public String getInternalSubset() {
        return ((DocumentType) delegateNode).getInternalSubset();
    }

    /**
     * Returns a NamedNodeMap containing the notations declared in the DTD.
     *
     * @return the notations map
     */
    public NamedNodeMap getNotations() {
        return ((DocumentType) delegateNode).getNotations();
    }

    /**
     * Returns the name of the DTD (i.e., the name immediately following the DOCTYPE keyword).
     *
     * @return the DTD name
     */
    public String getName() {
        return ((DocumentType) delegateNode).getName();
    }

    /**
     * Returns a NamedNodeMap containing the general entities declared in the DTD.
     *
     * @return the entities map
     */
    public NamedNodeMap getEntities() {
        return ((DocumentType) delegateNode).getEntities();
    }

    /**
     * Returns the system identifier of the external subset.
     *
     * @return the system ID
     */
    public String getSystemId() {
        return ((DocumentType) delegateNode).getSystemId();
    }

    /**
     * Returns the public identifier of the external subset.
     *
     * @return the public ID
     */
    public String getPublicId() {
        return ((DocumentType) delegateNode).getPublicId();
    }
}
