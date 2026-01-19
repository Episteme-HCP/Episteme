package org.jscience.arts.theater;

import java.util.*;
import org.jscience.arts.Artist;

/**
 * Represents a theatrical performance or play.
 */
public class Play {

    public enum Genre {
        TRAGEDY, COMEDY, DRAMA, MUSICAL, OPERA, BALLET,
        FARCE, MELODRAMA, PANTOMIME, EXPERIMENTAL
    }

    private final String title;
    private String playwright;
    private Genre genre;
    private final List<Act> acts = new ArrayList<>();
    private int durationMinutes;
    private int yearWritten;
    private final List<Character> characters = new ArrayList<>();

    public Play(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
    }

    public Play(String title, String playwright, Genre genre) {
        this(title);
        this.playwright = playwright;
        this.genre = genre;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getPlaywright() {
        return playwright;
    }

    public Genre getGenre() {
        return genre;
    }

    public List<Act> getActs() {
        return Collections.unmodifiableList(acts);
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getYearWritten() {
        return yearWritten;
    }

    public List<Character> getCharacters() {
        return Collections.unmodifiableList(characters);
    }

    // Setters
    public void setPlaywright(String playwright) {
        this.playwright = playwright;
    }
    
    public void setPlaywright(Artist artist) {
        if (artist != null) {
            this.playwright = artist.getName();
        }
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void addAct(Act act) {
        if (act != null) {
            acts.add(act);
        }
    }

    public void setDurationMinutes(int duration) {
        this.durationMinutes = duration;
    }

    public void setYearWritten(int year) {
        this.yearWritten = year;
    }

    public void addCharacter(Character character) {
        if (character != null) {
            characters.add(character);
        }
    }

    @Override
    public String toString() {
        return String.format("\"%s\" by %s (%s, %d)", title, playwright, genre, yearWritten);
    }
}


