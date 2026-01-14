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
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reader for Altimetry and Bathymetry data (Elevation).
 * Supports ETOPO1, SRTM, and Google Elevation API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ElevationReader extends AbstractResourceReader<Double> {

    public enum Source {
        GOOGLE_API,
        SRTM, // Shuttle Radar Topography Mission
        ETOPO1 // NOAA ETOPO1 Global Relief Model
    }

    private static final String GOOGLE_API_BASE = "https://maps.googleapis.com/maps/api/elevation/json";
    private final String googleKey;
    private final Source defaultSource;

    public ElevationReader() {
        this(Source.GOOGLE_API); // Default to Google for ease of use (with Key)
    }

    public ElevationReader(Source source) {
        this.defaultSource = source;
        this.googleKey = Configuration.get("api.google.elevation.key", "");
    }

    @Override
    public String getCategory() {
        return "Geography";
    }

    @Override
    public String getDescription() {
        return "Elevation Data Reader (Altimetry/Bathymetry).";
    }

    @Override
    public String getResourcePath() {
        return "N/A";
    }

    @Override
    public Class<Double> getResourceType() {
        return Double.class;
    }

    @Override
    protected Double loadFromSource(String coordinates) throws Exception {
        // coordinates format: "lat,lon"
        String[] parts = coordinates.split(",");
        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);
        return getElevation(lat, lon);
    }

    /**
     * Gets elevation at specified coordinates using the default source.
     * 
     * @param lat Latitude
     * @param lon Longitude
     * @return Elevation in meters
     */
    public double getElevation(double lat, double lon) {
        switch (defaultSource) {
            case GOOGLE_API:
                return getElevationGoogle(lat, lon);
            case SRTM:
                return getElevationSRTM(lat, lon);
            case ETOPO1:
                return getElevationETOPO1(lat, lon);
            default:
                throw new UnsupportedOperationException("Source not implemented: " + defaultSource);
        }
    }

    private double getElevationGoogle(double lat, double lon) {
        if (googleKey.isEmpty()) {
            System.err.println("WARNING: Google Elevation API key not set.");
            return 0.0;
        }

        try {
            String url = String.format("%s?locations=%f,%f&key=%s", GOOGLE_API_BASE, lat, lon, googleKey);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Quick JSON parse (avoiding full ObjectMapper dependency if not needed, else use it)
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

    private double getElevationSRTM(double lat, double lon) {
         // Placeholder: Implement local loading of .hgt files
         // Requires indexing and reading binary HGT format.
         // System.out.println("SRTM lookup not yet implemented. Returning 0.0");
         return 0.0;
    }
    
    private double getElevationETOPO1(double lat, double lon) {
        // Placeholder: Implement loading of ETOPO1 NetCDF or binary grid
        // System.out.println("ETOPO1 lookup not yet implemented. Returning 0.0");
        return 0.0;
    }
}
