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
package org.jscience.politics;

import java.time.LocalDate;
import java.util.*;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an election.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
    private Real turnoutPercentage;
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

    public Real getTurnoutPercentage() {
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

    public void setTurnoutPercentage(Real turnout) {
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
    public Real getVoteShare(String candidate) {
        if (totalVotes == 0)
            return Real.ZERO;
        Long votes = results.get(candidate);
        return votes != null ? Real.of(votes * 100.0 / totalVotes) : Real.ZERO;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - Winner: %s", name, type, date, winner);
    }
}
