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
 * This interface represents the <code>vector</code> element in MathML Content.
 * A vector is a mathematical object that has a direction and a magnitude.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLVectorElement extends MathMLContentElement {
    /**
     * Returns the number of components in this vector.
     *
     * @return the number of components.
     */
    public int getNcomponents();

    /**
     * Returns the <code>index</code>-th component of this vector.
     *
     * @param index the index of the component to retrieve.
     * @return the component at the specified index.
     */
    public MathMLContentElement getComponent(int index);

    /**
     * Inserts a new component into the vector at the specified <code>index</code>.
     *
     * @param newComponent the new component to insert.
     * @param index        the index at which the new component is to be inserted.
     * @return the newly inserted component.
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds.
     */
    public MathMLContentElement insertComponent(
            MathMLContentElement newComponent, int index) throws DOMException;

    /**
     * Replaces the component at the specified <code>index</code> with a new component.
     *
     * @param newComponent the new component to set.
     * @param index        the index of the component to replace.
     * @return the newly set component.
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds.
     */
    public MathMLContentElement setComponent(
            MathMLContentElement newComponent, int index) throws DOMException;

    /**
     * Deletes the component at the specified <code>index</code>.
     *
     * @param index the index of the component to delete.
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds.
     */
    public void deleteComponent(int index) throws DOMException;

    /**
     * Removes and returns the component at the specified <code>index</code>.
     *
     * @param index the index of the component to remove.
     * @return the removed component.
     */
    public MathMLContentElement removeComponent(int index);
}
;
