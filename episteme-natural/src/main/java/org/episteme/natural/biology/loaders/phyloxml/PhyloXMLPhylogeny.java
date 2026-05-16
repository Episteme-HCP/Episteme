/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.loaders.phyloxml;

/**
 * Represents a phylogeny in a PhyloXML document.
 */
public class PhyloXMLPhylogeny {
    private String name;
    private String description;
    private boolean rooted;
    private String branchLengthUnit;
    private PhyloXMLClade rootClade;

    public PhyloXMLPhylogeny() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isRooted() { return rooted; }
    public void setRooted(boolean rooted) { this.rooted = rooted; }

    public String getBranchLengthUnit() { return branchLengthUnit; }
    public void setBranchLengthUnit(String branchLengthUnit) { this.branchLengthUnit = branchLengthUnit; }

    public PhyloXMLClade getClade() { return rootClade; }
    public void setRootClade(PhyloXMLClade rootClade) { this.rootClade = rootClade; }
}

