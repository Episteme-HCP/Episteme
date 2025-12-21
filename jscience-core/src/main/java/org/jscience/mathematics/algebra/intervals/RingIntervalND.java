/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import java.util.function.BinaryOperator;

/**
 * N-dimensional interval over a Ring (elements support addition and
 * subtraction).
 * <p>
 * Extends the capabilities of OrderedSetIntervalND by requiring elements to
 * form
 * a Ring structure, enabling operations that depend on subtraction (like
 * computing
 * the width/extent in each dimension).
 * </p>
 * <p>
 * A Ring requires:
 * <ul>
 * <li>Addition: (a + b)</li>
 * <li>Additive inverse/subtraction: (a - b)</li>
 * <li>Multiplication: (a × b)</li>
 * <li>Additive identity (zero)</li>
 * <li>Multiplicative identity (one)</li>
 * </ul>
 * </p>
 * 
 * @param <T> the type of values in the interval, must be Comparable and support
 *            Ring operations
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RingIntervalND<T extends Comparable<T>> extends OrderedSetIntervalND<T> {

    private final BinaryOperator<T> subtractor;

    /**
     * Creates an N-dimensional Ring interval with specified bounds.
     * 
     * @param min         array of minimum values for each dimension
     * @param max         array of maximum values for each dimension
     * @param closedLeft  array indicating if left endpoints are closed
     * @param closedRight array indicating if right endpoints are closed
     * @param subtractor  function to compute (a - b) for elements
     */
    public RingIntervalND(T[] min, T[] max, boolean[] closedLeft, boolean[] closedRight,
            BinaryOperator<T> subtractor) {
        super(min, max, closedLeft, closedRight);
        this.subtractor = subtractor;
    }

    /**
     * Creates a closed N-dimensional Ring interval [min, max].
     */
    public RingIntervalND(T[] min, T[] max, BinaryOperator<T> subtractor) {
        super(min, max);
        this.subtractor = subtractor;
    }

    /**
     * Creates a 1-dimensional Ring interval.
     */
    public RingIntervalND(T min, T max, boolean closedLeft, boolean closedRight,
            BinaryOperator<T> subtractor) {
        super(min, max, closedLeft, closedRight);
        this.subtractor = subtractor;
    }

    /**
     * Creates a closed 1-dimensional Ring interval [min, max].
     */
    public RingIntervalND(T min, T max, BinaryOperator<T> subtractor) {
        super(min, max);
        this.subtractor = subtractor;
    }

    /**
     * Returns the width (extent) in the specified dimension.
     * <p>
     * Computed as max - min using the Ring's subtraction operation.
     * </p>
     * 
     * @param dimension the dimension index (0-based)
     * @return the width (max - min) in that dimension
     */
    public T getWidth(int dimension) {
        return subtractor.apply(getMax(dimension), getMin(dimension));
    }

    /**
     * Returns the widths in all dimensions.
     * 
     * @return array of widths for each dimension
     */
    @SuppressWarnings("unchecked")
    public T[] getWidths() {
        T[] widths = (T[]) new Comparable[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            widths[i] = getWidth(i);
        }
        return widths;
    }

    /**
     * Returns the subtraction operator used by this interval.
     * 
     * @return the binary subtraction operator
     */
    public BinaryOperator<T> getSubtractor() {
        return subtractor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Interval<T> intersection(Interval<T> other) {
        Interval<T> baseIntersection = super.intersection(other);
        if (baseIntersection == null) {
            return null;
        }

        // If other is also a RingInterval, preserve the subtractor
        if (other instanceof RingIntervalND) {
            int dim = baseIntersection.getDimension();
            T[] newMin = (T[]) new Comparable[dim];
            T[] newMax = (T[]) new Comparable[dim];
            boolean[] newClosedLeft = new boolean[dim];
            boolean[] newClosedRight = new boolean[dim];

            for (int i = 0; i < dim; i++) {
                newMin[i] = baseIntersection.getMin(i);
                newMax[i] = baseIntersection.getMax(i);
                newClosedLeft[i] = baseIntersection.isClosedLeft(i);
                newClosedRight[i] = baseIntersection.isClosedRight(i);
            }

            return new RingIntervalND<>(newMin, newMax, newClosedLeft, newClosedRight, subtractor);
        }

        return baseIntersection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Interval<T> boundingInterval(Interval<T> other) {
        Interval<T> baseBounding = super.boundingInterval(other);

        // Preserve the subtractor
        int dim = baseBounding.getDimension();
        T[] newMin = (T[]) new Comparable[dim];
        T[] newMax = (T[]) new Comparable[dim];
        boolean[] newClosedLeft = new boolean[dim];
        boolean[] newClosedRight = new boolean[dim];

        for (int i = 0; i < dim; i++) {
            newMin[i] = baseBounding.getMin(i);
            newMax[i] = baseBounding.getMax(i);
            newClosedLeft[i] = baseBounding.isClosedLeft(i);
            newClosedRight[i] = baseBounding.isClosedRight(i);
        }

        return new RingIntervalND<>(newMin, newMax, newClosedLeft, newClosedRight, subtractor);
    }
}