/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.biology.loaders.biopax;

import java.util.*;

/** 
 * BioPAX Pathway DTO.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BioPAXPathway {
    private String rdfId;
    private String displayName;
    private String name;
    private String comment;
    private String organism;
    private String dataSource;
    private final List<BioPAXInteraction> pathwayComponents = new ArrayList<>();

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

    public String getDataSource() { return dataSource; }
    public void setDataSource(String ds) { this.dataSource = ds; }
    
    public void addComponent(BioPAXInteraction i) { if (i != null) pathwayComponents.add(i); }
    public List<BioPAXInteraction> getPathwayComponents() { return Collections.unmodifiableList(pathwayComponents); }
    
    @Override
    public String toString() { return "BioPAXPathway{" + displayName + ", components=" + pathwayComponents.size() + "}"; }
}

