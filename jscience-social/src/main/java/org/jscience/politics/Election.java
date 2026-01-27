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

package org.jscience.politics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jscience.sociology.Situation;
import org.jscience.util.Temporal;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a political election event, aggregating votes for candidates.
 * An Election is a specific social Situation where Individuals assume Candidate roles.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Election extends Situation implements Temporal<org.jscience.history.time.TimeCoordinate> {

    private static final long serialVersionUID = 1L;

    @Attribute
    private final LocalDate date;
    
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Country country;
    
    @Attribute
    private final Map<String, Integer> results = new HashMap<>(); // Candidate Name -> Votes (aggregated)
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Ballot> ballots = new ArrayList<>(); // Individual cast ballots

    /**
     * Creates a new Election.
     * @param title   the name of the election
     * @param country the country where it is held
     * @param date    the date of the election
     * @throws NullPointerException if any argument is null
     */
    public Election(String title, Country country, LocalDate date) {
        super(title, "Political election in " + (country != null ? country.getName() : "unknown country"));
        this.country = Objects.requireNonNull(country, "Country cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
    }

    /**
     * Registers a cast ballot in this election.
     * @param ballot the ballot to add
     */
    public void addBallot(Ballot ballot) {
        if (ballot != null) {
            ballots.add(ballot);
            // Auto-aggregate for simple systems if this electionId matches
            String top = ballot.getTopChoice(getName());
            if (top != null) {
                addVote(top, 1);
            }
        }
    }

    /**
     * Returns the list of all individual cast ballots.
     * @return unmodifiable list of ballots
     */
    public List<Ballot> getBallots() {
        return Collections.unmodifiableList(ballots);
    }

    /**
     * Registers an individual as a candidate in this election.
     * 
     * @param individual the person running
     * @param office     the office sought
     * @return the created Candidate role
     */
    public Candidate addCandidate(org.jscience.biology.Individual individual, String office) {
        return new Candidate(individual, this, office);
    }

    /**
     * Registers an individual as a voter in this election.
     * @param individual the person who will vote
     * @return the created Voter role
     */
    public Voter addVoter(org.jscience.biology.Individual individual) {
        return new Voter(individual, this);
    }

    /**
     * Determines the winner(s) using a specific method and all cast ballots.
     * @param method the voting system algorithm to use
     * @param seats  number of seats to fill
     * @return list of winners
     */
    public List<String> calculateWinners(VotingMethod method, int seats) {
        // Use full ballots if possible
        if (!ballots.isEmpty()) {
            return VotingEngine.resolve(ballots, getName(), method, seats);
        }
        // Fallback to aggregated results
        Map<String, Long> longResults = new HashMap<>();
        results.forEach((k, v) -> longResults.put(k, (long) v));
        return VotingSystem.determineWinners(longResults, method, seats);
    }

    /**
     * Adds votes to a specific candidate or party name.
     * @param candidateName name of candidate or party
     * @param count         number of votes to add
     */
    public void addVote(String candidateName, int count) {
        if (candidateName != null) {
            results.merge(candidateName, count, (a, b) -> a + b);
        }
    }

    /**
     * Returns the current tally of aggregated results.
     * @return unmodifiable map of results
     */
    public Map<String, Integer> getResults() {
        return Collections.unmodifiableMap(results);
    }

    /**
     * Determines the winner based on the highest vote count (First Past The Post).
     * @return the name of the winner (top aggregated), or null if no votes
     */
    public String getWinner() {
        return results.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public org.jscience.history.time.TimeCoordinate getWhen() {
        return org.jscience.history.time.FuzzyTimePoint.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    public LocalDate getDate() {
        return date;
    }

    public Country getCountry() {
        return country;
    }
}
