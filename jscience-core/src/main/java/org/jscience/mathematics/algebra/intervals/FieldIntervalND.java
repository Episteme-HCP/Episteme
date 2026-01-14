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

import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * N-dimensional interval over a Field (elements support full arithmetic).
 * <p>
 * Extends RingIntervalND by requiring elements to form a Field structure,
 * enabling operations that depend on division (like computing the
 * center/midpoint
 * of each dimension).
 * </p>
 * <p>
 * A Field requires all Ring operations plus:
 * <ul>
 * <li>Division: (a / b) for b Ã¢â€°Â  0</li>
 * <li>Multiplicative inverse</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FieldIntervalND<T extends Comparable<T>> extends RingIntervalND<T> {

    private final BinaryOperator<T> adder;
    private final BinaryOperator<T> divider;
    private final Function<Integer, T> fromInt;

    /**
     * Creates an N-dimensional Field interval with specified bounds.
     * 
     * @param min         array of minimum values for each dimension
     * @param max         array of maximum values for each dimension
     * @param closedLeft  array indicating if left endpoints are closed
     * @param closedRight array indicating if right endpoints are closed
     * @param adder       function to compute (a + b)
     * @param subtractor  function to compute (a - b)
     * @param divider     function to compute (a / b)
     * @param fromInt     function to create element from integer (for computing 2
     *                    in midpoint)
     */
    public FieldIntervalND(T[] min, T[] max, boolean[] closedLeft, boolean[] closedRight,
            BinaryOperator<T> adder, BinaryOperator<T> subtractor,
            BinaryOperator<T> divider, Function<Integer, T> fromInt) {
        super(min, max, closedLeft, closedRight, subtractor);
        this.adder = adder;
        this.divider = divider;
        this.fromInt = fromInt;
    }

    /**
     * Creates a closed N-dimensional Field interval [min, max].
     */
    public FieldIntervalND(T[] min, T[] max, BinaryOperator<T> adder, BinaryOperator<T> subtractor,
            BinaryOperator<T> divider, Function<Integer, T> fromInt) {
        super(min, max, subtractor);
        this.adder = adder;
        this.divider = divider;
        this.fromInt = fromInt;
    }

    /**
     * Creates a 1-dimensional Field interval.
     */
    public FieldIntervalND(T min, T max, boolean closedLeft, boolean closedRight,
            BinaryOperator<T> adder, BinaryOperator<T> subtractor,
            BinaryOperator<T> divider, Function<Integer, T> fromInt) {
        super(min, max, closedLeft, closedRight, subtractor);
        this.adder = adder;
        this.divider = divider;
        this.fromInt = fromInt;
    }

    /**
     * Creates a closed 1-dimensional Field interval [min, max].
     */
    public FieldIntervalND(T min, T max, BinaryOperator<T> adder, BinaryOperator<T> subtractor,
            BinaryOperator<T> divider, Function<Integer, T> fromInt) {
        super(min, max, subtractor);
        this.adder = adder;
        this.divider = divider;
        this.fromInt = fromInt;
    }

    /**
     * Returns the midpoint (center) in the specified dimension.
     * <p>
     * Computed as (min + max) / 2 using the Field's arithmetic operations.
     * </p>
     * 
     * @param dimension the dimension index (0-based)
     * @return the midpoint value in that dimension
     */
    public T getMidpoint(int dimension) {
        T sum = adder.apply(getMin(dimension), getMax(dimension));
        T two = fromInt.apply(2);
        return divider.apply(sum, two);
    }

    /**
     * Returns the midpoints (center) in all dimensions.
     * 
     * @return array of midpoint values for each dimension
     */
    @SuppressWarnings("unchecked")
    public T[] getMidpoints() {
        T[] midpoints = (T[]) new Comparable[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            midpoints[i] = getMidpoint(i);
        }
        return midpoints;
    }

    /**
     * Returns the adder operator.
     */
    public BinaryOperator<T> getAdder() {
        return adder;
    }

    /**
     * Returns the divider operator.
     */
    public BinaryOperator<T> getDivider() {
        return divider;
    }

    /**
     * Returns the fromInt converter.
     */
    public Function<Integer, T> getFromInt() {
        return fromInt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Interval<T> intersection(Interval<T> other) {
        Interval<T> baseIntersection = super.intersection(other);
        if (baseIntersection == null) {
            return null;
        }

        // If result is already a RingInterval, upgrade to FieldInterval
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

        return new FieldIntervalND<>(newMin, newMax, newClosedLeft, newClosedRight,
                adder, getSubtractor(), divider, fromInt);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Interval<T> boundingInterval(Interval<T> other) {
        Interval<T> baseBounding = super.boundingInterval(other);

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

        return new FieldIntervalND<>(newMin, newMax, newClosedLeft, newClosedRight,
                adder, getSubtractor(), divider, fromInt);
    }
}

