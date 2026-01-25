package org.jscience.biology.loaders;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jscience.io.AbstractResourceReader;

/**
 * Service for GBIF data.
 * Stub implementation.
 */
public class GBIFReader extends AbstractResourceReader<List<GBIFReader.GbifSpecies>> {
    
    // Singleton instance access
    private static final GBIFReader INSTANCE = new GBIFReader();
    public static GBIFReader getInstance() { return INSTANCE; }

    @Override
    public String getName() { return "GBIF Reader"; }

    @Override
    public String getDescription() { return "Access Global Biodiversity Information Facility data"; }
    
    @Override
    public String getCategory() { return "Biology"; }

    @Override
    public String[] getSupportedVersions() { return new String[] { "1.0" }; }

    @Override
    public String getLongDescription() { 
        return "Search and retrieve species data from the Global Biodiversity Information Facility (GBIF)."; 
    }

    @Override
    public String getResourcePath() {
        return "https://api.gbif.org/v1/";
    }

    @Override
    public Class<List<GbifSpecies>> getResourceType() { 
        return (Class) List.class; 
    }

    @Override
    protected List<GbifSpecies> loadFromSource(String query) throws Exception {
        // Interpreting ID as search query for now, or could be a key
        return searchSpecies(query).join();
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
