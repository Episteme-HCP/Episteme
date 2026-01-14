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

//http://en.wikipedia.org/wiki/Random_ballot
//this is the simpliest possible implementation
//warning futures implementations of this class may lead to different results
//this class is therefore currently more a sample implementation than anything else
//you should provide your own implementation
//this class is for multiple selections and multiple winners
//use BinarySingleChoiceRandomBallotsProcessor otherwise
public class BinaryRandomBallotsProcessor implements BallotsProcessor {
    /** DOCUMENT ME! */
    private boolean validated;

    /** DOCUMENT ME! */
    private Set validBallots;

    /** DOCUMENT ME! */
    private boolean computedResults;

    /** DOCUMENT ME! */
    private BinaryBallot results;

/**
     * Creates a new BinaryRandomBallotsProcessor object.
     */
    public BinaryRandomBallotsProcessor() {
        validated = false;
        validBallots = Collections.EMPTY_SET;
        computedResults = false;
        results = new BinaryBallot();
    }

    //you have to call this method first
    /**
     * DOCUMENT ME!
     *
     * @param voters DOCUMENT ME!
     * @param round DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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

    //we provide a simple implementation of the random system
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Ballot getResults() {
        Iterator iterator;
        long value;
        BinaryBallot resultBallot;
        Iterator iterator2;
        boolean found;
        String choice;
        String option;

        if (validated) {
            if (!computedResults) {
                if (validBallots.size() > 0) {
                    value = new Double(Math.floor(
                                Math.random() * validBallots.size())).longValue();
                    iterator = validBallots.iterator();

                    while (iterator.hasNext() && (value != 0)) {
                        iterator.next();
                        value--;
                    }

                    resultBallot = (BinaryBallot) iterator.next();
                    iterator = resultBallot.getChoices().iterator();

                    while (iterator.hasNext()) {
                        choice = (String) iterator.next();
                        iterator2 = resultBallot.getOptionsForChoice(choice)
                                                .iterator();
                        option = new String();

                        while (iterator2.hasNext()) {
                            option = (String) iterator2.next();

                            if (resultBallot.isOptionSelected(choice, option)) {
                                //results.addChoice(choice);
                                //results.addOptionToChoice(choice, option);
                                results.setSelectionForOption(choice, option,
                                    true);
                            }
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
        if (validated) {
            if (!computedResults) {
                getResults();
            }

            return results.getOptionsForChoice(choice);
        } else {
            throw new IllegalArgumentException(
                "You have to validate ballots first.");
        }
    }

    //this is random vote and is always decisive
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean shouldProceedToNextRound() {
        return false;
    }
}
