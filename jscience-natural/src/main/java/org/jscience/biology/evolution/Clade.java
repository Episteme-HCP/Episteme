/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.evolution;

import org.jscience.biology.taxonomy.Species;
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
 * Represents a clade in a phylogenetic tree.
 * <p>
 * A clade is a group of organisms that consists of a common ancestor and all its lineal descendants.
 * In a phylogenetic tree, it corresponds to a node and the subtree rooted at that node.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Clade implements ComprehensiveIdentification {
    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private String name;

    @Attribute
    private Double branchLength;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Species species;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Clade> children = new ArrayList<>();

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    public Clade(String name) {
        this.id = new SimpleIdentification("CLADE:" + UUID.randomUUID());
        this.name = name;
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBranchLength() {
        return branchLength;
    }

    public void setBranchLength(Double branchLength) {
        this.branchLength = branchLength;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public void addChild(Clade child) {
        children.add(child);
    }

    public List<Clade> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Clade[%s]", name != null ? name : "unnamed");
    }
}
