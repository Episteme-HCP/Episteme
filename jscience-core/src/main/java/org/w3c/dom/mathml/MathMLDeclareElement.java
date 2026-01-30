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

package org.w3c.dom.mathml;

/**
 * This interface represents the MathML MathMLDeclareElement element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLDeclareElement extends MathMLContentElement {
    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @return the requested attribute or element
     */
    public String getType();

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @param type the value to set
     */
    public void setType(String type);

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @return the requested attribute or element
     */
    public int getNargs();

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @param nargs the value to set
     */
    public void setNargs(int nargs);

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @return the requested attribute or element
     */
    public String getOccurrence();

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @param occurrence the value to set
     */
    public void setOccurrence(String occurrence);

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @return the requested attribute or element
     */
    public String getDefinitionURL();

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @param definitionURL the value to set
     */
    public void setDefinitionURL(String definitionURL);

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @return the requested attribute or element
     */
    public String getEncoding();

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @param encoding the value to set
     */
    public void setEncoding(String encoding);

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @return the requested attribute or element
     */
    public MathMLCiElement getIdentifier();

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @param identifier the value to set
     */
    public void setIdentifier(MathMLCiElement identifier);

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @return the requested attribute or element
     */
    public MathMLElement getConstructor();

    /**
 * This interface represents the MathML MathMLDeclareElement element.
     *
     * @param constructor the value to set
     */
    public void setConstructor(MathMLElement constructor);
}
;
