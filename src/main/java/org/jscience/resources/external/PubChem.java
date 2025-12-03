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
 * <pre>{@code
 * // Search for Aspirin
 * String json = PubChem.search("aspirin");
 * 
 * // Get properties (MolecularFormula, MolecularWeight)
 * String props = PubChem.getProperties("aspirin", "MolecularFormula,MolecularWeight");
 * }</pre>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class PubChem {

    private static final String API_BASE = org.jscience.resources.Configuration.get("api.pubchem.base",
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
     * Placeholder for future mapping to Chemistry domain objects.
     */
    public static <T> T map(String json, Class<T> target) {
        throw new UnsupportedOperationException("Domain class " + target.getSimpleName() + " not yet designed.");
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

