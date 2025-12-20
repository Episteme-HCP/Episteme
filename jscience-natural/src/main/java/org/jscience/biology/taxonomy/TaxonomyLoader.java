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
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class TaxonomyLoader {

    public static List<Species> load(String resourcePath) {
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
                System.err.println("Taxonomy resource not found: " + resourcePath);
                return speciesList;
            }

            JsonNode root = mapper.readTree(is);

            // Map for linking ancestors by ID/Name
            Map<String, Species> cache = new HashMap<>();

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

            // Link ancestors if provided in JSON
            // (Assumes partial ordering or two-pass if IDs used)
            // For now, simple list load.

            return speciesList;
        } catch (Exception e) {
            e.printStackTrace();
            return speciesList;
        }
    }

    private static Species parseSpecies(JsonNode node) {
        String common = node.has("common_name") ? node.get("common_name").asText() : "";
        String scientific = node.has("scientific_name") ? node.get("scientific_name").asText() : "Unknown";

        Species s = new Species(common, scientific);

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
