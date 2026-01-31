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

package org.jscience.social.philosophy;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a well-defined group of philosophical ideas or schools of thought.
 * 
 * <p> A philosophical current consists of multiple models that together form 
 *      a cohesive system of belief.</p>
 * * @version 7.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class PhilosophicalCurrent extends Belief {

    private static final long serialVersionUID = 2L;

    /** The models belonging to this philosophical current. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Model> models = new HashSet<>();

    /**
     * Creates a new philosophical current with the specified name and comments.
     *
     * @param name     the name of the current
     * @param comments descriptive comments about the current
     */
    public PhilosophicalCurrent(String name, String comments) {
        super(name, comments);
    }

    /**
     * Returns the set of models associated with this current.
     *
     * @return the set of models
     */
    public Set<Model> getModels() {
        return models;
    }

    /**
     * Sets the models belonging to this current.
     *
     * @param models the set of models to associate
     * @throws IllegalArgumentException if the models set is null
     */
    public void setModels(Set<Model> models) {
        Objects.requireNonNull(models, "The set of models cannot be null");
        this.models.clear();
        this.models.addAll(models);
    }

    /**
     * Adds a model to this current.
     *
     * @param model the model to add
     */
    public void addModel(Model model) {
        if (model != null) {
            models.add(model);
        }
    }

    /**
     * Removes a model from this current.
     *
     * @param model the model to remove
     */
    public void removeModel(Model model) {
        models.remove(model);
    }

    /**
     * Retrieves all concepts associated with this current by aggregating 
     * concepts from all its models.
     *
     * @return a comprehensive set of concepts
     */
    public Set<Concept> getAllConcepts() {
        Set<Concept> result = new HashSet<>();
        for (Model model : models) {
            result.addAll(model.getAllConcepts());
        }
        return result;
    }
}

