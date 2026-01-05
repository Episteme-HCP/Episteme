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

package org.jscience.physics.loaders;

import org.jscience.io.AbstractResourceReader;

import java.io.*;
import java.net.http.*;
import java.net.URI;
import java.util.*;
import com.fasterxml.jackson.databind.*;

/**
 * Loader for SIMBAD astronomical database.
 * <p>
 * Fetches astronomical object data from CDS SIMBAD.
 * Uses the Sesame name resolver for object identification.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimbadLoader extends AbstractResourceReader<SimbadLoader.AstronomicalObject> {

    @Override
    protected AstronomicalObject loadFromSource(String id) throws Exception {
        return resolveByName(id).orElse(null);
    }

    @Override
    public String getResourcePath() {
        return "http://simbad.u-strasbg.fr"; // API-based, not file-based
    }

    @Override
    public Class<AstronomicalObject> getResourceType() {
        return AstronomicalObject.class;
    }

    private static final String BASE_URL;
    private static final String TAP_URL = "http://simbad.u-strasbg.fr/simbad/sim-tap/sync";

    static {
        Properties props = new Properties();
        try (InputStream is = SimbadLoader.class.getResourceAsStream("/jscience.properties")) {
            if (is != null)
                props.load(is);
        } catch (Exception e) {
            /* ignore */ }
        BASE_URL = props.getProperty("api.simbad.base", "http://cdsweb.u-strasbg.fr/cgi-bin/nph-sesame");
    }

    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * Represents an astronomical object from SIMBAD.
     */
    public static class AstronomicalObject {
        public final String name;
        public final String objectType;
        public final double ra; // Right Ascension in degrees
        public final double dec; // Declination in degrees
        public final Double magnitude; // Visual magnitude
        public final String spectralType;

        public AstronomicalObject(String name, String objectType, double ra, double dec,
                Double magnitude, String spectralType) {
            this.name = name;
            this.objectType = objectType;
            this.ra = ra;
            this.dec = dec;
            this.magnitude = magnitude;
            this.spectralType = spectralType;
        }

        /**
         * Returns Right Ascension in HMS format.
         */
        public String getRaHMS() {
            double hours = ra / 15.0;
            int h = (int) hours;
            double minutes = (hours - h) * 60;
            int m = (int) minutes;
            double s = (minutes - m) * 60;
            return String.format("%02dh%02dm%.2fs", h, m, s);
        }

        /**
         * Returns Declination in DMS format.
         */
        public String getDecDMS() {
            String sign = dec >= 0 ? "+" : "-";
            double absDec = Math.abs(dec);
            int d = (int) absDec;
            double minutes = (absDec - d) * 60;
            int m = (int) minutes;
            double s = (minutes - m) * 60;
            return String.format("%s%02dÃ‚Â°%02d'%.2f\"", sign, d, m, s);
        }

        @Override
        public String toString() {
            return String.format("%s (%s) at RA=%s, Dec=%s, V=%.2f",
                    name, objectType, getRaHMS(), getDecDMS(),
                    magnitude != null ? magnitude : 0.0);
        }
    }

    /**
     * Resolves an object name using Sesame name resolver.
     *
     * @param name the object name (e.g., "M31", "Betelgeuse", "HD 48915")
     * @return the resolved object, or empty if not found
     */
    public Optional<AstronomicalObject> resolveByName(String name) {
        try {
            // Use Sesame resolver with XML output
            String url = BASE_URL + "/-oI/A?" + java.net.URLEncoder.encode(name, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parseSesameResponse(response.body(), name);
            }
        } catch (Exception e) {
            System.err.println("SIMBAD resolve failed: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Searches for objects within a radius around coordinates.
     *
     * @param ra           Right Ascension in degrees
     * @param dec          Declination in degrees
     * @param radiusArcmin search radius in arcminutes
     * @param limit        maximum results
     * @return list of objects found
     */
    public List<AstronomicalObject> searchByCoordinates(double ra, double dec,
            double radiusArcmin, int limit) {
        List<AstronomicalObject> results = new ArrayList<>();
        try {
            // Use TAP query for coordinate search
            String query = String.format(
                    "SELECT TOP %d main_id, otype, ra, dec, flux_v, sp_type " +
                            "FROM basic WHERE CONTAINS(POINT('ICRS', ra, dec), " +
                            "CIRCLE('ICRS', %.6f, %.6f, %.6f)) = 1",
                    limit, ra, dec, radiusArcmin / 60.0);

            String params = "REQUEST=doQuery&LANG=ADQL&FORMAT=json&QUERY=" +
                    java.net.URLEncoder.encode(query, "UTF-8");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TAP_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Parse TAP JSON response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                JsonNode data = root.path("data");
                if (data.isArray()) {
                    for (JsonNode row : data) {
                        if (row.isArray() && row.size() >= 4) {
                            results.add(new AstronomicalObject(
                                    row.get(0).asText(),
                                    row.get(1).asText(),
                                    row.get(2).asDouble(),
                                    row.get(3).asDouble(),
                                    row.size() > 4 && !row.get(4).isNull() ? row.get(4).asDouble() : null,
                                    row.size() > 5 ? row.get(5).asText(null) : null));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("SIMBAD coordinate search failed: " + e.getMessage());
        }
        return results;
    }

    private Optional<AstronomicalObject> parseSesameResponse(String response, String name) {
        // Parse Sesame ASCII/XML response
        // Look for %J (ICRS coordinates)
        String[] lines = response.split("\n");
        Double ra = null, dec = null;
        String objType = null;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("%J ")) {
                String[] parts = line.substring(3).trim().split("\\s+");
                if (parts.length >= 2) {
                    try {
                        ra = Double.parseDouble(parts[0]);
                        dec = Double.parseDouble(parts[1]);
                    } catch (NumberFormatException e) {
                        /* ignore */ }
                }
            } else if (line.startsWith("%T ")) {
                objType = line.substring(3).trim();
            }
        }

        if (ra != null && dec != null) {
            return Optional.of(new AstronomicalObject(name, objType, ra, dec, null, null));
        }
        return Optional.empty();
    }
}
