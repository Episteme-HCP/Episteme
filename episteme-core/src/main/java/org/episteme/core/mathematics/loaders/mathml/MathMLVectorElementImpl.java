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

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLContentElement;
import org.w3c.dom.mathml.MathMLVectorElement;


/**
 * Implements a MathML <code>vector</code> element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLVectorElementImpl extends MathMLElementImpl
    implements MathMLVectorElement {
    /**
     * Constructs a MathML <code>vector</code> element.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLVectorElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Returns the number of components in the vector.
     *
     * @return the component count
     */
    public int getNcomponents() {
        return getComponentsGetLength();
    }

    /**
     * Returns the component at the specified index.
     *
     * @param index the component index (1-based)
     *
     * @return the content element
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public MathMLContentElement getComponent(int index)
        throws DOMException {
        Node component = getComponentsItem(index - 1);

        if (component == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLContentElement) component;
    }

    /**
     * Sets the component at the specified index.
     *
     * @param newComponent the new component element
     * @param index        the component index (1-based)
     *
     * @return the new component element
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public MathMLContentElement setComponent(
        MathMLContentElement newComponent, int index) throws DOMException {
        final int componentsLength = getComponentsGetLength();

        if ((index < 1) || (index > (componentsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if (index == (componentsLength + 1)) {
            return (MathMLContentElement) appendChild(newComponent);
        } else {
            return (MathMLContentElement) replaceChild(newComponent,
                getComponentsItem(index - 1));
        }
    }

    /**
     * Inserts a component at the specified index.
     *
     * @param newComponent the new component to insert
     * @param index        the index where to insert (0 for end)
     *
     * @return the inserted component element
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public MathMLContentElement insertComponent(
        MathMLContentElement newComponent, int index) throws DOMException {
        final int componentsLength = getComponentsGetLength();

        if ((index < 0) || (index > (componentsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if ((index == 0) || (index == (componentsLength + 1))) {
            return (MathMLContentElement) appendChild(newComponent);
        } else {
            return (MathMLContentElement) insertBefore(newComponent,
                getComponentsItem(index - 1));
        }
    }

    /**
     * Removes the component at the specified index.
     *
     * @param index the component index (1-based)
     *
     * @return the removed component element
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public MathMLContentElement removeComponent(int index)
        throws DOMException {
        Node component = getComponentsItem(index - 1);

        if (component == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLContentElement) removeChild(component);
    }

    /**
     * Deletes the component at the specified index.
     *
     * @param index the component index (1-based)
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public void deleteComponent(int index) throws DOMException {
        removeComponent(index);
    }

    /**
     * Returns the number of content elements.
     *
     * @return the component count
     */
    private int getComponentsGetLength() {
        final int length = getLength();
        int numComponents = 0;

        for (int i = 0; i < length; i++) {
            if (item(i) instanceof MathMLContentElement) {
                numComponents++;
            }
        }

        return numComponents;
    }

    /**
     * Returns the component at the specified 0-based index.
     *
     * @param index the 0-based index
     *
     * @return the component node, or null if out of bounds
     */
    private Node getComponentsItem(int index) {
        final int componentsLength = getComponentsGetLength();

        if ((index < 0) || (index >= componentsLength)) {
            return null;
        }

        Node node = null;
        int n = -1;

        for (int i = 0; n < index; i++) {
            node = item(i);

            if (node instanceof MathMLContentElement) {
                n++;
            }
        }

        return node;
    }
}

