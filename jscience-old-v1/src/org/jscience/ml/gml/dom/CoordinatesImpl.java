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
 * ---------------- amilanovic         6-Jun-2001    Enhanced the default
 * coordinate tuple delimitor to include space, tab, newline and carriage
 * return. amilanovic         4-Sep-2001    Made Coordinates an
 * UnknownConstructOwner. because it is possible that somebody adds child
 * elements. Fixed bug #430473 so now this GML construct is preserving the
 * information about the ordering of its child elements. amilanovic
 * 29-Mar-2002   Updated for the new package name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.infoset.*;
import org.jscience.ml.gml.util.CoordinateTupleIterator;
import org.jscience.ml.gml.util.GMLConstructIterator;
import org.jscience.ml.gml.util.UnknownConstructIterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.StringTokenizer;
import java.util.Vector;


/**
 * A DOM-based implementation of the Coordinates interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class CoordinatesImpl extends GMLConstructImpl implements Coordinates {
    // note that these defaults should be customizable
    // !!! this is currently not supported
    /** DOCUMENT ME! */
    public static final String DEFAULT_COORDINATE_TUPLE_DELIMITOR = " \t\n\r";

    /** DOCUMENT ME! */
    public static final String DEFAULT_COORDINATE_DELIMITOR = ",";

    // contains all coordinate tuples
    /** DOCUMENT ME! */
    private Vector coordinateTuples_;

/**
     * Initializes this coordinates construct and all GML constructs directly
     * owned by it.
     *
     * @param owner      The owner of this coordinates construct.
     * @param domElement The corresponding DOM element.
     * @param document   DOCUMENT ME!
     */
    public CoordinatesImpl(Geometry owner, Element domElement,
        GMLDocument document) {
        super(owner, domElement, document);
    }

    // Coordinates interface implementation
    /**
     * Provides access to coordinate tuples in this coordinates'
     * construct.
     *
     * @return Returns an iterator to coordinate tuples. Cannot be null.
     */
    public CoordinateTupleIterator getCoordinateTupleIterator() {
        CoordinateTupleIterator coordinateTupleIterator = new CoordinateTupleIteratorImpl(coordinateTuples_.iterator());

        return coordinateTupleIterator;
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

    // redefined methods
    /**
     * Returns a one-line string representation of this GML construct.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String result = "coordinates " + getAttributeLine() + ": ";
        CoordinateTupleIterator coordinateTupleIterator = getCoordinateTupleIterator();

        while (coordinateTupleIterator.hasNext()) {
            CoordinateTuple coordinateTuple = coordinateTupleIterator.nextCoordinateTuple();
            result += (coordinateTuple.toString() + ' ');
        }

        return result;
    }

    /**
     * Refreshes the internal cache of owned GML constructs.
     */
    protected void refreshInternals() {
        // first refresh the general GML construct internals
        super.refreshInternals();

        // purge the cache
        coordinateTuples_ = new Vector();

        // rebuild the cache
        Node childNode = getDOMElement().getFirstChild();

        if (childNode == null) {
            // empty coordinates element is allowed for some reason
            return;
        }

        String coordinateTuples = childNode.getNodeValue();
        StringTokenizer coordTupleTokenizer = new StringTokenizer(coordinateTuples,
                DEFAULT_COORDINATE_TUPLE_DELIMITOR);

        while (coordTupleTokenizer.hasMoreTokens()) {
            // process a coordinate tuple
            String coordinates = coordTupleTokenizer.nextToken();
            StringTokenizer coordTokenizer = new StringTokenizer(coordinates,
                    DEFAULT_COORDINATE_DELIMITOR);
            Vector coordinateVector = new Vector();

            while (coordTokenizer.hasMoreTokens()) {
                // process a single coordinate from the tuple
                String coordinateStr = coordTokenizer.nextToken();
                Coordinate coordinate = new CoordinateImpl(coordinateStr);
                coordinateVector.addElement(coordinate);
            }

            Coordinate[] coordinateInThisTuple = new Coordinate[coordinateVector.size()];

            for (int ii = 0; ii < coordinateVector.size(); ii++) {
                coordinateInThisTuple[ii] = (Coordinate) coordinateVector.elementAt(ii);
            }

            CoordinateTuple coordinateTuple = new CoordinateTupleImpl(coordinateInThisTuple);
            coordinateTuples_.addElement(coordinateTuple);
        }
    }
}
