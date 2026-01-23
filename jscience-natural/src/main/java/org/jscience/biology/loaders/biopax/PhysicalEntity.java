/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

import java.util.*;

/** Physical entity (protein, small molecule, complex). */
public class PhysicalEntity {
    private String rdfId;
    private String type;
    private String displayName;
    private String name;
    private String comment;
    private final List<String> componentRefs = new ArrayList<>();

    public String getRdfId() { return rdfId; }
    public void setRdfId(String id) { this.rdfId = id; }
    
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String n) { this.displayName = n; }
    
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    
    public String getComment() { return comment; }
    public void setComment(String c) { this.comment = c; }
    
    public void addComponentRef(String ref) { componentRefs.add(ref); }
    public List<String> getComponentRefs() { return Collections.unmodifiableList(componentRefs); }
    
    @Override
    public String toString() { return type + "{" + displayName + "}"; }
}
