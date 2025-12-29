/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for interacting with the Global Biodiversity Information Facility
 * (GBIF) API.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class GbifService {

    private static final String BASE_URL = "https://api.gbif.org/v1";
    private static GbifService instance;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private GbifService() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public static synchronized GbifService getInstance() {
        if (instance == null) {
            instance = new GbifService();
        }
        return instance;
    }

    /**
     * Searches for species by name.
     * 
     * @param query the search query
     * @return a list of species matching the query
     */
    public CompletableFuture<List<GbifSpecies>> searchSpecies(String query) {
        String url = String.format("%s/species/search?q=%s&limit=20", BASE_URL, query.replace(" ", "%20"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw new RuntimeException("GBIF API error: " + response.statusCode());
                    }
                    return parseSearchResponse(response.body());
                });
    }

    /**
     * Gets the media (images) for a species.
     * 
     * @param speciesKey the GBIF species key
     * @return a future with the first image URL found, or null
     */
    public CompletableFuture<String> getSpeciesMedia(int speciesKey) {
        String url = String.format("%s/species/%d/media", BASE_URL, speciesKey);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200)
                        return null;
                    try {
                        JsonNode root = objectMapper.readTree(response.body());
                        JsonNode results = root.get("results");
                        if (results != null && results.isArray() && results.size() > 0) {
                            for (JsonNode media : results) {
                                if ("StillImage".equals(media.path("type").asText())) {
                                    return media.path("identifier").asText(null);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

    private List<GbifSpecies> parseSearchResponse(String body) {
        List<GbifSpecies> speciesList = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode results = root.get("results");
            if (results != null && results.isArray()) {
                for (JsonNode node : results) {
                    speciesList.add(new GbifSpecies(
                            node.get("key").asInt(),
                            node.path("scientificName").asText("Unknown"),
                            node.path("canonicalName").asText("Unknown"),
                            node.path("rank").asText("Unknown"),
                            node.path("kingdom").asText("Unknown"),
                            node.path("phylum").asText("Unknown"),
                            node.path("clazz").asText("Unknown"),
                            node.path("order").asText("Unknown"),
                            node.path("family").asText("Unknown"),
                            node.path("genus").asText("Unknown")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return speciesList;
    }

    /**
     * Reprents a species from the GBIF API.
     */
    public record GbifSpecies(
            int key,
            String scientificName,
            String canonicalName,
            String rank,
            String kingdom,
            String phylum,
            String clazz,
            String order,
            String family,
            String genus) {
        @Override
        public String toString() {
            return String.format("%s (%s)", canonicalName, scientificName);
        }
    }
}
