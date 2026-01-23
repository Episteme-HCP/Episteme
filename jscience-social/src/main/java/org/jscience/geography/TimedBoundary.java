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

package org.jscience.geography;

import org.jscience.mathematics.analysis.Interval;
import org.jscience.mathematics.analysis.IntervalsList;

import java.util.Objects;

/**
 * A boundary that changes over time.
 * <p>
 * This is useful for representing historical borders, moving entities, 
 * or seasonal geographical phenomena. It maintains a list of time intervals
 * and corresponding boundary states.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class TimedBoundary extends Boundary {

    private static final long serialVersionUID = 1L;

    /** The time intervals for which each boundary state is valid. */
    private final IntervalsList dates;

    /** The sequence of coordinate sets corresponding to each time interval. */
    private final Coordinate[][] timedCoordinates;

    /** The sequence of edge inclusion flags for each boundary state. */
    private final boolean[][] timedEdgesIncluded;

    /**
     * Creates a new TimedBoundary valid for all time.
     *
     * @param initial the initial boundary state
     */
    public TimedBoundary(Boundary initial) {
        super(initial.getCoordinates(), initial.getEdgesIncluded());
        this.dates = new IntervalsList(new Interval(
            new org.jscience.mathematics.numbers.real.RealDouble(Double.NEGATIVE_INFINITY),
            new org.jscience.mathematics.numbers.real.RealDouble(Double.POSITIVE_INFINITY)));
        this.timedCoordinates = new Coordinate[][] { initial.getCoordinates() };
        this.timedEdgesIncluded = new boolean[][] { initial.getEdgesIncluded() };
    }

    /**
     * Creates a multi-state timed boundary.
     *
     * @param dates              the validity intervals
     * @param timedCoordinates   the coordinate sets for each interval
     * @param timedEdgesIncluded the edge inclusion flags for each interval
     * @throws IllegalArgumentException if array lengths don't match
     */
    public TimedBoundary(IntervalsList dates, Coordinate[][] timedCoordinates, 
                         boolean[][] timedEdgesIncluded) {
        super(timedCoordinates[0], timedEdgesIncluded[0]);
        
        Objects.requireNonNull(dates);
        Objects.requireNonNull(timedCoordinates);
        Objects.requireNonNull(timedEdgesIncluded);

        if (dates.getSize() != timedCoordinates.length || 
            dates.getSize() != timedEdgesIncluded.length) {
            throw new IllegalArgumentException("Intervals and data arrays must have same length");
        }

        this.dates = dates;
        this.timedCoordinates = timedCoordinates;
        this.timedEdgesIncluded = timedEdgesIncluded;
    }

    /**
     * Returns the boundary state valid at the given time.
     *
     * @param time the time to query
     * @return the boundary at that time, or the default (first) state if not found
     */
    public Boundary getAt(double time) {
        int index = findIntervalIndex(time);
        if (index >= 0) {
            return new Boundary(timedCoordinates[index], timedEdgesIncluded[index]);
        }
        return new Boundary(coordinates, edgesIncluded);
    }

    private int findIntervalIndex(double time) {
        // Implementation depends on IntervalsList structure
        // Assuming we can iterate as it's a list of intervals
        for (int i = 0; i < dates.getSize(); i++) {
            Interval interval = dates.get(i);
            if (interval.contains(new org.jscience.mathematics.numbers.real.RealDouble(time))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the time intervals.
     *
     * @return validity intervals
     */
    public IntervalsList getDates() {
        return dates;
    }

    @Override
    public Coordinate getCentroid() {
        // Return centroid of the current state (or default)
        return super.getCentroid();
    }

    @Override
    public String toString() {
        return String.format("TimedBoundary[%d states, current: %s]", 
            dates.getSize(), super.toString());
    }
}
