/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.phyloxml;

/**
 * Evolutionary events at a clade node.
 */
public class Events {
    
    private String type;
    private int speciations;
    private int duplications;
    private int losses;

    public Events() {
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getSpeciations() { return speciations; }
    public void setSpeciations(int speciations) { this.speciations = speciations; }

    public int getDuplications() { return duplications; }
    public void setDuplications(int duplications) { this.duplications = duplications; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    @Override
    public String toString() {
        return "Events{type='" + type + "', speciations=" + speciations + 
               ", duplications=" + duplications + "}";
    }
}
