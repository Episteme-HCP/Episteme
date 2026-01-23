/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.phyloxml;

/**
 * Taxonomic information for a clade.
 */
public class Taxonomy {
    
    private String id;
    private String idProvider;
    private String code;
    private String scientificName;
    private String commonName;
    private String rank;

    public Taxonomy() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdProvider() { return idProvider; }
    public void setIdProvider(String provider) { this.idProvider = provider; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String name) { this.scientificName = name; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String name) { this.commonName = name; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    @Override
    public String toString() {
        return "Taxonomy{" + scientificName + " (" + commonName + ")}";
    }
}
