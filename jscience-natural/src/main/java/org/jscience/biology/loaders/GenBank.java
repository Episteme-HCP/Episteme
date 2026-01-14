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

package org.jscience.biology.loaders;

import org.jscience.io.cache.ResourceCache;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

/**
 * Connector to NCBI GenBank for DNA/protein sequence retrieval.
 * <p>
 * <b>What it does</b>: Fetches nucleotide and protein sequences using
 * NCBI E-utilities (efetch/esearch).
 * </p>
 *
 * <p>
 * <b>Data Source</b>: NCBI Entrez E-utilities API
 * </p>
 * <p>
 * <b>Usage example</b>:
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GenBank {

    private static final String EFETCH_BASE = org.jscience.io.Configuration.get(
            "api.ncbi.efetch", "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi");

    /**
     * Fetches sequence in FASTA format.
     * 
     * @param accession GenBank accession number (e.g., "NC_000913.3",
     *                  "NM_001301717")
     * @return FASTA formatted sequence
     */
    public static String getFasta(String accession) {
        return fetch(accession, "fasta", "nuccore");
    }

    /**
     * Fetches protein sequence in FASTA format.
     * 
     * @param accession protein accession number
     * @return FASTA formatted protein sequence
     */
    public static String getProteinFasta(String accession) {
        return fetch(accession, "fasta", "protein");
    }

    /**
     * Fetches sequence in GenBank flat file format.
     * 
     * @param accession GenBank accession number
     * @return GenBank format record
     */
    public static String getGenBankRecord(String accession) {
        return fetch(accession, "gb", "nuccore");
    }

    /**
     * Parses FASTA to extract sequence and metadata.
     * 
     * @param accession GenBank accession
     * @return SequenceInfo with parsed data
     */
    public static SequenceInfo getSequenceInfo(String accession) {
        String fasta = getFasta(accession);
        return SequenceInfo.fromFasta(fasta);
    }

    private static String fetch(String accession, String format, String database) {
        String cacheKey = "genbank_" + database + "_" + accession + "_" + format;
        Optional<String> cached = ResourceCache.global().get(cacheKey);
        if (cached.isPresent())
            return cached.get();

        try {
            String urlStr = EFETCH_BASE + "?db=" + database +
                    "&id=" + accession +
                    "&rettype=" + format +
                    "&retmode=text";

            URL url = URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "JScience/2.0");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line).append("\n");
            }
            conn.disconnect();

            String data = output.toString();
            ResourceCache.global().put(cacheKey, data);
            return data;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch from GenBank: " + accession, e);
        }
    }

    private GenBank() {
    }
}
