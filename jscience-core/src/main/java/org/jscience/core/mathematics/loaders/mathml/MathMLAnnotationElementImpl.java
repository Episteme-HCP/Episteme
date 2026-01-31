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

package org.jscience.core.mathematics.loaders.mathml;

import org.w3c.dom.mathml.MathMLAnnotationElement;


/**
 * Implements a MathML annotation element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLAnnotationElementImpl extends MathMLElementImpl
    implements MathMLAnnotationElement {
/**
     * Constructs a MathML annotation element.
     *
     * @param owner         the document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLAnnotationElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Gets the content body of the annotation.
     *
     * @return the body text
     */
    public String getBody() {
        return getFirstChild().getNodeValue();
    }

    /**
     * Sets the content body of the annotation.
     *
     * @param body the new body text
     */
    public void setBody(String body) {
        getFirstChild().setNodeValue(body);
    }

    /**
     * Gets the encoding attribute of the annotation.
     *
     * @return the encoding
     */
    public String getEncoding() {
        return getAttribute("encoding");
    }

    /**
     * Sets the encoding attribute of the annotation.
     *
     * @param encoding the new encoding
     */
    public void setEncoding(String encoding) {
        setAttribute("encoding", encoding);
    }
}

