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

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;

import java.util.Vector;


/**
 * This class represent a person in a voting situation.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Voter extends Role {
    /** DOCUMENT ME! */
    private Vector hasVotedAtRoundI;

    /** DOCUMENT ME! */
    private Vector ballots;

/**
     * Creates a new Voter object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public Voter(Individual individual, VoteSituation situation) {
        super(individual, "Voter", situation, Role.CLIENT);
        hasVotedAtRoundI = new Vector();
        ballots = new Vector();
    }

    //i must be between 1 and currentRoundForVoter()
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Ballot getBallotForRoundI(int i) {
        return (Ballot) ballots.get(i - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getBallots() {
        return ballots;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumBallots() {
        return ballots.size();
    }

    //you must call setBallotForCurrentRound() before this method
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Ballot getCurrentBallot() {
        return (Ballot) ballots.get(ballots.size() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ballot DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setBallotForCurrentRound(Ballot ballot) {
        if (ballot != null) {
            ballots.add(ballot);
            hasVotedAtRoundI.add(new Boolean(false));
        } else {
            throw new IllegalArgumentException("You can't add a null Ballot.");
        }
    }

    //i must be between 1 and currentRoundForVoter()
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasVotedAtRoundI(int i) {
        return ((Boolean) hasVotedAtRoundI.get(i - 1)).booleanValue();
    }

    //returns 0 if no ballot has been put in the system
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCurrentRoundForVoter() {
        return ballots.size();
    }

    //actually increase round by one and fill a ballot
    /**
     * DOCUMENT ME!
     */
    protected void vote() {
        hasVotedAtRoundI.set(hasVotedAtRoundI.size() - 1, new Boolean(true));
    }

    //using the getBallotForRoundI(currentRoundForVoter())
    /**
     * DOCUMENT ME!
     */
    public abstract void select();
}
