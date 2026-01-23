/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.phyloxml;

/**
 * Represents a phylogenetic tree.
 */
public class Phylogeny {
    
    private String name;
    private String description;
    private boolean rooted;
    private String branchLengthUnit;
    private Clade rootClade;

    public Phylogeny() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isRooted() { return rooted; }
    public void setRooted(boolean rooted) { this.rooted = rooted; }

    public String getBranchLengthUnit() { return branchLengthUnit; }
    public void setBranchLengthUnit(String unit) { this.branchLengthUnit = unit; }

    public Clade getRootClade() { return rootClade; }
    public void setRootClade(Clade rootClade) { this.rootClade = rootClade; }

    /**
     * Returns the total number of terminal (leaf) nodes.
     */
    public int getLeafCount() {
        return rootClade != null ? rootClade.getLeafCount() : 0;
    }

    /**
     * Returns the maximum depth of the tree.
     */
    public int getMaxDepth() {
        return rootClade != null ? rootClade.getMaxDepth() : 0;
    }

    @Override
    public String toString() {
        return "Phylogeny{name='" + name + "', rooted=" + rooted + ", leaves=" + getLeafCount() + "}";
    }
}
