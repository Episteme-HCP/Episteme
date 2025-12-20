/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.politics;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents an election.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Election {

    public enum Type {
        PRESIDENTIAL, PARLIAMENTARY, LOCAL, REFERENDUM, PRIMARY
    }

    private final String name;
    private final Type type;
    private final LocalDate date;
    private String country;
    private final Map<String, Long> results = new LinkedHashMap<>();
    private long totalVotes;
    private double turnoutPercentage;
    private String winner;

    public Election(String name, Type type, LocalDate date) {
        this.name = name;
        this.type = type;
        this.date = date;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public double getTurnoutPercentage() {
        return turnoutPercentage;
    }

    public String getWinner() {
        return winner;
    }

    public Map<String, Long> getResults() {
        return Collections.unmodifiableMap(results);
    }

    // Setters
    public void setCountry(String country) {
        this.country = country;
    }

    public void setTotalVotes(long votes) {
        this.totalVotes = votes;
    }

    public void setTurnoutPercentage(double turnout) {
        this.turnoutPercentage = turnout;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void addResult(String candidate, long votes) {
        results.put(candidate, votes);
    }

    /**
     * Calculates vote share for a candidate.
     */
    public double getVoteShare(String candidate) {
        if (totalVotes == 0)
            return 0;
        Long votes = results.get(candidate);
        return votes != null ? (votes * 100.0 / totalVotes) : 0;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - Winner: %s", name, type, date, winner);
    }
}
