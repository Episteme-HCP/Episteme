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


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jscience.earth.Place;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.util.Temporal;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a discrete sports match between two teams or individuals.
 * Tracks scheduling status, venue, and score results.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Match implements ComprehensiveIdentification, Temporal<TimeCoordinate> {

    private static final long serialVersionUID = 1L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Sport sport;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final TimeCoordinate date;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Team homeTeam;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Team awayTeam;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Place venue;

    @Attribute
    private MatchStatus status;

    @Attribute
    private int homeScore;

    @Attribute
    private int awayScore;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Competition competition;

    /**
     * Creates a new Match.
     * 
     * @param sport    the sport being played
     * @param date     the scheduled date
     * @param homeTeam the hosting team
     * @param awayTeam the visiting team
     * @throws NullPointerException if any required argument is null
     */
    public Match(Sport sport, TimeCoordinate date, Team homeTeam, Team awayTeam) {
        this.id = new UUIDIdentification(UUID.randomUUID().toString());
        this.sport = Objects.requireNonNull(sport, "Sport cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.homeTeam = Objects.requireNonNull(homeTeam, "Home team cannot be null");
        this.awayTeam = Objects.requireNonNull(awayTeam, "Away team cannot be null");
        this.status = MatchStatus.SCHEDULED;
        setName(homeTeam.getName() + " vs " + awayTeam.getName());
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public TimeCoordinate getWhen() {
        return date;
    }

    public TimeCoordinate getTimestamp() {
        return date;
    }

    public Sport getSport() {
        return sport;
    }

    public TimeCoordinate getDate() {
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

    public MatchStatus getStatus() {
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

    public void setStatus(MatchStatus status) {
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
        this.status = MatchStatus.COMPLETED;
    }

    public Team getWinner() {
        if (!MatchStatus.COMPLETED.equals(status)) return null;
        if (homeScore > awayScore) return homeTeam;
        if (awayScore > homeScore) return awayTeam;
        return null; // Tie
    }

    public boolean isTie() {
        return MatchStatus.COMPLETED.equals(status) && homeScore == awayScore;
    }

    @Override
    public String toString() {
        if (status == MatchStatus.COMPLETED) {
            return String.format("%s %d-%d %s (%s)",
                    homeTeam.getName(), homeScore, awayScore, awayTeam.getName(), sport.getName());
        }
        return String.format("%s vs %s @ %s (%s)",
                homeTeam.getName(), awayTeam.getName(), date, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match match)) return false;
        return Objects.equals(id, match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
