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
package org.jscience.sports;

import java.util.*;

/**
 * Represents a sports team.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Team {

    private final String name;
    private String nickname;
    private String homeCity;
    private int foundedYear;
    private Sport sport;
    private final List<String> players = new ArrayList<>();
    private String coach;
    private String stadium;

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, Sport sport, String homeCity) {
        this(name);
        this.sport = sport;
        this.homeCity = homeCity;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public Sport getSport() {
        return sport;
    }

    public String getCoach() {
        return coach;
    }

    public String getStadium() {
        return stadium;
    }

    public List<String> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    // Setters
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHomeCity(String city) {
        this.homeCity = city;
    }

    public void setFoundedYear(int year) {
        this.foundedYear = year;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public void addPlayer(String player) {
        players.add(player);
    }

    public void removePlayer(String player) {
        players.remove(player);
    }

    public int getRosterSize() {
        return players.size();
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - %d players",
                name, sport != null ? sport.getName() : "N/A", homeCity, players.size());
    }
}