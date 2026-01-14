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
 * ---------------- amilanovic         4-Sep-2001    Made Coord a
 * PropertyOwner, hence X,Y,Z will be considered as such. Fixed bug #430473 so
 * now this GML construct is preserving the information about the ordering of
 * its child elements. amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.infoset.*;
import org.jscience.ml.gml.util.GMLConstructIterator;
import org.jscience.ml.gml.util.PropertyIterator;
import org.jscience.ml.gml.util.UnknownConstructIterator;

import org.w3c.dom.Element;

import java.util.Vector;


/**
 * A DOM-based implementation of the Coord interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class CoordImpl extends GMLConstructImpl implements Coord {
    // the tuple contained in this coord
    // if Java allowed multiple inheritance this class would be inherited from
    /** DOCUMENT ME! */
    private CoordinateTupleImpl tuple_;

    // the geometry owner of this coord
    /** DOCUMENT ME! */
    private Geometry owner_;

    // the associated DOM element
    /** DOCUMENT ME! */
    private Element domElement_;

    // describes the XML nature of the GML coord
    /** DOCUMENT ME! */
    private XMLDescriptor xmlDescriptor_;

/**
     * Initializes this coord construct and all GML constructs directly owned
     * by it.
     *
     * @param owner      The owner of this coord.
     * @param domElement The corresponding DOM element.
     * @param document   DOCUMENT ME!
     */
    public CoordImpl(Geometry owner, Element domElement, GMLDocument document) {
        super(owner, domElement, document);
    }

    // UnknownConstructOwner interface implementation
    /**
     * Returns an iterator to all unknown constructs of this feature.
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

    // Coord interface implementation
    /**
     * Convenience method to retrieve the first coordinate.
     *
     * @return Cannot be null.
     */
    public Coordinate getX() {
        return tuple_.getX();
    }

    /**
     * Convenience method to retrieve the second coordinate.
     *
     * @return Can be null if Y-coordinate is not present.
     */
    public Coordinate getY() {
        return tuple_.getY();
    }

    /**
     * Convenience method to retrieve the third coordinate.
     *
     * @return Can be null if Z-coordinate is not present.
     */
    public Coordinate getZ() {
        return tuple_.getZ();
    }

    /**
     * Returns the n-th coordinate of this coordinate tuple.
     *
     * @param index Represents an index into the coordinate tuple. The lowest
     *        index is 0.
     *
     * @return Returns null if the index is out of range.
     */
    public Coordinate getCoordinate(int index) {
        return tuple_.getCoordinate(index);
    }

    // redefined methods
    /**
     * Returns a one-line description of this coord.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String result = "coord " + getAttributeLine() + ": X = " +
            getX().getValue().toString();

        if (getY() != null) {
            result += ("  Y = " + getY().getValue().toString());
        }

        if (getZ() != null) {
            result += ("  Z = " + getZ().getValue().toString());
        }

        return result;
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasProperties() {
        return true;
    }

    /**
     * Refreshes the internal cache of owned GML constructs. This
     * method is called also upon the construction of the object.
     */
    protected void refreshInternals() {
        // first refresh the general GML construct internals
        super.refreshInternals();

        Coordinate[] coordinates = new Coordinate[3];

        PropertyIterator propertyIterator = getPropertyIterator();

        while (propertyIterator.hasNext()) {
            Property coordProperty = propertyIterator.nextProperty();
            String propertyValue = coordProperty.getValueAsString();
            String coordPropertyLocalName = coordProperty.getXMLDescriptor()
                                                         .getLocalName();

            if (coordPropertyLocalName.equals(X_PROP)) {
                coordinates[X_INDEX] = new CoordinateImpl(propertyValue);
            } else if (coordPropertyLocalName.equals(Y_PROP)) {
                coordinates[Y_INDEX] = new CoordinateImpl(propertyValue);
            } else if (coordPropertyLocalName.equals(Z_PROP)) {
                coordinates[Z_INDEX] = new CoordinateImpl(propertyValue);
            } else {
                // do nothing
            }
        }

        tuple_ = new CoordinateTupleImpl(coordinates);
    }
}
