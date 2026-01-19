package org.jscience.sports;

import java.util.UUID;
import java.util.Objects;
import org.jscience.util.identity.Identifiable;
import org.jscience.util.Temporal;
import org.jscience.history.time.UncertainDate;
import org.jscience.geography.Place;

/**
 * Represents a sports match/game.
 */
public class Match implements Identifiable<String>, Temporal {

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
    public java.time.Instant getTimestamp() {
        Object val = date.getMin(0);
        if (val instanceof java.time.Instant i) {
            return i;
        }
        if (val instanceof java.time.chrono.ChronoLocalDate cld) {
             return java.time.LocalDate.from(cld).atStartOfDay(java.time.ZoneId.of("UTC")).toInstant();
        }
        throw new IllegalStateException("Match date type not supported: " + (val == null ? "null" : val.getClass()));
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
            return String.format("%s vs %s: %d-%d (%s)",
                    homeTeam.getName(), awayTeam.getName(), homeScore, awayScore, sport.getName());
        }
        return String.format("%s vs %s @ %s (%s)",
                homeTeam.getName(), awayTeam.getName(), date, status);
    }
}


