/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.itis;

import org.jscience.biology.taxonomy.Species;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridge for converting ITIS DTOs to core JScience taxonomy objects.
 * <p>
 * ITIS (Integrated Taxonomic Information System) provides authoritative
 * taxonomic data. This bridge converts parsed ITIS data to JScience Species.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * ITIS API/XML → ITISReader → ITIS DTOs → ITISBridge → Core Objects
 *                                                       └── Species
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ITISBridge {

    /**
     * Converts ITIS taxon record to JScience Species.
     *
     * @param itisTaxon the ITIS taxon record
     * @return a fully populated Species object
     */
    public Species toSpecies(ITISTaxon itisTaxon) {
        if (itisTaxon == null) {
            return null;
        }
        
        String commonName = itisTaxon.getCommonName();
        String scientificName = itisTaxon.getScientificName();
        
        if (commonName == null || commonName.isEmpty()) {
            commonName = scientificName;
        }
        
        Species species = new Species(commonName, scientificName);
        
        // Set ITIS-specific identifiers
        species.setTrait("itis.tsn", itisTaxon.getTsn());
        species.setTrait("itis.usage", itisTaxon.getTaxonomicUsage());
        species.setTrait("itis.credibility", itisTaxon.getCredibilityRating());
        
        // Set taxonomic hierarchy
        if (itisTaxon.getKingdom() != null) {
            species.setKingdom(itisTaxon.getKingdom());
        }
        if (itisTaxon.getPhylum() != null) {
            species.setPhylum(itisTaxon.getPhylum());
        }
        if (itisTaxon.getTaxonomicClass() != null) {
            species.setTaxonomicClass(itisTaxon.getTaxonomicClass());
        }
        if (itisTaxon.getOrder() != null) {
            species.setOrder(itisTaxon.getOrder());
        }
        if (itisTaxon.getFamily() != null) {
            species.setFamily(itisTaxon.getFamily());
        }
        if (itisTaxon.getGenus() != null) {
            species.setGenus(itisTaxon.getGenus());
        }
        
        // Set author and year
        species.setTrait("author", itisTaxon.getAuthor());
        species.setTrait("publication.year", itisTaxon.getYear());
        
        // Set parent TSN for hierarchy building
        if (itisTaxon.getParentTsn() != null) {
            species.setTrait("itis.parent.tsn", itisTaxon.getParentTsn());
        }
        
        // Add vernacular names
        if (itisTaxon.getVernacularNames() != null) {
            species.setTrait("vernacular.names", itisTaxon.getVernacularNames());
        }
        
        // Add synonyms
        if (itisTaxon.getSynonyms() != null) {
            species.setTrait("synonyms", itisTaxon.getSynonyms());
        }
        
        return species;
    }

    /**
     * Converts multiple ITIS taxa to Species list.
     *
     * @param taxa list of ITIS taxon records
     * @return list of Species objects
     */
    public List<Species> toSpeciesList(List<ITISTaxon> taxa) {
        List<Species> species = new ArrayList<>();
        if (taxa != null) {
            for (ITISTaxon taxon : taxa) {
                Species s = toSpecies(taxon);
                if (s != null) {
                    species.add(s);
                }
            }
        }
        return species;
    }

    /**
     * Builds parent-child relationships between Species based on ITIS TSN hierarchy.
     *
     * @param speciesList list of Species with itis.tsn and itis.parent.tsn traits
     */
    public void buildHierarchy(List<Species> speciesList) {
        // Build TSN lookup map
        java.util.Map<String, Species> tsnMap = new java.util.HashMap<>();
        for (Species s : speciesList) {
            String tsn = (String) s.getTrait("itis.tsn");
            if (tsn != null) {
                tsnMap.put(tsn, s);
            }
        }
        
        // Link parents
        for (Species s : speciesList) {
            String parentTsn = (String) s.getTrait("itis.parent.tsn");
            if (parentTsn != null) {
                Species parent = tsnMap.get(parentTsn);
                if (parent != null) {
                    s.setAncestor(parent);
                }
            }
        }
    }
}
