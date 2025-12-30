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

package org.jscience.biology.loaders;

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
public class PDBLoader implements org.jscience.io.InputLoader<Map<String, String>> {

    private static final String API_URL = "https://data.rcsb.org/rest/v1/core/entry/";
    private static PDBLoader instance;

    public PDBLoader() {
    }

    public static synchronized PDBLoader getInstance() {
        if (instance == null)
            instance = new PDBLoader();
        return instance;
    }

    @Override
    public Map<String, String> load(String resourceId) throws Exception {
        return fetchById(resourceId);
    }

    @Override
    public String getResourcePath() {
        return API_URL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Map<String, String>> getResourceType() {
        return (Class) Map.class;
    }

    /**
     * Fetches PDB entry metadata by PDB ID.
     */
    public static Map<String, String> fetchById(String pdbId) {
        try {
            URL url = java.net.URI.create(API_URL + pdbId.toUpperCase()).toURL();
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
            result.put("pdb_id", pdbId.toUpperCase());
            result.put("raw_json", response.toString());

            String json = response.toString();
            extractField(json, "title", result, "title");
            extractField(json, "resolution_combined", result, "resolution");

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets PDB structure file URL (mmCIF format).
     */
    public static String getStructureFileUrl(String pdbId) {
        return "https://files.rcsb.org/download/" + pdbId.toUpperCase() + ".cif";
    }

    /**
     * Gets PDB viewer URL.
     */
    public static String getViewerUrl(String pdbId) {
        return "https://www.rcsb.org/3d-view/" + pdbId.toUpperCase();
    }

    /**
     * Gets PDB entry page URL.
     */
    public static String getEntryUrl(String pdbId) {
        return "https://www.rcsb.org/structure/" + pdbId.toUpperCase();
    }

    private static void extractField(String json, String field, Map<String, String> result, String key) {
        String pattern = "\"" + field + "\":";
        int idx = json.indexOf(pattern);
        if (idx > 0) {
            int start = idx + pattern.length();
            // Handle string or number
            if (json.charAt(start) == '"') {
                start++;
                int end = json.indexOf("\"", start);
                if (end > start) {
                    result.put(key, json.substring(start, end));
                }
            } else {
                int end = start;
                while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.')) {
                    end++;
                }
                if (end > start) {
                    result.put(key, json.substring(start, end));
                }
            }
        }
    }
}
