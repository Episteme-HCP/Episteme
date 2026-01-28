/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;


/** 
 * BioPAX Interaction DTO.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BioPAXInteraction {
    private String rdfId;
    private String type;
    private String displayName;

    public String getRdfId() { return rdfId; }
    public void setRdfId(String id) { this.rdfId = id; }
    
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String n) { this.displayName = n; }
    
    @Override
    public String toString() { return (type != null ? type : "Interaction") + "{" + rdfId + "}"; }
}
