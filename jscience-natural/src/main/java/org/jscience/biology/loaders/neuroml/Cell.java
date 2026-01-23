/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

/** Represents a neuron cell in NeuroML. */
public class Cell {
    private String id;
    private Morphology morphology;
    private BiophysicalProperties biophysicalProperties;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Morphology getMorphology() { return morphology; }
    public void setMorphology(Morphology m) { this.morphology = m; }
    
    public BiophysicalProperties getBiophysicalProperties() { return biophysicalProperties; }
    public void setBiophysicalProperties(BiophysicalProperties bp) { this.biophysicalProperties = bp; }
    
    @Override
    public String toString() { return "Cell{id='" + id + "'}"; }
}
