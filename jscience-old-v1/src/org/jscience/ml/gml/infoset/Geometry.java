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
package org.jscience.ml.gml.infoset;

import org.jscience.ml.gml.util.CoordinateTupleIterator;
import org.jscience.ml.gml.util.GeometryIterator;


/**
 * Defines the interface every GML geometry must implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Geometry extends GMLConstruct, PropertyOwner,
    UnknownConstructOwner // direct ownership
 {
    // known attribute names (GML v2.06)
    /** DOCUMENT ME! */
    public static final String GID_ATTR = "gid";

    /** DOCUMENT ME! */
    public static final String SRS_NAME_ATTR = "srsName";

    /**
     * Returns the name of the Spatial Reference System (SRS)
     *
     * @return Null if the SRS is unspecified.
     */
    public String getSRSName();

    /**
     * Returns the Id of this geometry, presumably specified with gid
     * attribute.
     *
     * @return Null if no Id attribute is specified.
     */
    public String getId();

    /**
     * A convenience method to gain access to geometry coordinates.
     *
     * @return Iterator to coordinate tuples. It is never null.
     */
    public CoordinateTupleIterator getCoordinateTupleIterator();

    /**
     * A convenience method to gain access to inner boundaries if
     * available.
     *
     * @return Iterator to inner boundaries. It is never null.
     */
    public GeometryIterator getInnerBoundaryIterator();
}
