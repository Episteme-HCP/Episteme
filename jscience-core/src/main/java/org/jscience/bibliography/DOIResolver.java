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

package org.jscience.bibliography;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DOIResolver {

    private static final String DOI_API_URL = "https://doi.org/";
    private static final String CROSSREF_API_URL = "https://api.crossref.org/works/";

    private DOIResolver() {
    }

    /**
     * Resolves a DOI to its target URL.
     * 
     * @param doi DOI string (e.g., "10.1000/xyz123")
     * @return Resolved URL
     */
    public static String resolveToUrl(String doi) {
        return DOI_API_URL + normalize(doi);
    }

    /**
     * Normalizes a DOI string.
     */
    public static String normalize(String doi) {
        String normalized = doi.trim();
        if (normalized.startsWith("https://doi.org/")) {
            normalized = normalized.substring(16);
        } else if (normalized.startsWith("http://doi.org/")) {
            normalized = normalized.substring(15);
        } else if (normalized.startsWith("doi:")) {
            normalized = normalized.substring(4);
        }
        return normalized;
    }

    /**
     * Validates DOI format.
     * DOIs start with 10. and have a prefix/suffix structure.
     */
    public static boolean isValidDOI(String doi) {
        String normalized = normalize(doi);
        return normalized.matches("10\\.\\d{4,}/.*");
    }

    /**
     * Fetches metadata from CrossRef API.
     * Note: This is a simplified implementation that returns raw JSON.
     * In production, parse the JSON properly.
     * 
     * @param doi DOI string
     * @return Map with basic metadata or null on failure
     */
    public static Map<String, String> fetchMetadata(String doi) {
        try {
            String normalized = normalize(doi);
            URL url = java.net.URI.create(CROSSREF_API_URL + normalized).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Simple extraction (production code should use JSON library)
            String json = response.toString();
            Map<String, String> metadata = new LinkedHashMap<>();
            metadata.put("doi", normalized);
            metadata.put("raw_json", json);

            // Extract title (very basic)
            int titleStart = json.indexOf("\"title\":[\"");
            if (titleStart > 0) {
                int start = titleStart + 10;
                int end = json.indexOf("\"]", start);
                if (end > start) {
                    metadata.put("title", json.substring(start, end));
                }
            }

            return metadata;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a citation-ready DOI link.
     */
    public static String formatAsLink(String doi) {
        return "https://doi.org/" + normalize(doi);
    }
}


