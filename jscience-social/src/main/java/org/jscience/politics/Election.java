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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jscience.biology.Individual;
import org.jscience.sociology.Situation;
import org.jscience.util.Temporal;

/**
 * Represents a political election event, aggregating votes for candidates.
 * An Election is a specific social Situation where Individuals assume Candidate roles.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public class Election extends Situation implements Temporal, Serializable {

    private static final long serialVersionUID = 1L;

    private final LocalDate date;
    private final Country country;
    private final Map<String, Integer> results = new HashMap<>(); // Candidate Name -> Votes

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

    @Override
    public java.time.Instant getTimestamp() {
        return java.time.Instant.from(date.atStartOfDay(java.time.ZoneId.of("UTC")));
    }

    /**
     * Registers an individual as a candidate in this election.
     * 
     * @param individual the person running
     * @param office     the office sought
     * @return the created Candidate role
     */
    public Candidate addCandidate(Individual individual, String office) {
        Candidate candidate = new Candidate(individual, this, office);
        // Candidate role automatically adds itself to the situation's roles
        return candidate;
    }

    public String getTitle() {
        return getName();
    }

    public Country getCountry() {
        return country;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Adds votes to a specific candidate or party name.
     * @param candidateName name of candidate or party
     * @param count         number of votes to add
     */
    public void addVote(String candidateName, int count) {
        if (candidateName != null) {
            results.merge(candidateName, count, Integer::sum);
        }
    }

    /**
     * Adds votes for a specific Candidate role.
     * @param candidate the candidate receiving votes
     * @param count     number of votes
     */
    public void addVote(Candidate candidate, int count) {
        if (candidate != null) {
            addVote(candidate.getIndividual().getName(), count);
        }
    }

    /**
     * Returns the current tally of results.
     * @return unmodifiable map of results
     */
    public Map<String, Integer> getResults() {
        return Collections.unmodifiableMap(results);
    }

    /**
     * Determines the winner based on the highest vote count (First Past The Post).
     * @return the name of the winner, or null if no votes
     */
    public String getWinner() {
        return results.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
