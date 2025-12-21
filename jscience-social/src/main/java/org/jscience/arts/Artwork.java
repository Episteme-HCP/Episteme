/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.arts;

/**
 * Represents an artwork with attribution and classification.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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