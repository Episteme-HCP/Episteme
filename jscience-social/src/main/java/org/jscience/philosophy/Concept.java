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
 * Represents a key idea or element within a philosophical framework.
 * 
 * <p> Concepts can be interlinked to form a semantic network, allowing for
 *     complex conceptual mapping and analysis of philosophical systems.</p>
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 * * @version 7.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Concept implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Concept> relatedConcepts = new HashSet<>();

    /**
     * Creates a new Concept.
     *
     * @param name the name of the concept
     * @throws IllegalArgumentException if name is null or empty
     */
    public Concept(String name) {
        this(name, "");
    }

    /**
     * Rich constructor with description.
     */
    public Concept(String name, String description) {
        this.id = new SimpleIdentification("Concept:" + UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Concept name cannot be null"));
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Concept name cannot be empty");
        }
        setComments(description);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getDescription() {
        return getComments();
    }

    public void setDescription(String description) {
        setComments(description);
    }

    /**
     * Retrieves the set of directly related concepts.
     * @return unmodifiable set of related concepts
     */
    public Set<Concept> getRelatedConcepts() {
        return Collections.unmodifiableSet(relatedConcepts);
    }

    /**
     * Adds a relation to another concept.
     * @param concept the concept to relate to
     */
    public void addRelatedConcept(Concept concept) {
        if (concept != null && concept != this) {
            relatedConcepts.add(concept);
        }
    }

    /**
     * Removes a relation to another concept.
     * @param concept the concept to unlink
     */
    public void removeRelatedConcept(Concept concept) {
        relatedConcepts.remove(concept);
    }

    /**
     * Recursively retrieves all reachable concepts from this concept.
     * Performs a breadth-first traversal.
     * 
     * @return set of all reachable concepts (including this one)
     */
    public Set<Concept> getAllConcepts() {
        Set<Concept> result = new HashSet<>();
        Set<Concept> visited = new HashSet<>();
        
        // Initialize with self
        result.add(this);
        Set<Concept> currentLevel = new HashSet<>();
        currentLevel.add(this);
        
        while (!currentLevel.isEmpty()) {
            visited.addAll(currentLevel);
            Set<Concept> nextLevel = new HashSet<>();
            
            for (Concept c : currentLevel) {
                for (Concept related : c.relatedConcepts) {
                    if (!visited.contains(related) && !result.contains(related)) {
                        result.add(related);
                        nextLevel.add(related);
                    }
                }
            }
            currentLevel = nextLevel;
        }
        
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}
