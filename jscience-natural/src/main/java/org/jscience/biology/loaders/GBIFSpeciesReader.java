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

import java.io.*;
import java.net.http.*;
import java.net.URI;
import java.util.*;
import com.fasterxml.jackson.databind.*;

/**
 * Loader for GBIF (Global Biodiversity Information Facility) Species API.
 * <p>
 * Fetches species occurrence and taxonomy data from GBIF.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GBIFSpeciesReader {

    public static String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.biology", "Biology");
    }

    public static String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.gbif.desc", "GBIF Species API Reader.");
    }

    private static final String BASE_URL;

    static {
        Properties props = new Properties();
        try (InputStream is = GBIFSpeciesReader.class.getResourceAsStream("/jscience.properties")) {
            if (is != null)
                props.load(is);
        } catch (Exception e) {
            /* ignore */ }
        BASE_URL = props.getProperty("api.gbif.taxonomy.base", "https://api.gbif.org/v1/species");
    }

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Represents a species entry from GBIF.
     */
    public static class SpeciesEntry {
        public final long key;
        public final String scientificName;
        public final String canonicalName;
        public final String rank;
        public final String kingdom;
        public final String phylum;
        public final String clazz;
        public final String order;
        public final String family;
        public final String genus;
        public final String species;

        public SpeciesEntry(long key, String scientificName, String canonicalName, String rank,
                String kingdom, String phylum, String clazz, String order,
                String family, String genus, String species) {
            this.key = key;
            this.scientificName = scientificName;
            this.canonicalName = canonicalName;
            this.rank = rank;
            this.kingdom = kingdom;
            this.phylum = phylum;
            this.clazz = clazz;
            this.order = order;
            this.family = family;
            this.genus = genus;
            this.species = species;
        }

        @Override
        public String toString() {
            return String.format("%s [%s] - %s > %s > %s",
                    canonicalName != null ? canonicalName : scientificName,
                    rank, kingdom, family, genus);
        }
    }

    /**
     * Fetches species by GBIF key.
     *
     * @param key the GBIF species key
     * @return the species entry, or empty if not found
     */
    public Optional<SpeciesEntry> fetchByKey(long key) {
        try {
            String url = BASE_URL + "/" + key;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parseSpecies(mapper.readTree(response.body()));
            }
        } catch (Exception e) {
            System.err.println("GBIF fetch failed: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Searches for species by name.
     *
     * @param name  the species name to search
     * @param limit maximum results
     * @return list of matching species
     */
    public List<SpeciesEntry> searchByName(String name, int limit) {
        List<SpeciesEntry> results = new ArrayList<>();
        try {
            String url = BASE_URL + "/search?q=" +
                    java.net.URLEncoder.encode(name, "UTF-8") + "&limit=" + limit;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode resultList = root.path("results");
                if (resultList.isArray()) {
                    for (JsonNode node : resultList) {
                        parseSpecies(node).ifPresent(results::add);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("GBIF search failed: " + e.getMessage());
        }
        return results;
    }

    /**
     * Gets children of a taxon.
     */
    public List<SpeciesEntry> getChildren(long parentKey, int limit) {
        List<SpeciesEntry> children = new ArrayList<>();
        try {
            String url = BASE_URL + "/" + parentKey + "/children?limit=" + limit;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode results = root.path("results");
                if (results.isArray()) {
                    for (JsonNode node : results) {
                        parseSpecies(node).ifPresent(children::add);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("GBIF children fetch failed: " + e.getMessage());
        }
        return children;
    }

    private Optional<SpeciesEntry> parseSpecies(JsonNode node) {
        if (!node.has("key"))
            return Optional.empty();

        return Optional.of(new SpeciesEntry(
                node.path("key").asLong(),
                node.path("scientificName").asText(null),
                node.path("canonicalName").asText(null),
                node.path("rank").asText(null),
                node.path("kingdom").asText(null),
                node.path("phylum").asText(null),
                node.path("class").asText(null),
                node.path("order").asText(null),
                node.path("family").asText(null),
                node.path("genus").asText(null),
                node.path("species").asText(null)));
    }

    /**
     * Gets the media (images) for a species.
     * 
     * @param speciesKey the GBIF species key
     * @return the first image URL found, or empty if not found
     */
    public Optional<String> getSpeciesMedia(long speciesKey) {
        try {
            String url = String.format("%s/%d/media", BASE_URL, speciesKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode results = root.get("results");
                if (results != null && results.isArray() && results.size() > 0) {
                    for (JsonNode media : results) {
                        if ("StillImage".equals(media.path("type").asText())) {
                            return Optional.ofNullable(media.path("identifier").asText(null));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("GBIF media fetch failed: " + e.getMessage());
        }
        return Optional.empty();
    }
}
