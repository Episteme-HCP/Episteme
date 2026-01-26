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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * A voter that selects multiple random options for every choice on the ballot.
 * For {@link BinaryBallot}, it selects a random number of options (at least one).
 * For {@link RankedBallot}, it ranks all available options in a random order.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class MultipleChoicesRandomVoter extends Voter {
    
    private static final Random RANDOM = new Random();

    /**
     * Creates a new MultipleChoicesRandomVoter.
     *
     * @param human     the individual casting votes
     * @param situation the context of the vote
     */
    public MultipleChoicesRandomVoter(Human human, VoteSituation situation) {
        super(human, situation);
    }

    @Override
    public void select() {
        Ballot ballot = getCurrentBallot();
        for (String choice : ballot.getChoices()) {
            List<String> options = new ArrayList<>(ballot.getOptionsForChoice(choice));
            if (options.isEmpty()) continue;

            Collections.shuffle(options, RANDOM);

            if (ballot instanceof BinaryBallot) {
                BinaryBallot binaryBallot = (BinaryBallot) ballot;
                // Select a random number of options (between 1 and options.size())
                int countToSelect = RANDOM.nextInt(options.size()) + 1;
                for (int i = 0; i < countToSelect; i++) {
                    binaryBallot.setSelectionForOption(choice, options.get(i), true);
                }
            } else if (ballot instanceof RankedBallot) {
                RankedBallot rankedBallot = (RankedBallot) ballot;
                // Rank all options from 1 to options.size()
                for (int i = 0; i < options.size(); i++) {
                    rankedBallot.setOptionSelection(choice, options.get(i), i + 1);
                }
            } else {
                throw new UnsupportedOperationException(
                    "Random voter does not support ballot type: " + ballot.getClass().getName());
            }
        }
    }
}

