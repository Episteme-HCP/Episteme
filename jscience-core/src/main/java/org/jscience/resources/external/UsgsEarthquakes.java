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

/**
 * Connector to USGS Earthquake Hazards Program API.
 * <p>
 * <b>What it does</b>: Provides real-time and historical earthquake data.
 * </p>
 * 
 * <p>
 * <b>Data Source</b>: USGS (https://earthquake.usgs.gov/fdsnws/event/1/)
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
 * // Get significant earthquakes in the past 30 days
 * String json = UsgsEarthquakes.getSignificantMonth();
 * }</pre>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class UsgsEarthquakes {

    private static final String API_BASE = org.jscience.resources.Configuration.get("api.usgs.earthquakes.base",
            "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary");

    /**
     * Gets significant earthquakes from the past 30 days.
     * 
     * @return GeoJSON response
     */
    public static String getSignificantMonth() {
        String cacheKey = "usgs_significant_month";
        // Short cache duration logic could be implemented here, but for now we use
        // standard cache
        // In a real app, we might want to force refresh for real-time data

        try {
            String urlStr = API_BASE + "/significant_month.geojson";
            String data = fetchUrl(urlStr);
            ResourceCache.global().put(cacheKey, data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException("USGS API request failed", e);
        }
    }

    /**
     * Placeholder for future mapping to Geology domain objects.
     */
    public static <T> T map(String json, Class<T> target) {
        System.err.println(
                "WARNING: Domain mapping for " + target.getSimpleName() + " is not yet implemented. Returning null.");
        return null;
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
            throw new RuntimeException("Failed to fetch from USGS", e);
        }
    }

    private UsgsEarthquakes() {
    }
}
