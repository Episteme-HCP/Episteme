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

package org.w3c.dom.mathml;

import org.w3c.dom.Node;


/**
 * This interface represents the MathML MathMLContentToken element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLContentToken extends MathMLContentElement {
    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @return the requested attribute or element
     */
    public MathMLNodeList getArguments();

    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @return the requested attribute or element
     */
    public String getDefinitionURL();

    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @param definitionURL the value to set
     */
    public void setDefinitionURL(String definitionURL);

    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @return the requested attribute or element
     */
    public String getEncoding();

    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @param encoding the value to set
     */
    public void setEncoding(String encoding);

    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @param index the value to set
     * @return the requested attribute or element
     */
    public Node getArgument(int index);

    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @param newArgument the value to set
     * @param index       the index at which to insert the argument.
     * @return the requested attribute or element
     */
    public Node insertArgument(Node newArgument, int index);

    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @param newArgument the value to set
     * @param index       the index of the argument to set.
     * @return the requested attribute or element
     */
    public Node setArgument(Node newArgument, int index);

    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @param index the value to set
     */
    public void deleteArgument(int index);

    /**
 * This interface represents the MathML MathMLContentToken element.
     *
     * @param index the value to set
     * @return the requested attribute or element
     */
    public Node removeArgument(int index);
}
;
