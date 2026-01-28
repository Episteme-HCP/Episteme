/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.phyloxml;

/**
 * Represents taxonomic information in a PhyloXML document.
 */
public class PhyloXMLTaxonomy {
    private String scientificName;
    private String commonName;
    private String rank;
    private String code;
    private String id;
    private String idProvider;

    public PhyloXMLTaxonomy() {
    }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdProvider() { return idProvider; }
    public void setIdProvider(String idProvider) { this.idProvider = idProvider; }
}
