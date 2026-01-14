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
 * Defines the interface that every GML coordinate tuple must implement. A
 * coordinate tuple does NOT have a corresponding GML element. It is an
 * abstraction of a coordinate tuple, which may appear in coord and
 * coordinates GML constructs. Coord, which is a coordinate tuple with a
 * corresponding GML element, extends this interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface CoordinateTuple {
    // defines the indices for the first three coordinates (x,y,z)
    /** DOCUMENT ME! */
    public static final int X_INDEX = 0;

    /** DOCUMENT ME! */
    public static final int Y_INDEX = 1;

    /** DOCUMENT ME! */
    public static final int Z_INDEX = 2;

    /**
     * Convenience method to retrieve the first coordinate.
     *
     * @return Cannot be null.
     */
    public Coordinate getX();

    /**
     * Convenience method to retrieve the second coordinate.
     *
     * @return Can be null if Y-coordinate is not present.
     */
    public Coordinate getY();

    /**
     * Convenience method to retrieve the third coordinate.
     *
     * @return Can be null if Z-coordinate is not present.
     */
    public Coordinate getZ();

    /**
     * Returns the n-th coordinate of this coordinate tuple.
     *
     * @param index Represents an index into the coordinate tuple. The lowest
     *        index is 0.
     *
     * @return Returns null if the index is out of range.
     */
    public Coordinate getCoordinate(int index);
}
