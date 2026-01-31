/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.biology.loaders.itis;

import java.util.List;

/**
 * Data Transfer Object for ITIS taxon records.
 */
public class ITISTaxon {
    private String tsn;
    private String parentTsn;
    private String scientificName;
    private String commonName;
    private String kingdom;
    private String phylum;
    private String taxonomicClass;
    private String order;
    private String family;
    private String genus;
    private String author;
    private String year;
    private String taxonomicUsage;
    private String credibilityRating;
    private List<String> vernacularNames;
    private List<String> synonyms;

    public ITISTaxon() {}

    public String getTsn() { return tsn; }
    public void setTsn(String tsn) { this.tsn = tsn; }

    public String getParentTsn() { return parentTsn; }
    public void setParentTsn(String parentTsn) { this.parentTsn = parentTsn; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getKingdom() { return kingdom; }
    public void setKingdom(String kingdom) { this.kingdom = kingdom; }

    public String getPhylum() { return phylum; }
    public void setPhylum(String phylum) { this.phylum = phylum; }

    public String getTaxonomicClass() { return taxonomicClass; }
    public void setTaxonomicClass(String taxonomicClass) { this.taxonomicClass = taxonomicClass; }

    public String getOrder() { return order; }
    public void setOrder(String order) { this.order = order; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public String getGenus() { return genus; }
    public void setGenus(String genus) { this.genus = genus; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getTaxonomicUsage() { return taxonomicUsage; }
    public void setTaxonomicUsage(String taxonomicUsage) { this.taxonomicUsage = taxonomicUsage; }

    public String getCredibilityRating() { return credibilityRating; }
    public void setCredibilityRating(String credibilityRating) { this.credibilityRating = credibilityRating; }

    public List<String> getVernacularNames() { return vernacularNames; }
    public void setVernacularNames(List<String> vernacularNames) { this.vernacularNames = vernacularNames; }

    public List<String> getSynonyms() { return synonyms; }
    public void setSynonyms(List<String> synonyms) { this.synonyms = synonyms; }
}

