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

package org.jscience.sports;

import java.time.LocalDateTime;
import java.util.UUID;
import org.jscience.util.identity.Identifiable;
import org.jscience.util.Temporal;

/**
 * Represents a sports match/game.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Match implements Identifiable<String>, Temporal {

    public enum Status {
        SCHEDULED, IN_PROGRESS, COMPLETED, POSTPONED, CANCELLED
    }

    private final String id;
    private final Sport sport;
    private final LocalDateTime dateTime;
    private final Team homeTeam;
    private final Team awayTeam;
    private String venue;
    private Status status;
    private int homeScore;
    private int awayScore;
    private String competition;

    public Match(Sport sport, LocalDateTime dateTime, Team homeTeam, Team awayTeam) {
        this.id = UUID.randomUUID().toString();
        this.sport = sport;
        this.dateTime = dateTime;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.status = Status.SCHEDULED;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public java.time.Instant getTimestamp() {
        return dateTime != null ? java.time.Instant.ofEpochSecond(dateTime.toEpochSecond(java.time.ZoneOffset.UTC))
                : java.time.Instant.MIN;
    }

    // Getters
    public Sport getSport() {
        return sport;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
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
    public Team getWinner() {
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
                    homeTeam.getName(), awayTeam.getName(), homeScore, awayScore, sport.getName());
        }
        return String.format("%s vs %s @ %s (%s)",
                homeTeam.getName(), awayTeam.getName(), dateTime, status);
    }
}


