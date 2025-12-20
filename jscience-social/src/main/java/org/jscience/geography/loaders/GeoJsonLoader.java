/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.geography.loaders;

import org.jscience.geography.Region;
import org.jscience.geography.Coordinate;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Basic GeoJSON Loader.
 * Uses simple string parsing to avoid heavy dependencies for now.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class GeoJsonLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public List<Region> load(InputStream input) throws IOException {
        List<Region> regions = new ArrayList<>();
        if (input == null)
            return regions;

        try {
            FeatureCollection collection = MAPPER.readValue(input, FeatureCollection.class);
            if (collection.features != null) {
                for (Feature f : collection.features) {
                    if (f.properties == null)
                        continue;

                    String name = (String) f.properties.getOrDefault("name",
                            f.properties.getOrDefault("NAME",
                                    f.properties.getOrDefault("admin",
                                            f.properties.getOrDefault("ADMIN", "Unknown"))));

                    Region region = new Region(name, Region.Type.COUNTRY); // Default to COUNTRY

                    // Try to map specific properties
                    if (f.properties.containsKey("pop_est")) {
                        Object pop = f.properties.get("pop_est");
                        if (pop instanceof Number) {
                            region.setPopulation(((Number) pop).longValue());
                        }
                    } else if (f.properties.containsKey("POP_EST")) {
                        Object pop = f.properties.get("POP_EST");
                        if (pop instanceof Number) {
                            region.setPopulation(((Number) pop).longValue());
                        }
                    }

                    // Geometry for center (only supporting Point for now)
                    if (f.geometry != null && "Point".equalsIgnoreCase(f.geometry.type)) {
                        JsonNode coords = f.geometry.coordinates;
                        if (coords != null && coords.isArray() && coords.size() >= 2) {
                            // GeoJSON is (lon, lat)
                            double lon = coords.get(0).asDouble();
                            double lat = coords.get(1).asDouble();
                            region.setCenter(new Coordinate(lat, lon));
                        }
                    }

                    regions.add(region);
                }
            }
        } catch (Exception e) {
            throw new IOException("Failed to parse GeoJSON", e);
        }

        return regions;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class FeatureCollection {
        public List<Feature> features;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Feature {
        public Geometry geometry;
        public java.util.Map<String, Object> properties;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Geometry {
        public String type;
        public JsonNode coordinates;
    }
}
