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

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * This class represents a way by which you compute results.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://en.wikipedia.org/wiki/Approval_voting
//this is the simpliest possible implementation
//warning futures implementations of this class may lead to different results
//this class is therefore currently more a sample implementation than anything else
//you should provide your own implementation
//Multiple winners
//Approval voting can be extended to multiple winner elections, either as block approval voting,
//a simple variant on block voting where each voter can select an unlimited number of candidates
//and the candidates with the most approval votes win, or as proportional approval voting which
//seeks to maximise the overall satisfaction with the final result using approval voting. That
//first system has been called minisum to distinguish it from minimax, a system which uses approval
//ballots and aims to elect the slate of candidates that differs from the least-satisfied voter's
//ballot as little as possible.
public class BinaryApprovalBallotsProcessor implements BallotsProcessor {
    /** DOCUMENT ME! */
    private boolean validated;

    /** DOCUMENT ME! */
    private Set validBallots;

    /** DOCUMENT ME! */
    private boolean computedResults;

    /** DOCUMENT ME! */
    private RankedBallot results;

    /** DOCUMENT ME! */
    private boolean tie;

/**
     * Creates a new BinaryApprovalBallotsProcessor object.
     */
    public BinaryApprovalBallotsProcessor() {
        validated = false;
        validBallots = Collections.EMPTY_SET;
        computedResults = false;
        results = new RankedBallot();
        tie = false;
    }

    //you have to call this method first
    /**
     * DOCUMENT ME!
     *
     * @param voters DOCUMENT ME!
     * @param round DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Set validateBallots(Set voters, int round) {
        Iterator iterator;
        Object value;
        Voter voter;

        if (!validated) {
            iterator = voters.iterator();

            while (iterator.hasNext()) {
                value = iterator.next();

                if (value instanceof Voter) {
                    voter = (Voter) value;

                    if ((round >= 1) && (round <= voter.getNumBallots())) {
                        if (voter.hasVotedAtRoundI(round)) {
                            if (voter.getBallotForRoundI(round) instanceof BinaryBallot) {
                                if (isBallotValid(voter.getBallotForRoundI(
                                                round))) {
                                    validBallots.add(voter.getBallotForRoundI(
                                            round));
                                }
                            } else {
                                throw new IllegalArgumentException(
                                    "This voting system only accepts BinaryBallot.");
                            }
                        }
                    } else {
                        throw new IllegalArgumentException(
                            "One of the voter has no ballot for selected round or the request round is less than 1.");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "The voters Set should contain only voters.");
                }
            }

            validated = true;
        } else {
            throw new IllegalArgumentException("You can only validate once.");
        }

        return validBallots;
    }

    //we do minimal check here but more complex implementation could eliminate more ballots
    /**
     * DOCUMENT ME!
     *
     * @param ballot DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isBallotValid(Ballot ballot) {
        return true;
    }

    //we provide a simple implementation of the approval system
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Ballot getResults() {
        BinaryBallot ballot;
        Iterator iterator;
        Iterator iterator2;
        Iterator iterator3;
        String choice;
        String option;
        int max;
        String maxOption;

        if (validated) {
            if (!computedResults) {
                if (validBallots.size() > 0) {
                    iterator = validBallots.iterator();

                    while (iterator.hasNext()) {
                        ballot = (BinaryBallot) iterator.next();
                        iterator2 = ballot.getChoices().iterator();

                        while (iterator2.hasNext()) {
                            choice = (String) iterator2.next();
                            iterator3 = ballot.getOptionsForChoice(choice)
                                              .iterator();
                            option = new String();

                            while (iterator3.hasNext()) {
                                option = (String) iterator3.next();

                                if (ballot.isOptionSelected(choice, option)) {
                                    //todo: we could speed up computation by using an intermediate array instead of directly using results
                                    //results.addChoice(choice);
                                    //results.addOptionToChoice(choice, option);
                                    results.setOptionSelection(choice, option,
                                        results.getOptionSelection(choice,
                                            option) + 1);
                                }
                            }
                        }
                    }

                    //tie computation
                    iterator = results.getChoices().iterator();

                    while (iterator.hasNext() && (!tie)) {
                        choice = (String) iterator.next();
                        iterator2 = results.getOptionsForChoice(choice)
                                           .iterator();
                        max = 0;
                        maxOption = null;

                        while (iterator2.hasNext()) {
                            option = (String) iterator2.next();

                            if (results.getOptionSelection(choice, option) > max) {
                                max = results.getOptionSelection(choice, option);
                                maxOption = option;
                            }
                        }

                        iterator2 = results.getOptionsForChoice(choice)
                                           .iterator();

                        while (iterator2.hasNext() && (!tie)) {
                            option = (String) iterator2.next();
                            tie = (results.getOptionSelection(choice, option) == max) &&
                                (option != maxOption);
                        }
                    }

                    computedResults = true;
                } else {
                    throw new IllegalArgumentException(
                        "You need at least one valid ballot to compute the results.");
                }
            }
        } else {
            throw new IllegalArgumentException(
                "You have to validate ballots first.");
        }

        return results;
    }

    /**
     * DOCUMENT ME!
     *
     * @param choice DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Set getResults(String choice) {
        Iterator iterator;
        String option;
        int max;
        Set value;

        if (validated) {
            if (!computedResults) {
                getResults();
            }

            if (results.getOptionsForChoice(choice).size() > 0) {
                iterator = results.getOptionsForChoice(choice).iterator();
                max = 0;
                value = Collections.EMPTY_SET;

                while (iterator.hasNext()) {
                    option = (String) iterator.next();

                    if (results.getOptionSelection(choice, option) > max) {
                        max = results.getOptionSelection(choice, option);
                    }
                }

                iterator = results.getOptionsForChoice(choice).iterator();

                while (iterator.hasNext() && (!tie)) {
                    option = (String) iterator.next();

                    if (results.getOptionSelection(choice, option) == max) {
                        value.add(option);
                    }
                }

                return value;
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException(
                "You have to validate ballots first.");
        }
    }

    //this should happen only if there is a tie (two or more options of a choice have an equal number of votes)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean shouldProceedToNextRound() {
        return tie;
    }
}
