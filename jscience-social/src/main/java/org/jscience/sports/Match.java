/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.sports;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a sports match/game.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Match {

    public enum Status {
        SCHEDULED, IN_PROGRESS, COMPLETED, POSTPONED, CANCELLED
    }

    private final Sport sport;
    private final LocalDateTime dateTime;
    private final String homeTeam;
    private final String awayTeam;
    private String venue;
    private Status status;
    private int homeScore;
    private int awayScore;
    private String competition;

    public Match(Sport sport, LocalDateTime dateTime, String homeTeam, String awayTeam) {
        this.sport = sport;
        this.dateTime = dateTime;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.status = Status.SCHEDULED;
    }

    // Getters
    public Sport getSport() {
        return sport;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getVenue() {
        return venue;
    }

    public Status getStatus() {
        return status;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public String getCompetition() {
        return competition;
    }

    // Setters
    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    /**
     * Records the final score.
     */
    public void setScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.status = Status.COMPLETED;
    }

    /**
     * Returns the winner or null if tie/not finished.
     */
    public String getWinner() {
        if (status != Status.COMPLETED)
            return null;
        if (homeScore > awayScore)
            return homeTeam;
        if (awayScore > homeScore)
            return awayTeam;
        return null; // Tie
    }

    /**
     * Checks if match ended in a tie.
     */
    public boolean isTie() {
        return status == Status.COMPLETED && homeScore == awayScore;
    }

    @Override
    public String toString() {
        if (status == Status.COMPLETED) {
            return String.format("%s vs %s: %d-%d (%s)",
                    homeTeam, awayTeam, homeScore, awayScore, sport.getName());
        }
        return String.format("%s vs %s @ %s (%s)",
                homeTeam, awayTeam, dateTime, status);
    }
}
