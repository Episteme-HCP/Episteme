/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.loaders.phyloxml;

/**
 * Represents sequence associations in a PhyloXML document.
 */
public class PhyloXMLSequenceInfo {
    private String name;
    private String symbol;
    private String accession;
    private String location;
    private String molSeq;

    public PhyloXMLSequenceInfo() {
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
}

