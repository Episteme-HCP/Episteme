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

import org.w3c.dom.DOMException;


/**
 * This interface represents the MathML MathMLMatrixrowElement element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLMatrixrowElement extends MathMLContentElement {
    /**
 * This interface represents the MathML MathMLMatrixrowElement element.
     *
     * @return the requested attribute or element
     */
    public int getNEntries();

    /**
 * This interface represents the MathML MathMLMatrixrowElement element.
     *
     * @param index the value to set
     *
     * @return the requested attribute or element
     *
     * @throws DOMException if an error occurs
     */
    public MathMLContentElement getEntry(int index) throws DOMException;

    /**
 * This interface represents the MathML MathMLMatrixrowElement element.
     *
     * @param newEntry the value to set
     * @param index the value to set
     *
     * @return the requested attribute or element
     *
     * @throws DOMException if an error occurs
     */
    public MathMLContentElement insertEntry(MathMLContentElement newEntry,
        int index) throws DOMException;

    /**
 * This interface represents the MathML MathMLMatrixrowElement element.
     *
     * @param newEntry the value to set
     * @param index the value to set
     *
     * @return the requested attribute or element
     *
     * @throws DOMException if an error occurs
     */
    public MathMLContentElement setEntry(MathMLContentElement newEntry,
        int index) throws DOMException;

    /**
 * This interface represents the MathML MathMLMatrixrowElement element.
     *
     * @param index the value to set
     *
     * @throws DOMException if an error occurs
     */
    public void deleteEntry(int index) throws DOMException;

    /**
 * This interface represents the MathML MathMLMatrixrowElement element.
     *
     * @param index the value to set
     *
     * @return the requested attribute or element
     *
     * @throws DOMException if an error occurs
     */
    public MathMLContentElement removeEntry(int index)
        throws DOMException;
}
;
