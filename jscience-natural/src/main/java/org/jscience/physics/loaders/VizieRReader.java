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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * VizieR astronomical catalog loader.
 * <p>
 * This reader retrieves data from the VizieR Service (CDS, Strasbourg) 
 * in VOTable format and parses it into structured records.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @version 2.0 (Enhanced with VOTable support)
 */
public class VizieRReader extends AbstractResourceReader<List<Map<String, String>>> {

    private static final String API_URL = org.jscience.JScience.getProperty("data.vizier.api.url", "https://vizier.cds.unistra.fr/viz-bin/votable");

    public VizieRReader() {
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.astronomy", "Astronomy");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.vizierreader.desc", "Access to VizieR astronomical catalogs.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.vizierreader.longdesc", "Queries VizieR astronomical catalogs by object name or coordinates (conesearch). Parsed using VOTable standard.");
    }

    @Override
    public String getResourcePath() {
        return API_URL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Map<String, String>>> getResourceType() {
        return (Class<List<Map<String, String>>>) (Class<?>) List.class;
    }

    @Override
    protected List<Map<String, String>> loadFromSource(String id) throws Exception {
        return queryByObject(id, HIPPARCOS);
    }

    @Override
    protected MiniCatalog<List<Map<String, String>>> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<List<Map<String, String>>> getAll() {
                return List.of();
            }

            @Override
            public Optional<List<Map<String, String>>> findByName(String name) {
                try {
                    return Optional.of(queryByObject(name, HIPPARCOS));
                } catch (Exception e) {
                    return Optional.empty();
                }
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
    public static List<Map<String, String>> queryByObject(String objectName, String catalog) {
        try {
            String urlStr = API_URL + "?-source=" + catalog
                    + "&-c=" + java.net.URLEncoder.encode(objectName, "UTF-8")
                    + "&-out.max=10";
            
            String rawXml = fetchRaw(urlStr);
            if (rawXml == null) return new ArrayList<>();
            
            return VOTableBridge.parse(rawXml);
        } catch (Exception e) {
            return new ArrayList<>();
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
    public static List<Map<String, String>> queryByCoordinates(double ra, double dec, double radiusArcmin, String catalog) {
        try {
            String urlStr = API_URL + "?-source=" + catalog
                    + "&-c=" + ra + "+" + dec
                    + "&-c.rm=" + radiusArcmin
                    + "&-out.max=50";
            
            String rawXml = fetchRaw(urlStr);
            if (rawXml == null) return new ArrayList<>();
            
            return VOTableBridge.parse(rawXml);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static String fetchRaw(String urlStr) throws Exception {
        URL url = java.net.URI.create(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(15000);

        if (conn.getResponseCode() != 200) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        }
    }

    /**
     * Queries stars within radius of coordinates (Real precision).
     */
    public static List<Map<String, String>> queryByCoordinates(Real ra, Real dec, double radiusArcmin, String catalog) {
        return queryByCoordinates(ra.doubleValue(), dec.doubleValue(), radiusArcmin, catalog);
    }
    
    /**
     * Queries stars within radius of coordinates (Real precision).
     */
    public static List<Map<String, String>> queryByCoordinates(Real ra, Real dec, Real radiusArcmin, String catalog) {
        return queryByCoordinates(ra.doubleValue(), dec.doubleValue(), radiusArcmin.doubleValue(), catalog);
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"VOTable 1.4", "VOTable 1.3", "VOTable 1.2"};
    }

    @Override public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("reader.vizierreader.name", "VizieR Reader"); }
}
