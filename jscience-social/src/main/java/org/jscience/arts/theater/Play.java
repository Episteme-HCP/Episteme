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
package org.jscience.arts.theater;

import java.util.*;

/**
 * Represents a theatrical performance or play.
 * * @author Silvere Martin-Michiellot
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
    private int acts;
    private int durationMinutes;
    private int yearWritten;
    private final List<String> characters = new ArrayList<>();

    public Play(String title) {
        this.title = title;
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

    public int getActs() {
        return acts;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getYearWritten() {
        return yearWritten;
    }

    public List<String> getCharacters() {
        return Collections.unmodifiableList(characters);
    }

    // Setters
    public void setPlaywright(String playwright) {
        this.playwright = playwright;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setActs(int acts) {
        this.acts = acts;
    }

    public void setDurationMinutes(int duration) {
        this.durationMinutes = duration;
    }

    public void setYearWritten(int year) {
        this.yearWritten = year;
    }

    public void addCharacter(String character) {
        characters.add(character);
    }

    @Override
    public String toString() {
        return String.format("\"%s\" by %s (%s, %d)", title, playwright, genre, yearWritten);
    }

    // Famous plays
    public static Play hamlet() {
        Play p = new Play("Hamlet", "William Shakespeare", Genre.TRAGEDY);
        p.setYearWritten(1600);
        p.setActs(5);
        p.addCharacter("Hamlet");
        p.addCharacter("Claudius");
        p.addCharacter("Ophelia");
        return p;
    }

    public static Play romeoAndJuliet() {
        Play p = new Play("Romeo and Juliet", "William Shakespeare", Genre.TRAGEDY);
        p.setYearWritten(1597);
        p.setActs(5);
        return p;
    }
}