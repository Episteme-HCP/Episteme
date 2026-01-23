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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * Implementation of the Random Ballot algorithm.
 * One valid ballot is selected at random, and its selections determine the outcome.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Random_ballot">Random Ballot</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class BinaryRandomBallotsProcessor implements BallotsProcessor {
    
    private static final Random RANDOM = new Random();
    
    private boolean validated;
    private final Set<Ballot> validBallots;
    private boolean computedResults;
    private BinaryBallot results;

    /**
     * Creates a new BinaryRandomBallotsProcessor.
     */
    public BinaryRandomBallotsProcessor() {
        this.validated = false;
        this.validBallots = new HashSet<>();
        this.computedResults = false;
        this.results = new BinaryBallot();
    }

    @Override
    public Set<Ballot> validateBallots(Set<Voter> voters, int round) {
        if (validated) {
            throw new IllegalStateException("Validation can only be performed once.");
        }

        for (Voter voter : voters) {
            if (round >= 1 && round <= voter.getNumBallots()) {
                if (voter.hasVotedAtRoundI(round)) {
                    Ballot ballot = voter.getBallotForRoundI(round);
                    if (ballot instanceof BinaryBallot) {
                        validBallots.add(ballot);
                    } else {
                        throw new IllegalArgumentException("This processor only accepts BinaryBallot.");
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid round " + round + " for voter " + voter.getName());
            }
        }

        validated = true;
        return validBallots;
    }

    @Override
    public Ballot getResults() {
        if (!validated) {
            throw new IllegalStateException("Ballots must be validated first.");
        }

        if (!computedResults) {
            if (validBallots.isEmpty()) {
                throw new IllegalStateException("No valid ballots found.");
            }

            List<Ballot> list = new ArrayList<>(validBallots);
            BinaryBallot winner = (BinaryBallot) list.get(RANDOM.nextInt(list.size()));
            this.results = winner.clone();
            computedResults = true;
        }

        return results;
    }

    /**
     * Returns the selected winning options for a specific choice.
     */
    public Set<String> getWinners(String choice) {
        if (!computedResults) getResults();
        
        Set<String> winners = new HashSet<>();
        for (String option : results.getOptionsForChoice(choice)) {
            if (results.isOptionSelected(choice, option)) {
                winners.add(option);
            }
        }
        return winners;
    }

    @Override
    public boolean shouldProceedToNextRound() {
        return false;
    }
}

