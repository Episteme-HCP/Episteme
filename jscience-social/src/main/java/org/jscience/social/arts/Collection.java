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

package org.jscience.social.arts;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a collection of artworks, typically held by a museum, gallery,
 * or private collector.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Collection implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;
    
    @Attribute
    private final Map<String, Object> traits = new HashMap<>();
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Artwork> artworks;

    /**
     * Creates a new Collection with a given name.
     * @param name common name of the collection
     */
    public Collection(String name) {
        this.id = new UUIDIdentification(UUID.randomUUID().toString());
        setName(name);
        this.artworks = new HashSet<>();
    }
    
    public Collection() {
        this("Untitled Collection");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public String getName() {
        String n = (String) getTrait("name");
        return n != null ? n : "Untitled Collection";
    }

    /**
     * Sets a new name for the collection.
     * @param name the new name
     */
    @Override
    public void setName(String name) {
        setTrait("name", Objects.requireNonNull(name, "Collection name cannot be null."));
    }

    /**
     * Returns an unmodifiable set of artworks in this collection.
     * @return set of artworks
     */
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
     * Finds an artwork in this collection by its exact name.
     * 
     * @param artworkName the name to search for
     * @return an Optional containing the artwork if found
     */
    public Optional<Artwork> findByName(String artworkName) {
        return artworks.stream()
                .filter(a -> a.getName().equals(artworkName))
                .findFirst();
    }
}

