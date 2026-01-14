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
 * ---------------- amilanovic         29-Mar-2002   Tailored from SchemaTree.
 */
package org.jscience.ml.gml.xml.schema;

import java.util.Hashtable;
import java.util.Vector;


/**
 * Represents a graph of schema data objects used by SchemaParser.
 *
 * @author Aleksandar Milanovic
 * @version 1.2
 */
public class SchemaGraph {
    /** DOCUMENT ME! */
    private SchemaNode _root = null;

/**
     * Constructor.
     */
    public SchemaGraph() {
    }

    /**
     * Purges the graph.
     */
    public void clean() {
        _root = new SchemaNode(XMLSchema.XML_SCHEMA_ELEMENT_TYPE,
                QName.getQName(XMLSchema.XML_SCHEMA_NAMESPACE, "schema"),
                QName.getQName(XMLSchema.XML_NAMESPACE, "XMLSchema"));
    }

    /*-------------------------------------------------------------
    *
    *
    *-------------------------------------------------------------*/
    public SchemaNode getRoot() {
        return _root;
    }

    /**
     * Sets the root of the schema graph.
     *
     * @param name DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SchemaNode setRoot(QName name, QName type) {
        _root = new SchemaNode(XMLSchema.XML_SCHEMA_ELEMENT_TYPE, name, type);

        return _root;
    }

    /*-------------------------------------------------------------
    *
    *
    *-------------------------------------------------------------*/
    public SchemaNode addNode(SchemaNode parent, SchemaNode child) {
        parent.add(child);

        //System.out.println(child.toString());
        return child;
    }

    /**
     * DOCUMENT ME!
     *
     * @param construct DOCUMENT ME!
     * @param elemQName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SchemaNode getGlobalConstruct(String construct, QName elemQName) {
        for (int index = 0; index < _root.getChildCount(); index++) {
            SchemaNode element = (SchemaNode) _root.getChildAt(index);

            if (element.getName().equals(elemQName) &&
                    construct.equals(element.getXmlSchemaType())) {
                return element;
            }
        }

        return null;
    }

    /*-------------------------------------------------------------
    *
    *
    *-------------------------------------------------------------*/
    public void addSubstGroups(SchemaNode parent, Hashtable substTable) {
        int length = parent.getChildCount();

        for (int index = 0; index < length; index++) {
            SchemaNode child = (SchemaNode) parent.getChildAt(index);
            addSubstGroups(child, substTable);

            QName name = child.getName();
            Hashtable group = (Hashtable) substTable.get(name);

            if (group == null) {
                continue;
            }

            Vector nodes = new Vector(group.values());

            for (int jndex = 0; jndex < nodes.size(); jndex++) {
                SchemaNode newChild = (SchemaNode) nodes.get(jndex);
                parent.add(newChild);

                //parent.add(cloneRecursive(newChild));
            }
        }
    }

    /*-------------------------------------------------------------
    *
    *
    *-------------------------------------------------------------*/
    public boolean isOfType(QName elementName, QName typeName) {
        SchemaNode item = findNode(_root, elementName);

        if (item == null) {
            return false;
        }

        return item.isSuperTypeOrThisType(typeName);
    }

    /*-------------------------------------------------------------
    *
    *
    *-------------------------------------------------------------*/
    public SchemaNode findNode(SchemaNode parent, QName elementName) {
        for (int index = 0; index < parent.getChildCount(); index++) {
            SchemaNode child = (SchemaNode) parent.getChildAt(index);
            QName childName = child.getName();

            if (elementName.equals(childName)) {
                return child;
            }

            // for references the following is false
            if (child.getParent() == parent) {
                // for non-references recurse farther
                findNode(child, elementName);
            }
        }

        return null;
    }

    /*-------------------------------------------------------------
    *
    *
    *-------------------------------------------------------------*/
    public boolean isChildOf(QName parentName, QName childName) {
        SchemaNode parent = findNode(_root, parentName);

        if (parent == null) {
            return false;
        }

        for (int index = 0; index < parent.getChildCount(); index++) {
            SchemaNode item = (SchemaNode) parent.getChildAt(index);
            QName itemName = item.getName();

            if (childName.equals(itemName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     */
    public void dump() {
        if (_root != null) {
            dump(_root, 0, 2);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param indentLevel DOCUMENT ME!
     * @param indentSize DOCUMENT ME!
     */
    public void dump(SchemaNode node, int indentLevel, int indentSize) {
        int spacesToIndent = indentSize * indentLevel;
        StringBuffer strBuffer = new StringBuffer((spacesToIndent * 2) + 160);

        for (int ii = 0; ii < spacesToIndent; ii++) {
            strBuffer.append(' ');
        }

        strBuffer.append(node.toString());
        strBuffer.append('\n');

        Vector typeChain = node.getTypeChain();

        for (int ii = 0; ii < spacesToIndent; ii++) {
            strBuffer.append(' ');
        }

        strBuffer.append("Type chain = ");

        for (int ii = 0; ii < typeChain.size(); ii++) {
            QName superType = (QName) typeChain.elementAt(ii);
            strBuffer.append(superType.getLocalName() + ' ');
        }

        System.out.println(strBuffer.toString());

        if (!node.isReference()) {
            int childCount = node.getChildCount();

            for (int ii = 0; ii < childCount; ii++) {
                SchemaNode childNode = node.getChildAt(ii);
                dump(childNode, indentLevel + 1, indentSize);
            }
        }
    }
}
