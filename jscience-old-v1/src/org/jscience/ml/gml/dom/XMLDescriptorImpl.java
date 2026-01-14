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
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.infoset.XMLDescriptor;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * A DOM-based implementation of the XMLDescriptor interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class XMLDescriptorImpl implements XMLDescriptor {
    // the DOM node that provides info about the GML object
    /** DOCUMENT ME! */
    private Node node_;

/**
     * Constructs the XMLDescriptor object using a DOM node.
     *
     * @param node DOCUMENT ME!
     */
    public XMLDescriptorImpl(Node node) {
        node_ = node;
    }

    /**
     * Returns the XML local name of the GML element/attribute.
     *
     * @return DOCUMENT ME!
     */
    public String getLocalName() {
        return node_.getLocalName();
    }

    /**
     * Returns the namespace of the GML element/attribute.
     *
     * @return DOCUMENT ME!
     */
    public String getNamespace() {
        return node_.getNamespaceURI();
    }

    /**
     * Returns the prefix of this XML element/attribute.
     *
     * @return DOCUMENT ME!
     */
    public String getPrefix() {
        return node_.getPrefix();
    }

    /**
     * Returns the XML type name of the GML element/attribute.
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchMethodError DOCUMENT ME!
     */
    public String getTypeName() {
        throw new NoSuchMethodError("XMLDescriptorImpl.getTypeName() is " +
            "not implemented yet.");
    }

    /**
     * Returns the namespace of the element/attribute type.
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchMethodError DOCUMENT ME!
     */
    public String getTypeNamespace() {
        throw new NoSuchMethodError("XMLDescriptorImpl.getTypeNamespace" +
            "space() is not implemented yet.");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String result = null;

        if (node_ instanceof Attr) {
            result = "Attribute ";
        } else if (node_ instanceof Element) {
            result = "Element ";
        } else // unknown node type
         {
            result = "Unknown DOM node type ";
        }

        result += (getNamespace() + ':' + getLocalName());

        return result;
    }
}
