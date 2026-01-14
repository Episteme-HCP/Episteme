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

/**
 * Defines the interface every feature must implement. Feature extends the
 * PropertyOwner interface because a feature may own properties.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Feature extends GMLConstruct, UnknownConstructOwner,
    PropertyOwner // it may own properties
 {
    // known attribute and property names (GML v2.06)
    /** DOCUMENT ME! */
    public static final String FID_ATTR = "fid";

    /** DOCUMENT ME! */
    public static final String NAME_PROP = "name";

    /** DOCUMENT ME! */
    public static final String DESCRIPTION_PROP = "description";

    /** DOCUMENT ME! */
    public static final String BOUNDED_BY_PROP = "boundedBy";

    /**
     * Returns the Id of this feature, presumably stored in the fid
     * attribute.
     *
     * @return Null if an Id is not specified.
     */
    public String getId();

    /**
     * Returns the value of the name property of this feature.
     *
     * @return Null if the name property is not specified.
     */
    public String getName();

    /**
     * Returns the value of the description property of this feature.
     *
     * @return Null if the description property is not specified.
     */
    public String getDescription();

    /**
     * Returns the boundedBy property of this feature. This property
     * defines the bounding box for this feature. In the future, the abstract
     * geometry will be replaced by a Box.
     *
     * @return The Geometry/Box object if specified, otherwise null.
     */
    public Geometry getBoundedBy();
}
