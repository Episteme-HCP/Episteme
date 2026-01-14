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

package org.jscience.biology.loaders;

import org.jscience.io.cache.ResourceCache;
import org.jscience.io.AbstractResourceReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * Connector to the Global Biodiversity Information Facility (GBIF) API.
 * <p>
 * <b>What it does</b>: Provides access to the GBIF taxonomy backbone, allowing
 * search and retrieval of biological species data without storing the massive
 * dataset locally.
 * </p>
 *
 * <p>
 * <b>Data Source</b>: GBIF (https://www.gbif.org/developer/species)
 * </p>
 * <p>
 * <b>License</b>: Data is free (CC0 or CC-BY), API is open.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GBIFTaxonomy extends AbstractResourceReader<String> {

    @Override
    protected String loadFromSource(String resourceId) throws Exception {
        // resourceId can be name or usageKey
        try {
            int key = Integer.parseInt(resourceId);
            return getSpeciesJson(key);
        } catch (NumberFormatException e) {
            return search(resourceId);
        }
    }

    @Override
    public String getResourcePath() {
        return "https://api.gbif.org/v1/species";
    }

    @Override
    public Class<String> getResourceType() {
        return String.class;
    }

    private static final String API_BASE = org.jscience.io.Configuration.get("api.gbif.taxonomy.base",
            "https://api.gbif.org/v1/species");

    /**
     * Searches for species matching the query.
     * 
     * @param query scientific or common name
     * @return raw JSON response string
     * @throws RuntimeException if connection fails
     */
    public static String search(String query) {
        String cacheKey = "gbif_search_" + query;
        Optional<String> cached = ResourceCache.global().get(cacheKey);
        if (cached.isPresent())
            return cached.get();

        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String urlStr = API_BASE + "/match?name=" + encodedQuery;
            String data = fetchUrl(urlStr);
            ResourceCache.global().put(cacheKey, data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException("GBIF API search failed", e);
        }
    }

    /**
     * Placeholder for future mapping to Biology domain objects.
     */
    public static <T> T map(String json, Class<T> target) {
        System.err.println(
                "WARNING: Domain mapping for " + target.getSimpleName() + " is not yet implemented. Returning null.");
        return null;
    }

    /**
     * Fetches species details by GBIF usage key.
     * 
     * @param usageKey the unique GBIF identifier
     * @return raw JSON response string
     */
    public static String getSpeciesJson(int usageKey) {
        return fetchUrl(API_BASE + "/" + usageKey);
    }

    private static String fetchUrl(String urlStr) {
        try {
            URL url = URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            conn.disconnect();
            return output.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch from GBIF", e);
        }
    }

    public GBIFTaxonomy() {
    }
}
