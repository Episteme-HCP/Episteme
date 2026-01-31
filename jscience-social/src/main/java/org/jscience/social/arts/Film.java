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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jscience.social.economics.money.Money;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents a film or motion picture within the cinematic arts.
 * Tracks production details, casting, genre classification, and commercial performance.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Film extends Artwork {

    private static final long serialVersionUID = 3L;

    /**
     * Cinematic genres.
     */
    public enum Genre {
        ACTION, COMEDY, DRAMA, HORROR, SCIENCE_FICTION, FANTASY,
        THRILLER, ROMANCE, DOCUMENTARY, ANIMATION, MUSICAL, WESTERN
    }

    /**
     * Film ratings (MPAA based).
     */
    public enum Rating {
        G, PG, PG_13, R, NC_17, UNRATED
    }

    @Attribute
    private String director;
    @Attribute
    private int durationMinutes;
    @Attribute
    private Genre genre;
    @Attribute
    private Rating rating;
    @Attribute
    private String studio;
    @Attribute
    private final List<String> cast = new ArrayList<>();
    @Attribute
    private Money boxOffice;
    @Attribute
    private Real imdbRating;

    /**
     * Creates a new Film with a title.
     * @param title the movie title
     */
    public Film(String title) {
        super(title, "", null, null, ArtForm.CINEMA);
    }

    /**
     * Creates a new Film with production details.
     * @param title the movie title
     * @param director the name of the director
     * @param releaseDate the official release date
     */
    public Film(String title, String director, TimeCoordinate releaseDate) {
        super(title, "", releaseDate, null, ArtForm.CINEMA);
        this.director = director;
    }

    public String getTitle() {
        return getName();
    }

    public String getDirector() {
        return director;
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

    public Money getBoxOffice() {
        return boxOffice;
    }

    public Real getImdbRating() {
        return imdbRating;
    }

    public List<String> getCast() {
        return Collections.unmodifiableSet(new java.util.HashSet<>(cast)).stream().toList(); // simplified
    }

    public void setDirector(String director) {
        this.director = director;
    }
    
    public void setDirector(Artist artist) {
        if (artist != null) {
            this.director = artist.getName();
        }
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

    public void setBoxOffice(Money boxOffice) {
        this.boxOffice = boxOffice;
    }

    public void setImdbRating(Real rating) {
        this.imdbRating = rating;
    }

    public void addCastMember(String actor) {
        if (actor != null) {
            cast.add(actor);
        }
    }
    
    public void addCastMember(Artist artist) {
        if (artist != null) {
            cast.add(artist.getName());
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s) dir. %s", getTitle(), getProductionDate() != null ? getProductionDate().toString() : "Unknown", director);
    }
}

