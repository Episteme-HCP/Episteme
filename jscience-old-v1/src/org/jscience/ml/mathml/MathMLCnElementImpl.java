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

package org.jscience.ml.mathml;

import org.w3c.dom.mathml.MathMLCnElement;


/**
 * Implements a MathML numeric content element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLCnElementImpl extends MathMLContentTokenImpl
    implements MathMLCnElement {
/**
     * Constructs a MathML numeric content element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLCnElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        String type = getAttribute("type");

        if (type == "") {
            type = "real";
        }

        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setType(String type) {
        setAttribute("type", type);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBase() {
        String base = getAttribute("base");

        if (base == "") {
            base = "10";
        }

        return base;
    }

    /**
     * DOCUMENT ME!
     *
     * @param base DOCUMENT ME!
     */
    public void setBase(String base) {
        setAttribute("base", base);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNargs() {
        final int length = getLength();
        int numArgs = 1;

        for (int i = 0; i < length; i++) {
            String localName = item(i).getLocalName();

            if ((localName != null) && localName.equals("sep")) {
                numArgs++;
            }
        }

        return numArgs;
    }
}
