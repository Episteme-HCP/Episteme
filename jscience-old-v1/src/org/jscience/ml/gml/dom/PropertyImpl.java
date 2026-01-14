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
 * ---------------- amilanovic         4-Sep-2001    Fixed bug #430473 so now
 * this GML construct is preserving the information about the ordering of its
 * child elements. amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.infoset.*;
import org.jscience.ml.gml.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.StringTokenizer;
import java.util.Vector;


/**
 * A DOM-based implementation of the Property interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class PropertyImpl extends GMLConstructImpl implements Property {
/**
     * Initializes this property and all GML constructs directly owned by it.
     *
     * @param owner      The owner of this property.
     * @param domElement The corresponding DOM element.
     * @param document   DOCUMENT ME!
     */
    public PropertyImpl(PropertyOwner owner, Element domElement,
        GMLDocument document) {
        super(owner, domElement, document);
    }

    // Property interface implementation.
    /**
     * Returns the String value of this property. This is to be used
     * for "simple" properties.
     *
     * @return Null if the property contains no String value or if it is null.
     */
    public String getValueAsString() {
        Node firstChild = getDOMElement().getFirstChild();

        return (firstChild != null) ? firstChild.getNodeValue() : null;
    }

    // FeatureOwner interface implementation.
    /**
     * Provides access to features owned by this FeatureOwner.
     *
     * @return FeatureIterator that can be used for iterating on features.
     */
    public FeatureIterator getFeatureIterator() {
        GMLConstructIterator gmlConstructIterator = getGMLConstructIterator();
        Vector features = new Vector();

        while (gmlConstructIterator.hasNext()) {
            GMLConstruct gmlConstruct = (GMLConstruct) gmlConstructIterator.next();

            if (gmlConstruct instanceof Feature) {
                features.addElement(gmlConstruct);
            }
        }

        FeatureIterator featureterator = new FeatureIteratorImpl(features.iterator());

        return featureterator;
    }

    // GeometryOwner interface implementation.
    /**
     * Provides access to geometries owned by this GeometryOwner.
     *
     * @return GeometryIterator that can be used for iterating on geometries.
     */
    public GeometryIterator getGeometryIterator() {
        GMLConstructIterator gmlConstructIterator = getGMLConstructIterator();
        Vector geometries = new Vector();

        while (gmlConstructIterator.hasNext()) {
            GMLConstruct gmlConstruct = (GMLConstruct) gmlConstructIterator.next();

            if (gmlConstruct instanceof Geometry) {
                geometries.addElement(gmlConstruct);
            }
        }

        GeometryIterator geometryIterator = new GeometryIteratorImpl(geometries.iterator());

        return geometryIterator;
    }

    // PropertyOwner interface implementation
    /**
     * Returns an iterator to all properties of this feature.
     *
     * @return DOCUMENT ME!
     */
    public PropertyIterator getPropertyIterator() {
        GMLConstructIterator gmlConstructIterator = getGMLConstructIterator();
        Vector properties = new Vector();

        while (gmlConstructIterator.hasNext()) {
            GMLConstruct gmlConstruct = (GMLConstruct) gmlConstructIterator.next();

            if (gmlConstruct instanceof Property) {
                properties.addElement(gmlConstruct);
            }
        }

        PropertyIterator propertyIterator = new PropertyIteratorImpl(properties.iterator());

        return propertyIterator;
    }

    // UnknownConstructOwner interface implementation
    /**
     * Returns an iterator to all unknown constructs of this property.
     *
     * @return DOCUMENT ME!
     */
    public UnknownConstructIterator getUnknownConstructIterator() {
        GMLConstructIterator gmlConstructIterator = getGMLConstructIterator();
        Vector unknownConstructs = new Vector();

        while (gmlConstructIterator.hasNext()) {
            GMLConstruct gmlConstruct = (GMLConstruct) gmlConstructIterator.next();

            if (gmlConstruct instanceof UnknownConstruct) {
                unknownConstructs.addElement(gmlConstruct);
            }
        }

        UnknownConstructIterator unknownConstructIterator = new UnknownConstructIteratorImpl(unknownConstructs.iterator());

        return unknownConstructIterator;
    }

    // redefined methods
    /**
     * Returns a one-line string representation of this property.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        // try to see if the string value consists of blanks only
        // we don't want to show blanks
        String stringValue = getValueAsString();

        if (stringValue != null) {
            StringTokenizer tokenizer = new StringTokenizer(stringValue, " \t\n");

            if (!tokenizer.hasMoreTokens()) {
                stringValue = null;
            }
        }

        String result = "Property " + getXMLDescriptor().getLocalName();
        result += (' ' + getAttributeLine() + ' ');

        if (stringValue != null) {
            result += ('\"' + getValueAsString() + '\"');
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasProperties() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasGeometries() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasFeatures() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasUnknownConstructs() {
        return true;
    }

    /**
     * Refreshes the internal data cache from the DOM source tree. This
     * method should be called each time the underlying DOM structure has
     * changed.
     */
    protected void refreshInternals() {
        // first refresh the general GML construct internals
        super.refreshInternals();

        // do nothing extra
    }
}
