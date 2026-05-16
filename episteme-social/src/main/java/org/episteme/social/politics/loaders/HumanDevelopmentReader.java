/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.social.politics.loaders;

import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.util.persistence.PersistenceManager;
import org.episteme.social.politics.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader for UNDP Human Development Index (HDI) data.
 * <p>
 * Expects a CSV format: ISO3,CountryName,HDIValue
 * </p>
 */
public class HumanDevelopmentReader extends AbstractResourceReader<List<Country>> {

    private static final Logger LOG = LoggerFactory.getLogger(HumanDevelopmentReader.class);

    @Override
    protected List<Country> loadFromSource(String id) throws Exception {
        List<Country> updated = new ArrayList<>();
        
        try (InputStream is = getClass().getResourceAsStream(id)) {
            if (is == null) throw new Exception("Resource not found: " + id);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            
            // Skip header if present
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String iso3 = parts[0].trim();
                    String hdiStr = parts[2].trim();
                    
                    try {
                        double hdi = Double.parseDouble(hdiStr);
                        // Find country in persistence or create partial
                        // In Episteme, we often lookup by ISO code
                        Country country = findCountry(iso3);
                        if (country != null) {
                            country.setHdi(org.episteme.core.measure.Quantities.create(hdi, org.episteme.core.measure.Units.ONE));
                            PersistenceManager.getInstance().save(country);
                            updated.add(country);
                        }
                    } catch (NumberFormatException e) {
                        LOG.warn("Invalid HDI value for {}: {}", iso3, hdiStr);
                    }
                }
            }
        }
        
        return updated;
    }

    private Country findCountry(String iso3) {
        // Mock lookup - in real app we'd query PersistenceManager
        return null; 
    }

    @Override public String getName() { return "Human Development Index Reader"; }
    @Override public String getDescription() { return "Loads HDI data from UNDP sources."; }
    @Override public String getLongDescription() { return "Provides a loader for the UNDP Human Development Index (HDI) CSV data, enabling the enrichment of Country objects with socioeconomic metrics."; }
    @Override public String getCategory() { return "Social Science/Economics"; }
    @Override 
    @SuppressWarnings("unchecked")
    public Class<List<Country>> getResourceType() { return (Class<List<Country>>) (Class<?>) List.class; }
    @Override public String getResourcePath() { return "/data/economics/"; }
    @Override public String[] getSupportedVersions() { return new String[]{"2024"}; }
}
