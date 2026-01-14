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

package org.jscience.ml.mathml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.*;


/**
 * Implements a MathML content container.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLContentContainerImpl extends MathMLElementImpl
    implements MathMLContentContainer {
/**
     * Constructs a MathML content container.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLContentContainerImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLConditionElement getCondition() {
        return (MathMLConditionElement) getNodeByName("condition");
    }

    /**
     * DOCUMENT ME!
     *
     * @param condition DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setCondition(MathMLConditionElement condition)
        throws DOMException {
        setNodeByName(condition, "condition");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getOpDegree() {
        return (MathMLElement) getNodeByName("degree");
    }

    /**
     * DOCUMENT ME!
     *
     * @param opDegree DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setOpDegree(MathMLElement opDegree) throws DOMException {
        setNodeByName(opDegree, "degree");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getDomainOfApplication() {
        return (MathMLElement) getNodeByName("domainofapplication");
    }

    /**
     * DOCUMENT ME!
     *
     * @param domainOfApplication DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setDomainOfApplication(MathMLElement domainOfApplication)
        throws DOMException {
        setNodeByName(domainOfApplication, "domainofapplication");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getMomentAbout() {
        return (MathMLElement) getNodeByName("momentabout");
    }

    /**
     * DOCUMENT ME!
     *
     * @param momentAbout DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setMomentAbout(MathMLElement momentAbout)
        throws DOMException {
        setNodeByName(momentAbout, "momentabout");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNBoundVariables() {
        return getBoundVariablesGetLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLBvarElement getBoundVariable(int index) {
        Node bvar = getBoundVariablesItem(index - 1);

        return (MathMLBvarElement) bvar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newBvar DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLBvarElement setBoundVariable(MathMLBvarElement newBvar,
        int index) throws DOMException {
        final int bvarsLength = getBoundVariablesGetLength();

        return (MathMLBvarElement) replaceChild(newBvar,
            getBoundVariablesItem(index - 1));
    }

    /**
     * DOCUMENT ME!
     *
     * @param newBvar DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLBvarElement insertBoundVariable(MathMLBvarElement newBvar,
        int index) throws DOMException {
        final int bvarsLength = getBoundVariablesGetLength();

        if (index == 0) {
            return (MathMLBvarElement) appendChild(newBvar);
        } else {
            return (MathMLBvarElement) insertBefore(newBvar,
                getBoundVariablesItem(index - 1));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLBvarElement removeBoundVariable(int index) {
        Node bvar = getBoundVariablesItem(index - 1);

        return (MathMLBvarElement) removeChild(bvar);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void deleteBoundVariable(int index) {
        removeBoundVariable(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNArguments() {
        return getArgumentsGetLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param newArgument DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param newArgument DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void deleteArgument(int index) throws DOMException {
        removeArgument(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param newDeclaration DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param newDeclaration DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void deleteDeclaration(int index) throws DOMException {
        removeDeclaration(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param newNode DOCUMENT ME!
     * @param name DOCUMENT ME!
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
