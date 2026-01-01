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
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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


