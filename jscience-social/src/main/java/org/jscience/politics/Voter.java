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

import org.jscience.biology.Individual;
import org.jscience.sociology.Role;
import org.jscience.sociology.RoleKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Represents a person participating in a voting process.
 * A voter manages their ballots across multiple rounds of voting.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Voter extends Role {
    
    private static final long serialVersionUID = 1L;
    
    private final List<Boolean> hasVotedAtRoundI = new ArrayList<>();
    private final List<Ballot> ballots = new ArrayList<>();

    /**
     * Creates a new Voter role.
     *
     * @param individual the individual casting votes
     * @param election   the context of the vote
     */
    public Voter(Individual individual, Election election) {
        super(individual, "Voter", election, RoleKind.CLIENT);
    }

    /**
     * Retrieves the ballot cast at a specific round.
     *
     * @param i the round number (1-indexed)
     * @return the cast ballot
     */
    public Ballot getBallotForRoundI(int i) {
        return ballots.get(i - 1);
    }

    /**
     * Returns the list of all ballots cast by this voter.
     * @return the list of ballots
     */
    public List<Ballot> getBallots() {
        return ballots;
    }

    /**
     * Retrieves the most recent ballot.
     * @return the current ballot
     */
    public Ballot getCurrentBallot() {
        if (ballots.isEmpty()) return null;
        return ballots.get(ballots.size() - 1);
    }

    /**
     * Casts a ballot for the current round.
     *
     * @param ballot the ballot to cast
     */
    public void castBallot(Ballot ballot) {
        this.ballots.add(Objects.requireNonNull(ballot, "Ballot cannot be null."));
        this.hasVotedAtRoundI.add(true);
    }

    /**
     * Checks if the voter has cast their vote in a specific round.
     *
     * @param i the round number (1-indexed)
     * @return true if they voted
     */
    public boolean hasVotedAtRoundI(int i) {
        if (i <= 0 || i > hasVotedAtRoundI.size()) return false;
        return hasVotedAtRoundI.get(i - 1);
    }

    /**
     * Returns the current round number according to this voter's activity.
     * @return current round count
     */
    public int getCurrentRoundForVoter() {
        return ballots.size();
    }
}
