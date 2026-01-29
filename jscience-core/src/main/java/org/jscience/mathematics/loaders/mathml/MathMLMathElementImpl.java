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

package org.jscience.mathematics.loaders.mathml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLDeclareElement;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLNodeList;


/**
 * Implements a MathML <code>math</code> element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLMathElementImpl extends MathMLElementImpl
    implements MathMLMathElement {
/**
     * Constructs a MathML <code>math</code> element.
     *
     * @param owner         the document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLMathElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Gets the macros attribute of the math element.
     *
     * @return the macros string
     */
    public String getMacros() {
        return getAttribute("macros");
    }

    /**
     * Sets the macros attribute of the math element.
     *
     * @param macros the new macros string
     */
    public void setMacros(String macros) {
        setAttribute("macros", macros);
    }

    /**
     * Gets the display attribute of the math element.
     *
     * @return the display mode (e.g., "block" or "inline")
     */
    public String getDisplay() {
        return getAttribute("display");
    }

    /**
     * Sets the display attribute of the math element.
     *
     * @param display the new display mode
     */
    public void setDisplay(String display) {
        setAttribute("display", display);
    }

    /**
     * Gets the number of arguments (child elements that are not declarations).
     *
     * @return the argument count
     */
    public int getNArguments() {
        return getArgumentsGetLength();
    }

    /**
     * Gets the list of arguments (child elements that are not declarations).
     *
     * @return the argument list
     */
    public MathMLNodeList getArguments() {
        return new MathMLNodeList() {
                public int getLength() {
                    return getArgumentsGetLength();
                }

                public Node item(int index) {
                    return getArgumentsItem(index);
                }
            };
    }

    /**
     * Gets the list of declarations within this math element.
     *
     * @return the declaration list
     */
    public MathMLNodeList getDeclarations() {
        return new MathMLNodeList() {
                public int getLength() {
                    return getDeclarationsGetLength();
                }

                public Node item(int index) {
                    return getDeclarationsItem(index);
                }
            };
    }

    /**
     * Gets a specific argument by index.
     *
     * @param index the 1-based index of the argument
     * @return the MathML element at the specified index
     * @throws DOMException if the index is out of bounds
     */
    public MathMLElement getArgument(int index) throws DOMException {
        Node arg = getArgumentsItem(index - 1);

        if (arg == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLElement) arg;
    }

    /**
     * Sets or replaces an argument at a specific index.
     *
     * @param newArgument the new argument element
     * @param index       the 1-based index (if index is length+1, it appends)
     * @return the replaced or appended element
     * @throws DOMException if the index is out of bounds
     */
    public MathMLElement setArgument(MathMLElement newArgument, int index)
        throws DOMException {
        final int argsLength = getArgumentsGetLength();

        if ((index < 1) || (index > (argsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if (index == (argsLength + 1)) {
            return (MathMLElement) appendChild(newArgument);
        } else {
            return (MathMLElement) replaceChild(newArgument,
                getArgumentsItem(index - 1));
        }
    }

    /**
     * Inserts an argument at a specific index.
     *
     * @param newArgument the new argument element
     * @param index       the 1-based index to insert before (0 or length+1 appends)
     * @return the inserted element
     * @throws DOMException if the index is out of bounds
     */
    public MathMLElement insertArgument(MathMLElement newArgument, int index)
        throws DOMException {
        final int argsLength = getArgumentsGetLength();

        if ((index < 0) || (index > (argsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if ((index == 0) || (index == (argsLength + 1))) {
            return (MathMLElement) appendChild(newArgument);
        } else {
            return (MathMLElement) insertBefore(newArgument,
                getArgumentsItem(index - 1));
        }
    }

    /**
     * Removes an argument at a specific index.
     *
     * @param index the 1-based index of the argument to remove
     * @return the removed element
     * @throws DOMException if the index is out of bounds
     */
    public MathMLElement removeArgument(int index) throws DOMException {
        Node arg = getArgumentsItem(index - 1);

        if (arg == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLElement) removeChild(arg);
    }

    /**
     * Deletes an argument at a specific index.
     *
     * @param index the 1-based index of the argument to delete
     * @throws DOMException if the index is out of bounds
     */
    public void deleteArgument(int index) throws DOMException {
        removeArgument(index);
    }

    /**
     * Gets a specific declaration by index.
     *
     * @param index the 1-based index of the declaration
     * @return the declaration element
     * @throws DOMException if the index is out of bounds
     */
    public MathMLDeclareElement getDeclaration(int index)
        throws DOMException {
        Node decl = getDeclarationsItem(index - 1);

        if (decl == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLDeclareElement) decl;
    }

    /**
     * Sets or replaces a declaration at a specific index.
     *
     * @param newDeclaration the new declaration element
     * @param index          the 1-based index
     * @return the replaced or appended element
     * @throws DOMException if the index is out of bounds
     */
    public MathMLDeclareElement setDeclaration(
        MathMLDeclareElement newDeclaration, int index)
        throws DOMException {
        final int declsLength = getDeclarationsGetLength();

        if ((index < 1) || (index > (declsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if (index == (declsLength + 1)) {
            return (MathMLDeclareElement) appendChild(newDeclaration);
        } else {
            return (MathMLDeclareElement) replaceChild(newDeclaration,
                getDeclarationsItem(index - 1));
        }
    }

    /**
     * Inserts a declaration at a specific index.
     *
     * @param newDeclaration the new declaration element
     * @param index          the 1-based index (0 or length+1 appends)
     * @return the inserted element
     * @throws DOMException if the index is out of bounds
     */
    public MathMLDeclareElement insertDeclaration(
        MathMLDeclareElement newDeclaration, int index)
        throws DOMException {
        final int declsLength = getDeclarationsGetLength();

        if ((index < 0) || (index > (declsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if ((index == 0) || (index == (declsLength + 1))) {
            return (MathMLDeclareElement) appendChild(newDeclaration);
        } else {
            return (MathMLDeclareElement) insertBefore(newDeclaration,
                getDeclarationsItem(index - 1));
        }
    }

    /**
     * Removes a declaration at a specific index.
     *
     * @param index the 1-based index of the declaration to remove
     * @return the removed element
     * @throws DOMException if the index is out of bounds
     */
    public MathMLDeclareElement removeDeclaration(int index)
        throws DOMException {
        Node decl = getDeclarationsItem(index - 1);

        if (decl == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLDeclareElement) removeChild(decl);
    }

    /**
     * Deletes a declaration at a specific index.
     *
     * @param index the 1-based index of the declaration to delete
     * @throws DOMException if the index is out of bounds
     */
    public void deleteDeclaration(int index) throws DOMException {
        removeDeclaration(index);
    }

    /**
     * Helper method to calculate the number of arguments.
     *
     * @return the number of non-declaration children
     */
    private int getArgumentsGetLength() {
        final int length = getLength();
        int numArgs = 0;

        for (int i = 0; i < length; i++) {
            if (!(item(i) instanceof MathMLDeclareElement)) {
                numArgs++;
            }
        }

        return numArgs;
    }

    /**
     * Helper method to retrieve an argument by its non-declaration index.
     *
     * @param index the 0-based index of the argument
     * @return the argument node, or null if index is out of bounds
     */
    private Node getArgumentsItem(int index) {
        final int argsLength = getArgumentsGetLength();

        if ((index < 0) || (index >= argsLength)) {
            return null;
        }

        Node node = null;
        int n = -1;

        for (int i = 0; n < index; i++) {
            node = item(i);

            if (!(node instanceof MathMLDeclareElement)) {
                n++;
            }
        }

        return node;
    }

    /**
     * Helper method to calculate the number of declarations.
     *
     * @return the number of declaration children
     */
    private int getDeclarationsGetLength() {
        final int length = getLength();
        int numDecls = 0;

        for (int i = 0; i < length; i++) {
            if (item(i) instanceof MathMLDeclareElement) {
                numDecls++;
            }
        }

        return numDecls;
    }

    /**
     * Helper method to retrieve a declaration by its index among declarations.
     *
     * @param index the 0-based index of the declaration
     * @return the declaration node, or null if index is out of bounds
     */
    private Node getDeclarationsItem(int index) {
        final int declLength = getDeclarationsGetLength();

        if ((index < 0) || (index >= declLength)) {
            return null;
        }

        Node node = null;
        int n = -1;

        for (int i = 0; n < index; i++) {
            node = item(i);

            if (node instanceof MathMLDeclareElement) {
                n++;
            }
        }

        return node;
    }
}
