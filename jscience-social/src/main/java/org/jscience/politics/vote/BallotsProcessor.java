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

import java.util.Set;


/**
 * Interface for algorithms that process ballots and determine election results.
 * Implementations should follow a sequence of validating ballots before retrieving results.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public interface BallotsProcessor {
    
    /**
     * Validates a set of cast ballots for a specific round.
     * This method must be called before {@link #getResults()}.
     *
     * @param voters the set of voters who participated
     * @param round  the current election round
     * @return the set of ballots that were deemed valid
     */
    Set<Ballot> validateBallots(Set<Voter> voters, int round);

    /**
     * Computes and returns the aggregated results of the validated ballots.
     *
     * @return a pseudo-ballot containing the winning choices and options
     */
    Ballot getResults();

    /**
     * Determines if the election process should continue to another round.
     * Useful for runoff systems or tiered elections.
     *
     * @return true if another round is required
     */
    boolean shouldProceedToNextRound();
}

