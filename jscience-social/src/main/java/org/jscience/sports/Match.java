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
 * THE above copyright notice and this permission notice shall be included in
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

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;
import java.util.UUID;
import org.jscience.geography.Place;
import org.jscience.history.time.UncertainDate;
import org.jscience.util.Temporal;
import org.jscience.util.identity.Identified;

/**
 * Represents a discrete sports match between two teams or individuals.
 * Tracks scheduling status, venue, and score results.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class Match implements Identified<String>, Temporal, Serializable {

    private static final long serialVersionUID = 1L;

    /** Current lifecycle state of the match. */
    public enum Status {
        SCHEDULED, IN_PROGRESS, COMPLETED, POSTPONED, CANCELLED
    }

    private final String id;
    private final Sport sport;
    private final UncertainDate date;
    private final Team homeTeam;
    private final Team awayTeam;
    private Place venue;
    private Status status;
    private int homeScore;
    private int awayScore;
    private Competition competition;

    /**
     * Creates a new Match.
     * 
     * @param sport    the sport being played
     * @param date     the scheduled date (can be uncertain)
     * @param homeTeam the hosting team
     * @param awayTeam the visiting team
     * @throws NullPointerException if any required argument is null
     */
    public Match(Sport sport, UncertainDate date, Team homeTeam, Team awayTeam) {
        this.id = UUID.randomUUID().toString();
        this.sport = Objects.requireNonNull(sport, "Sport cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.homeTeam = Objects.requireNonNull(homeTeam, "Home team cannot be null");
        this.awayTeam = Objects.requireNonNull(awayTeam, "Away team cannot be null");
        this.status = Status.SCHEDULED;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Instant getTimestamp() {
        Object val = date.getMin(0);
        if (val instanceof Instant i) return i;
        if (val instanceof ChronoLocalDate cld) {
             return LocalDate.from(cld).atStartOfDay(ZoneId.of("UTC")).toInstant();
        }
        return Instant.EPOCH;
    }

    public Sport getSport() {
        return sport;
    }

    public UncertainDate getDate() {
        return date;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Place getVenue() {
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

    public Competition getCompetition() {
        return competition;
    }

    public void setVenue(Place venue) {
        this.venue = venue;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    /**
     * Finalizes the match result and updates status to COMPLETED.
     */
    public void setScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.status = Status.COMPLETED;
    }

    public Team getWinner() {
        if (status != Status.COMPLETED) return null;
        if (homeScore > awayScore) return homeTeam;
        if (awayScore > homeScore) return awayTeam;
        return null; // Tie
    }

    public boolean isTie() {
        return status == Status.COMPLETED && homeScore == awayScore;
    }

    @Override
    public String toString() {
        if (status == Status.COMPLETED) {
            return String.format("%s %d-%d %s (%s)",
                    homeTeam.getName(), homeScore, awayScore, awayTeam.getName(), sport.getName());
        }
        return String.format("%s vs %s @ %s (%s)",
                homeTeam.getName(), awayTeam.getName(), date, status);
    }
}
