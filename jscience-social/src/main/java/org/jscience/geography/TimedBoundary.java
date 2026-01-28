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

import org.jscience.history.time.TimeCoordinate;
import org.jscience.history.time.TimePoint;
import org.jscience.mathematics.geometry.boundaries.Boundary;
import org.jscience.util.Temporal;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A geographic boundary that changes over time.
 * Stores multiple states, each representing the boundary at a specific point in time.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class TimedBoundary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<BoundaryState> states = new ArrayList<>();

    public TimedBoundary() {
    }

    /**
     * Adds a boundary state at a given time.
     * The states are kept sorted by time.
     */
    public void addState(TimeCoordinate timestamp, Boundary<?> boundary) {
        states.add(new BoundaryState(timestamp, boundary));
        Collections.sort(states);
    }

    public List<BoundaryState> getStates() {
        return Collections.unmodifiableList(states);
    }

    /**
     * Returns the boundary active at the given time.
     */
    public Boundary<?> getAt(TimeCoordinate time) {
        Boundary<?> result = null;
        for (BoundaryState state : states) {
            if (state.getTimestamp().compareTo(time) <= 0) {
                result = state.getBoundary();
            } else {
                break;
            }
        }
        return result != null ? result : (states.isEmpty() ? null : states.get(0).getBoundary());
    }

    /**
     * Returns the current boundary.
     */
    public Boundary<?> getCurrent() {
        return getAt(TimePoint.now());
    }

    /**
     * Represents a snapshot of a boundary at a specific time.
     */
    public static class BoundaryState implements Serializable, Comparable<BoundaryState>, Temporal<TimeCoordinate> {
        private static final long serialVersionUID = 1L;
        private final TimeCoordinate timestamp;
        private final Boundary<?> boundary;

        public BoundaryState(TimeCoordinate timestamp, Boundary<?> boundary) {
            this.timestamp = Objects.requireNonNull(timestamp);
            this.boundary = Objects.requireNonNull(boundary);
        }

        public TimeCoordinate getTimestamp() {
            return timestamp;
        }

        public Boundary<?> getBoundary() {
            return boundary;
        }

        @Override
        public TimeCoordinate getWhen() {
            return timestamp;
        }

        @Override
        public int compareTo(BoundaryState other) {
            return this.timestamp.compareTo(other.timestamp);
        }
    }
}
