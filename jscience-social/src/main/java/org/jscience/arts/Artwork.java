/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.arts;

/**
 * Represents an artwork with attribution and classification.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Artwork {

    public enum Medium {
        PAINTING, SCULPTURE, PHOTOGRAPHY, DRAWING, PRINT, MIXED_MEDIA,
        DIGITAL, INSTALLATION, PERFORMANCE, VIDEO, TEXTILE, CERAMIC
    }

    public enum Period {
        ANCIENT, MEDIEVAL, RENAISSANCE, BAROQUE, NEOCLASSICAL, ROMANTIC,
        IMPRESSIONIST, MODERN, CONTEMPORARY, POSTMODERN
    }

    private final String title;
    private final String artist;
    private final int year;
    private final Medium medium;
    private final Period period;
    private final String location; // museum/collection

    public Artwork(String title, String artist, int year, Medium medium,
            Period period, String location) {
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.medium = medium;
        this.period = period;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getYear() {
        return year;
    }

    public Medium getMedium() {
        return medium;
    }

    public Period getPeriod() {
        return period;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("\"%s\" by %s (%d) - %s, %s",
                title, artist, year, medium, period);
    }

    // Famous artworks
    public static final Artwork MONA_LISA = new Artwork("Mona Lisa", "Leonardo da Vinci",
            1503, Medium.PAINTING, Period.RENAISSANCE, "Louvre, Paris");
    public static final Artwork STARRY_NIGHT = new Artwork("The Starry Night", "Vincent van Gogh",
            1889, Medium.PAINTING, Period.IMPRESSIONIST, "MoMA, New York");
    public static final Artwork DAVID = new Artwork("David", "Michelangelo",
            1504, Medium.SCULPTURE, Period.RENAISSANCE, "Galleria dell'Accademia, Florence");
}
