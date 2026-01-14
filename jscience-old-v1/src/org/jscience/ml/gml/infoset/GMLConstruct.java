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
 * Defines the interface that represents an abstract GML construct. Any element
 * that appears in a GML document is classified as a GML construct. In
 * addition to the known GML constructs such as Feature and Geometry, this
 * notion includes even "unknown" constructs.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface GMLConstruct extends GMLConstructOwner, XMLDescribable {
    /**
     * Returns the owner of this GML construct.
     *
     * @return In a valid GML structure, the owner should not be null.
     */
    public GMLConstructOwner getOwner();

    // methods for reading attributes
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAttributeCount();

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attribute getAttribute(int index);

    /**
     * DOCUMENT ME!
     *
     * @param namespace DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attribute getAttribute(String namespace, String localName);

    /**
     * DOCUMENT ME!
     *
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attribute getAttribute(String localName);
}
