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

package org.jscience.physics.loaders;

import org.jscience.io.AbstractResourceReader;

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
 * <b>What it does</b>: Provides access to the catalog of confirmed exoplanets.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NASAExoplanetsReader extends AbstractResourceReader<ExoplanetInfo> {

    private static final String API_BASE = org.jscience.io.Configuration.get("api.nasa.exoplanets.tap",
            "https://exoplanetarchive.ipac.caltech.edu/TAP/sync");

    private static final NASAExoplanetsReader INSTANCE = new NASAExoplanetsReader();

    public static NASAExoplanetsReader getInstance() {
        return INSTANCE;
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
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.nasaexoplanetsreader.name", "NASA Exoplanets");
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.astronomy", "Astronomy");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.nasaexoplanetsreader.desc", "Confirmed exoplanets from NASA Archive.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.nasaexoplanetsreader.longdesc", "Provides access to the catalog of confirmed exoplanets from the NASA Exoplanet Archive.");
    }

    @Override
    public String getResourcePath() {
        return API_BASE;
    }

    @Override
    public Class<ExoplanetInfo> getResourceType() {
        return ExoplanetInfo.class;
    }

    private static String executeQuery(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String urlStr = API_BASE + "?query=" + encodedQuery + "&format=csv";

            URL url = URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code: " + conn.getResponseCode());
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    output.append(line).append("\n");
                }
                return output.toString();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to query NASA Exoplanet Archive", e);
        }
    }

    private static List<ExoplanetInfo> parseCSV(String csv) {
        List<ExoplanetInfo> results = new ArrayList<>();
        String[] lines = csv.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length < 7) continue;

            try {
                ExoplanetInfo.Builder builder = new ExoplanetInfo.Builder()
                        .name(parts[0].trim())
                        .hostStar(parts[1].trim());

                if (!parts[2].isEmpty()) builder.massJupiter(Double.parseDouble(parts[2]));
                if (!parts[3].isEmpty()) builder.radiusJupiter(Double.parseDouble(parts[3]));
                if (!parts[4].isEmpty()) builder.orbitalPeriodDays(Double.parseDouble(parts[4]));
                if (!parts[5].isEmpty()) builder.equilibriumTemperatureK(Double.parseDouble(parts[5]));
                if (!parts[6].isEmpty()) builder.distanceParsecs(Double.parseDouble(parts[6]));

                results.add(builder.build());
            } catch (NumberFormatException ignored) {
            }
        }
        return results;
    }

    public NASAExoplanetsReader() {
    }
}
