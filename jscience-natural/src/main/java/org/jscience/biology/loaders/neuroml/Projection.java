/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

/** Synaptic projection between populations. */
public class Projection {
    private String id;
    private String presynapticPopulation;
    private String postsynapticPopulation;
    private String synapse;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getPresynapticPopulation() { return presynapticPopulation; }
    public void setPresynapticPopulation(String p) { this.presynapticPopulation = p; }
    
    public String getPostsynapticPopulation() { return postsynapticPopulation; }
    public void setPostsynapticPopulation(String p) { this.postsynapticPopulation = p; }
    
    public String getSynapse() { return synapse; }
    public void setSynapse(String s) { this.synapse = s; }
}
