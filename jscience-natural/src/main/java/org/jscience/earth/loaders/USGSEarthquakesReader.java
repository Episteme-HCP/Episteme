/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import org.jscience.earth.seismology.Earthquake;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.Configuration;
import org.jscience.ui.i18n.I18N;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Modern connector to USGS Earthquake Hazards Program API.
 * <p>
 * This reader retrieves seismic event data from USGS in GeoJSON format
 * and converts it into structured {@link Earthquake} domain objects.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://earthquake.usgs.gov/earthquakes/feed/">USGS Earthquake Hazards Program</a>
 */
public class USGSEarthquakesReader extends AbstractResourceReader<List<Earthquake>> {

    private static final Logger LOGGER = Logger.getLogger(USGSEarthquakesReader.class.getName());
    private static final String API_BASE = Configuration.get("api.usgs.earthquakes.base",
            "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary");

    private final ObjectMapper mapper = new ObjectMapper();

    public USGSEarthquakesReader() {
    }

    @Override
    public String getCategory() {
        return I18N.getInstance().get("reader.usgsearthquakes.category", "Earth");
    }

    @Override
    public String getName() {
        return I18N.getInstance().get("reader.usgsearthquakes.name", "USGS Earthquakes Reader");
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().get("reader.usgsearthquakes.desc", "Reads seismic events from the US Geological Survey API.");
    }

    @Override
    public String getLongDescription() {
        return I18N.getInstance().get("reader.usgsearthquakes.longdesc", 
                "Provides access to USGS Global Earthquake feeds, including significant monthly summaries and near real-time data.");
    }

    @Override
    public String getResourcePath() {
        return API_BASE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Earthquake>> getResourceType() {
        return (Class<List<Earthquake>>) (Class<?>) List.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"GeoJSON 1.0"};
    }

    /**
     * Loads earthquake data from a specific feed.
     * 
     * @param feedName feed name (e.g., "significant_month", "all_day")
     * @return List of parsed Earthquake objects
     */
    @Override
    protected List<Earthquake> loadFromSource(String feedName) throws Exception {
        String urlStr = API_BASE + "/" + feedName + ".geojson";
        String rawJSON = fetchUrl(urlStr);
        return parseGeoJSON(rawJSON);
    }

    private List<Earthquake> parseGeoJSON(String json) throws Exception {
        List<Earthquake> earthquakes = new ArrayList<>();
        JsonNode root = mapper.readTree(json);
        JsonNode features = root.path("features");

        if (features.isArray()) {
            for (JsonNode feature : features) {
                JsonNode props = feature.path("properties");
                JsonNode geom = feature.path("geometry");
                JsonNode coords = geom.path("coordinates");

                if (coords.isArray() && coords.size() >= 3) {
                    double lon = coords.get(0).asDouble();
                    double lat = coords.get(1).asDouble();
                    double depth = coords.get(2).asDouble();
                    double mag = props.path("mag").asDouble();

                    earthquakes.add(new Earthquake(lat, lon, mag, depth));
                }
            }
        }
        return earthquakes;
    }

    private String fetchUrl(String urlStr) throws Exception {
        URL url = URI.create(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("USGS API returned HTTP " + conn.getResponseCode());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        }
    }

    /**
     * Legacy method preserved for compatibility, now using the modernized backend.
     */
    public List<Earthquake> getSignificantMonth() {
        try {
            return load("significant_month");
        } catch (Exception e) {
            LOGGER.severe("Failed to load significant earthquakes: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
