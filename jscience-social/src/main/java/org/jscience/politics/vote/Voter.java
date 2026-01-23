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
package org.jscience.politics.vote;

import org.jscience.biology.human.Human;
import org.jscience.sociology.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Represents a person participating in a voting process.
 * A voter manages their ballots across multiple rounds of voting.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class Voter extends Role {
    
    private final List<Boolean> hasVotedAtRoundI;
    private final List<Ballot> ballots;

    /**
     * Creates a new Voter.
     *
     * @param human      the individual casting votes
     * @param situation  the context of the vote
     */
    public Voter(Human human, VoteSituation situation) {
        super(human, "Voter", situation, Role.CLIENT);
        this.hasVotedAtRoundI = new ArrayList<>();
        this.ballots = new ArrayList<>();
    }

    /**
     * Retrieves the ballot cast at a specific round.
     *
     * @param i the round number (1-indexed)
     * @return the cast ballot
     * @throws IndexOutOfBoundsException if round is invalid
     */
    public Ballot getBallotForRoundI(int i) {
        return ballots.get(i - 1);
    }

    /**
     * Returns the list of all ballots cast by this voter.
     *
     * @return the list of ballots
     */
    public List<Ballot> getBallots() {
        return ballots;
    }

    /**
     * Returns the total number of ballots cast.
     *
     * @return number of ballots
     */
    public int getNumBallots() {
        return ballots.size();
    }

    /**
     * Retrieves the most recent ballot.
     *
     * @return the current ballot
     * @throws IndexOutOfBoundsException if no ballots exist
     */
    public Ballot getCurrentBallot() {
        return ballots.get(ballots.size() - 1);
    }

    /**
     * Prepares a ballot for the current round.
     *
     * @param ballot the ballot to use
     * @throws NullPointerException if ballot is null
     */
    public void setBallotForCurrentRound(Ballot ballot) {
        this.ballots.add(Objects.requireNonNull(ballot, "Ballot cannot be null."));
        this.hasVotedAtRoundI.add(false);
    }

    /**
     * Checks if the voter has cast their vote in a specific round.
     *
     * @param i the round number (1-indexed)
     * @return true if they voted
     */
    public boolean hasVotedAtRoundI(int i) {
        return hasVotedAtRoundI.get(i - 1);
    }

    /**
     * Returns the current round number according to this voter's activity.
     *
     * @return current round count
     */
    public int getCurrentRoundForVoter() {
        return ballots.size();
    }

    /**
     * Simulates the act of casting the vote for the current round.
     */
    protected void vote() {
        if (!hasVotedAtRoundI.isEmpty()) {
            hasVotedAtRoundI.set(hasVotedAtRoundI.size() - 1, true);
        }
    }

    /**
     * Abstract method to be implemented by specific voter types to make their choices on the ballot.
     */
    public abstract void select();
}

