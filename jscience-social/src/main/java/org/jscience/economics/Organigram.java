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

import org.jscience.util.NAryTree;
import org.jscience.util.Named;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a formal organizational structure (organigram).
 * This defines the intended hierarchy and grouping of workers within an organization.
 * It is structured as an N-Ary tree of organizational units.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Organigram extends NAryTree<String> implements Named {

    /** Workers associated with this specific level of the organigram. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Worker> workers;

    /**
     * Creates a new Organigram level.
     *
     * @param name the name of this organizational unit
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public Organigram(String name) {
        super(Objects.requireNonNull(name, "Name cannot be null"));
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.workers = new HashSet<>();
    }

    /**
     * Creates a new Organigram level with an initial set of workers.
     *
     * @param name    the name of this organizational unit
     * @param workers the initial workers
     * @throws NullPointerException if name or workers is null
     * @throws IllegalArgumentException if name is empty
     */
    public Organigram(String name, Set<Worker> workers) {
        super(Objects.requireNonNull(name, "Name cannot be null"));
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.workers = new HashSet<>(Objects.requireNonNull(workers, "Workers set cannot be null"));
    }

    /**
     * Returns the name of this organizational unit.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return getContents();
    }

    /**
     * Sets the name of this organizational unit.
     *
     * @param name the new name
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public void setName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        setContents(name);
    }

    /**
     * Returns an unmodifiable view of the workers at this level.
     *
     * @return the workers
     */
    public Set<Worker> getWorkers() {
        return Collections.unmodifiableSet(workers);
    }

    /**
     * Adds a worker to this level.
     *
     * @param worker the worker to add
     * @throws NullPointerException if worker is null
     */
    public void addWorker(Worker worker) {
        workers.add(Objects.requireNonNull(worker, "Worker cannot be null"));
    }

    /**
     * Removes a worker from this level.
     *
     * @param worker the worker to remove
     */
    public void removeWorker(Worker worker) {
        workers.remove(worker);
    }

    /**
     * Sets the set of workers for this level.
     *
     * @param workers the new set of workers
     * @throws NullPointerException if workers is null
     */
    public void setWorkers(Set<Worker> workers) {
        this.workers = new HashSet<>(Objects.requireNonNull(workers, "Workers set cannot be null"));
    }

    /**
     * Returns all workers in this organigram level and all its sub-levels.
     *
     * @return a set of all workers in the hierarchy
     */
    public Set<Worker> getAllWorkers() {
        Set<Worker> result = new HashSet<>(workers);

        for (NAryTree<String> child : getChildren()) {
            if (child instanceof Organigram organigram) {
                result.addAll(organigram.getAllWorkers());
            }
        }

        return result;
    }
}
