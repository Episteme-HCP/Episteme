/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.LinkedHashMap;
import org.jscience.io.AbstractLoader;

/**
 * Loader for UniProt protein database.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class UniProtLoader extends AbstractLoader<Map<String, String>> {

    private static final String API_URL = "https://rest.uniprot.org/uniprotkb/";
    private static final String SEARCH_URL = "https://rest.uniprot.org/uniprotkb/search?query=";

    @Override
    protected Map<String, String> loadFromSource(String resourceId) throws Exception {
        // resourceId should be accession or query
        Map<String, String> res = fetchByAccession(resourceId);
        if (res == null) {
            res = searchByName(resourceId);
        }
        return res;
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

    public UniProtLoader() {
    }

    /**
     * Fetches protein entry by UniProt accession ID.
     */
    public static Map<String, String> fetchByAccession(String accession) {
        try {
            URL url = java.net.URI.create(API_URL + accession + ".json").toURL();
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
            result.put("accession", accession);
            result.put("raw_json", response.toString());

            // Extract protein name
            String json = response.toString();
            extractField(json, "fullName", result, "protein_name");
            extractField(json, "scientificName", result, "organism");

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Search UniProt by gene name or protein name.
     */
    public static Map<String, String> searchByName(String query) {
        try {
            String urlStr = SEARCH_URL + java.net.URLEncoder.encode(query, "UTF-8") + "&size=5";
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
            result.put("query", query);
            result.put("raw_json", response.toString());

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private static void extractField(String json, String field, Map<String, String> result, String key) {
        String pattern = "\"" + field + "\":\"";
        int idx = json.indexOf(pattern);
        if (idx > 0) {
            int start = idx + pattern.length();
            int end = json.indexOf("\"", start);
            if (end > start) {
                result.put(key, json.substring(start, end));
            }
        }
    }

    /**
     * Gets UniProt entry URL.
     */
    public static String getEntryUrl(String accession) {
        return "https://www.uniprot.org/uniprotkb/" + accession;
    }
}
