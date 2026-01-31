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

package org.jscience.social.philosophy.epistemology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jscience.social.philosophy.Belief;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents an epistemic agent (human, organization, or AI) capable of 
 * holding beliefs and acquiring knowledge.
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Subject implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Belief> beliefs;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Knowledge> knowledgeBase;

    /**
     * Creates a new Subject.
     *
     * @param name the name of the subject
     */
    public Subject(String name) {
        this.id = new SimpleIdentification("Subject:" + UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Subject name cannot be null"));
        this.beliefs = new ArrayList<>();
        this.knowledgeBase = new ArrayList<>();
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
     * Adds a belief to the subject's belief system.
     * @param belief the belief to add
     */
    public void addBelief(Belief belief) {
        if (belief != null && !beliefs.contains(belief)) {
            beliefs.add(belief);
        }
    }

    /**
     * Adds knowledge to the subject's knowledge base.
     * Also adds the underlying belief if not already present.
     * @param knowledge the knowledge to add
     */
    public void addKnowledge(Knowledge knowledge) {
        if (knowledge != null && !knowledgeBase.contains(knowledge)) {
            knowledgeBase.add(knowledge);
            addBelief(knowledge.getBelief());
        }
    }

    /**
     * Returns an unmodifiable view of the subject's beliefs.
     * @return the list of beliefs
     */
    public List<Belief> getBeliefs() {
        return Collections.unmodifiableList(beliefs);
    }

    /**
     * Returns an unmodifiable view of the subject's knowledge base.
     * @return the list of knowledge
     */
    public List<Knowledge> getKnowledgeBase() {
        return Collections.unmodifiableList(knowledgeBase);
    }

    /**
     * Checks if the subject "knows" a certain proposition.
     * @param proposition the proposition to check
     * @return true if it exists in the knowledge base
     */
    public boolean knows(Proposition proposition) {
        return knowledgeBase.stream()
                .anyMatch(k -> k.getProposition().equals(proposition));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Subject: " + getName() + " (" + knowledgeBase.size() + " items known, " + beliefs.size() + " beliefs)";
    }
}

