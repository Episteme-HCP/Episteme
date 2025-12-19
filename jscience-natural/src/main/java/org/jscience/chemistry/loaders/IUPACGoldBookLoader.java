package org.jscience.chemistry.loaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * IUPAC Gold Book loader for chemistry nomenclature.
 */
public class IUPACGoldBookLoader {

    private static final String API_URL = "https://goldbook.iupac.org/terms/";
    private static final String SEARCH_URL = "https://goldbook.iupac.org/indexes/autocomplete?term=";

    private IUPACGoldBookLoader() {
    }

    /**
     * Search for a term in the IUPAC Gold Book.
     */
    public static Map<String, String> searchTerm(String term) {
        try {
            String urlStr = SEARCH_URL + java.net.URLEncoder.encode(term, "UTF-8");
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
            result.put("query", term);
            result.put("raw_json", response.toString());

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the URL for a specific Gold Book term code.
     */
    public static String getTermUrl(String termCode) {
        return API_URL + termCode;
    }

    /**
     * Common IUPAC term codes.
     */
    public static final String MOLE = "M03980";
    public static final String AVOGADRO_CONSTANT = "A00543";
    public static final String MOLARITY = "A00295";
    public static final String PH = "P04524";
    public static final String CATALYST = "C00876";
    public static final String ENTHALPY = "E02141";
    public static final String ENTROPY = "E02149";
    public static final String FREE_ENERGY = "G02629";

    /**
     * Creates a definition entry from known data.
     */
    public static Map<String, String> createDefinition(String term, String definition, String termCode) {
        Map<String, String> entry = new LinkedHashMap<>();
        entry.put("term", term);
        entry.put("definition", definition);
        entry.put("code", termCode);
        entry.put("url", getTermUrl(termCode));
        return entry;
    }
}
