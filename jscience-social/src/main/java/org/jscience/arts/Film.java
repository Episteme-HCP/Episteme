package org.jscience.arts;

import java.util.*;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.history.time.UncertainDate;
import org.jscience.economics.money.Money;

/**
 * Represents a film/movie.
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
    private UncertainDate releaseDate;
    private int durationMinutes;
    private Genre genre;
    private Rating rating;
    private String studio;
    private final List<String> cast = new ArrayList<>();
    private Money boxOffice;
    private Real imdbRating;

    public Film(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
    }

    public Film(String title, String director, UncertainDate releaseDate) {
        this(title);
        this.director = director;
        this.releaseDate = releaseDate;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public UncertainDate getReleaseDate() {
        return releaseDate;
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
        return Collections.unmodifiableList(cast);
    }

    // Setters
    public void setDirector(String director) {
        this.director = director;
    }
    
    public void setDirector(Artist artist) {
        if (artist != null) {
            this.director = artist.getName();
        }
    }

    public void setReleaseDate(UncertainDate date) {
        this.releaseDate = date;
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
        return String.format("%s (%s) dir. %s", title, releaseDate != null ? releaseDate.toString() : "Unknown", director);
    }
}


