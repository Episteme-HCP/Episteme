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

package org.jscience.economics;

import org.jscience.mathematics.discrete.RootedTree;
import org.jscience.util.Named;
import org.jscience.util.Commented;
import org.jscience.util.Temporal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.identity.SimpleIdentification;

/**
 * Represents a formal organizational structure (organigram).
 * This defines the intended hierarchy and grouping of workers within an organization.
 * Uses the graph-based RootedTree for hierarchical representation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Organigram implements Named, Identified<Identification>, Commented, Temporal {

    @Id
    private Identification identification;

    @Attribute
    private String name;

    @Attribute
    private String comments;

    @Attribute
    private Instant timestamp;

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
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.identification = new SimpleIdentification("Organigram:" + name);
        this.timestamp = Instant.now();
        this.structure = new RootedTree<>(name);
        this.workers = new HashSet<>();
    }

    /**
     * Returns the name of this organigram.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this organigram.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public Identification getId() {
        return identification;
    }

    @Override
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public Instant getTimestamp() {
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
}
