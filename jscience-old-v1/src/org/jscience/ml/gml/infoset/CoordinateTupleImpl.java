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
 * Convenience class that implements the CoordinateTuple interface. This
 * class can either be used directly or be extended to add custom
 * functionality. A coordinate tuple must have at least the X coordinate.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class CoordinateTupleImpl implements CoordinateTuple {
    // stores the coordinates
    // the array size is adjusted to the number of coordinates
    /** DOCUMENT ME! */
    private Coordinate[] coordinates_;

/**
     * Creates a "dummy" CoordinateTuple object. This constructor is meant to
     * be used only by subclasses.
     */
    protected CoordinateTupleImpl() {
        coordinates_ = new Coordinate[1];
        coordinates_[X_INDEX] = new CoordinateImpl("0");
    }

/**
     * Initializes this coordinate tuple using the given coordinates. The
     * content of the array is copied.
     *
     * @param coordinates DOCUMENT ME!
     */
    public CoordinateTupleImpl(Coordinate[] coordinates) {
        initialize(coordinates);
    }

    /**
     * Initializes this coordinate tuple. The array is trimmed from the
     * first unoccupied slot.
     *
     * @param coordinates The source array. Array members are copied into a new
     *        array, but they are not cloned.
     */
    protected void initialize(Coordinate[] coordinates) {
        // calculate the size of the array (not its length)
        int size = 0;

        while ((size < coordinates.length) && (coordinates[size] != null)) {
            size++;
        }

        coordinates_ = new Coordinate[size];
        System.arraycopy(coordinates, 0, coordinates_, 0, size);
    }

    // CoordinateTuple interface implementation
    /**
     * Convenience method to retrieve the first coordinate.
     *
     * @return DOCUMENT ME!
     */
    public Coordinate getX() {
        return getCoordinate(X_INDEX);
    }

    /**
     * Convenience method to retrieve the second coordinate.
     *
     * @return DOCUMENT ME!
     */
    public Coordinate getY() {
        return getCoordinate(Y_INDEX);
    }

    /**
     * Convenience method to retrieve the third coordinate.
     *
     * @return DOCUMENT ME!
     */
    public Coordinate getZ() {
        return getCoordinate(Z_INDEX);
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
        return ((index >= X_INDEX) && (index < coordinates_.length))
        ? coordinates_[index] : null;
    }

    // redefined methods
    /**
     * Returns a one-line string representation of this coordinate
     * tuple.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String result = coordinates_[0].toString();

        for (int ii = 1; ii < coordinates_.length; ii++) {
            result += (',' + coordinates_[ii].toString());
        }

        return result;
    }

    // new methods
    /**
     * Provides access to internal coordinates' structure. Meant to be
     * used by subclasses.
     *
     * @return DOCUMENT ME!
     */
    protected Coordinate[] getCoordinates() {
        return coordinates_;
    }
}
