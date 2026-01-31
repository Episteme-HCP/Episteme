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

package org.jscience.natural.chemistry.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.natural.chemistry.Compound;
import org.jscience.core.io.AbstractResourceReader;
import org.jscience.core.io.Configuration;
import org.jscience.core.ui.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Modernized loader for the PubChem chemical compound database.
 * <p>
 * Fetches compound information from the NCBI PubChem PUG REST API.
 * Uses the JScience ResourceReader framework for consistent data access.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PubChemReader extends AbstractResourceReader<Compound> {

    private static final Logger LOG = LoggerFactory.getLogger(PubChemReader.class);
    private static final String DEFAULT_BASE_URL = "https://pubchem.ncbi.nlm.nih.gov/rest/pug";
    private final String baseUrl;

    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public PubChemReader() {
        this(Configuration.get("api.pubchem.base", DEFAULT_BASE_URL));
    }

    public PubChemReader(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public String getCategory() {
        return I18N.getInstance().get("category.chemistry", "Chemistry");
    }

    @Override
    public String getName() {
        return I18N.getInstance().get("reader.pubchem.name", "PubChem Reader");
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().get("reader.pubchem.desc", "Electronic database of chemical molecules.");
    }

    @Override
    public String getLongDescription() {
        return I18N.getInstance().get("reader.pubchem.longdesc", 
            "Official reader for NCBI PubChem, providing access to millions of compound properties, structures, and bioactivities.");
    }

    @Override
    public String getResourcePath() {
        return baseUrl;
    }

    @Override
    public Class<Compound> getResourceType() {
        return Compound.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"PUG REST v1.0"};
    }

    @Override
    protected Compound loadFromSource(String resourceId) throws Exception {
        // resourceId can be a CID (numeric string) or a SMILES
        if (resourceId.matches("\\d+")) {
            return fetchByCid(Long.parseLong(resourceId)).join();
        } else if (resourceId.contains("=") || resourceId.contains("(") || resourceId.length() > 5) {
            // Very basic heuristic for SMILES
            return fetchBySmiles(resourceId).join();
        }
        
        // Try search by name as a last resort
        List<Long> cids = searchByName(resourceId).join();
        if (!cids.isEmpty()) {
            return fetchByCid(cids.get(0)).join();
        }
        
        return null;
    }

    /**
     * Fetches compound by PubChem CID.
     */
    public CompletableFuture<Compound> fetchByCid(long cid) {
        String url = baseUrl + "/compound/cid/" + cid + "/property/" +
                "IUPACName,MolecularFormula,MolecularWeight,CanonicalSMILES,InChI,InChIKey/JSON";
        
        return executeRequest(url)
                .thenApply(response -> {
                    try {
                        JsonNode root = mapper.readTree(response);
                        JsonNode props = root.path("PropertyTable").path("Properties");
                        if (props.isArray() && props.size() > 0) {
                            return parseCompound(props.get(0));
                        }
                    } catch (Exception e) {
                        LOG.error("Failed to parse PubChem response for CID {}", cid, e);
                    }
                    return null;
                });
    }

    /**
     * Searches compounds by name, returning a list of CIDs.
     */
    public CompletableFuture<List<Long>> searchByName(String name) {
        try {
            String url = baseUrl + "/compound/name/" +
                    java.net.URLEncoder.encode(name, "UTF-8") + "/cids/JSON";
            
            return executeRequest(url)
                    .thenApply(response -> {
                        List<Long> cids = new ArrayList<>();
                        try {
                            JsonNode root = mapper.readTree(response);
                            JsonNode cidList = root.path("IdentifierList").path("CID");
                            if (cidList.isArray()) {
                                for (JsonNode cid : cidList) {
                                    cids.add(cid.asLong());
                                }
                            }
                        } catch (Exception e) {
                            LOG.error("Failed to parse search results for {}", name, e);
                        }
                        return cids;
                    });
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Fetches compound by SMILES string.
     */
    public CompletableFuture<Compound> fetchBySmiles(String smiles) {
        try {
            String url = baseUrl + "/compound/smiles/" +
                    java.net.URLEncoder.encode(smiles, "UTF-8") + "/property/" +
                    "IUPACName,MolecularFormula,MolecularWeight,CanonicalSMILES,InChI,InChIKey/JSON";
            
            return executeRequest(url)
                    .thenApply(response -> {
                        try {
                            JsonNode root = mapper.readTree(response);
                            JsonNode props = root.path("PropertyTable").path("Properties");
                            if (props.isArray() && props.size() > 0) {
                                return parseCompound(props.get(0));
                            }
                        } catch (Exception e) {
                            LOG.error("Failed to parse SMILES response", e);
                        }
                        return null;
                    });
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private CompletableFuture<String> executeRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "JScience/2.0")
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        LOG.warn("PubChem API error: HTTP {} for {}", response.statusCode(), url);
                        return "{}";
                    }
                    return response.body();
                });
    }

    private Compound parseCompound(JsonNode node) {
        return new Compound(
                node.path("CID").asLong(),
                node.path("IUPACName").asText(null),
                node.path("MolecularFormula").asText(null),
                node.path("MolecularWeight").asDouble(0),
                node.path("CanonicalSMILES").asText(null),
                node.path("InChI").asText(null),
                node.path("InChIKey").asText(null));
    }

    /**
     * Gets 2D structure image URL for a compound.
     */
    public String getStructureImageUrl(long cid) {
        return baseUrl + "/compound/cid/" + cid + "/PNG";
    }
}


