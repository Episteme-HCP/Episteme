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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Implementation of Plurality Voting (Single Choice) for binary ballots.
 * Each voter must select exactly one option per choice.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class BinarySingleChoiceApprovalBallotsProcessor implements BallotsProcessor {

    private boolean validated;
    private final Set<Ballot> validBallots;
    private boolean computedResults;
    private final RankedBallot results;
    private boolean tie;

    /**
     * Creates a new BinarySingleChoiceApprovalBallotsProcessor.
     */
    public BinarySingleChoiceApprovalBallotsProcessor() {
        this.validated = false;
        this.validBallots = new HashSet<>();
        this.computedResults = false;
        this.results = new RankedBallot();
        this.tie = false;
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
                        if (isBallotValid(ballot)) {
                            validBallots.add(ballot);
                        }
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

    /**
     * Validates that exactly one option is selected for every choice on the ballot.
     */
    private boolean isBallotValid(Ballot ballot) {
        if (ballot == null) return false;
        
        for (String choice : ballot.getChoices()) {
            int count = 0;
            for (String option : ballot.getOptionsForChoice(choice)) {
                if (ballot.isOptionSelected(choice, option)) {
                    count++;
                }
            }
            if (count != 1) return false;
        }
        return true;
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

            for (Ballot b : validBallots) {
                BinaryBallot binaryBallot = (BinaryBallot) b;
                for (String choice : binaryBallot.getChoices()) {
                    results.addChoice(choice);
                    for (String option : binaryBallot.getOptionsForChoice(choice)) {
                        results.addOptionToChoice(choice, option);
                        if (binaryBallot.isOptionSelected(choice, option)) {
                            int current = results.getOptionSelection(choice, option);
                            results.setOptionSelection(choice, option, Math.max(0, current) + 1);
                        }
                    }
                }
            }

            // Tie detection
            for (String choice : results.getChoices()) {
                int max = -1;
                int countMax = 0;
                for (String option : results.getOptionsForChoice(choice)) {
                    int val = results.getOptionSelection(choice, option);
                    if (val > max) {
                        max = val;
                        countMax = 1;
                    } else if (val == max && val != -1) {
                        countMax++;
                    }
                }
                if (countMax > 1) {
                    this.tie = true;
                }
            }

            computedResults = true;
        }
        return results;
    }

    /**
     * Returns the winning options for a specific choice.
     */
    public Set<String> getWinners(String choice) {
        if (!computedResults) getResults();
        
        Set<String> options = results.getOptionsForChoice(choice);
        if (options.isEmpty()) return Collections.emptySet();

        int max = options.stream()
                .mapToInt(opt -> results.getOptionSelection(choice, opt))
                .max()
                .orElse(-1);

        if (max == -1) return Collections.emptySet();

        return options.stream()
                .filter(opt -> results.getOptionSelection(choice, opt) == max)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean shouldProceedToNextRound() {
        return tie;
    }
}

