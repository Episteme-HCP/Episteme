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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a fundamental conviction or tenet within a philosophical system.
 * 
 * <p> Beliefs are the core units of conviction that form the basis of larger 
 *     philosophical frameworks and argumentations.</p>
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 * * @version 7.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Belief implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Concept> relatedConcepts = new HashSet<>();

    /**
     * Creates a new Belief.
     *
     * @param name     the name or title of the belief
     * @param comments descriptive text or rationale
     * @throws IllegalArgumentException if null or empty arguments are provided
     */
    public Belief(String name, String comments) {
        this.id = new SimpleIdentification("Belief:" + UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        setComments(Objects.requireNonNull(comments, "Comments cannot be null"));
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public void addRelatedConcept(Concept concept) {
        if (concept != null) relatedConcepts.add(concept);
    }

    public Set<Concept> getRelatedConcepts() {
        return Collections.unmodifiableSet(relatedConcepts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Belief that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName() + ": " + getComments();
    }
}

