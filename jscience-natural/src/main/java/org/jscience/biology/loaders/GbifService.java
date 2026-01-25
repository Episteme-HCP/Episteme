package org.jscience.biology.loaders;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for accessing GBIF data.
 */
public class GbifService {
    
    private static final GbifService INSTANCE = new GbifService();
    
    public static GbifService getInstance() {
        return INSTANCE;
    }
    
    public record GbifSpecies(int key, String scientificName, String rank, String kingdom, String phylum, String clazz, String order, String family, String genus) {}
    
    /**
     * Searches for species.
     */
    public CompletableFuture<List<GbifSpecies>> searchSpecies(String query) {
        return CompletableFuture.supplyAsync(Collections::emptyList);
    }
    
    /**
     * Returns a URL to an image/media of the species.
     */
    public CompletableFuture<String> getSpeciesMedia(int key) {
        return CompletableFuture.completedFuture(null);
    }
}
