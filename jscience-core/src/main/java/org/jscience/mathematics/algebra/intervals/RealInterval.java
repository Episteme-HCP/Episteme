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

import org.jscience.mathematics.numbers.real.Real;

/**
 * Factory and convenience class for creating intervals over Real numbers.
 * <p>
 * Provides static factory methods for creating 1D and N-dimensional intervals
 * with proper Field operations for Real numbers.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@SuppressWarnings("null")
public final class RealInterval {

    private RealInterval() {
        // Utility class, no instantiation
    }

    /**
     * Creates a closed 1D interval [min, max] over Real numbers.
     */
    public static FieldIntervalND<Real> closed(Real min, Real max) {
        return new FieldIntervalND<>(min, max,
                Real::add, Real::subtract, Real::divide, Real::of);
    }

    /**
     * Creates a closed 1D interval [min, max] from doubles.
     */
    public static FieldIntervalND<Real> closed(double min, double max) {
        return closed(Real.of(min), Real.of(max));
    }

    /**
     * Creates an open 1D interval (min, max) over Real numbers.
     */
    public static FieldIntervalND<Real> open(Real min, Real max) {
        return new FieldIntervalND<>(min, max, false, false,
                Real::add, Real::subtract, Real::divide, Real::of);
    }

    /**
     * Creates an open 1D interval (min, max) from doubles.
     */
    public static FieldIntervalND<Real> open(double min, double max) {
        return open(Real.of(min), Real.of(max));
    }

    /**
     * Creates a half-open 1D interval [min, max) over Real numbers.
     */
    public static FieldIntervalND<Real> closedOpen(Real min, Real max) {
        return new FieldIntervalND<>(min, max, true, false,
                Real::add, Real::subtract, Real::divide, Real::of);
    }

    /**
     * Creates a half-open 1D interval [min, max) from doubles.
     */
    public static FieldIntervalND<Real> closedOpen(double min, double max) {
        return closedOpen(Real.of(min), Real.of(max));
    }

    /**
     * Creates a half-open 1D interval (min, max] over Real numbers.
     */
    public static FieldIntervalND<Real> openClosed(Real min, Real max) {
        return new FieldIntervalND<>(min, max, false, true,
                Real::add, Real::subtract, Real::divide, Real::of);
    }

    /**
     * Creates a half-open 1D interval (min, max] from doubles.
     */
    public static FieldIntervalND<Real> openClosed(double min, double max) {
        return openClosed(Real.of(min), Real.of(max));
    }

    /**
     * Creates a 1D interval with specified endpoint types.
     */
    public static FieldIntervalND<Real> of(Real min, Real max, boolean closedLeft, boolean closedRight) {
        return new FieldIntervalND<>(min, max, closedLeft, closedRight,
                Real::add, Real::subtract, Real::divide, Real::of);
    }

    /**
     * Creates an N-dimensional closed interval over Real numbers.
     */
    public static FieldIntervalND<Real> closedND(Real[] min, Real[] max) {
        boolean[] closed = new boolean[min.length];
        java.util.Arrays.fill(closed, true);
        return new FieldIntervalND<>(min, max, closed, closed,
                Real::add, Real::subtract, Real::divide, Real::of);
    }

    /**
     * Creates an N-dimensional interval with specified endpoint types.
     */
    public static FieldIntervalND<Real> ofND(Real[] min, Real[] max,
            boolean[] closedLeft, boolean[] closedRight) {
        return new FieldIntervalND<>(min, max, closedLeft, closedRight,
                Real::add, Real::subtract, Real::divide, Real::of);
    }

    /**
     * Returns the length (width) of a 1D Real interval.
     */
    public static Real length(Interval<Real> interval) {
        if (interval.getDimension() != 1) {
            throw new IllegalArgumentException("Length is only defined for 1D intervals");
        }
        return interval.getMax(0).subtract(interval.getMin(0));
    }

    /**
     * Returns the midpoint of a 1D Real interval.
     */
    public static Real midpoint(Interval<Real> interval) {
        if (interval.getDimension() != 1) {
            throw new IllegalArgumentException("Midpoint is only defined for 1D intervals");
        }
        return interval.getMin(0).add(interval.getMax(0)).divide(Real.of(2));
    }
}