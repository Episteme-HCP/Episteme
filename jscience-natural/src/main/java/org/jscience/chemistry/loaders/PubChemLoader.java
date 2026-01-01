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

import java.io.*;
import java.net.http.*;
import java.net.URI;
import java.util.*;
import com.fasterxml.jackson.databind.*;

/**
 * Loader for PubChem chemical compound database.
 * <p>
 * Fetches compound information from NCBI PubChem REST API.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PubChemLoader {

    private static final String BASE_URL;

    static {
        Properties props = new Properties();
        try (InputStream is = PubChemLoader.class.getResourceAsStream("/jscience.properties")) {
            if (is != null)
                props.load(is);
        } catch (Exception e) {
            /* ignore */ }
        BASE_URL = props.getProperty("api.pubchem.base", "https://pubchem.ncbi.nlm.nih.gov/rest/pug");
    }

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Represents a chemical compound from PubChem.
     */
    public static class CompoundEntry {
        public final long cid;
        public final String iupacName;
        public final String molecularFormula;
        public final double molecularWeight;
        public final String canonicalSmiles;
        public final String inchi;
        public final String inchiKey;

        public CompoundEntry(long cid, String iupacName, String molecularFormula,
                double molecularWeight, String canonicalSmiles, String inchi, String inchiKey) {
            this.cid = cid;
            this.iupacName = iupacName;
            this.molecularFormula = molecularFormula;
            this.molecularWeight = molecularWeight;
            this.canonicalSmiles = canonicalSmiles;
            this.inchi = inchi;
            this.inchiKey = inchiKey;
        }

        @Override
        public String toString() {
            return String.format("%s (CID:%d) - %s, MW=%.2f",
                    iupacName, cid, molecularFormula, molecularWeight);
        }
    }

    /**
     * Fetches compound by PubChem CID.
     *
     * @param cid the PubChem compound ID
     * @return the compound entry, or empty if not found
     */
    public Optional<CompoundEntry> fetchByCid(long cid) {
        try {
            String url = BASE_URL + "/compound/cid/" + cid + "/property/" +
                    "IUPACName,MolecularFormula,MolecularWeight,CanonicalSMILES,InChI,InChIKey/JSON";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode props = root.path("PropertyTable").path("Properties");
                if (props.isArray() && props.size() > 0) {
                    return parseCompound(props.get(0));
                }
            }
        } catch (Exception e) {
            System.err.println("PubChem fetch failed: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Searches compounds by name.
     *
     * @param name the compound name
     * @return list of matching CIDs
     */
    public List<Long> searchByName(String name) {
        List<Long> cids = new ArrayList<>();
        try {
            String url = BASE_URL + "/compound/name/" +
                    java.net.URLEncoder.encode(name, "UTF-8") + "/cids/JSON";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode cidList = root.path("IdentifierList").path("CID");
                if (cidList.isArray()) {
                    for (JsonNode cid : cidList) {
                        cids.add(cid.asLong());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("PubChem search failed: " + e.getMessage());
        }
        return cids;
    }

    /**
     * Fetches compound by SMILES string.
     */
    public Optional<CompoundEntry> fetchBySmiles(String smiles) {
        try {
            String url = BASE_URL + "/compound/smiles/" +
                    java.net.URLEncoder.encode(smiles, "UTF-8") + "/property/" +
                    "IUPACName,MolecularFormula,MolecularWeight,CanonicalSMILES,InChI,InChIKey/JSON";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode props = root.path("PropertyTable").path("Properties");
                if (props.isArray() && props.size() > 0) {
                    return parseCompound(props.get(0));
                }
            }
        } catch (Exception e) {
            System.err.println("PubChem SMILES fetch failed: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Gets 2D structure image URL for a compound.
     */
    public String getStructureImageUrl(long cid) {
        return BASE_URL + "/compound/cid/" + cid + "/PNG";
    }

    private Optional<CompoundEntry> parseCompound(JsonNode node) {
        return Optional.of(new CompoundEntry(
                node.path("CID").asLong(),
                node.path("IUPACName").asText(null),
                node.path("MolecularFormula").asText(null),
                node.path("MolecularWeight").asDouble(0),
                node.path("CanonicalSMILES").asText(null),
                node.path("InChI").asText(null),
                node.path("InChIKey").asText(null)));
    }
}


