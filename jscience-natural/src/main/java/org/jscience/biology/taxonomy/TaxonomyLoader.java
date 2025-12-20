/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Loads species taxonomy data from JSON resources.
 * Consolidates functionality from former loaders package.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class TaxonomyLoader {

    private static final String DEFAULT_RESOURCE = "/org/jscience/biology/taxonomy/species.json";

    // Static cache for the singleton-like usage pattern
    private static final Map<String, Species> cache = new HashMap<>();
    private static boolean loaded = false;

    /**
     * Loads species from the default JSON resource.
     * Ensures data is loaded only once per session unless clearCache is called.
     * 
     * @return List of loaded species.
     */
    public static synchronized List<Species> loadAll() {
        if (!loaded) {
            List<Species> loadedList = loadFromResource(DEFAULT_RESOURCE);
            // Cache is populated during loadFromResource
            loaded = true;
            return loadedList;
        }
        return new ArrayList<>(cache.values());
    }

    /**
     * Retrieves a species by its scientific name.
     * 
     * @param scientificName e.g., "Escherichia coli"
     * @return The Species object, or null if not found.
     */
    public static synchronized Species getSpecies(String scientificName) {
        if (!loaded) {
            loadAll();
        }
        return cache.get(scientificName);
    }

    /**
     * Clears the internal cache and resets loaded state.
     */
    public static synchronized void clearCache() {
        cache.clear();
        loaded = false;
    }

    /**
     * Loads species from a specific resource path.
     * 
     * @param resourcePath Classpath to the JSON file.
     * @return List of species found in the file.
     */
    public static List<Species> loadFromResource(String resourcePath) {
        List<Species> speciesList = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = TaxonomyLoader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                is = TaxonomyLoader.class.getClassLoader().getResourceAsStream(resourcePath);
            }
            if (is == null && !resourcePath.startsWith("/")) {
                is = TaxonomyLoader.class.getResourceAsStream("/" + resourcePath);
            }

            if (is == null) {
                System.err.println("TaxonomyLoader: Resource not found: " + resourcePath);
                return speciesList;
            }

            JsonNode root = mapper.readTree(is);

            if (root.isArray()) {
                for (JsonNode node : root) {
                    Species s = parseSpecies(node);
                    speciesList.add(s);
                    cache.put(s.getScientificName(), s);
                }
            } else if (root.has("species")) {
                for (JsonNode node : root.get("species")) {
                    Species s = parseSpecies(node);
                    speciesList.add(s);
                    cache.put(s.getScientificName(), s);
                }
            }

            return speciesList;
        } catch (Exception e) {
            e.printStackTrace();
            return speciesList;
        }
    }

    private static Species parseSpecies(JsonNode node) {
        String commonName = node.has("commonName") ? node.get("commonName").asText()
                : (node.has("common_name") ? node.get("common_name").asText() : "");

        String scientificName = node.has("scientificName") ? node.get("scientificName").asText()
                : (node.has("scientific_name") ? node.get("scientific_name").asText() : "");

        Species s = new Species(commonName, scientificName);

        if (node.has("kingdom"))
            s.setKingdom(node.get("kingdom").asText());
        if (node.has("phylum"))
            s.setPhylum(node.get("phylum").asText());
        if (node.has("class"))
            s.setTaxonomicClass(node.get("class").asText());
        if (node.has("order"))
            s.setOrder(node.get("order").asText());
        if (node.has("family"))
            s.setFamily(node.get("family").asText());
        if (node.has("genus"))
            s.setGenus(node.get("genus").asText());

        if (node.has("conservation")) {
            String code = node.get("conservation").asText();
            for (Species.ConservationStatus status : Species.ConservationStatus.values()) {
                if (status.getCode().equalsIgnoreCase(code)) {
                    s.setConservationStatus(status);
                    break;
                }
            }
        }

        if (node.has("attributes")) {
            JsonNode attrs = node.get("attributes");
            Iterator<String> fieldNames = attrs.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                s.addAttribute(key, attrs.get(key).asText());
            }
        }

        return s;
    }
}
