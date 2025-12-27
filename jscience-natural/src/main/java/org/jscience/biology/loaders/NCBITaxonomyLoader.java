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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.biology.loaders;

import java.io.*;
import java.net.http.*;
import java.net.URI;
import java.util.*;
import com.fasterxml.jackson.databind.*;

/**
 * Loader for NCBI Taxonomy database.
 * <p>
 * Fetches taxonomic information from NCBI E-utilities API.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NCBITaxonomyLoader {

    private static final String BASE_URL;
    private static final String SEARCH_URL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi";

    static {
        Properties props = new Properties();
        try (InputStream is = NCBITaxonomyLoader.class.getResourceAsStream("/jscience.properties")) {
            if (is != null)
                props.load(is);
        } catch (Exception e) {
            /* ignore */ }
        BASE_URL = props.getProperty("api.ncbi.taxonomy.base",
                "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi");
    }

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Represents a taxonomic entry from NCBI.
     */
    public static class TaxonEntry {
        public final long taxId;
        public final String scientificName;
        public final String rank;
        public final String lineage;
        public final String division;

        public TaxonEntry(long taxId, String scientificName, String rank, String lineage, String division) {
            this.taxId = taxId;
            this.scientificName = scientificName;
            this.rank = rank;
            this.lineage = lineage;
            this.division = division;
        }

        @Override
        public String toString() {
            return String.format("%s (taxid:%d) [%s]", scientificName, taxId, rank);
        }
    }

    /**
     * Fetches taxonomy by NCBI taxon ID.
     *
     * @param taxId the NCBI taxonomy ID
     * @return the taxon entry, or empty if not found
     */
    public Optional<TaxonEntry> fetchByTaxId(long taxId) {
        try {
            String url = BASE_URL + "?db=taxonomy&id=" + taxId + "&retmode=xml";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parseXmlResponse(response.body(), taxId);
            }
        } catch (Exception e) {
            System.err.println("NCBI fetch failed: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Searches for taxa by scientific name.
     *
     * @param name the scientific name to search
     * @return list of matching taxon IDs
     */
    public List<Long> searchByName(String name) {
        List<Long> ids = new ArrayList<>();
        try {
            String url = SEARCH_URL + "?db=taxonomy&term=" +
                    java.net.URLEncoder.encode(name, "UTF-8") + "&retmode=json";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode idList = root.path("esearchresult").path("idlist");
                if (idList.isArray()) {
                    for (JsonNode id : idList) {
                        ids.add(Long.parseLong(id.asText()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("NCBI search failed: " + e.getMessage());
        }
        return ids;
    }

    private Optional<TaxonEntry> parseXmlResponse(String xml, long taxId) {
        // Simple XML parsing - extract key fields
        String scientificName = extractXmlValue(xml, "ScientificName");
        String rank = extractXmlValue(xml, "Rank");
        String lineage = extractXmlValue(xml, "Lineage");
        String division = extractXmlValue(xml, "Division");

        if (scientificName != null) {
            return Optional.of(new TaxonEntry(taxId, scientificName, rank, lineage, division));
        }
        return Optional.empty();
    }

    private String extractXmlValue(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int start = xml.indexOf(open);
        int end = xml.indexOf(close);
        if (start >= 0 && end > start) {
            return xml.substring(start + open.length(), end).trim();
        }
        return null;
    }
}
