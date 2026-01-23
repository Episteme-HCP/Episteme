/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.pdbml;

/** Polymer entity (protein, nucleic acid). */
public class PolymerEntity {
    private String entityId;
    private String type;
    private String sequence;
    private String sequenceCanonical;

    public String getEntityId() { return entityId; }
    public void setEntityId(String id) { this.entityId = id; }
    
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    
    public String getSequence() { return sequence; }
    public void setSequence(String s) { this.sequence = s; }
    
    public String getSequenceCanonical() { return sequenceCanonical; }
    public void setSequenceCanonical(String s) { this.sequenceCanonical = s; }
    
    public int getSequenceLength() {
        return sequenceCanonical != null ? sequenceCanonical.replaceAll("\\s", "").length() : 0;
    }
    
    public boolean isProtein() {
        return type != null && type.toLowerCase().contains("polypeptide");
    }
    
    public boolean isNucleicAcid() {
        return type != null && (type.toLowerCase().contains("dna") || 
                                type.toLowerCase().contains("rna"));
    }
}
