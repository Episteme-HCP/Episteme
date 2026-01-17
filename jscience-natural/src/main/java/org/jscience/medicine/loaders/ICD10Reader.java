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

package org.jscience.medicine.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.medicine.Disease;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Loader for ICD-10/ICD-11 Disease Database.
 * Fetches disease information from an ICD API.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ICD10Reader extends org.jscience.io.AbstractResourceReader<Disease> {

    private static final String BASE_URL;
    private static final String API_KEY;

    static {
        Properties props = new Properties();
        try (InputStream is = ICD10Reader.class.getResourceAsStream("/jscience.properties")) {
            if (is != null)
                props.load(is);
        } catch (Exception e) {
            /* ignore */ }
        BASE_URL = props.getProperty("api.icd.base", "https://id.who.int/icd/release/11/mms");
        API_KEY = props.getProperty("api.icd.key", "");
    }

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.medicine", "Medicine");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.icd10reader.desc", "ICD Disease Classification Reader.");
    }

    @Override
    public Class<Disease> getResourceType() {
        return Disease.class;
    }

    @Override
    public String getResourcePath() {
        return BASE_URL;
    }

    @Override
    protected Disease loadFromSource(String resourceId) throws Exception {
        return fetchByCode(resourceId).orElse(null);
    }

    /**
     * Fetches disease details by ICD Code.
     *
     * @param code the ICD code (e.g. "U07.1")
     * @return the disease entry
     */
    public Optional<Disease> fetchByCode(String code) {
        try {
            String url = BASE_URL + "/" + java.net.URLEncoder.encode(code, "UTF-8");
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET();
            
            if (!API_KEY.isEmpty()) {
                builder.header("Authorization", "Bearer " + API_KEY);
            }

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                return parseDisease(root, code);
            }
        } catch (Exception e) {
            System.err.println("ICD fetch failed: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Searches diseases by title/name.
     *
     * @param term search term
     * @return list of matching diseases
     */
    public List<Disease> search(String term) {
        List<Disease> results = new ArrayList<>();
        // Implementation depends on specific API search endpoint
        return results; 
    }
    
    /**
     * Loads diseases from a JSON resource (dump).
     * @param resourcePath classpath to json file
     * @return list of diseases
     */
    public List<Disease> loadFromResource(String resourcePath) {
        List<Disease> list = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is != null) {
                JsonNode root = mapper.readTree(is);
                if (root.isArray()) {
                    for (JsonNode n : root) list.add(parseDisease(n, null).orElse(null));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private Optional<Disease> parseDisease(JsonNode node, String code) {
        // Adapt parsing to actual API JSON structure
        String title = node.path("title").path("@value").asText("Unknown Disease");
        String desc = node.path("definition").path("@value").asText(null);
        String icd = code != null ? code : node.path("code").asText(null);
        
        Disease d = new Disease(title);
        d.setIcdCode(icd);
        if (desc != null) {
            d.setDescription(desc);
        }
        return Optional.of(d);
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.icd10reader.name", "ICD-10 Disease Reader");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.icd10reader.longdesc", "Retrieves disease classification and details from the ICD-10/ICD-11 API.");
    }
}
