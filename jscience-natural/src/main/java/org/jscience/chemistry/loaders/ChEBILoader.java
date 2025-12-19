package org.jscience.chemistry.loaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * ChEBI (Chemical Entities of Biological Interest) database loader.
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
