/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.sports;

import java.util.*;

/**
 * Represents a sports competition/tournament.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Competition {

    public enum Format {
        KNOCKOUT, ROUND_ROBIN, LEAGUE, GROUP_STAGE, PLAYOFF
    }

    private final String name;
    private final Sport sport;
    private Format format;
    private int year;
    private String organizer;
    private final List<Team> participants = new ArrayList<>();
    private final List<Match> matches = new ArrayList<>();
    private String winner;

    public Competition(String name, Sport sport, int year) {
        this.name = name;
        this.sport = sport;
        this.year = year;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Sport getSport() {
        return sport;
    }

    public Format getFormat() {
        return format;
    }

    public int getYear() {
        return year;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getWinner() {
        return winner;
    }

    public List<Team> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    // Setters
    public void setFormat(Format format) {
        this.format = format;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void addParticipant(Team team) {
        participants.add(team);
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public int getParticipantCount() {
        return participants.size();
    }

    @Override
    public String toString() {
        return String.format("%s %d (%s) - %d teams", name, year, sport.getName(), participants.size());
    }

    // Notable competitions
    public static Competition worldCup(int year) {
        Competition c = new Competition("FIFA World Cup", Sport.SOCCER, year);
        c.setFormat(Format.GROUP_STAGE);
        c.setOrganizer("FIFA");
        return c;
    }

    public static Competition olympics(int year) {
        Competition c = new Competition("Olympic Games", null, year);
        c.setFormat(Format.KNOCKOUT);
        c.setOrganizer("IOC");
        return c;
    }
}
