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

package org.jscience.geography.loaders;

import org.jscience.io.AbstractResourceReader;
import org.jscience.io.Configuration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Reader for Google Maps Elevation API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GoogleElevationReader extends AbstractResourceReader<Double> {

    private static final String API_BASE = Configuration.get("api.google.elevation.base", "https://maps.googleapis.com/maps/api/elevation/json");
    private final String apiKey;

    public GoogleElevationReader() {
        this.apiKey = Configuration.get("api.google.elevation.key", "");
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.geography", "Geography");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.googleelevation.desc", "Google Elevation API Reader.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.googleelevation.longdesc", "Retrieves elevation data from the Google Maps Elevation API.");
    }

    @Override
    public String getResourcePath() {
        return API_BASE;
    }

    @Override
    public Class<Double> getResourceType() {
        return Double.class;
    }

    @Override
    protected Double loadFromSource(String coordinates) throws Exception {
        // coordinates: "lat,lon"
        String[] parts = coordinates.split(",");
        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);
        return getElevation(lat, lon);
    }

    public double getElevation(double lat, double lon) {
        if (apiKey.isEmpty()) {
            System.err.println("WARNING: Google Elevation API key not set.");
            return 0.0;
        }

        try {
            String url = String.format("%s?locations=%f,%f&key=%s", API_BASE, lat, lon, apiKey);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String body = response.body();
                int idx = body.indexOf("\"elevation\" :");
                if (idx != -1) {
                    int start = idx + 13;
                    int end = body.indexOf(",", start);
                    String val = body.substring(start, end).trim();
                    return Double.parseDouble(val);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("reader.googleelevation.name", "Google Elevation Reader"); }
}
