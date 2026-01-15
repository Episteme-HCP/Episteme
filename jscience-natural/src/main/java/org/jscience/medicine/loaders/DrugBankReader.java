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
import org.jscience.medicine.Medication;

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
 * Loader for Drug/Medication Database (e.g. DrugBank or similar API).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DrugBankReader implements org.jscience.io.ResourceReader<Medication> {

    private static final String BASE_URL;
    private static final String API_KEY;

    static {
        Properties props = new Properties();
        try (InputStream is = DrugBankReader.class.getResourceAsStream("/jscience.properties")) {
            if (is != null)
                props.load(is);
        } catch (Exception e) {
            /* ignore */ }
        BASE_URL = props.getProperty("api.drugbank.base", "https://api.drugbank.com/v1");
        API_KEY = props.getProperty("api.drugbank.key", "");
    }

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getCategory() {
        return "Medicine";
    }

    @Override
    public String getDescription() {
        return "DrugBank Medication Data Reader.";
    }

    @Override
    public Class<Medication> getResourceType() {
        return Medication.class;
    }

    @Override
    public String getResourcePath() {
        return BASE_URL;
    }

    @Override
    public Medication load(String resourceId) throws Exception {
        return fetchById(resourceId).orElse(null);
    }

    /**
     * Fetches medication details by Drug ID.
     *
     * @param id the DrugBank ID or equivalent
     * @return the medication entry
     */
    public Optional<Medication> fetchById(String id) {
        try {
            String url = BASE_URL + "/drugs/" + java.net.URLEncoder.encode(id, "UTF-8");
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET();

            if (!API_KEY.isEmpty()) {
                builder.header("Authorization", "Bearer " + API_KEY);
            }

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                return parseMedication(root);
            }
        } catch (Exception e) {
            System.err.println("Drug fetch failed: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Searches drugs by name using the API.
     *
     * @param name search term
     * @return list of matching drugs
     */
    public List<Medication> searchByName(String name) {
        List<Medication> results = new ArrayList<>();
        // Placeholder for API search
        return results;
    }
    
    /**
     * Loads medications from a JSON resource (dump).
     * @param resourcePath classpath to json file
     * @return list of medications
     */
    public List<Medication> loadFromResource(String resourcePath) {
        List<Medication> list = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is != null) {
                JsonNode root = mapper.readTree(is);
                if (root.isArray()) {
                    for (JsonNode n : root) list.add(parseMedication(n).orElse(null));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private Optional<Medication> parseMedication(JsonNode node) {
        String name = node.path("name").asText("Unknown Drug");
        String formStr = node.path("form").asText("TABLET");
        String routeStr = node.path("route").asText("ORAL");
        
        Medication m = new Medication(name);
        try {
            m.setForm(Medication.Form.valueOf(formStr.toUpperCase()));
        } catch(IllegalArgumentException e) {
            // keep default or log
        }
        try {
            m.setRoute(Medication.Route.valueOf(routeStr.toUpperCase()));
        } catch(IllegalArgumentException e) {
            // keep default
        }
        
        m.setDosage(node.path("dosage").asText(""));
        m.setGenericName(node.path("generic_name").asText(null));
        m.setBrandName(node.path("brand_name").asText(null));
        
        if (node.has("active_ingredients")) {
            for(JsonNode ing : node.get("active_ingredients")) {
                m.addActiveIngredient(ing.asText());
            }
        }
        
        return Optional.of(m);
    }

    @Override
    public String getName() {
        return "DrugBank"; // Simple fallback
    }
}
