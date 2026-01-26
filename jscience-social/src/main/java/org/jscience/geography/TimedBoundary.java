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

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.mathematics.geometry.boundaries.Boundary;

/**
 * A boundary that changes over time.
 * <p>
 * This is useful for representing historical borders, moving entities, 
 * or seasonal geographical phenomena. It maintains a list of time points
 * and corresponding boundary states.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TimedBoundary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * A snapshot of a boundary at a specific time.
     */
    public static class BoundaryState implements Serializable, Comparable<BoundaryState> {
        private static final long serialVersionUID = 1L;
        
        private final Instant timestamp;
        private final Boundary<?> boundary;

        public BoundaryState(Instant timestamp, Boundary<?> boundary) {
            this.timestamp = Objects.requireNonNull(timestamp);
            this.boundary = Objects.requireNonNull(boundary);
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public Boundary<?> getBoundary() {
            return boundary;
        }

        @Override
        public int compareTo(BoundaryState other) {
            return this.timestamp.compareTo(other.timestamp);
        }
    }

    /** The name or identifier for this timed boundary. */
    private final String name;

    /** The sequence of boundary states over time. */
    private final List<BoundaryState> states;

    /**
     * Creates a new TimedBoundary with an initial state.
     *
     * @param name    the name of the boundary
     * @param initial the initial boundary state
     */
    public TimedBoundary(String name, Boundary<?> initial) {
        this.name = Objects.requireNonNull(name);
        this.states = new ArrayList<>();
        this.states.add(new BoundaryState(Instant.EPOCH, initial));
    }

    /**
     * Creates a TimedBoundary with pre-defined states.
     *
     * @param name   the name
     * @param states the list of boundary states
     */
    public TimedBoundary(String name, List<BoundaryState> states) {
        this.name = Objects.requireNonNull(name);
        this.states = new ArrayList<>(states);
        Collections.sort(this.states);
    }

    /**
     * Adds a new boundary state at the specified time.
     *
     * @param timestamp when this boundary becomes active
     * @param boundary  the boundary at this time
     */
    public void addState(Instant timestamp, Boundary<?> boundary) {
        states.add(new BoundaryState(timestamp, boundary));
        Collections.sort(states);
    }

    /**
     * Returns the boundary valid at the given time.
     *
     * @param time the time to query
     * @return the boundary at that time
     */
    public Boundary<?> getAt(Instant time) {
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
     * Returns the boundary valid at the given epoch milliseconds.
     *
     * @param epochMillis epoch milliseconds
     * @return the boundary at that time
     */
    public Boundary<?> getAt(long epochMillis) {
        return getAt(Instant.ofEpochMilli(epochMillis));
    }

    /**
     * Returns the current boundary state.
     *
     * @return the most recent boundary
     */
    public Boundary<?> getCurrent() {
        return getAt(Instant.now());
    }

    /**
     * Returns all boundary states.
     *
     * @return unmodifiable list of states
     */
    public List<BoundaryState> getStates() {
        return Collections.unmodifiableList(states);
    }

    /**
     * Returns the number of states.
     */
    public int getStateCount() {
        return states.size();
    }

    /**
     * Returns the name of this timed boundary.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("TimedBoundary[%s, %d states]", name, states.size());
    }
}
