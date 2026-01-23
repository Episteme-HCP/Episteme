/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.phyloxml;

/**
 * Sequence information associated with a clade.
 */
public class SequenceInfo {
    
    private String name;
    private String symbol;
    private String accession;
    private String location;
    private String molSeq;

    public SequenceInfo() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getAccession() { return accession; }
    public void setAccession(String accession) { this.accession = accession; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getMolSeq() { return molSeq; }
    public void setMolSeq(String molSeq) { this.molSeq = molSeq; }

    @Override
    public String toString() {
        return "SequenceInfo{name='" + name + "', accession='" + accession + "'}";
    }
}
