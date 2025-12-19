package org.jscience.physics.loaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * VizieR astronomical catalog loader (CDS).
 */
public class VizieRLoader {

    private static final String API_URL = "https://vizier.cds.unistra.fr/viz-bin/votable";

    private VizieRLoader() {
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
        return "https://vizier.cds.unistra.fr/viz-bin/VizieR?-source=" + catalog;
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
}
