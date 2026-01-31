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

import org.w3c.dom.mathml.MathMLPredefinedSymbol;


/**
 * Implements a MathML predefined symbol.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLPredefinedSymbolImpl extends MathMLElementImpl
    implements MathMLPredefinedSymbol {
    /**
     * Constructs a MathML predefined symbol.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLPredefinedSymbolImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Returns the definition URL of the symbol.
     *
     * @return the definition URL
     */
    public String getDefinitionURL() {
        return getAttribute("definitionURL");
    }

    /**
     * Sets the definition URL of the symbol.
     *
     * @param definitionURL the definition URL to set
     */
    public void setDefinitionURL(String definitionURL) {
        setAttribute("definitionURL", definitionURL);
    }

    /**
     * Returns the encoding of the symbol.
     *
     * @return the encoding
     */
    public String getEncoding() {
        return getAttribute("encoding");
    }

    /**
     * Sets the encoding of the symbol.
     *
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        setAttribute("encoding", encoding);
    }

    /**
     * Returns the arity of the symbol.
     *
     * @return the arity (always "0" for predefined symbols)
     */
    public String getArity() {
        return "0";
    }

    /**
     * Returns the name of the symbol.
     *
     * @return the local name of the element
     */
    public String getSymbolName() {
        return getLocalName();
    }
}

