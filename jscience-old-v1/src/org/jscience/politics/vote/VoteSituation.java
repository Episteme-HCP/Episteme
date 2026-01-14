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

import org.jscience.sociology.Situation;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * This class represent a person in a voting situation. This class
 * represents all the information and process to collectively choose something
 * by people (ie: vote).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//see http://en.wikipedia.org/wiki/Voting_system
//and http://en.wikipedia.org/wiki/Effects_of_different_voting_systems_under_similar_circumstances
//inspired by http://www.cs.unc.edu/~wluebke/comp204/liberty/
//this system is not a distributed real time system to be used in production environment
//it is a simulation system (that offers the same functionality although not multi process or backed up by a database)
//which aim is to help design or test voting systems and outcomes
public abstract class VoteSituation extends Situation {
    //results computation system
    //mutually exclusive
    //single
    //public final static int BINARY_OTHER = 1000;
    //public final static int BINARY_APPROVAL = 1001;
    //public final static int BINARY_RUNOFF = 1002;
    //public final static int BINARY_TOP_TWO = 1003;
    //public final static int BINARY_RANDOM = 1004;
    //public final static int RANKED_OTHER = 2000;
    //public final static int RANKED_INSTANT_RUNOFF = 2001;
    //public final static int RANKED_BORDA_COUNT = 2002;
    //public final static int RANKED_COOMBS = 2003;
    //public final static int RANKED_SUPPLEMENTARY = 2004;
    //public final static int RANKED_BUCKLIN = 2005;
    //public final static int CONDORCET_OTHER = 3000;
    //public final static int CONDORCET_MINIMAX = 3001;
    //public final static int CONDORCET_COPLEAND = 3002;
    //public final static int CONDORCET_SCHULZE = 3003;
    //public final static int CONDORCET_RANKED_PAIRS = 3004;
    //public final static int RATED_OTHER = 4000;
    //public final static int RATED_RANGE = 4001;
    //public final static int RATED_CUMULATIVE = 4002;
    //multiple
    //public final static int PROPORTIONAL_OTHER = 10000;
    //public final static int PROPORTIONAL_SINGLE_TRANSFERABLE = 10001;
    //public final static int PROPORTIONAL_SAINTE_LAGUE = 10002;//PROPORTIONAL_HIGHEST_AVERAGE
    //public final static int PROPORTIONAL_HONDT = 10003;//PROPORTIONAL_HIGHEST_AVERAGE
    //public final static int PROPORTIONAL_LARGEST_REMAINDER = 10004;
    //public final static int PARTLY_PROPORTIONAL_OTHER = 11000;
    //public final static int PARTLY_PROPORTIONAL_CUMULATIVE = 11001;
    //public final static int PARTLY_PROPORTIONAL_SINGLE_NON_TRANSFERABLE = 11002;
    //public final static int NON_PROPORTIONAL_OTHER = 12000;
    //public final static int NON_PROPORTIONAL_BLOC = 12001;
    /** DOCUMENT ME! */
    private Vector ballots;

    /** DOCUMENT ME! */
    private boolean closedRound;

/**
     * Creates a new VoteSituation object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public VoteSituation(String name, String comments) {
        super(name, comments);
        ballots = new Vector();
        closedRound = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addSingleChoiceRandomVoter(Individual individual) {
        super.addRole(new SingleChoiceRandomVoter(individual, this));
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addMultipleChoicesRandomVoter(Individual individual) {
        super.addRole(new MultipleChoicesRandomVoter(individual, this));
    }

    //actually starts a new round (and gives a new Ballot to fill to all voters)
    /**
     * DOCUMENT ME!
     *
     * @param ballot DOCUMENT ME!
     */
    public void setBallotForRoundI(Ballot ballot) {
        Iterator iterator;

        if (ballot != null) {
            if (isRoundClosed()) {
                ballots.add(ballot);
                iterator = getVoters().iterator();

                while (iterator.hasNext()) {
                    ((Voter) iterator.next()).setBallotForCurrentRound((Ballot) ballot.clone());
                }

                closedRound = false;
            } else {
                throw new IllegalArgumentException(
                    "You can't add a new Ballot until the round is closed. Call closeVote() first or let people all vote.");
            }
        } else {
            throw new IllegalArgumentException("You can't add a null Ballot.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCurrentRoundNumber() {
        return ballots.size();
    }

    //you must call setBallotForCurrentRound() before this method
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
     * @return DOCUMENT ME!
     */
    public Set getVoters() {
        Iterator iterator;
        Object value;
        Set result;

        iterator = getRoles().iterator();
        result = Collections.EMPTY_SET;

        while (iterator.hasNext()) {
            value = iterator.next();

            if (value instanceof Voter) {
                result.add(value);
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param voter DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void vote(Voter voter) {
        if (voter != null) {
            if (getRoles().contains(voter)) {
                voter.select();
                voter.vote();
            } else {
                throw new IllegalArgumentException("Only voters can vote.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't make vote a null voter.");
        }
    }

    //Returns whether a voter has voted or not for this round.
    /**
     * DOCUMENT ME!
     *
     * @param voter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getVotedStatus(Voter voter) {
        if (voter != null) {
            return voter.hasVotedAtRoundI(getCurrentRoundNumber());
        } else {
            throw new IllegalArgumentException(
                "You can't get the status of a null voter.");
        }
    }

    //returns whether this round is over or not
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRoundClosed() {
        Iterator iterator;
        boolean found;

        if (closedRound) {
            return true;
        } else {
            iterator = getVoters().iterator();
            found = false;

            while (iterator.hasNext() && !found) {
                found = ((Voter) iterator.next()).hasVotedAtRoundI(getCurrentRoundNumber());
            }

            return !found;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void closeRound() {
        closedRound = true;
    }

    //this is a rather useless convenience method
    /**
     * DOCUMENT ME!
     *
     * @param method DOCUMENT ME!
     * @param round DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean processResult(BallotsProcessor method, int round) {
        Set valids;

        //Ballot resultBallot;
        boolean otherRound;

        if (method != null) {
            valids = method.validateBallots(getVoters(), round);

            //resultBallot = method.getResults();
            return otherRound = method.shouldProceedToNextRound();
        } else {
            throw new IllegalArgumentException(
                "You can't get a result with a null method.");
        }
    }

    //unused from liberty:
    //Adds a choice to the choice database.
    //public void addChoice(String title, String prompt, List<String> options) {
    //}
    //_Factory Method_ for creating a ChoicesConnection object.
    //public static ChoicesConnection create(String databaseURL) {
    //}
    //Returns the options for a choice from the database.
    //public List<String> getChoiceOptions(String title) {
    //}
    //Returns the prompt for a choice from the database.
    //public String getChoicePrompt(String title) {
    //}
    //Returns a list of titles from the database.
    //public List<String> getChoiceTitles() {
    //}
    //Initializes the internal connection object with the specified database URL.
    //protected void initialize(String databaseURL) {
    //}
    //Removes a choice from the database, or throws an exception if the choice does not exist in the database.
    //public void removeChoice(String title) {
    //}
    //Returns the options for a choice from the database.
    //public List<String> getChoiceOptions(String title) {
    //}
    //Returns the number of votes for the specified option of the choice with the specified title.
    //public long getChoiceOptionTally(String title, String option) {
    //}
    //Returns the prompt for a choice from the database.
    //public String getChoicePrompt(String title) {
    //}
    //Returns a list of titles from the database.
    //public List<String> getChoiceTitles() {
    //}
    //Returns whether a voter has voted or not.
    //public boolean getVotedStatus(String firstName, String middleName, String lastName, String ssn) {
    //}
    //Increments the number of votes for the specified option of the choice with the specified title.
    //public void incrementChoiceOption(String title, String option) {
    //}
    //Initializes the internal connection objects.
    //protected void initialize() {
    //}
    //Returns whether or not the voter with the specified name and social security number is registered in the database.
    //public boolean isVoterRegistered(String firstName, String middleName, String lastName, String ssn) {
    //}
    //Called to record whether a voter has voted with the system.
    //public void setVotedStatus(String firstName, String middleName, String lastName, String ssn, boolean voted) {
    //}
}
