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

import org.jscience.io.cache.ResourceCache;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * Connector to PubChem (National Institutes of Health).
 * <p>
 * <b>What it does</b>: Provides access to chemical data for millions of
 * compounds.
 * Retrieves structures, properties (molecular weight, formula), and
 * identifiers.
 * </p>
 *
 * <p>
 * <b>Data Source</b>: PubChem PUG REST API (https://pubchem.ncbi.nlm.nih.gov/)
 * </p>
 * <p>
 * <b>License</b>: Public Domain (US Government work).
 * </p>
 *
 * <p>
 * <b>Usage example</b>:
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PubChem {

    private static final String API_BASE = org.jscience.io.Configuration.get("api.pubchem.base",
            "https://pubchem.ncbi.nlm.nih.gov/rest/pug");

    /**
     * Searches for a compound by name and returns full record.
     * 
     * @param name compound name (e.g., "aspirin", "water")
     * @return JSON response
     */
    public static String search(String name) {
        String cacheKey = "pubchem_search_" + name;
        Optional<String> cached = ResourceCache.global().get(cacheKey);
        if (cached.isPresent())
            return cached.get();

        try {
            // Path: /compound/name/{name}/JSON
            String encodedName = URLEncoder.encode(name, "UTF-8");
            String urlStr = API_BASE + "/compound/name/" + encodedName + "/JSON";
            String data = fetchUrl(urlStr);
            ResourceCache.global().put(cacheKey, data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException("PubChem search failed", e);
        }
    }

    /**
     * Searches for a compound and returns a parsed CompoundInfo object.
     * This is a convenience method that combines search() with JSON parsing.
     * 
     * @param name compound name (e.g., "aspirin")
     * @return CompoundInfo with parsed data, or null if not found
     */
    public static CompoundInfo searchCompound(String name) {
        try {
            String propsJson = getProperties(name,
                    "MolecularFormula,MolecularWeight,IUPACName,InChIKey,CanonicalSMILES");
            return parsePropertiesJson(name, propsJson);
        } catch (Exception e) {
            System.err.println("WARNING: Failed to fetch compound info for '" + name + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Parses PubChem properties JSON into CompoundInfo.
     * 
     * @param name the compound name used in the query
     * @param json raw JSON response from PubChem properties API
     * @return parsed CompoundInfo
     */
    private static CompoundInfo parsePropertiesJson(String name, String json) {
        // Simple JSON parsing without external dependencies
        // PubChem returns: {"PropertyTable":{"Properties":[{...}]}}
        CompoundInfo.Builder builder = new CompoundInfo.Builder().name(name);

        // Extract CID
        String cidVal = extractJsonValue(json, "CID");
        if (cidVal != null)
            builder.cid(Long.parseLong(cidVal));

        // Extract MolecularFormula
        String formula = extractJsonValue(json, "MolecularFormula");
        if (formula != null)
            builder.molecularFormula(formula);

        // Extract MolecularWeight
        String mw = extractJsonValue(json, "MolecularWeight");
        if (mw != null)
            builder.molecularWeight(Double.parseDouble(mw));

        // Extract IUPACName
        String iupac = extractJsonValue(json, "IUPACName");
        if (iupac != null)
            builder.iupacName(iupac);

        // Extract InChIKey
        String inchiKey = extractJsonValue(json, "InChIKey");
        if (inchiKey != null)
            builder.inchiKey(inchiKey);

        // Extract CanonicalSMILES
        String smiles = extractJsonValue(json, "CanonicalSMILES");
        if (smiles != null)
            builder.canonicalSmiles(smiles);

        return builder.build();
    }

    /**
     * Simple JSON value extractor (avoids external JSON library dependency).
     */
    private static String extractJsonValue(String json, String key) {
        // Match patterns like "Key":123 or "Key":"value"
        int keyIdx = json.indexOf("\"" + key + "\":");
        if (keyIdx == -1)
            return null;

        int valueStart = keyIdx + key.length() + 3; // skip key, quotes, colon
        if (valueStart >= json.length())
            return null;

        // Skip whitespace
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }

        if (json.charAt(valueStart) == '"') {
            // String value
            int valueEnd = json.indexOf('"', valueStart + 1);
            return json.substring(valueStart + 1, valueEnd);
        } else {
            // Numeric value
            int valueEnd = valueStart;
            while (valueEnd < json.length() &&
                    (Character.isDigit(json.charAt(valueEnd)) || json.charAt(valueEnd) == '.')) {
                valueEnd++;
            }
            return json.substring(valueStart, valueEnd);
        }
    }

    /**
     * Maps JSON response to a domain object.
     * 
     * @param json   raw JSON from PubChem API
     * @param target target class (currently supports CompoundInfo.class)
     * @return mapped object or null if mapping not supported
     */
    @SuppressWarnings("unchecked")
    public static <T> T map(String json, Class<T> target) {
        if (target == CompoundInfo.class) {
            return (T) parsePropertiesJson("unknown", json);
        }
        System.err.println("WARNING: Domain mapping for " + target.getSimpleName() +
                " is not yet implemented. Use searchCompound() for CompoundInfo.");
        return null;
    }

    /**
     * Retrieves specific properties for a compound.
     * 
     * @param name       compound name
     * @param properties comma-separated list (e.g.,
     *                   "MolecularFormula,MolecularWeight,InChIKey")
     * @return JSON response
     */
    public static String getProperties(String name, String properties) {
        try {
            // Path: /compound/name/{name}/property/{properties}/JSON
            String encodedName = URLEncoder.encode(name, "UTF-8");
            String urlStr = API_BASE + "/compound/name/" + encodedName + "/property/" + properties + "/JSON";
            return fetchUrl(urlStr);
        } catch (Exception e) {
            throw new RuntimeException("PubChem properties fetch failed", e);
        }
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
            throw new RuntimeException("Failed to fetch from PubChem", e);
        }
    }

    private PubChem() {
    }
}
