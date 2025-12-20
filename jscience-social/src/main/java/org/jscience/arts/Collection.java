/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.arts;

import java.util.*;

/**
 * Represents a collection of artworks (museum, gallery, private collection).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Collection {

    public enum Type {
        MUSEUM, GALLERY, PRIVATE, CORPORATE, UNIVERSITY, GOVERNMENT
    }

    private final String name;
    private Type type;
    private String location;
    private final List<Artwork> artworks = new ArrayList<>();
    private String curator;
    private int foundedYear;

    public Collection(String name) {
        this.name = name;
    }

    public Collection(String name, Type type, String location) {
        this(name);
        this.type = type;
        this.location = location;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getCurator() {
        return curator;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    // Setters
    public void setType(Type type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCurator(String curator) {
        this.curator = curator;
    }

    public void setFoundedYear(int year) {
        this.foundedYear = year;
    }

    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
    }

    public void removeArtwork(Artwork artwork) {
        artworks.remove(artwork);
    }

    public List<Artwork> getArtworks() {
        return Collections.unmodifiableList(artworks);
    }

    public int size() {
        return artworks.size();
    }

    public boolean contains(Artwork artwork) {
        return artworks.contains(artwork);
    }

    /**
     * Finds artworks by artist name.
     */
    public List<Artwork> findByArtist(String artistName) {
        return artworks.stream()
                .filter(a -> a.getArtist().equalsIgnoreCase(artistName))
                .toList();
    }

    /**
     * Finds artworks by medium.
     */
    public List<Artwork> findByMedium(Artwork.Medium medium) {
        return artworks.stream()
                .filter(a -> a.getMedium() == medium)
                .toList();
    }

    /**
     * Finds artworks by period.
     */
    public List<Artwork> findByPeriod(Artwork.Period period) {
        return artworks.stream()
                .filter(a -> a.getPeriod() == period)
                .toList();
    }

    @Override
    public String toString() {
        return String.format("Collection '%s' (%s): %d artworks", name, type, size());
    }

    // Notable collections
    public static Collection louvre() {
        Collection c = new Collection("Louvre Museum", Type.MUSEUM, "Paris, France");
        c.setFoundedYear(1793);
        c.addArtwork(Artwork.MONA_LISA);
        return c;
    }
}
