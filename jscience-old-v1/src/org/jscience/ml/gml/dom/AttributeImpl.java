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

import org.jscience.ml.gml.infoset.Attribute;
import org.jscience.ml.gml.infoset.GMLConstruct;
import org.jscience.ml.gml.infoset.XMLDescriptor;

import org.w3c.dom.Attr;


/**
 * Encapsulates an XML attribute. Attributes are treated as string values.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class AttributeImpl implements Attribute {
    // the GML construct (corresponding to an XML element) that owns this attr
    /** DOCUMENT ME! */
    private GMLConstruct owner_;

    /** DOCUMENT ME! */
    private Attr domAttr_;

    /** DOCUMENT ME! */
    private XMLDescriptor xmlDescriptor_;

/**
     *
     */
    public AttributeImpl(GMLConstruct owner, Attr domAttr) {
        owner_ = owner;
        domAttr_ = domAttr;
        xmlDescriptor_ = new XMLDescriptorImpl(domAttr);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XMLDescriptor getXMLDescriptor() {
        return xmlDescriptor_;
    }

    /**
     * Returns the GML construct that owns this XML attribute.
     *
     * @return DOCUMENT ME!
     */
    public GMLConstruct getOwner() {
        return owner_;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attr getDOMAttr() {
        return domAttr_;
    }

    /**
     * Returns the value of this attribute.
     *
     * @return DOCUMENT ME!
     */
    public String getValue() {
        return domAttr_.getFirstChild().getNodeValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String prefix = getXMLDescriptor().getPrefix();
        String result = null;

        if (prefix == null) {
            result = '@' + getXMLDescriptor().getLocalName() + "=\"" +
                getValue() + "\"";
        } else {
            result = '@' + prefix + ':' + getXMLDescriptor().getLocalName() +
                "=\"" + getValue() + "\"";
        }

        return result;
    }
}
