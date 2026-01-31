/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.biology.loaders.phyloxml;

import java.util.*;

/**
 * Represents a clade (node) in a PhyloXML document.
 */
public class PhyloXMLClade {
    
    private String name;
    private Double branchLength;
    private final Map<String, Double> confidenceValues = new HashMap<>();
    private PhyloXMLTaxonomy taxonomy;
    private PhyloXMLSequenceInfo sequence;
    private PhyloXMLEvents events;
    private final List<PhyloXMLClade> children = new ArrayList<>();

    public PhyloXMLClade() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getBranchLength() { return branchLength; }
    public void setBranchLength(Double branchLength) { this.branchLength = branchLength; }

    public void addConfidence(String type, Double value) {
        confidenceValues.put(type, value);
    }

    public Map<String, Double> getConfidenceValues() {
        return Collections.unmodifiableMap(confidenceValues);
    }

    public PhyloXMLTaxonomy getTaxonomy() { return taxonomy; }
    public void setTaxonomy(PhyloXMLTaxonomy taxonomy) { this.taxonomy = taxonomy; }

    public PhyloXMLSequenceInfo getSequence() { return sequence; }
    public void setSequence(PhyloXMLSequenceInfo sequence) { this.sequence = sequence; }

    public PhyloXMLEvents getEvents() { return events; }
    public void setEvents(PhyloXMLEvents events) { this.events = events; }

    public List<PhyloXMLClade> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(PhyloXMLClade child) {
        if (child != null) {
            children.add(child);
        }
    }
}

