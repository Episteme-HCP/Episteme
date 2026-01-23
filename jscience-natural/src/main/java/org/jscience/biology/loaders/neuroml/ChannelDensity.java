/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

/** Density of ion channels on membrane. */
public class ChannelDensity {
    private String id;
    private String ionChannel;
    private String condDensity;
    private String ion;
    private String erev;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getIonChannel() { return ionChannel; }
    public void setIonChannel(String ch) { this.ionChannel = ch; }
    
    public String getCondDensity() { return condDensity; }
    public void setCondDensity(String d) { this.condDensity = d; }
    
    public String getIon() { return ion; }
    public void setIon(String ion) { this.ion = ion; }
    
    public String getErev() { return erev; }
    public void setErev(String erev) { this.erev = erev; }
}
