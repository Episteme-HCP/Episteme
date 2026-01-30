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

import org.w3c.dom.DOMException;


/**
 * This interface is used by MathML elements that can contain other elements.
 * It provides methods for managing arguments and declarations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLContainer {
    /**
     * Returns the number of arguments (children) of this container.
     *
     * @return the number of arguments.
     */
    public int getNArguments();

    /**
     * Returns the list of all arguments (children) of this container.
     *
     * @return a MathMLNodeList containing the arguments.
     */
    public MathMLNodeList getArguments();

    /**
     * Returns the list of all declarations within this container.
     *
     * @return a MathMLNodeList containing the declarations.
     */
    public MathMLNodeList getDeclarations();

    /**
     * Retrieves an argument at the specified index.
     *
     * @param index the index of the argument to retrieve.
     * @return the argument at the given index.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than or equal to the number of arguments.
     */
    public MathMLElement getArgument(int index) throws DOMException;

    /**
     * Sets or replaces an argument at the specified index.
     *
     * @param newArgument the new MathMLElement to set.
     * @param index the index at which to set the argument.
     * @return the newly set argument.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than the number of arguments.
     */
    public MathMLElement setArgument(MathMLElement newArgument, int index)
        throws DOMException;

    /**
     * Inserts an argument at the specified index.
     *
     * @param newArgument the MathMLElement to insert.
     * @param index the index at which to insert the argument.
     * @return the newly inserted argument.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than the number of arguments.
     */
    public MathMLElement insertArgument(MathMLElement newArgument, int index)
        throws DOMException;

    /**
     * Deletes the argument at the specified index.
     *
     * @param index the index of the argument to delete.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than or equal to the number of arguments.
     */
    public void deleteArgument(int index) throws DOMException;

    /**
     * Removes the argument at the specified index and returns it.
     *
     * @param index the index of the argument to remove.
     * @return the removed argument.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than or equal to the number of arguments.
     */
    public MathMLElement removeArgument(int index) throws DOMException;

    /**
     * Retrieves a declaration at the specified index.
     *
     * @param index the index of the declaration to retrieve.
     * @return the declaration at the given index.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than or equal to the number of declarations.
     */
    public MathMLDeclareElement getDeclaration(int index)
        throws DOMException;

    /**
     * Sets or replaces a declaration at the specified index.
     *
     * @param newDeclaration the new MathMLDeclareElement to set.
     * @param index the index at which to set the declaration.
     * @return the newly set declaration.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than the number of declarations.
     */
    public MathMLDeclareElement setDeclaration(
        MathMLDeclareElement newDeclaration, int index)
        throws DOMException;

    /**
     * Inserts a declaration at the specified index.
     *
     * @param newDeclaration the MathMLDeclareElement to insert.
     * @param index the index at which to insert the declaration.
     * @return the newly inserted declaration.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than the number of declarations.
     */
    public MathMLDeclareElement insertDeclaration(
        MathMLDeclareElement newDeclaration, int index)
        throws DOMException;

    /**
     * Removes the declaration at the specified index and returns it.
     *
     * @param index the index of the declaration to remove.
     * @return the removed declaration.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than or equal to the number of declarations.
     */
    public MathMLDeclareElement removeDeclaration(int index)
        throws DOMException;

    /**
     * Deletes the declaration at the specified index.
     *
     * @param index the index of the declaration to delete.
     * @throws DOMException INDEX_SIZE_ERR: if index is greater than or equal to the number of declarations.
     */
    public void deleteDeclaration(int index) throws DOMException;
}
;
