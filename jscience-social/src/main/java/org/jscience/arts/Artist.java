/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.arts;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents an artist (painter, sculptor, musician, etc.).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Artist {

    public enum Medium {
        PAINTING, SCULPTURE, MUSIC, DANCE, THEATER, FILM,
        PHOTOGRAPHY, ARCHITECTURE, LITERATURE, DIGITAL
    }

    private final String name;
    private final Set<Medium> media = EnumSet.noneOf(Medium.class);
    private String nationality;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String movement; // e.g., "Impressionism", "Baroque"
    private final List<String> notableWorks = new ArrayList<>();
    private String biography;

    public Artist(String name) {
        this.name = name;
    }

    public Artist(String name, Medium primaryMedium) {
        this(name);
        this.media.add(primaryMedium);
    }

    // Getters
    public String getName() {
        return name;
    }

    public Set<Medium> getMedia() {
        return Collections.unmodifiableSet(media);
    }

    public String getNationality() {
        return nationality;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public String getMovement() {
        return movement;
    }

    public String getBiography() {
        return biography;
    }

    public List<String> getNotableWorks() {
        return Collections.unmodifiableList(notableWorks);
    }

    // Setters
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setBirthDate(LocalDate date) {
        this.birthDate = date;
    }

    public void setDeathDate(LocalDate date) {
        this.deathDate = date;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public void setBiography(String bio) {
        this.biography = bio;
    }

    public void addMedium(Medium medium) {
        media.add(medium);
    }

    public void addNotableWork(String work) {
        notableWorks.add(work);
    }

    public boolean isAlive() {
        return deathDate == null;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", name, media, nationality);
    }

    // Famous artists
    public static Artist leonardoDaVinci() {
        Artist a = new Artist("Leonardo da Vinci", Medium.PAINTING);
        a.addMedium(Medium.SCULPTURE);
        a.setNationality("Italian");
        a.setBirthDate(LocalDate.of(1452, 4, 15));
        a.setDeathDate(LocalDate.of(1519, 5, 2));
        a.setMovement("Renaissance");
        a.addNotableWork("Mona Lisa");
        a.addNotableWork("The Last Supper");
        return a;
    }

    public static Artist beethoven() {
        Artist a = new Artist("Ludwig van Beethoven", Medium.MUSIC);
        a.setNationality("German");
        a.setBirthDate(LocalDate.of(1770, 12, 17));
        a.setDeathDate(LocalDate.of(1827, 3, 26));
        a.setMovement("Romanticism");
        a.addNotableWork("Symphony No. 9");
        a.addNotableWork("Moonlight Sonata");
        return a;
    }
}
