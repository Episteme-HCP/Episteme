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
import org.jscience.mathematics.numbers.integers.Natural;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Factory and convenience class for creating intervals over Natural numbers.
 * <p>
 * Natural numbers are non-negative integers: 0, 1, 2, 3, ...
 * Unlike real intervals, Natural intervals are discrete (countable), enabling:
 * <ul>
 * <li>Iteration over all elements</li>
 * <li>Exact element count</li>
 * <li>Successor/predecessor operations</li>
 * </ul>
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class NaturalInterval {

    private NaturalInterval() {
        // Utility class, no instantiation
    }

    /**
     * Creates a closed interval [min, max] over Natural numbers.
     * 
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return a closed interval containing all naturals from min to max
     * @throws IllegalArgumentException if min > max
     */
    public static RingIntervalND<Natural> closed(Natural min, Natural max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return new RingIntervalND<>(min, max, Natural::subtract);
    }

    /**
     * Creates a closed interval [min, max] from long values.
     */
    public static RingIntervalND<Natural> closed(long min, long max) {
        return closed(Natural.of(min), Natural.of(max));
    }

    /**
     * Creates a half-open interval [min, max) over Natural numbers.
     * 
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     * @return a half-open interval
     */
    public static RingIntervalND<Natural> closedOpen(Natural min, Natural max) {
        if (min.compareTo(max) >= 0) {
            throw new IllegalArgumentException("min must be < max for half-open interval");
        }
        return new RingIntervalND<>(min, max, true, false, Natural::subtract);
    }

    /**
     * Creates a half-open interval [min, max) from long values.
     */
    public static RingIntervalND<Natural> closedOpen(long min, long max) {
        return closedOpen(Natural.of(min), Natural.of(max));
    }

    /**
     * Creates an open interval (min, max) over Natural numbers.
     */
    public static RingIntervalND<Natural> open(Natural min, Natural max) {
        return new RingIntervalND<>(min, max, false, false, Natural::subtract);
    }

    /**
     * Creates an open interval (min, max) from long values.
     */
    public static RingIntervalND<Natural> open(long min, long max) {
        return open(Natural.of(min), Natural.of(max));
    }

    /**
     * Returns an iterator over all Natural numbers in the interval.
     * <p>
     * For closed intervals [a, b], iterates a, a+1, ..., b.
     * For half-open [a, b), iterates a, a+1, ..., b-1.
     * </p>
     * 
     * @param interval the interval to iterate
     * @return an iterator over the Natural numbers in the interval
     */
    public static Iterator<Natural> iterator(Interval<Natural> interval) {
        if (interval.getDimension() != 1) {
            throw new IllegalArgumentException("Iterator only supported for 1D intervals");
        }

        Natural start = interval.isClosedLeft(0) ? interval.getMin(0) : interval.getMin(0).add(Natural.ONE);
        Natural end = interval.isClosedRight(0) ? interval.getMax(0) : interval.getMax(0).subtract(Natural.ONE);

        return new Iterator<Natural>() {
            private Natural current = start;

            @Override
            public boolean hasNext() {
                return current.compareTo(end) <= 0;
            }

            @Override
            public Natural next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Natural result = current;
                current = current.add(Natural.ONE);
                return result;
            }
        };
    }

    /**
     * Returns a stream of all Natural numbers in the interval.
     * 
     * @param interval the interval to stream
     * @return a Stream of Natural numbers
     */
    public static Stream<Natural> stream(Interval<Natural> interval) {
        Iterable<Natural> iterable = () -> iterator(interval);
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * Returns the number of Natural numbers in the interval.
     * 
     * @param interval the interval
     * @return the count of elements (cardinality)
     */
    public static long size(Interval<Natural> interval) {
        if (interval.getDimension() != 1) {
            throw new IllegalArgumentException("Size only supported for 1D intervals");
        }

        Natural min = interval.getMin(0);
        Natural max = interval.getMax(0);

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
     * @return true if the interval contains no Natural numbers
     */
    public static boolean isEmpty(Interval<Natural> interval) {
        return size(interval) == 0;
    }

    /**
     * Creates an interval containing a single Natural number.
     * 
     * @param value the singleton value
     * @return a closed interval [value, value]
     */
    public static RingIntervalND<Natural> singleton(Natural value) {
        return closed(value, value);
    }

    /**
     * Creates an interval containing a single Natural number from a long.
     */
    public static RingIntervalND<Natural> singleton(long value) {
        return singleton(Natural.of(value));
    }
}

