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

package org.jscience.social.politics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents a cast ballot in an election, supporting various voting methods.
 * A ballot can capture multiple decision points (e.g., President, Governor), 
 * and for each, it supports single choices, ranked lists of preferences, or ratings.
 * Integrated with the persistence system and uses Real for internal scores.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.1
 * @since 1.0
 */
@Persistent
public final class Ballot implements Serializable {

    private static final long serialVersionUID = 2L;

    @Attribute
    private final String voterId; // Unique identifier for the voter (anonymized in practice)
    
    @Attribute
    private final Map<String, List<String>> selections; // Map of Choice/Election ID -> rankings
    
    @Attribute
    private final Map<String, Map<String, Real>> ratings; // Map of Choice/Election ID -> (Candidate -> Score)

    /**
     * Primary constructor.
     */
    public Ballot(String voterId, Map<String, List<String>> selections, Map<String, Map<String, Real>> ratings) {
        this.voterId = Objects.requireNonNull(voterId);
        this.selections = selections != null ? selections : new HashMap<>();
        this.ratings = ratings != null ? ratings : new HashMap<>();
    }

    /**
     * Creates a simple single-choice ballot for a specific election.
     * 
     * @param voterId    Identifier of the voter
     * @param electionId The decision point ID (e.g., "PRESIDENT_2026")
     * @param choice     The selected candidate or choice
     * @return A new Ballot instance
     */
    public static Ballot singleChoice(String voterId, String electionId, String choice) {
        Map<String, List<String>> picks = new HashMap<>();
        picks.put(electionId, List.of(choice));
        return new Ballot(voterId, picks, null);
    }

    /**
     * Creates a ranked-choice ballot for a specific election.
     * 
     * @param voterId    Identifier of the voter
     * @param electionId The decision point ID
     * @param rankings   Ordered list of candidates (index 0 is top preference)
     * @return A new Ballot instance
     */
    public static Ballot rankedChoice(String voterId, String electionId, List<String> rankings) {
        Map<String, List<String>> picks = new HashMap<>();
        picks.put(electionId, List.copyOf(rankings));
        return new Ballot(voterId, picks, null);
    }

    /**
     * Creates a cardinal/rating ballot for a specific election from double values.
     * 
     * @param voterId    Identifier of the voter
     * @param electionId The decision point ID
     * @param scores     Map of Candidate -> Score (double)
     * @return A new Ballot instance
     */
    public static Ballot ratedChoice(String voterId, String electionId, Map<String, Double> scores) {
        Map<String, Map<String, Real>> rateMap = new HashMap<>();
        Map<String, Real> realScores = new HashMap<>();
        if (scores != null) {
            scores.forEach((k, v) -> realScores.put(k, Real.of(v)));
        }
        rateMap.put(electionId, realScores);
        return new Ballot(voterId, null, rateMap);
    }
    
    public String voterId() { return voterId; }
    public Map<String, List<String>> selections() { return selections; }
    public Map<String, Map<String, Real>> ratings() { return ratings; }
    
    /**
     * Retrieves the top choice for a given election.
     * @param electionId the ID of the election
     * @return the top candidate name, or null if none
     */
    public String getTopChoice(String electionId) {
        if (selections != null && selections.containsKey(electionId)) {
            List<String> list = selections.get(electionId);
            return list.isEmpty() ? null : list.get(0);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Ballot[" + voterId + "]";
    }
}

