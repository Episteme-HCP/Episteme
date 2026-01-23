/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.pdbml;

/** Secondary structure element (helix, sheet). */
public class SecondaryStructure {
    private String id;
    private String type;
    private String beginChainId;
    private int beginSeqId;
    private String endChainId;
    private int endSeqId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    
    public String getBeginChainId() { return beginChainId; }
    public void setBeginChainId(String id) { this.beginChainId = id; }
    
    public int getBeginSeqId() { return beginSeqId; }
    public void setBeginSeqId(int id) { this.beginSeqId = id; }
    
    public String getEndChainId() { return endChainId; }
    public void setEndChainId(String id) { this.endChainId = id; }
    
    public int getEndSeqId() { return endSeqId; }
    public void setEndSeqId(int id) { this.endSeqId = id; }
    
    public int getLength() { return endSeqId - beginSeqId + 1; }
    
    public boolean isHelix() { return type != null && type.contains("HELIX"); }
    public boolean isSheet() { return "SHEET".equals(type); }
}
