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

package org.jscience.social.economics;

import org.jscience.core.mathematics.discrete.RootedTree;
import org.jscience.core.util.Temporal;
import org.jscience.social.history.time.TimeCoordinate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;

/**
 * Represents a formal organizational structure (organigram).
 * This defines the intended hierarchy and grouping of workers within an organization.
 * Uses the graph-based RootedTree for hierarchical representation.
 * Modernized to implement ComprehensiveIdentification and follow new standards.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Organigram implements ComprehensiveIdentification, Temporal<TimeCoordinate> {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private TimeCoordinate timestamp;

    /** The actual hierarchical structure of organizational unit names. */
    private RootedTree<String> structure;

    /** Mapping of organizational unit names to workers. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Worker> workers;

    /**
     * Creates a new Organigram.
     *
     * @param name the name of the organization or the root unit
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public Organigram(String name) {
        this.id = new SimpleIdentification("Organigram:" + name);
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.timestamp = org.jscience.social.history.time.TimePoint.now();
        this.structure = new RootedTree<>(name);
        this.workers = new HashSet<>();
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public TimeCoordinate getWhen() {
        return timestamp;
    }

    public void setTimestamp(TimeCoordinate timestamp) {
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    /**
     * Legacy getter.
     * @return the timestamp
     */
    @Deprecated
    public TimeCoordinate getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the workers associated with this organigram.
     *
     * @return the workers
     */
    public Set<Worker> getWorkers() {
        return Collections.unmodifiableSet(workers);
    }

    /**
     * Adds a worker to the organigram.
     *
     * @param worker the worker to add
     */
    public void addWorker(Worker worker) {
        workers.add(Objects.requireNonNull(worker));
    }

    /**
     * Returns the hierarchical structure.
     *
     * @return the rooted tree of unit names
     */
    public RootedTree<String> getStructure() {
        return structure;
    }

    /**
     * Returns all workers in the hierarchy.
     *
     * @return a set of all workers
     */
    public Set<Worker> getAllWorkers() {
        return workers; // Currently all workers are in this set
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organigram that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

