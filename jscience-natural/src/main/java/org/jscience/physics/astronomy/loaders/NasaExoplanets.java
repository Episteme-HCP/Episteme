/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.physics.astronomy.loaders;

import org.jscience.io.AbstractLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Connector to NASA Exoplanet Archive.
 * <p>
 * <b>What it does</b>: Provides access to the catalog of confirmed exoplanets,
 * including orbital parameters, planet mass, radius, and host star properties.
 * </p>
 * 
 * <p>
 * <b>Data Source</b>: NASA Exoplanet Archive TAP API
 * (https://exoplanetarchive.ipac.caltech.edu)
 * </p>
 * <p>
 * <b>License</b>: Public Domain (US Government work).
 * </p>
 * 
 * <p>
 * <b>Usage example</b>:
 * </p>
 * 
 * <pre>{@code
 * // Get data for a specific exoplanet
 * ExoplanetInfo exo = NasaExoplanets.getInstance().load("Kepler-442 b");
 * 
 * // Search for Earth-like planets
 * List<ExoplanetInfo> earthLike = NasaExoplanets.searchHabitable();
 * }</pre>
 * 
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NasaExoplanets extends AbstractLoader<ExoplanetInfo> {

    private static final String API_BASE = org.jscience.io.Configuration.get("api.nasa.exoplanets",
            "https://exoplanetarchive.ipac.caltech.edu/TAP/sync");

    private static final NasaExoplanets INSTANCE = new NasaExoplanets();

    public static NasaExoplanets getInstance() {
        return INSTANCE;
    }

    /**
     * Queries the NASA Exoplanet Archive for a specific planet.
     * 
     * @param planetName planet name (e.g., "Kepler-442 b", "TRAPPIST-1 e")
     * @return ExoplanetInfo or null if not found
     */
    public static ExoplanetInfo getExoplanet(String planetName) {
        try {
            return INSTANCE.load(planetName);
        } catch (Exception e) {
            System.err.println("WARNING: Failed to fetch exoplanet '" + planetName + "': " + e.getMessage());
            return null;
        }
    }

    @Override
    protected ExoplanetInfo loadFromSource(String id) throws Exception {
        String query = "SELECT pl_name,hostname,pl_bmassj,pl_radj,pl_orbper,pl_eqt,sy_dist " +
                "FROM ps WHERE pl_name='" + id.replace("'", "''") + "'";
        String csv = executeQuery(query);
        List<ExoplanetInfo> results = parseCSV(csv);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public String getResourcePath() {
        return API_BASE; // API-based, not file-based
    }

    @Override
    public Class<ExoplanetInfo> getResourceType() {
        return ExoplanetInfo.class;
    }

    /**
     * Searches for potentially habitable exoplanets.
     * Habitable zone approximation: equilibrium temperature 200-320 K
     * 
     * @return list of habitable exoplanets
     */
    public static List<ExoplanetInfo> searchHabitable() {
        try {
            String query = "SELECT pl_name,hostname,pl_bmassj,pl_radj,pl_orbper,pl_eqt,sy_dist " +
                    "FROM ps WHERE pl_eqt>200 AND pl_eqt<320 ORDER BY sy_dist ASC";
            String csv = executeQuery(query);
            return parseCSV(csv);
        } catch (Exception e) {
            System.err.println("WARNING: Failed to search habitable exoplanets: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Searches for exoplanets around a specific host star.
     * 
     * @param hostStar star name (e.g., "TRAPPIST-1", "Kepler-442")
     * @return list of exoplanets
     */
    public static List<ExoplanetInfo> getByHostStar(String hostStar) {
        try {
            String query = "SELECT pl_name,hostname,pl_bmassj,pl_radj,pl_orbper,pl_eqt,sy_dist " +
                    "FROM ps WHERE hostname='" + hostStar.replace("'", "''") + "'";
            String csv = executeQuery(query);
            return parseCSV(csv);
        } catch (Exception e) {
            System.err.println("WARNING: Failed to fetch exoplanets for host '" + hostStar + "': " + e.getMessage());
            return List.of();
        }
    }

    private static String executeQuery(String query) {
        // AbstractLoader handles its own cache for individual items via load().
        // For list queries, we manage a separate cache key mechanism or rely on direct
        // execution.
        // Given AbstractLoader doesn't natively support "querying" a list, we keep this
        // helper static.

        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String urlStr = API_BASE + "?query=" + encodedQuery + "&format=csv";

            URL url = URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line).append("\n");
            }
            conn.disconnect();

            return output.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to query NASA Exoplanet Archive", e);
        }
    }

    private static List<ExoplanetInfo> parseCSV(String csv) {
        List<ExoplanetInfo> results = new ArrayList<>();
        String[] lines = csv.split("\n");

        // Skip header
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split(",");
            if (parts.length < 7)
                continue;

            try {
                ExoplanetInfo.Builder builder = new ExoplanetInfo.Builder()
                        .name(parts[0].trim())
                        .hostStar(parts[1].trim());

                // Mass in Jupiter masses
                if (!parts[2].isEmpty()) {
                    builder.massJupiter(Double.parseDouble(parts[2]));
                }
                // Radius in Jupiter radii
                if (!parts[3].isEmpty()) {
                    builder.radiusJupiter(Double.parseDouble(parts[3]));
                }
                // Orbital period in days
                if (!parts[4].isEmpty()) {
                    builder.orbitalPeriodDays(Double.parseDouble(parts[4]));
                }
                // Equilibrium temperature in K
                if (!parts[5].isEmpty()) {
                    builder.equilibriumTemperatureK(Double.parseDouble(parts[5]));
                }
                // Distance in parsecs
                if (!parts[6].isEmpty()) {
                    builder.distanceParsecs(Double.parseDouble(parts[6]));
                }

                results.add(builder.build());
            } catch (NumberFormatException ignored) {
                // Skip malformed rows
            }
        }

        return results;
    }

    private NasaExoplanets() {
        setBackupPath("/org/jscience/physics/astronomy/loaders/backup/");
    }
}