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
 * Implementation of Approval Voting for binary ballots.
 * Voters can select any number of candidates, and the ones with the most votes win.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Approval_voting">Approval Voting</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class BinaryApprovalBallotsProcessor implements BallotsProcessor {

    private boolean validated;
    private final Set<Ballot> validBallots;
    private boolean computedResults;
    private final RankedBallot results;
    private boolean tie;

    /**
     * Creates a new BinaryApprovalBallotsProcessor.
     */
    public BinaryApprovalBallotsProcessor() {
        this.validated = false;
        this.validBallots = new HashSet<>();
        this.computedResults = false;
        this.results = new RankedBallot();
        this.tie = false;
    }

    @Override
    public Set<Ballot> validateBallots(Set<Voter> voters, int round) {
        if (validated) {
            throw new IllegalStateException("Validation can only be performed once per instance.");
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

    private boolean isBallotValid(Ballot ballot) {
        return ballot != null;
    }

    @Override
    public Ballot getResults() {
        if (!validated) {
            throw new IllegalStateException("Ballots must be validated before computing results.");
        }

        if (!computedResults) {
            if (validBallots.isEmpty()) {
                throw new IllegalStateException("No valid ballots to process.");
            }

            // Count votes for each option
            for (Ballot b : validBallots) {
                BinaryBallot binaryBallot = (BinaryBallot) b;
                for (String choice : binaryBallot.getChoices()) {
                    results.addChoice(choice);
                    for (String option : binaryBallot.getOptionsForChoice(choice)) {
                        results.addOptionToChoice(choice, option);
                        if (binaryBallot.isOptionSelected(choice, option)) {
                            int currentCount = results.getOptionSelection(choice, option);
                            results.setOptionSelection(choice, option, Math.max(0, currentCount) + 1);
                        }
                    }
                }
            }

            // Check for ties in each choice
            for (String choice : results.getChoices()) {
                int maxVotes = -1;
                Set<String> topOptions = new HashSet<>();

                for (String option : results.getOptionsForChoice(choice)) {
                    int votes = results.getOptionSelection(choice, option);
                    if (votes > maxVotes) {
                        maxVotes = votes;
                        topOptions.clear();
                        topOptions.add(option);
                    } else if (votes == maxVotes && votes != -1) {
                        topOptions.add(option);
                    }
                }

                if (topOptions.size() > 1) {
                    this.tie = true;
                }
            }

            computedResults = true;
        }

        return results;
    }

    /**
     * Returns the winning options for a specific choice.
     *
     * @param choice the choice title
     * @return the set of winning option names
     */
    public Set<String> getWinners(String choice) {
        if (!computedResults) {
            getResults();
        }

        Set<String> options = results.getOptionsForChoice(choice);
        if (options.isEmpty()) return Collections.emptySet();

        int maxVotes = options.stream()
                .mapToInt(opt -> results.getOptionSelection(choice, opt))
                .max()
                .orElse(-1);

        if (maxVotes == -1) return Collections.emptySet();

        return options.stream()
                .filter(opt -> results.getOptionSelection(choice, opt) == maxVotes)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean shouldProceedToNextRound() {
        return tie;
    }
}

