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
package org.jscience.philosophy;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.util.Named;

/**
 * Represents a logical organization or cluster of interrelated concepts.
 * 
 * <p> Models are used to structure philosophical systems or scientific 
 *     theories, providing a framework for organizing conceptual networks.</p>
 *
 * @author <a href="mailto:silvere.martin-michiellot@jscience.org">Silvere Martin-Michiellot</a>
 * @author Gemini AI (Google DeepMind)
 * @version 6.0, July 21, 2014
 */
public class Model implements Named, Serializable {


    private static final long serialVersionUID = 1L;

    private final String name;
    private final Set<Concept> concepts = new HashSet<>();

    /**
     * Creates a new Model with the specified name.
     *
     * @param name the identifying name of the model
     * @throws IllegalArgumentException if name is null or empty
     */
    public Model(String name) {
        this.name = Objects.requireNonNull(name, "Model name cannot be null");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Model name cannot be empty");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable set of direct concepts in this model.
     * @return set of concepts
     */
    public Set<Concept> getConcepts() {
        return Collections.unmodifiableSet(concepts);
    }

    /**
     * Adds a concept to the model.
     * @param concept the concept to add
     * @throws NullPointerException if concept is null
     */
    public void addConcept(Concept concept) {
        concepts.add(Objects.requireNonNull(concept, "Concept cannot be null"));
    }

    /**
     * Removes a concept from the model.
     * @param concept the concept to remove
     */
    public void removeConcept(Concept concept) {
        concepts.remove(concept);
    }

    /**
     * Retrieves all unique concepts, including those related to the direct concepts 
     * in this model, through recursive traversal.
     * 
     * @return a comprehensive set of all concepts in the logical graph
     */
    public Set<Concept> getAllConcepts() {
        Set<Concept> result = new HashSet<>();
        for (Concept concept : concepts) {
            result.addAll(concept.getAllConcepts());
        }
        return result;
    }

    @Override
    public String toString() {
        return name + " (" + concepts.size() + " concepts)";
    }
}
