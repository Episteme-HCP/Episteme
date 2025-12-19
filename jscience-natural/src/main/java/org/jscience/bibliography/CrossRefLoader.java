package org.jscience.bibliography;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * CrossRef API loader for bibliography and citations.
 */
public class CrossRefLoader {

    private static final String API_URL = "https://api.crossref.org/works/";
    private static final String SEARCH_URL = "https://api.crossref.org/works?query=";

    private CrossRefLoader() {
    }

    /**
     * Fetches work metadata by DOI.
     */
    public static Map<String, String> fetchByDOI(String doi) {
        try {
            String normalized = DOIResolver.normalize(doi);
            URL url = java.net.URI.create(API_URL + normalized).toURL();
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

            return parseWorkResponse(response.toString(), normalized);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Searches for works by query string.
     */
    public static List<Map<String, String>> search(String query, int maxResults) {
        List<Map<String, String>> results = new ArrayList<>();
        try {
            String urlStr = SEARCH_URL + java.net.URLEncoder.encode(query, "UTF-8") + "&rows=" + maxResults;
            URL url = java.net.URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            if (conn.getResponseCode() != 200) {
                return results;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Simple parsing - extract DOIs
            String json = response.toString();
            int idx = 0;
            while ((idx = json.indexOf("\"DOI\":\"", idx)) > 0) {
                int start = idx + 7;
                int end = json.indexOf("\"", start);
                if (end > start) {
                    String foundDoi = json.substring(start, end);
                    Map<String, String> entry = new LinkedHashMap<>();
                    entry.put("doi", foundDoi);
                    results.add(entry);
                    idx = end;
                }
                if (results.size() >= maxResults)
                    break;
            }

        } catch (Exception e) {
            // Return partial results
        }
        return results;
    }

    private static Map<String, String> parseWorkResponse(String json, String doi) {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("doi", doi);
        result.put("raw_json", json);

        // Extract title
        extractField(json, "title", result, "title");

        // Extract container-title (journal)
        int ctIdx = json.indexOf("\"container-title\":[\"");
        if (ctIdx > 0) {
            int start = ctIdx + 20;
            int end = json.indexOf("\"", start);
            if (end > start) {
                result.put("journal", json.substring(start, end));
            }
        }

        // Extract publisher
        extractSimpleField(json, "publisher", result, "publisher");

        // Extract type
        extractSimpleField(json, "type", result, "type");

        return result;
    }

    private static void extractField(String json, String field, Map<String, String> result, String key) {
        String pattern = "\"" + field + "\":[\"";
        int idx = json.indexOf(pattern);
        if (idx > 0) {
            int start = idx + pattern.length();
            int end = json.indexOf("\"", start);
            if (end > start) {
                result.put(key, json.substring(start, end));
            }
        }
    }

    private static void extractSimpleField(String json, String field, Map<String, String> result, String key) {
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
     * Gets citation count for a DOI.
     */
    public static int getCitationCount(String doi) {
        Map<String, String> work = fetchByDOI(doi);
        if (work != null) {
            String json = work.get("raw_json");
            if (json != null) {
                int idx = json.indexOf("\"is-referenced-by-count\":");
                if (idx > 0) {
                    int start = idx + 25;
                    int end = start;
                    while (end < json.length() && Character.isDigit(json.charAt(end))) {
                        end++;
                    }
                    if (end > start) {
                        try {
                            return Integer.parseInt(json.substring(start, end));
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                }
            }
        }
        return -1;
    }
}
