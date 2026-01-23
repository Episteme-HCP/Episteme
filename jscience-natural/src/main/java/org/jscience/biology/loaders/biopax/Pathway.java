/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

import java.util.*;

/** Biological pathway. */
public class Pathway {
    private String rdfId;
    private String displayName;
    private String name;
    private String comment;
    private String organism;
    private final List<String> componentRefs = new ArrayList<>();

    public String getRdfId() { return rdfId; }
    public void setRdfId(String id) { this.rdfId = id; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String n) { this.displayName = n; }
    
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    
    public String getComment() { return comment; }
    public void setComment(String c) { this.comment = c; }
    
    public String getOrganism() { return organism; }
    public void setOrganism(String o) { this.organism = o; }
    
    public void addComponentRef(String ref) { componentRefs.add(ref); }
    public List<String> getComponentRefs() { return Collections.unmodifiableList(componentRefs); }
    
    @Override
    public String toString() { return "Pathway{" + displayName + ", components=" + componentRefs.size() + "}"; }
}
