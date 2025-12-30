/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jscience.io.AbstractLoader;

/**
 * Connector to the National Center for Biotechnology Information (NCBI)
 * Taxonomy API.
 * <p>
 * <b>What it does</b>: Accesses the NCBI taxonomy database for organism
 * classification.
 * Useful for bioinformatics and genetics applications.
 * </p>
 *
 * <p>
 * <b>Data Source</b>: NCBI (https://www.ncbi.nlm.nih.gov/taxonomy)
 * </p>
 * <p>
 * <b>License</b>: Public Domain (US Government work).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NCBITaxonomy extends AbstractLoader<String> {

    @Override
    protected String loadFromSource(String resourceId) throws Exception {
        // resourceId should be TaxID
        try {
            int taxId = Integer.parseInt(resourceId);
            return fetchTaxonomyXml(taxId);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getResourcePath() {
        return "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
    }

    @Override
    public Class<String> getResourceType() {
        return String.class;
    }

    // Using E-utilities API for taxonomy
    private static final String API_BASE = org.jscience.io.Configuration.get("api.ncbi.taxonomy.base",
            "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi");

    /**
     * Fetches taxonomy details by TaxID.
     * 
     * @param taxId NCBI Taxonomy ID (e.g., 9606 for Homo sapiens)
     * @return XML response string (NCBI default)
     */
    public static String fetchTaxonomyXml(int taxId) {
        try {
            String urlStr = API_BASE + "?db=taxonomy&id=" + taxId + "&retmode=xml";
            return fetchUrl(urlStr);
        } catch (Exception e) {
            throw new RuntimeException("NCBI API fetch failed", e);
        }
    }

    private static String fetchUrl(String urlStr) {
        try {
            java.net.URI uri = java.net.URI.create(urlStr);
            URL url = uri.toURL();
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
            throw new RuntimeException("Failed to fetch from NCBI", e);
        }
    }

    public NCBITaxonomy() {
    }
}
