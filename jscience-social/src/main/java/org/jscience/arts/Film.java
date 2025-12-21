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

import java.util.*;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a film/movie.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Film {

    public enum Genre {
        ACTION, COMEDY, DRAMA, HORROR, SCIENCE_FICTION, FANTASY,
        THRILLER, ROMANCE, DOCUMENTARY, ANIMATION, MUSICAL, WESTERN
    }

    public enum Rating {
        G, PG, PG_13, R, NC_17, UNRATED
    }

    private final String title;
    private String director;
    private int releaseYear;
    private int durationMinutes;
    private Genre genre;
    private Rating rating;
    private String studio;
    private final List<String> cast = new ArrayList<>();
    private long boxOffice;
    private Real imdbRating;

    public Film(String title) {
        this.title = title;
    }

    public Film(String title, String director, int releaseYear) {
        this(title);
        this.director = director;
        this.releaseYear = releaseYear;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public Genre getGenre() {
        return genre;
    }

    public Rating getRating() {
        return rating;
    }

    public String getStudio() {
        return studio;
    }

    public long getBoxOffice() {
        return boxOffice;
    }

    public Real getImdbRating() {
        return imdbRating;
    }

    public List<String> getCast() {
        return Collections.unmodifiableList(cast);
    }

    // Setters
    public void setDirector(String director) {
        this.director = director;
    }

    public void setReleaseYear(int year) {
        this.releaseYear = year;
    }

    public void setDurationMinutes(int duration) {
        this.durationMinutes = duration;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public void setBoxOffice(long amount) {
        this.boxOffice = amount;
    }

    public void setImdbRating(Real rating) {
        this.imdbRating = rating;
    }

    public void addCastMember(String actor) {
        cast.add(actor);
    }

    @Override
    public String toString() {
        return String.format("%s (%d) dir. %s", title, releaseYear, director);
    }

    // Classic films
    public static Film citizenKane() {
        Film f = new Film("Citizen Kane", "Orson Welles", 1941);
        f.setGenre(Genre.DRAMA);
        f.setDurationMinutes(119);
        f.setImdbRating(Real.of(8.3));
        f.addCastMember("Orson Welles");
        return f;
    }

    public static Film twoThousandOne() {
        Film f = new Film("2001: A Space Odyssey", "Stanley Kubrick", 1968);
        f.setGenre(Genre.SCIENCE_FICTION);
        f.setDurationMinutes(149);
        f.setImdbRating(Real.of(8.3));
        return f;
    }
}