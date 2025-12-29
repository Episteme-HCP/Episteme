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

package org.jscience.chemistry.loaders;

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
public class ChEBILoader {

    private static final String SEARCH_URL = "https://www.ebi.ac.uk/ols/api/search?ontology=chebi&q=";

    private ChEBILoader() {
    }

    /**
     * Search ChEBI for a compound by name.
     * Returns simplified metadata.
     */
    public static Map<String, String> searchByName(String name) {
        try {
            String urlStr = SEARCH_URL + java.net.URLEncoder.encode(name, "UTF-8");
            URL url = java.net.URI.create(urlStr).toURL();
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

            Map<String, String> result = new LinkedHashMap<>();
            result.put("query", name);
            result.put("raw_response", response.toString());

            // Extract first result label if exists
            String json = response.toString();
            int labelIdx = json.indexOf("\"label\":\"");
            if (labelIdx > 0) {
                int start = labelIdx + 9;
                int end = json.indexOf("\"", start);
                if (end > start) {
                    result.put("name", json.substring(start, end));
                }
            }

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets ChEBI ID from search result.
     */
    public static String extractChebiId(String jsonResponse) {
        int idx = jsonResponse.indexOf("CHEBI:");
        if (idx > 0) {
            int end = jsonResponse.indexOf("\"", idx);
            if (end > idx) {
                return jsonResponse.substring(idx, end);
            }
        }
        return null;
    }

    /**
     * Builds ChEBI compound URL.
     */
    public static String getCompoundUrl(String chebiId) {
        return "https://www.ebi.ac.uk/chebi/searchId.do?chebiId=" + chebiId;
    }
}
