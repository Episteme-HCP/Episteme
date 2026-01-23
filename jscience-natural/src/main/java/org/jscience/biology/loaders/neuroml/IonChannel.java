/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

/** Ion channel model. */
public class IonChannel {
    private String id;
    private String species;
    private String conductance;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getSpecies() { return species; }
    public void setSpecies(String s) { this.species = s; }
    
    public String getConductance() { return conductance; }
    public void setConductance(String c) { this.conductance = c; }
}
