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

import org.jscience.ml.gml.GMLSchema;
import org.jscience.ml.gml.infoset.*;
import org.jscience.ml.gml.util.GeometryIterator;
import org.jscience.ml.gml.util.PropertyIterator;

import org.w3c.dom.Element;

import java.util.Vector;


/**
 * A DOM-based implementation of the GeometryCollection interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class GeometryCollectionImpl extends GeometryImpl
    implements GeometryCollection {
/**
     * Initializes this geometry collection and all GML constructs directly
     * owned by it. Should be called from newGeometry() method only.
     *
     * @param owner      The owner of this geometry collection.
     * @param domElement The corresponding DOM element.
     * @param document   DOCUMENT ME!
     */
    protected GeometryCollectionImpl(GeometryOwner owner, Element domElement,
        GMLDocument document) {
        super(owner, domElement, document);
    }

    // GeometryOwner interface implementation
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GeometryIterator getGeometryIterator() {
        Vector geometries = new Vector();

        PropertyIterator propertyIterator = getPropertyIterator();

        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.nextProperty();
            XMLDescriptor xmlDescr = property.getXMLDescriptor();
            String propertyName = xmlDescr.getLocalName();
            String propertyNamespace = xmlDescr.getNamespace();

            if (GMLSchema.isGeometryMemberProperty(propertyName,
                        propertyNamespace)) {
                GeometryIterator geometryIterator = property.getGeometryIterator();

                // !!! assuming there's exactly one member geometry
                Geometry memberGeometry = geometryIterator.nextGeometry();
                geometries.addElement(memberGeometry);
            }
        }

        GeometryIterator geometryIterator = new GeometryIteratorImpl(geometries.iterator());

        return geometryIterator;
    }

    // redefined methods
    /**
     * Returns a one-line string description of this object.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String result = "Geometry collection " +
            getXMLDescriptor().getLocalName() + ' ' + getAttributeLine();

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasCoords() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasCoordinates() {
        return false;
    }

    /**
     * Refreshes the internal data cache from the DOM source tree. This
     * method should be called each time the underlying DOM structure has
     * changed.
     */
    protected void refreshInternals() {
        // refresh the internals of a general geometry
        super.refreshInternals();

        // nothing to do here
    }
}
