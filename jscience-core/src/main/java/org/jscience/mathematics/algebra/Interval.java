/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.algebra;

/**
 * Interface for N-dimensional intervals over ordered sets.
 * <p>
 * An interval represents a contiguous hyper-rectangular region in an ordered
 * space.
 * This is the base interface for intervals, requiring only that elements be
 * comparable.
 * More specialized intervals (over Rings, Fields) extend this interface with
 * additional
 * capabilities.
 * </p>
 * <p>
 * Supports various endpoint configurations:
 * <ul>
 * <li>[a,b] - closed (includes endpoints)</li>
 * <li>(a,b) - open (excludes endpoints)</li>
 * <li>[a,b) or (a,b] - half-open</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Interval<T extends Comparable<T>> {

    /**
     * Returns the number of dimensions of this interval.
     * 
     * @return the dimensionality (1 for 1D intervals, N for ND)
     */
    int getDimension();

    /**
     * Returns the lower bound in the specified dimension.
     * 
     * @param dimension the dimension index (0-based)
     * @return the minimum value in that dimension
     * @throws IndexOutOfBoundsException if dimension is out of range
     */
    T getMin(int dimension);

    /**
     * Returns the upper bound in the specified dimension.
     * 
     * @param dimension the dimension index (0-based)
     * @return the maximum value in that dimension
     * @throws IndexOutOfBoundsException if dimension is out of range
     */
    T getMax(int dimension);

    /**
     * Returns whether the lower endpoint is closed in the specified dimension.
     * 
     * @param dimension the dimension index (0-based)
     * @return true if the interval includes its minimum value in that dimension
     */
    boolean isClosedLeft(int dimension);

    /**
     * Returns whether the upper endpoint is closed in the specified dimension.
     * 
     * @param dimension the dimension index (0-based)
     * @return true if the interval includes its maximum value in that dimension
     */
    boolean isClosedRight(int dimension);

    /**
     * Checks if a point is contained in this interval.
     * 
     * @param point array of coordinates (must match dimensionality)
     * @return true if the point is within the interval bounds
     * @throws IllegalArgumentException if point dimensionality doesn't match
     */
    boolean contains(T[] point);

    /**
     * Checks if this interval contains another interval entirely.
     * 
     * @param other the interval to check
     * @return true if other is a subset of this interval
     */
    boolean contains(Interval<T> other);

    /**
     * Checks if this interval overlaps with another.
     * 
     * @param other the interval to check
     * @return true if the intervals share at least one point
     */
    boolean overlaps(Interval<T> other);

    /**
     * Returns the intersection of this interval with another.
     * 
     * @param other the interval to intersect with
     * @return the intersection interval, or null if empty
     */
    Interval<T> intersection(Interval<T> other);

    /**
     * Returns the smallest interval containing both this and another interval.
     * 
     * @param other the interval to union with
     * @return the bounding interval
     */
    Interval<T> boundingInterval(Interval<T> other);

    /**
     * Returns whether this interval is empty.
     * 
     * @return true if the interval contains no elements
     */
    boolean isEmpty();

    /**
     * Returns whether this interval is open in all dimensions.
     * 
     * @return true if all endpoints are open
     */
    default boolean isOpen() {
        for (int i = 0; i < getDimension(); i++) {
            if (isClosedLeft(i) || isClosedRight(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether this interval is closed in all dimensions.
     * 
     * @return true if all endpoints are closed
     */
    default boolean isClosed() {
        for (int i = 0; i < getDimension(); i++) {
            if (!isClosedLeft(i) || !isClosedRight(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the standard mathematical notation for this interval.
     * 
     * @return interval notation string (e.g., "[0,1] Ãƒâ€” (2,3]")
     */
    String notation();
}

