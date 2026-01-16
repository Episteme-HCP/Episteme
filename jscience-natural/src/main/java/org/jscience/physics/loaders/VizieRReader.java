/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * VizieR astronomical catalog loader.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VizieRReader extends AbstractResourceReader<Map<String, String>> {

    private static final String API_URL = org.jscience.JScience.getProperty("data.vizier.api.url", "https://vizier.cds.unistra.fr/viz-bin/votable");

    public VizieRReader() {
    }

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getDescription() {
        return "Access to VizieR astronomical catalogs.";
    }

    @Override
    public String getResourcePath() {
        return API_URL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Map<String, String>> getResourceType() {
        return (Class<Map<String, String>>) (Class<?>) Map.class;
    }

    @Override
    protected Map<String, String> loadFromSource(String id) throws Exception {
        return queryByObject(id, HIPPARCOS);
    }

    @Override
    protected MiniCatalog<Map<String, String>> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<Map<String, String>> getAll() {
                return List.of(Map.of());
            }

            @Override
            public Optional<Map<String, String>> findByName(String name) {
                return Optional.of(Map.of());
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    /**
     * Queries a VizieR catalog by object name.
     */
    public static Map<String, String> queryByObject(String objectName, String catalog) {
        try {
            String urlStr = API_URL + "?-source=" + catalog
                    + "&-c=" + java.net.URLEncoder.encode(objectName, "UTF-8")
                    + "&-out.max=10";
            URL url = java.net.URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();

            Map<String, String> result = new LinkedHashMap<>();
            result.put("object", objectName);
            result.put("catalog", catalog);
            result.put("raw_votable", response.toString());

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Common catalog identifiers.
     */
    public static final String HIPPARCOS = "I/239/hip_main";
    public static final String TYCHO2 = "I/259/tyc2";
    public static final String GAIA_DR3 = "I/355/gaiadr3";
    public static final String SIMBAD = "II/246/out";
    public static final String USNO_B1 = "I/284/out";

    /**
     * Gets catalog URL for browsing.
     */
    public static String getCatalogUrl(String catalog) {
        return org.jscience.JScience.getProperty("data.vizier.query.url", "https://vizier.cds.unistra.fr/viz-bin/VizieR?-source=") + catalog;
    }

    /**
     * Queries stars within radius of coordinates.
     */
    public static Map<String, String> queryByCoordinates(double ra, double dec, double radiusArcmin, String catalog) {
        try {
            String urlStr = API_URL + "?-source=" + catalog
                    + "&-c=" + ra + "+" + dec
                    + "&-c.rm=" + radiusArcmin
                    + "&-out.max=50";
            URL url = java.net.URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();

            Map<String, String> result = new LinkedHashMap<>();
            result.put("ra", String.valueOf(ra));
            result.put("dec", String.valueOf(dec));
            result.put("radius_arcmin", String.valueOf(radiusArcmin));
            result.put("catalog", catalog);
            result.put("raw_votable", response.toString());

            return result;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Queries stars within radius of coordinates (Real precision).
     */
    public static Map<String, String> queryByCoordinates(Real ra, Real dec, double radiusArcmin, String catalog) {
        return queryByCoordinates(ra.doubleValue(), dec.doubleValue(), radiusArcmin, catalog);
    }
    
    /**
     * Queries stars within radius of coordinates (Real precision).
     */
    public static Map<String, String> queryByCoordinates(Real ra, Real dec, Real radiusArcmin, String catalog) {
        return queryByCoordinates(ra.doubleValue(), dec.doubleValue(), radiusArcmin.doubleValue(), catalog);
    }

    @Override public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("reader.vizier.name"); }
}

