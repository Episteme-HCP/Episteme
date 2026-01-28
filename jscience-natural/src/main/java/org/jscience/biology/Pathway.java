/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology;

import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a biological pathway - a series of biochemical reactions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Pathway implements ComprehensiveIdentification {
    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<BioChemicalReaction> reactions = new ArrayList<>();

    public Pathway(String name) {
        this.id = new SimpleIdentification("PATHWAY:" + UUID.randomUUID());
        setName(name);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public void addReaction(BioChemicalReaction reaction) {
        reactions.add(reaction);
    }

    public List<BioChemicalReaction> getReactions() {
        return reactions;
    }

    @Override
    public String toString() {
        return String.format("Pathway[%s, %d reactions]", getName(), reactions.size());
    }
}
