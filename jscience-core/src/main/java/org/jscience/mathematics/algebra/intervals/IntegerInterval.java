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

package org.jscience.mathematics.algebra.intervals;

import org.jscience.mathematics.algebra.Interval;
import org.jscience.mathematics.numbers.integers.Integer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Factory and convenience class for creating intervals over Integer numbers.
 * <p>
 * Integers include all whole numbers: ..., -3, -2, -1, 0, 1, 2, 3, ...
 * Unlike real intervals, Integer intervals are discrete (countable), enabling:
 * <ul>
 * <li>Iteration over all elements</li>
 * <li>Exact element count</li>
 * <li>Successor/predecessor operations</li>
 * </ul>
 * </p>
 * <p>
 * This differs from {@link NaturalInterval} by supporting negative values.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class IntegerInterval {

    private IntegerInterval() {
        // Utility class, no instantiation
    }

    /**
     * Creates a closed interval [min, max] over Integer numbers.
     * 
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return a closed interval containing all integers from min to max
     * @throws IllegalArgumentException if min > max
     */
    public static RingIntervalND<Integer> closed(Integer min, Integer max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return new RingIntervalND<>(min, max, Integer::subtract);
    }

    /**
     * Creates a closed interval [min, max] from long values.
     */
    public static RingIntervalND<Integer> closed(long min, long max) {
        return closed(Integer.of(min), Integer.of(max));
    }

    /**
     * Creates a half-open interval [min, max) over Integer numbers.
     * 
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     * @return a half-open interval
     */
    public static RingIntervalND<Integer> closedOpen(Integer min, Integer max) {
        if (min.compareTo(max) >= 0) {
            throw new IllegalArgumentException("min must be < max for half-open interval");
        }
        return new RingIntervalND<>(min, max, true, false, Integer::subtract);
    }

    /**
     * Creates a half-open interval [min, max) from long values.
     */
    public static RingIntervalND<Integer> closedOpen(long min, long max) {
        return closedOpen(Integer.of(min), Integer.of(max));
    }

    /**
     * Creates an open interval (min, max) over Integer numbers.
     */
    public static RingIntervalND<Integer> open(Integer min, Integer max) {
        return new RingIntervalND<>(min, max, false, false, Integer::subtract);
    }

    /**
     * Creates an open interval (min, max) from long values.
     */
    public static RingIntervalND<Integer> open(long min, long max) {
        return open(Integer.of(min), Integer.of(max));
    }

    /**
     * Returns an iterator over all Integer numbers in the interval.
     * <p>
     * For closed intervals [a, b], iterates a, a+1, ..., b.
     * For half-open [a, b), iterates a, a+1, ..., b-1.
     * </p>
     * 
     * @param interval the interval to iterate
     * @return an iterator over the Integer numbers in the interval
     */
    public static Iterator<Integer> iterator(Interval<Integer> interval) {
        if (interval.getDimension() != 1) {
            throw new IllegalArgumentException("Iterator only supported for 1D intervals");
        }

        Integer start = interval.isClosedLeft(0) ? interval.getMin(0) : interval.getMin(0).add(Integer.ONE);
        Integer end = interval.isClosedRight(0) ? interval.getMax(0) : interval.getMax(0).subtract(Integer.ONE);

        return new Iterator<Integer>() {
            private Integer current = start;

            @Override
            public boolean hasNext() {
                return current.compareTo(end) <= 0;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Integer result = current;
                current = current.add(Integer.ONE);
                return result;
            }
        };
    }

    /**
     * Returns a stream of all Integer numbers in the interval.
     * 
     * @param interval the interval to stream
     * @return a Stream of Integer numbers
     */
    public static Stream<Integer> stream(Interval<Integer> interval) {
        Iterable<Integer> iterable = () -> iterator(interval);
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * Returns the number of Integer numbers in the interval.
     * 
     * @param interval the interval
     * @return the count of elements (cardinality)
     */
    public static long size(Interval<Integer> interval) {
        if (interval.getDimension() != 1) {
            throw new IllegalArgumentException("Size only supported for 1D intervals");
        }

        Integer min = interval.getMin(0);
        Integer max = interval.getMax(0);

        if (min.compareTo(max) > 0) {
            return 0;
        }

        long count = max.subtract(min).longValue() + 1;

        // Adjust for open endpoints
        if (!interval.isClosedLeft(0)) {
            count--;
        }
        if (!interval.isClosedRight(0)) {
            count--;
        }

        return Math.max(0, count);
    }

    /**
     * Returns whether the interval is empty.
     * 
     * @param interval the interval to check
     * @return true if the interval contains no Integer numbers
     */
    public static boolean isEmpty(Interval<Integer> interval) {
        return size(interval) == 0;
    }

    /**
     * Creates an interval containing a single Integer number.
     * 
     * @param value the singleton value
     * @return a closed interval [value, value]
     */
    public static RingIntervalND<Integer> singleton(Integer value) {
        return closed(value, value);
    }

    /**
     * Creates an interval containing a single Integer number from a long.
     */
    public static RingIntervalND<Integer> singleton(long value) {
        return singleton(Integer.of(value));
    }

    /**
     * Creates a range from 0 to n-1 (useful for array indexing).
     * 
     * @param n the exclusive upper bound
     * @return a half-open interval [0, n)
     */
    public static RingIntervalND<Integer> range(long n) {
        return closedOpen(0, n);
    }

    /**
     * Creates a range from start to end-1 (Python-style range).
     * 
     * @param start the inclusive lower bound
     * @param end   the exclusive upper bound
     * @return a half-open interval [start, end)
     */
    public static RingIntervalND<Integer> range(long start, long end) {
        return closedOpen(start, end);
    }
}