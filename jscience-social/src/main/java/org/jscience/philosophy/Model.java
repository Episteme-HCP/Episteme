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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a logical organization or cluster of interrelated concepts.
 * 
 * <p> Models are used to structure philosophical systems or scientific 
 *     theories, providing a framework for organizing conceptual networks.</p>
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 * * @version 7.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Model implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Concept> concepts = new HashSet<>();

    /**
     * Creates a new Model with the specified name.
     *
     * @param name the identifying name of the model
     * @throws IllegalArgumentException if name is null or empty
     */
    public Model(String name) {
        this.id = new SimpleIdentification("Model:" + UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Model name cannot be null"));
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Model name cannot be empty");
        }
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Model that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName() + " (" + concepts.size() + " concepts)";
    }
}
