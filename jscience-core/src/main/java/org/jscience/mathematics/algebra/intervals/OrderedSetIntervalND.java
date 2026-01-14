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

package org.jscience.mathematics.algebra.intervals;

import org.jscience.mathematics.algebra.Interval;

import java.util.Arrays;

/**
 * N-dimensional interval over an ordered set (elements must be Comparable).
 * <p>
 * This is the most basic interval implementation, requiring only that elements
 * support comparison operations. Suitable for any totally ordered set.
 * </p>
 * <p>
 * Supports containment checks, subset testing, and overlap detection.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OrderedSetIntervalND<T extends Comparable<T>> implements Interval<T> {

    private final T[] min;
    private final T[] max;
    private final boolean[] closedLeft;
    private final boolean[] closedRight;
    private final int dimension;

    /**
     * Creates an N-dimensional interval with specified bounds and endpoint types.
     * 
     * @param min         array of minimum values for each dimension
     * @param max         array of maximum values for each dimension
     * @param closedLeft  array indicating if left endpoints are closed
     * @param closedRight array indicating if right endpoints are closed
     */
    public OrderedSetIntervalND(T[] min, T[] max, boolean[] closedLeft, boolean[] closedRight) {
        if (min.length != max.length || min.length != closedLeft.length || min.length != closedRight.length) {
            throw new IllegalArgumentException("All arrays must have the same length");
        }
        this.dimension = min.length;
        this.min = Arrays.copyOf(min, dimension);
        this.max = Arrays.copyOf(max, dimension);
        this.closedLeft = Arrays.copyOf(closedLeft, dimension);
        this.closedRight = Arrays.copyOf(closedRight, dimension);

        // Normalize: ensure min <= max in each dimension
        for (int i = 0; i < dimension; i++) {
            if (this.min[i].compareTo(this.max[i]) > 0) {
                T temp = this.min[i];
                this.min[i] = this.max[i];
                this.max[i] = temp;
                boolean tempClosed = this.closedLeft[i];
                this.closedLeft[i] = this.closedRight[i];
                this.closedRight[i] = tempClosed;
            }
        }
    }

    /**
     * Creates a closed N-dimensional interval [min, max].
     */
    public OrderedSetIntervalND(T[] min, T[] max) {
        this(min, max, createAllTrue(min.length), createAllTrue(min.length));
    }

    /**
     * Creates a 1-dimensional interval.
     */
    @SuppressWarnings("unchecked")
    public OrderedSetIntervalND(T min, T max, boolean closedLeft, boolean closedRight) {
        this.dimension = 1;
        this.min = (T[]) new Comparable[] { min };
        this.max = (T[]) new Comparable[] { max };
        this.closedLeft = new boolean[] { closedLeft };
        this.closedRight = new boolean[] { closedRight };

        if (this.min[0].compareTo(this.max[0]) > 0) {
            T temp = this.min[0];
            this.min[0] = this.max[0];
            this.max[0] = temp;
            boolean tempClosed = this.closedLeft[0];
            this.closedLeft[0] = this.closedRight[0];
            this.closedRight[0] = tempClosed;
        }
    }

    /**
     * Creates a closed 1-dimensional interval [min, max].
     */
    public OrderedSetIntervalND(T min, T max) {
        this(min, max, true, true);
    }

    private static boolean[] createAllTrue(int length) {
        boolean[] result = new boolean[length];
        Arrays.fill(result, true);
        return result;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public T getMin(int dimension) {
        checkDimension(dimension);
        return min[dimension];
    }

    @Override
    public T getMax(int dimension) {
        checkDimension(dimension);
        return max[dimension];
    }

    @Override
    public boolean isClosedLeft(int dimension) {
        checkDimension(dimension);
        return closedLeft[dimension];
    }

    @Override
    public boolean isClosedRight(int dimension) {
        checkDimension(dimension);
        return closedRight[dimension];
    }

    private void checkDimension(int dim) {
        if (dim < 0 || dim >= dimension) {
            throw new IndexOutOfBoundsException("Dimension " + dim + " out of range [0, " + dimension + ")");
        }
    }

    @Override
    public boolean contains(T[] point) {
        if (point.length != dimension) {
            throw new IllegalArgumentException("Point dimension must match interval dimension");
        }
        for (int i = 0; i < dimension; i++) {
            if (!containsInDimension(point[i], i)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsInDimension(T value, int dim) {
        int cmpMin = value.compareTo(min[dim]);
        int cmpMax = max[dim].compareTo(value);

        boolean aboveMin = closedLeft[dim] ? (cmpMin >= 0) : (cmpMin > 0);
        boolean belowMax = closedRight[dim] ? (cmpMax >= 0) : (cmpMax > 0);

        return aboveMin && belowMax;
    }

    @Override
    public boolean contains(Interval<T> other) {
        if (other.getDimension() != dimension) {
            return false;
        }
        for (int i = 0; i < dimension; i++) {
            // Check if our interval contains the other's interval in this dimension
            T otherMin = other.getMin(i);
            T otherMax = other.getMax(i);

            boolean containsMin = containsInDimension(otherMin, i) ||
                    (min[i].equals(otherMin) && (closedLeft[i] || !other.isClosedLeft(i)));

            boolean containsMax = containsInDimension(otherMax, i) ||
                    (max[i].equals(otherMax) && (closedRight[i] || !other.isClosedRight(i)));

            if (!containsMin || !containsMax) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean overlaps(Interval<T> other) {
        return intersection(other) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Interval<T> intersection(Interval<T> other) {
        if (other.getDimension() != dimension) {
            throw new IllegalArgumentException("Intervals must have same dimension");
        }

        T[] newMin = (T[]) new Comparable[dimension];
        T[] newMax = (T[]) new Comparable[dimension];
        boolean[] newClosedLeft = new boolean[dimension];
        boolean[] newClosedRight = new boolean[dimension];

        for (int i = 0; i < dimension; i++) {
            T otherMin = other.getMin(i);
            T otherMax = other.getMax(i);

            // Determine new min
            int cmpMin = min[i].compareTo(otherMin);
            if (cmpMin > 0) {
                newMin[i] = min[i];
                newClosedLeft[i] = closedLeft[i];
            } else if (cmpMin < 0) {
                newMin[i] = otherMin;
                newClosedLeft[i] = other.isClosedLeft(i);
            } else {
                newMin[i] = min[i];
                newClosedLeft[i] = closedLeft[i] && other.isClosedLeft(i);
            }

            // Determine new max
            int cmpMax = max[i].compareTo(otherMax);
            if (cmpMax < 0) {
                newMax[i] = max[i];
                newClosedRight[i] = closedRight[i];
            } else if (cmpMax > 0) {
                newMax[i] = otherMax;
                newClosedRight[i] = other.isClosedRight(i);
            } else {
                newMax[i] = max[i];
                newClosedRight[i] = closedRight[i] && other.isClosedRight(i);
            }

            // Check if empty in this dimension
            int cmpMinMax = newMin[i].compareTo(newMax[i]);
            if (cmpMinMax > 0) {
                return null;
            }
            if (cmpMinMax == 0 && !(newClosedLeft[i] && newClosedRight[i])) {
                return null;
            }
        }

        return new OrderedSetIntervalND<>(newMin, newMax, newClosedLeft, newClosedRight);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Interval<T> boundingInterval(Interval<T> other) {
        if (other.getDimension() != dimension) {
            throw new IllegalArgumentException("Intervals must have same dimension");
        }

        T[] newMin = (T[]) new Comparable[dimension];
        T[] newMax = (T[]) new Comparable[dimension];
        boolean[] newClosedLeft = new boolean[dimension];
        boolean[] newClosedRight = new boolean[dimension];

        for (int i = 0; i < dimension; i++) {
            T otherMin = other.getMin(i);
            T otherMax = other.getMax(i);

            int cmpMin = min[i].compareTo(otherMin);
            if (cmpMin < 0) {
                newMin[i] = min[i];
                newClosedLeft[i] = closedLeft[i];
            } else if (cmpMin > 0) {
                newMin[i] = otherMin;
                newClosedLeft[i] = other.isClosedLeft(i);
            } else {
                newMin[i] = min[i];
                newClosedLeft[i] = closedLeft[i] || other.isClosedLeft(i);
            }

            int cmpMax = max[i].compareTo(otherMax);
            if (cmpMax > 0) {
                newMax[i] = max[i];
                newClosedRight[i] = closedRight[i];
            } else if (cmpMax < 0) {
                newMax[i] = otherMax;
                newClosedRight[i] = other.isClosedRight(i);
            } else {
                newMax[i] = max[i];
                newClosedRight[i] = closedRight[i] || other.isClosedRight(i);
            }
        }

        return new OrderedSetIntervalND<>(newMin, newMax, newClosedLeft, newClosedRight);
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < dimension; i++) {
            if (min[i].equals(max[i]) && !(closedLeft[i] && closedRight[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String notation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            if (i > 0) {
                sb.append(" Ãƒâ€” ");
            }
            sb.append(closedLeft[i] ? "[" : "(");
            sb.append(min[i]);
            sb.append(", ");
            sb.append(max[i]);
            sb.append(closedRight[i] ? "]" : ")");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return notation();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Interval))
            return false;
        Interval<?> other = (Interval<?>) obj;
        if (other.getDimension() != dimension)
            return false;
        for (int i = 0; i < dimension; i++) {
            if (!min[i].equals(other.getMin(i)) || !max[i].equals(other.getMax(i)) ||
                    closedLeft[i] != other.isClosedLeft(i) || closedRight[i] != other.isClosedRight(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(min);
        result = 31 * result + Arrays.hashCode(max);
        result = 31 * result + Arrays.hashCode(closedLeft);
        result = 31 * result + Arrays.hashCode(closedRight);
        return result;
    }
}

