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

package org.jscience.physics.loaders;

import org.jscience.io.cache.ResourceCache;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * Connector to SIMBAD (Set of Identifications, Measurements, and Bibliography
 * for Astronomical Data).
 * <p>
 * <b>What it does</b>: Provides access to astronomical data for stars,
 * galaxies, and other objects.
 * Uses the CDS (Centre de DonnÃƒÂ©es astronomiques de Strasbourg) Sesame
 * resolver.
 * </p>
 *
 * <p>
 * <b>Data Source</b>: CDS Strasbourg (http://cds.u-strasbg.fr/)
 * </p>
 * <p>
 * <b>License</b>: Free for research/educational use with acknowledgement.
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
public class SimbadCatalog {

    // CDS Sesame resolver is a standard HTTP API for name resolution
    private static final String API_BASE = org.jscience.io.Configuration.get("api.simbad.base",
            "http://cdsweb.u-strasbg.fr/cgi-bin/nph-sesame");

    /**
     * Resolves an astronomical object name to position and basic data.
     * 
     * @param objectName name (e.g., "Sirius", "M31", "Alpha Centauri")
     * @return XML response from Sesame
     */
    public static String resolve(String objectName) {
        String cacheKey = "simbad_" + objectName;
        Optional<String> cached = ResourceCache.global().get(cacheKey);
        if (cached.isPresent())
            return cached.get();

        try {
            // -x returns XML format
            String encodedName = URLEncoder.encode(objectName, "UTF-8");
            String urlStr = API_BASE + "/-ox?" + encodedName;
            String data = fetchUrl(urlStr);
            ResourceCache.global().put(cacheKey, data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException("SIMBAD resolution failed", e);
        }
    }

    /**
     * Placeholder for future mapping to Astronomy domain objects.
     */
    public static <T> T map(String xml, Class<T> target) {
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
            throw new RuntimeException("Failed to fetch from SIMBAD", e);
        }
    }

    private SimbadCatalog() {
    }
}
