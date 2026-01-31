/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.biology.loaders.phyloxml;

/**
 * Represents evolutionary events in a PhyloXML document.
 */
public class PhyloXMLEvents {
    private Integer speciations;
    private Integer duplications;
    private String type;

    public PhyloXMLEvents() {
    }

    public Integer getSpeciations() { return speciations; }
    public void setSpeciations(Integer speciations) { this.speciations = speciations; }

    public Integer getDuplications() { return duplications; }
    public void setDuplications(Integer duplications) { this.duplications = duplications; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

