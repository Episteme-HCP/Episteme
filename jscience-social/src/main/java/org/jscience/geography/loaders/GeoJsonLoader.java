/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.geography.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.geography.Coordinate;
import org.jscience.geography.Region;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads geographic data from GeoJSON format.
 * Supports loading regions/features with coordinates and properties.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeoJsonLoader implements org.jscience.io.InputLoader<List<org.jscience.geography.Region>> {

    @Override
    public List<org.jscience.geography.Region> load(String resourceId) throws java.io.IOException {
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            return loadRegions(is);
        }
    }

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<org.jscience.geography.Region>> getResourceType() {
        return (Class<List<org.jscience.geography.Region>>) (Class<?>) List.class;
    }

    private final ObjectMapper mapper;

    public GeoJsonLoader() {
        this.mapper = new ObjectMapper();
    }

    /**
     * Loads regions from a GeoJSON FeatureCollection.
     *
     * @param is Input stream containing GeoJSON data
     * @return List of Region objects
     * @throws IOException on parse error
     */
    public List<Region> loadRegions(InputStream is) throws IOException {
        List<Region> regions = new ArrayList<>();
        if (is == null)
            return regions;

        JsonNode root = mapper.readTree(is);
        JsonNode features = root.path("features");

        if (features.isArray()) {
            for (JsonNode feature : features) {
                Region region = parseFeature(feature);
                if (region != null) {
                    regions.add(region);
                }
            }
        }

        return regions;
    }

    /**
     * Parses a single GeoJSON Feature into a Region.
     */
    private Region parseFeature(JsonNode feature) {
        JsonNode properties = feature.path("properties");
        JsonNode geometry = feature.path("geometry");

        String name = properties.path("name").asText(null);
        if (name == null) {
            name = properties.path("NAME").asText(null);
        }
        if (name == null) {
            name = "Unknown";
        }

        Region.Type type = Region.Type.COUNTRY; // Default
        String typeStr = properties.path("type").asText("");
        if (!typeStr.isEmpty()) {
            try {
                type = Region.Type.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Use default
            }
        }

        Region region = new Region(name, type);

        // Extract population if present
        if (properties.has("population")) {
            region.setPopulation(properties.path("population").asLong(0));
        }

        // Extract center coordinate from geometry
        String geometryType = geometry.path("type").asText("");
        JsonNode coordinates = geometry.path("coordinates");

        if ("Point".equals(geometryType) && coordinates.isArray() && coordinates.size() >= 2) {
            double lon = coordinates.get(0).asDouble();
            double lat = coordinates.get(1).asDouble();
            region.setCenter(new Coordinate(lat, lon));
        } else if ("Polygon".equals(geometryType) || "MultiPolygon".equals(geometryType)) {
            // For polygons, compute centroid from first ring
            Coordinate centroid = computeCentroid(coordinates, geometryType);
            if (centroid != null) {
                region.setCenter(centroid);
            }
        }

        return region;
    }

    /**
     * Computes approximate centroid of a polygon.
     */
    private Coordinate computeCentroid(JsonNode coordinates, String type) {
        try {
            JsonNode ring;
            if ("MultiPolygon".equals(type)) {
                // First polygon, first ring
                ring = coordinates.get(0).get(0);
            } else {
                // First ring
                ring = coordinates.get(0);
            }

            if (ring == null || !ring.isArray() || ring.isEmpty()) {
                return null;
            }

            double sumLat = 0, sumLon = 0;
            int count = 0;
            for (JsonNode point : ring) {
                if (point.isArray() && point.size() >= 2) {
                    sumLon += point.get(0).asDouble();
                    sumLat += point.get(1).asDouble();
                    count++;
                }
            }

            if (count > 0) {
                return new Coordinate(sumLat / count, sumLon / count);
            }
        } catch (Exception e) {
            // Ignore and return null
        }
        return null;
    }

    /**
     * Loads raw coordinate pairs from a GeoJSON geometry.
     *
     * @param is Input stream containing GeoJSON data
     * @return List of Coordinate objects
     * @throws IOException on parse error
     */
    public List<Coordinate> loadCoordinates(InputStream is) throws IOException {
        List<Coordinate> coords = new ArrayList<>();
        if (is == null)
            return coords;

        JsonNode root = mapper.readTree(is);
        extractCoordinates(root, coords);
        return coords;
    }

    private void extractCoordinates(JsonNode node, List<Coordinate> coords) {
        if (node.isArray()) {
            // Check if this is a coordinate pair [lon, lat]
            if (node.size() >= 2 && node.get(0).isNumber() && node.get(1).isNumber()) {
                double lon = node.get(0).asDouble();
                double lat = node.get(1).asDouble();
                coords.add(new Coordinate(lat, lon));
            } else {
                // Recurse into nested arrays
                for (JsonNode child : node) {
                    extractCoordinates(child, coords);
                }
            }
        } else if (node.has("coordinates")) {
            extractCoordinates(node.path("coordinates"), coords);
        } else if (node.has("features")) {
            for (JsonNode feature : node.path("features")) {
                extractCoordinates(feature.path("geometry"), coords);
            }
        }
    }
}
