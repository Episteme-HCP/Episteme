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
 * Implementation of the Random Ballot social choice function for single-choice 
 * binary ballots. This processor selects one valid ballot at random and uses 
 * its contents as the winner of the election.
 * 
 * <p>While highly non-deterministic, the Random Ballot is notable for being 
 * "strategy-proof" (incentivizing honest voting) and always decisive.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 * @see <a href="http://en.wikipedia.org/wiki/Random_ballot">Random Ballot (Wikipedia)</a>
 */
public class BinarySingleChoiceRandomBallotsProcessor implements BallotsProcessor {

    private boolean validated = false;
    private final List<BinaryBallot> validBallots = new ArrayList<>();
    private boolean computedResults = false;
    private final BinaryBallot results = new BinaryBallot();
    private final Random random = new Random();

    /**
     * Initializes a new processor.
     */
    public BinarySingleChoiceRandomBallotsProcessor() {
    }

    /**
     * Filters the set of voters to extract and validate single-choice binary ballots 
     * for a specific election round.
     * 
     * @param voters set of participating voters
     * @param round the election round index (1-based)
     * @return the set of successfully validated ballots
     * @throws IllegalArgumentException if validation is attempted twice or if ballots 
     *      are incompatible with this processor
     */
    @Override
    public Set<Ballot> validateBallots(Set<Voter> voters, int round) {
        if (validated) {
            throw new IllegalArgumentException("Ballot validation can only be performed once.");
        }

        for (Voter voter : voters) {
            if (round >= 1 && round <= voter.getNumBallots()) {
                if (voter.hasVotedAtRoundI(round)) {
                    Ballot ballot = voter.getBallotForRoundI(round);
                    if (ballot instanceof BinaryBallot binaryBallot) {
                        if (isBallotValid(binaryBallot)) {
                            validBallots.add(binaryBallot);
                        }
                    } else {
                        throw new IllegalArgumentException("This processor only accepts BinaryBallot instances.");
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid round requested for a voter.");
            }
        }

        validated = true;
        return new HashSet<>(validBallots);
    }

    /**
     * Validates that a ballot contains exactly one selection per choice.
     */
    private boolean isBallotValid(BinaryBallot ballot) {
        for (String choice : (Set<String>) ballot.getChoices()) {
            int selectionCount = 0;
            for (String option : (Set<String>) ballot.getOptionsForChoice(choice)) {
                if (ballot.isOptionSelected(choice, option)) {
                    selectionCount++;
                }
            }
            if (selectionCount != 1) return false;
        }
        return true;
    }

    /**
     * Selects one valid ballot at random and designates its choices as 
     * the winner of the election.
     * 
     * @return a BinaryBallot representing the consolidated results
     * @throws IllegalStateException if results are requested before validation 
     *      or if no valid ballots were found
     */
    @Override
    public Ballot getResults() {
        if (!validated) {
            throw new IllegalStateException("You must validate ballots before computing results.");
        }
        if (computedResults) {
            return results;
        }
        
        if (validBallots.isEmpty()) {
            throw new IllegalStateException("No valid ballots available to compute a random selection.");
        }

        // The core logic of Random Ballot: pick one winner at random.
        BinaryBallot winningBallot = validBallots.get(random.nextInt(validBallots.size()));

        for (String choice : (Set<String>) winningBallot.getChoices()) {
            for (String option : (Set<String>) winningBallot.getOptionsForChoice(choice)) {
                if (winningBallot.isOptionSelected(choice, option)) {
                    results.setSelectionForOption(choice, option, true);
                    break; 
                }
            }
        }

        computedResults = true;
        return results;
    }

    /**
     * Retrieves the specific winner for a given choice category.
     * 
     * @param choice the category/question being voted on
     * @return the name of the winning option
     */
    public String getResults(String choice) {
        if (!computedResults) {
            getResults();
        }
        Set<String> winners = results.getOptionsForChoice(choice);
        return winners.isEmpty() ? null : winners.iterator().next();
    }

    /**
     * A Random Ballot system is always decisive in its selection and does 
     * not require further rounds.
     * 
     * @return false
     */
    @Override
    public boolean shouldProceedToNextRound() {
        return false;
    }
}
