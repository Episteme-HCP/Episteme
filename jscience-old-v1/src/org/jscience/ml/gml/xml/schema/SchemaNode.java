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

/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Tailored from
 * SchemaTree's TreeNode & NodeInfo
 */
package org.jscience.ml.gml.xml.schema;

import java.util.Vector;


/**
 * Represents a node in a schema graph. A node is either a reference to
 * another node, or contains info about a new schema construct.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class SchemaNode implements Cloneable {
    /** DOCUMENT ME! */
    private Vector _children;

    /** DOCUMENT ME! */
    private SchemaNode _parent;

    /** DOCUMENT ME! */
    private SchemaNode _reference;

    /** DOCUMENT ME! */
    private boolean _isReference;

    /** DOCUMENT ME! */
    private QName _name;

    /** DOCUMENT ME! */
    private QName _type;

    /** DOCUMENT ME! */
    private Vector _typeChain;

    /** DOCUMENT ME! */
    private String _xmlSchemaType;

    /** DOCUMENT ME! */
    private boolean _subst; // is substitutable with anything?

    /** DOCUMENT ME! */
    private QName _substGroup;

    /** DOCUMENT ME! */
    private boolean _isAbstract;

/**
     * Constructor for a reference.
     *
     * @param schemaType DOCUMENT ME!
     * @param reference  DOCUMENT ME!
     * @throws IllegalStateException DOCUMENT ME!
     */
    public SchemaNode(String schemaType, SchemaNode reference) {
        if (!schemaType.equals(XMLSchema.REFERENCE_XML_TYPE)) {
            throw new IllegalStateException("A reference node must be of the " +
                "corresponding internal type.");
        }

        _xmlSchemaType = schemaType; // must be reference
        _reference = reference;

        if (_reference == this) {
            throw new IllegalStateException(
                "Schema error: Cannot reference itself.");
        }

        _isReference = true;
    }

/**
     * Constructor for a new XML schema construct (not a reference).
     *
     * @param schemaType DOCUMENT ME!
     * @param name       DOCUMENT ME!
     * @param type       DOCUMENT ME!
     */
    public SchemaNode(String schemaType, QName name, QName type) {
        // _isReference = false;
        _xmlSchemaType = schemaType;
        _name = name;
        _type = type;
        _children = new Vector();
        _typeChain = new Vector();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isReference() {
        return _isReference;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SchemaNode getReferencedConstruct() {
        return _reference;
    }

    /**
     * DOCUMENT ME!
     *
     * @param reference DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setReferencedConstruct(SchemaNode reference) {
        if (!isReference()) {
            throw new IllegalStateException("Cannot set the referenced " +
                "construct - this is not a reference.");
        }

        _reference = reference;
    }

    /**
     * Clones all information except the parent. The type chain is
     * copied to a new vector.
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        if (isReference()) {
            return new SchemaNode(getXmlSchemaType(), getReferencedConstruct());
        } else {
            SchemaNode clone = new SchemaNode(getXmlSchemaType(), getName(),
                    getType());
            clone.setSubst(getSubst());
            clone.setSubstGroup(getSubstGroup());
            clone.setIsAbstract(getIsAbstract());
            clone.setTypeChain(getTypeChain());

            return clone;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SchemaNode deepClone() {
        SchemaNode clonedNode = (SchemaNode) clone();

        if (!clonedNode.isReference()) {
            // copy children only for non-references
            for (int index = 0; index < getChildCount(); index++) {
                SchemaNode child = (SchemaNode) getChildAt(index);
                clonedNode.add(child.deepClone());
            }
        }

        return clonedNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getChildCount() {
        if (isReference()) {
            return _reference.getChildCount();
        }

        int count = _children.size();

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SchemaNode getChildAt(int index) {
        if (isReference()) {
            return _reference.getChildAt(index);
        }

        SchemaNode childNode = (SchemaNode) _children.elementAt(index);

        return childNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SchemaNode getParent() {
        return _parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newParent DOCUMENT ME!
     */
    public void setParent(SchemaNode newParent) {
        _parent = newParent;
    }

    /**
     * Adds a child node. If the node is a child of another node, the
     * link is dereferenced. This node becomes the parent of this new child.
     *
     * @param childNode DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void add(SchemaNode childNode) {
        if (isReference()) {
            throw new IllegalStateException(
                "Cannot add children to a reference.");
        }

        SchemaNode parent = childNode.getParent();

        if (parent != null) {
            parent.remove(childNode);
        }

        childNode.setParent(this);
        _children.addElement(childNode);
    }

    /**
     * DOCUMENT ME!
     *
     * @param childNode DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void remove(SchemaNode childNode) {
        if (isReference()) {
            throw new IllegalStateException(
                "Cannot remove children from a reference.");
        }

        _children.remove(childNode);
        childNode.setParent(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXmlSchemaType() {
        return _xmlSchemaType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param subst DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setSubst(boolean subst) {
        if (isReference()) {
            throw new IllegalStateException("Cannot set the subst flag of a " +
                "reference");
        }

        _subst = subst;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getSubst() {
        if (isReference()) {
            return _reference.getSubst();
        }

        return _subst;
    }

    /**
     * DOCUMENT ME!
     *
     * @param isAbstract DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setIsAbstract(boolean isAbstract) {
        if (isReference()) {
            throw new IllegalStateException(
                "Cannot set the abstract flag of a " + "reference");
        }

        _isAbstract = isAbstract;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getIsAbstract() {
        if (isReference()) {
            return _reference.getIsAbstract();
        }

        return _isAbstract;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setType(QName type) {
        if (isReference()) {
            throw new IllegalStateException("Cannot set the type of a " +
                "reference");
        }

        _type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public QName getType() {
        if (isReference()) {
            return _reference.getType();
        }

        return _type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void addToTypeChain(QName type) {
        if (isReference()) {
            throw new IllegalStateException("Cannot add to the type chain info" +
                " of a reference");
        }

        _typeChain.add(type);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getTypeChain() {
        if (isReference()) {
            return _reference.getTypeChain();
        }

        return _typeChain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param typeChain DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setTypeChain(Vector typeChain) {
        if (isReference()) {
            throw new IllegalStateException("Cannot set the type chain info" +
                " for a reference");
        }

        _typeChain.removeAllElements();
        _typeChain.addAll(typeChain);
    }

    /**
     * Determines if the given type is a super type or this type.
     *
     * @param otherType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSuperTypeOrThisType(QName otherType) {
        if (_type == null) {
            return false;
        }

        if (_type.equals(otherType)) {
            return true;
        }

        for (int index = 0; index < _typeChain.size(); index++) {
            QName currentTypeInfo = (QName) _typeChain.get(index);

            if (currentTypeInfo.equals(otherType)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setName(QName name) {
        if (isReference()) {
            throw new IllegalStateException("Cannot set the name" +
                " of a reference");
        }

        _name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public QName getName() {
        if (isReference()) {
            return _reference.getName();
        }

        return _name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param substGroup DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setSubstGroup(QName substGroup) {
        if (isReference()) {
            throw new IllegalStateException("Cannot set the substitution group" +
                " of a reference");
        }

        _substGroup = substGroup;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public QName getSubstGroup() {
        if (isReference()) {
            return _reference.getSubstGroup();
        }

        return _substGroup;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        QName nodeName = getName();
        QName nodeType = getType();
        String nameNsUri = (nodeName == null) ? null : nodeName.getNsUri();
        String localName = (nodeName == null) ? null : nodeName.getLocalName();
        String typeNsUri = (nodeType == null) ? null : nodeType.getNsUri();
        String typeLocalName = (nodeType == null) ? null : nodeType.getLocalName();
        String result = getXmlSchemaType();

        if (isReference()) {
            result = "REF ";
        }

        result = "[" + result + "]" + nameNsUri + " : " + localName +
            " of type " + typeNsUri + " : " + typeLocalName;

        return result;
    }
}
