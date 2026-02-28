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

package org.episteme.core.mathematics.loaders.mathml;

import org.w3c.dom.mathml.MathMLCnElement;


/**
 * Implements a MathML numeric content element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLCnElementImpl extends MathMLContentTokenImpl
    implements MathMLCnElement {
    /**
     * Constructs a MathML numeric content element.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLCnElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Returns the type of the number (real, integer, complex, etc.).
     *
     * @return the number type, defaults to "real"
     */
    public String getType() {
        String type = getAttribute("type");

        if (type == "") {
            type = "real";
        }

        return type;
    }

    /**
     * Sets the type of the number.
     *
     * @param type the number type to set
     */
    public void setType(String type) {
        setAttribute("type", type);
    }

    /**
     * Returns the base of the number representation.
     *
     * @return the number base, defaults to "10"
     */
    public String getBase() {
        String base = getAttribute("base");

        if (base == "") {
            base = "10";
        }

        return base;
    }

    /**
     * Sets the base of the number representation.
     *
     * @param base the number base to set
     */
    public void setBase(String base) {
        setAttribute("base", base);
    }

    /**
     * Returns the number of arguments (parts separated by sep elements).
     *
     * @return the argument count
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

