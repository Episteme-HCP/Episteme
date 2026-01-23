/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

import java.util.*;

/** Molecular interaction (reaction, catalysis). */
public class Interaction {
    private String rdfId;
    private String type;
    private String displayName;
    private final List<String> leftRefs = new ArrayList<>();
    private final List<String> rightRefs = new ArrayList<>();
    private String controllerRef;
    private String controlledRef;

    public String getRdfId() { return rdfId; }
    public void setRdfId(String id) { this.rdfId = id; }
    
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String n) { this.displayName = n; }
    
    public void addLeftRef(String ref) { leftRefs.add(ref); }
    public List<String> getLeftRefs() { return Collections.unmodifiableList(leftRefs); }
    
    public void addRightRef(String ref) { rightRefs.add(ref); }
    public List<String> getRightRefs() { return Collections.unmodifiableList(rightRefs); }
    
    public String getControllerRef() { return controllerRef; }
    public void setControllerRef(String ref) { this.controllerRef = ref; }
    
    public String getControlledRef() { return controlledRef; }
    public void setControlledRef(String ref) { this.controlledRef = ref; }
    
    @Override
    public String toString() { return type + "{" + rdfId + "}"; }
}
