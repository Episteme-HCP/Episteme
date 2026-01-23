/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

/** Synapse model (exponential decay). */
public class Synapse {
    private String id;
    private String tauRise;
    private String tauDecay;
    private String erev;
    private String gbase;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTauRise() { return tauRise; }
    public void setTauRise(String t) { this.tauRise = t; }
    
    public String getTauDecay() { return tauDecay; }
    public void setTauDecay(String t) { this.tauDecay = t; }
    
    public String getErev() { return erev; }
    public void setErev(String e) { this.erev = e; }
    
    public String getGbase() { return gbase; }
    public void setGbase(String g) { this.gbase = g; }
}
