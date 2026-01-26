
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

import org.jscience.sociology.Human;
import org.jscience.sociology.Situation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Represents a collective decision-making process where individuals cast votes.
 * This situation manages multiple rounds, ballot definitions, and voter participation.
 * It is primarily designed for simulation and testing of various voting systems.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class VoteSituation extends Situation {

    private final List<Ballot> ballots;
    private boolean roundClosed;

    /**
     * Creates a new VoteSituation.
     *
     * @param name     the name of the election/vote
     * @param comments descriptive details
     */
    public VoteSituation(String name, String comments) {
        super(name, comments);
        this.ballots = new ArrayList<>();
        this.roundClosed = true; // Initially closed until a ballot is set
    }

    /**
     * Adds a voter who chooses a single option randomly.
     * @param human the individual
     */
    public void addSingleChoiceRandomVoter(Human human) {
        super.addRole(new SingleChoiceRandomVoter(human, this));
    }

    /**
     * Adds a voter who chooses multiple options randomly.
     * @param human the individual
     */
    public void addMultipleChoicesRandomVoter(Human human) {
        super.addRole(new MultipleChoicesRandomVoter(human, this));
    }

    /**
     * Sets the template ballot for a new round and distributes copies to all voters.
     *
     * @param ballot the master ballot for the round
     * @throws IllegalArgumentException if the previous round is not yet closed
     */
    public void setBallotForRoundI(Ballot ballot) {
        Objects.requireNonNull(ballot, "Ballot cannot be null.");
        
        if (!isRoundClosed()) {
            throw new IllegalArgumentException("Cannot start a new round until the current one is closed.");
        }

        ballots.add(ballot);
        Set<Voter> voters = getVoters();
        for (Voter voter : voters) {
            voter.setBallotForCurrentRound(ballot.clone());
        }
        roundClosed = false;
    }

    /**
     * Returns the current round number (1-indexed).
     * @return current round
     */
    public int getCurrentRoundNumber() {
        return ballots.size();
    }

    /**
     * Retrieves the master ballot for a specific round.
     * @param i the round number (1-indexed)
     * @return the ballot
     */
    public Ballot getBallotForRoundI(int i) {
        return ballots.get(i - 1);
    }

    /**
     * Returns all master ballots used in the election.
     * @return list of ballots
     */
    public List<Ballot> getBallots() {
        return Collections.unmodifiableList(ballots);
    }

    /**
     * Returns the template ballot for the current round.
     * @return current master ballot
     */
    public Ballot getCurrentBallot() {
        if (ballots.isEmpty()) return null;
        return ballots.get(ballots.size() - 1);
    }

    /**
     * Returns the set of all voters currently registered in this situation.
     * @return set of voters
     */
    public Set<Voter> getVoters() {
        return getRoles().stream()
                .filter(Voter.class::isInstance)
                .map(Voter.class::cast)
                .collect(Collectors.toSet());
    }

    /**
     * Triggers the voting action for a specific voter.
     * @param voter the voter casting a ballot
     */
    public void vote(Voter voter) {
        Objects.requireNonNull(voter, "Voter cannot be null.");
        if (!getRoles().contains(voter)) {
            throw new IllegalArgumentException("Individual is not a registered voter in this situation.");
        }
        voter.select();
        voter.vote();
    }

    /**
     * Checks if a specific voter has already cast their ballot for the current round.
     * @param voter the voter to check
     * @return true if voted
     */
    public boolean getVotedStatus(Voter voter) {
        Objects.requireNonNull(voter, "Voter cannot be null.");
        return voter.hasVotedAtRoundI(getCurrentRoundNumber());
    }

    /**
     * Checks if the current round is closed.
     * A round is considered closed if explicitly called or if all voters have finished.
     * @return true if closed
     */
    public boolean isRoundClosed() {
        if (roundClosed) return true;
        
        Set<Voter> voters = getVoters();
        if (voters.isEmpty()) return true;

        return voters.stream().allMatch(v -> v.hasVotedAtRoundI(getCurrentRoundNumber()));
    }

    /**
     * Manually closes the current voting round.
     */
    public void closeRound() {
        this.roundClosed = true;
    }

    /**
     * Processes the results of a specific round using the provided algorithm.
     *
     * @param processor the algorithm to use for counting
     * @param round     the round to process
     * @return true if a next round is required according to the processor
     */
    public boolean processResult(BallotsProcessor processor, int round) {
        Objects.requireNonNull(processor, "Processor cannot be null.");
        processor.validateBallots(getVoters(), round);
        return processor.shouldProceedToNextRound();
    }
}

