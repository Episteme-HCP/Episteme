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

package org.jscience.ml.sbml.math;

import org.xml.sax.Attributes;

import java.util.ArrayList;


/**
 * Represents a node in a parse tree based on XML (in particular MathML).
 * Node is publicly a non-mutable class.  Therefore, it is safe to give many
 * Objects a references to the same Node. This code is licensed under the
 * DARPA BioCOMP Open Source License.  See LICENSE for more details.
 *
 * @author Marc Vass
 */
public class Node {
    /** DOCUMENT ME! */
    protected ArrayList children;

    /** DOCUMENT ME! */
    protected Attributes attributes;

    /** DOCUMENT ME! */
    protected Node parent;

    /** DOCUMENT ME! */
    protected String localName;

    /** DOCUMENT ME! */
    protected String qName;

    /** DOCUMENT ME! */
    protected String uri;

    /** DOCUMENT ME! */
    protected StringBuffer value;

/**
     * Creates a new Node object.
     *
     * @param oldNode DOCUMENT ME!
     */
    public Node(Node oldNode) {
        attributes = oldNode.attributes;
        children = new ArrayList(oldNode.children);
        localName = oldNode.localName;
        parent = oldNode.parent;
        qName = oldNode.qName;
        uri = oldNode.uri;
        value = new StringBuffer(oldNode.value.toString());
    }

/**
     * Creates a new Node object.
     *
     * @param parent     DOCUMENT ME!
     * @param uri        DOCUMENT ME!
     * @param localName  DOCUMENT ME!
     * @param qName      DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     */
    public Node(Node parent, String uri, String localName, String qName,
        Attributes attributes) {
        this.parent = parent;
        this.attributes = attributes;
        this.localName = localName;
        this.qName = qName;
        this.uri = uri;
        value = new StringBuffer();
        children = new ArrayList();
    }

/**
     * Creates a new Node object.
     *
     * @param uri        DOCUMENT ME!
     * @param localName  DOCUMENT ME!
     * @param qName      DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     */
    public Node(String uri, String localName, String qName,
        Attributes attributes) {
        this(null, uri, localName, qName, attributes);
    }

/**
     * Creates a new Node object.
     *
     * @param qName    DOCUMENT ME!
     * @param value    DOCUMENT ME!
     * @param children DOCUMENT ME!
     */
    public Node(String qName, String value, Node[] children) {
        this(null, null, null, qName, null);
        this.value = new StringBuffer(value);

        for (int i = 0; i < children.length; i++)
            addChild(children[i]);
    }

/**
     * Creates a new Node object.
     *
     * @param qName  DOCUMENT ME!
     * @param value  DOCUMENT ME!
     * @param child1 DOCUMENT ME!
     * @param child2 DOCUMENT ME!
     */
    public Node(String qName, String value, Node child1, Node child2) {
        this(null, null, null, qName, null);
        this.value = new StringBuffer(value);
        addChild(child1);
        addChild(child2);
    }

/**
     * Creates a new Node object.
     *
     * @param qName DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param child DOCUMENT ME!
     */
    public Node(String qName, String value, Node child) {
        this(null, null, null, qName, null);
        this.value = new StringBuffer(value);
        addChild(child);
    }

/**
     * Creates a new Node object.
     *
     * @param qName DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public Node(String qName, String value) {
        this(null, null, null, qName, null);
        this.value = new StringBuffer(value);
    }

/**
     * Creates a new Node object.
     */
    public Node() {
        this(null, "", "", "", null);
    }

    // Getters follow
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getChild(int index) {
        return (Node) children.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node[] getChildren() {
        return (Node[]) children.toArray(new Node[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumChildren() {
        return children.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getParent() {
        return parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getQName() {
        return qName;
    }

    // Returns the qName with out a prepended "math:"
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSimpleName() {
        if (qName.startsWith("math:")) {
            return qName.substring(5);
        }

        return qName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUri() {
        return uri;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getValue() {
        return value.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    protected void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    protected void appendToValue(String s) {
        value.append(s);
    }

    // End Getters
    /**
     * DOCUMENT ME!
     */
    protected void removeAllChildren() {
        children = new ArrayList();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Node removeChild(int index) {
        return (Node) children.remove(index);
    }

    // Setters follow
    /**
     * DOCUMENT ME!
     *
     * @param attributes DOCUMENT ME!
     */
    protected void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param localName DOCUMENT ME!
     */
    protected void setLocalName(String localName) {
        this.localName = localName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    protected void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param qName DOCUMENT ME!
     */
    protected void setQName(String qName) {
        this.qName = qName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param uri DOCUMENT ME!
     */
    protected void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    protected void setValue(String value) {
        this.value = new StringBuffer(value);
    }

    // End Setters
}
