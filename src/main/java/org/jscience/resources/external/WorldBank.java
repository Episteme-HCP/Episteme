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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.resources.external;

import org.jscience.resources.cache.ResourceCache;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * Connector to the World Bank Data API.
 * <p>
 * <b>What it does</b>: Provides access to global economic and social
 * indicators.
 * Covers GDP, inflation, population, health, education, etc.
 * </p>
 * 
 * <p>
 * <b>Data Source</b>: World Bank (https://datahelpdesk.worldbank.org/)
 * </p>
 * <p>
 * <b>License</b>: CC-BY 4.0 (Free with attribution).
 * </p>
 * 
 * <p>
 * <b>Usage example</b>:
 * </p>
 * 
 * <pre>{@code
 * // Get GDP for Brazil (NY.GDP.MKTP.CD)
 * String json = WorldBank.getIndicator("BRA", "NY.GDP.MKTP.CD");
 * }</pre>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class WorldBank {

    private static final String API_BASE = org.jscience.resources.Configuration.get("api.worldbank.base",
            "https://api.worldbank.org/v2");

    /**
     * Gets an indicator value for a country.
     * 
     * @param countryCode   ISO 3-letter code (e.g., "BRA", "USA", "FRA")
     * @param indicatorCode World Bank indicator (e.g., "NY.GDP.MKTP.CD")
     * @return JSON response
     */
    public static String getIndicator(String countryCode, String indicatorCode) {
        String cacheKey = "wb_" + countryCode + "_" + indicatorCode;
        Optional<String> cached = ResourceCache.global().get(cacheKey);
        if (cached.isPresent())
            return cached.get();

        try {
            // Format: /country/{country}/indicator/{indicator}?format=json
            String urlStr = API_BASE + "/country/" + countryCode + "/indicator/" + indicatorCode + "?format=json";
            String data = fetchUrl(urlStr);
            ResourceCache.global().put(cacheKey, data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException("World Bank API request failed", e);
        }
    }

    /**
     * Placeholder for future mapping to Economics domain objects.
     */
    public static <T> T map(String json, Class<T> target) {
        throw new UnsupportedOperationException("Domain class " + target.getSimpleName() + " not yet designed.");
    }

    private static String fetchUrl(String urlStr) {
        try {
            URL url = URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

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
            throw new RuntimeException("Failed to fetch from World Bank", e);
        }
    }

    private WorldBank() {
    }
}

