package org.jscience.arts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

/**
 * A Collection of Artworks.
 * Represents a group of artworks, typically at a single location (e.g., Museum, Gallery).
 */
public class Collection {

    private String name;
    private final Set<Artwork> artworks;

    public Collection(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Collection name cannot be null.");
        }
        this.name = name;
        this.artworks = new HashSet<>();
    }
    
    public Collection() {
        this("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Collection name cannot be null.");
        }
        this.name = name;
    }

    public Set<Artwork> getArtworks() {
        return Collections.unmodifiableSet(artworks);
    }
    
    public void addArtwork(Artwork artwork) {
        if (artwork != null) {
            artworks.add(artwork);
        }
    }

    public void removeArtwork(Artwork artwork) {
        artworks.remove(artwork);
    }

    public boolean contains(Artwork artwork) {
        return artworks.contains(artwork);
    }
    
    /**
     * Finds an artwork by name (since we don't have Identification system ported yet).
     */
    public Optional<Artwork> findByName(String artworkName) {
        return artworks.stream()
                .filter(a -> a.getName().equals(artworkName))
                .findFirst();
    }
}
