package org.jscience.biology.services;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for GBIF data.
 * Stub implementation.
 */
public class GbifService {
    private static final GbifService INSTANCE = new GbifService();

    public static GbifService getInstance() {
        return INSTANCE;
    }

    public CompletableFuture<List<GbifSpecies>> searchSpecies(String query) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    public CompletableFuture<String> getSpeciesMedia(long key) {
        return CompletableFuture.completedFuture(null);
    }

    public record GbifSpecies(long key, String scientificName, String rank, String kingdom, String phylum, String clazz,
            String order, String family, String genus) {
    }
}
