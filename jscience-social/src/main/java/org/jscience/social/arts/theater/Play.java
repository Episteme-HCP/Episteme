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

package org.jscience.social.arts.theater;

import java.util.*;
import org.jscience.social.arts.Artist;

/**
 * Represents a theatrical performance or play.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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



