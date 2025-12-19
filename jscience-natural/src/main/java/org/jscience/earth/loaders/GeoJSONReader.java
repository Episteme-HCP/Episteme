package org.jscience.earth.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.earth.coordinates.GeodeticCoordinate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reader for GeoJSON Point features.
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public class GeoJSONReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Reads a GeoJSON FeatureCollection and extracts Point geometries as
     * GeodeticCoordinates.
     * 
     * @param inputStream the input stream containing GeoJSON
     * @return list of coordinates
     * @throws IOException if reading fails
     */
    public static List<GeodeticCoordinate> readPoints(InputStream inputStream) throws IOException {
        JsonNode root = MAPPER.readTree(inputStream);
        List<GeodeticCoordinate> points = new ArrayList<>();

        if (root.has("features") && root.get("features").isArray()) {
            for (JsonNode feature : root.get("features")) {
                JsonNode geometry = feature.get("geometry");
                if (geometry != null && "Point".equalsIgnoreCase(geometry.get("type").asText())) {
                    JsonNode coords = geometry.get("coordinates");
                    if (coords != null && coords.isArray() && coords.size() >= 2) {
                        double lon = coords.get(0).asDouble();
                        double lat = coords.get(1).asDouble();
                        double alt = (coords.size() > 2) ? coords.get(2).asDouble() : 0.0;

                        points.add(new GeodeticCoordinate(lat, lon, alt));
                    }
                }
            }
        }
        return Collections.unmodifiableList(points);
    }
}
