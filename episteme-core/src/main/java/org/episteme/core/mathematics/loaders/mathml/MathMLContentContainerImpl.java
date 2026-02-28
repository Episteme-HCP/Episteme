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
import org.w3c.dom.mathml.*;


/**
 * Implements a MathML content container.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLContentContainerImpl extends MathMLElementImpl
    implements MathMLContentContainer {
    /**
     * Constructs a MathML content container.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element type
     */
    public MathMLContentContainerImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Returns the <code>condition</code> child element.
     *
     * @return the condition element
     */
    public MathMLConditionElement getCondition() {
        return (MathMLConditionElement) getNodeByName("condition");
    }

    /**
     * Sets the <code>condition</code> child element.
     *
     * @param condition the condition element to set
     *
     * @throws DOMException if the condition element cannot be set
     */
    public void setCondition(MathMLConditionElement condition)
        throws DOMException {
        setNodeByName(condition, "condition");
    }

    /**
     * Returns the <code>degree</code> child element.
     *
     * @return the degree element
     */
    public MathMLElement getOpDegree() {
        return (MathMLElement) getNodeByName("degree");
    }

    /**
     * Sets the <code>degree</code> child element.
     *
     * @param opDegree the degree element to set
     *
     * @throws DOMException if the degree element cannot be set
     */
    public void setOpDegree(MathMLElement opDegree) throws DOMException {
        setNodeByName(opDegree, "degree");
    }

    /**
     * Returns the <code>domainofapplication</code> child element.
     *
     * @return the domain of application element
     */
    public MathMLElement getDomainOfApplication() {
        return (MathMLElement) getNodeByName("domainofapplication");
    }

    /**
     * Sets the <code>domainofapplication</code> child element.
     *
     * @param domainOfApplication the domain of application element to set
     *
     * @throws DOMException if the element cannot be set
     */
    public void setDomainOfApplication(MathMLElement domainOfApplication)
        throws DOMException {
        setNodeByName(domainOfApplication, "domainofapplication");
    }

    /**
     * Returns the <code>momentabout</code> child element.
     *
     * @return the moment about element
     */
    public MathMLElement getMomentAbout() {
        return (MathMLElement) getNodeByName("momentabout");
    }

    /**
     * Sets the <code>momentabout</code> child element.
     *
     * @param momentAbout the moment about element to set
     *
     * @throws DOMException if the element cannot be set
     */
    public void setMomentAbout(MathMLElement momentAbout)
        throws DOMException {
        setNodeByName(momentAbout, "momentabout");
    }

    /**
     * Returns the number of bound variables.
     *
     * @return the number of bound variables
     */
    public int getNBoundVariables() {
        return getBoundVariablesGetLength();
    }

    /**
     * Returns the bound variable at the specified index.
     *
     * @param index the index of the bound variable (1-based)
     *
     * @return the bound variable
     */
    public MathMLBvarElement getBoundVariable(int index) {
        Node bvar = getBoundVariablesItem(index - 1);

        return (MathMLBvarElement) bvar;
    }

    /**
     * Sets the bound variable at the specified index.
     *
     * @param newBvar the new bound variable to set
     * @param index   the index of the bound variable (1-based)
     *
     * @return the new bound variable
     *
     * @throws DOMException if the bound variable cannot be set
     */
    public MathMLBvarElement setBoundVariable(MathMLBvarElement newBvar,
        int index) throws DOMException {
        return (MathMLBvarElement) replaceChild(newBvar,
            getBoundVariablesItem(index - 1));
    }

    /**
     * Inserts a new bound variable at the specified index.
     *
     * @param newBvar the new bound variable to insert
     * @param index   the index where to insert (0 for start)
     *
     * @return the new bound variable
     *
     * @throws DOMException if the bound variable cannot be inserted
     */
    public MathMLBvarElement insertBoundVariable(MathMLBvarElement newBvar,
        int index) throws DOMException {
        if (index == 0) {
            return (MathMLBvarElement) appendChild(newBvar);
        } else {
            return (MathMLBvarElement) insertBefore(newBvar,
                getBoundVariablesItem(index - 1));
        }
    }

    /**
     * Removes the bound variable at the specified index.
     *
     * @param index the index of the bound variable to remove (1-based)
     *
     * @return the removed bound variable
     */
    public MathMLBvarElement removeBoundVariable(int index) {
        Node bvar = getBoundVariablesItem(index - 1);

        return (MathMLBvarElement) removeChild(bvar);
    }

    /**
     * Deletes the bound variable at the specified index.
     *
     * @param index the index of the bound variable to delete (1-based)
     */
    public void deleteBoundVariable(int index) {
        removeBoundVariable(index);
    }

    /**
     * Returns the number of arguments.
     *
     * @return the number of arguments
     */
    public int getNArguments() {
        return getArgumentsGetLength();
    }

    /**
     * Returns a list of arguments.
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
     * Returns a list of declarations.
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
     * Returns the argument at the specified index.
     *
     * @param index the index of the argument to retrieve (1-based)
     *
     * @return the argument
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
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
     * Sets the argument at the specified index.
     *
     * @param newArgument the new argument to set
     * @param index       the index where to set (1-based)
     *
     * @return the new argument
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
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
     * Inserts a new argument at the specified index.
     *
     * @param newArgument the new argument to insert
     * @param index       the index where to insert (0 for end)
     *
     * @return the inserted argument
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
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
     * Removes the argument at the specified index.
     *
     * @param index the index of the argument to remove (1-based)
     *
     * @return the removed argument
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
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
     * Deletes the argument at the specified index.
     *
     * @param index the index of the argument to delete (1-based)
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
     */
    public void deleteArgument(int index) throws DOMException {
        removeArgument(index);
    }

    /**
     * Returns the declaration at the specified index.
     *
     * @param index the index of the declaration (1-based)
     *
     * @return the declaration element
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
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
     * Sets the declaration at the specified index.
     *
     * @param newDeclaration the new declaration to set
     * @param index          the index where to set (1-based)
     *
     * @return the new declaration
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
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
     * Inserts a new declaration at the specified index.
     *
     * @param newDeclaration the new declaration to insert
     * @param index          the index where to insert (0 for end)
     *
     * @return the inserted declaration
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
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
     * Removes the declaration at the specified index.
     *
     * @param index the index of the declaration to remove (1-based)
     *
     * @return the removed declaration
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
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
     * Deletes the declaration at the specified index.
     *
     * @param index the index of the declaration to delete (1-based)
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
     */
    public void deleteDeclaration(int index) throws DOMException {
        removeDeclaration(index);
    }

    /**
     * Returns the number of bound variable elements.
     *
     * @return the count of bound variables
     */
    private int getBoundVariablesGetLength() {
        final int length = getLength();
        int numBvars = 0;

        for (int i = 0; i < length; i++) {
            if (item(i) instanceof MathMLBvarElement) {
                numBvars++;
            }
        }

        return numBvars;
    }

    /**
     * Returns the bound variable at the specified index.
     *
     * @param index the 0-based index
     *
     * @return the bound variable node, or null if out of bounds
     */
    private Node getBoundVariablesItem(int index) {
        final int bvarLength = getDeclarationsGetLength();

        if ((index < 0) || (index >= bvarLength)) {
            return null;
        }

        Node node = null;
        int n = -1;

        for (int i = 0; n < index; i++) {
            node = item(i);

            if (node instanceof MathMLBvarElement) {
                n++;
            }
        }

        return node;
    }

    /**
     * Returns the number of argument elements (non-declare nodes).
     *
     * @return the count of arguments
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
     * Returns the argument at the specified index.
     *
     * @param index the 0-based index
     *
     * @return the argument node, or null if out of bounds
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
     * Returns the number of declaration elements.
     *
     * @return the count of declarations
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
     * Returns the declaration at the specified index.
     *
     * @param index the 0-based index
     *
     * @return the declaration node, or null if out of bounds
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

    /**
     * Finds a child node by its local name.
     *
     * @param name the local name to search for
     *
     * @return the matching node, or null if not found
     */
    protected Node getNodeByName(String name) {
        final int length = getLength();
        Node node;

        for (int i = 0; i < length; i++) {
            node = item(i);

            String localName = node.getLocalName();

            if ((localName != null) && localName.equals(name)) {
                return node;
            }
        }

        return null;
    }

    /**
     * Replaces the child node with the given local name.
     *
     * @param newNode the new node to set
     * @param name    the local name of the node to replace
     */
    protected void setNodeByName(Node newNode, String name) {
        final int length = getLength();
        Node node;

        for (int i = 0; i < length; i++) {
            node = item(i);

            String localName = node.getLocalName();

            if ((localName != null) && localName.equals(name)) {
                replaceChild(newNode, node);

                return;
            }
        }
    }
}

