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
import java.util.Random;


/**
 * A voter that selects exactly one option at random for every choice defined on the ballot.
 * Supports both {@link BinaryBallot} (selected=true) and {@link RankedBallot} (rank/score=1).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class SingleChoiceRandomVoter extends Voter {
    
    private static final Random RANDOM = new Random();

    /**
     * Creates a new SingleChoiceRandomVoter.
     *
     * @param human     the individual casting votes
     * @param situation the context of the vote
     */
    public SingleChoiceRandomVoter(Human human, VoteSituation situation) {
        super(human, situation);
    }

    @Override
    public void select() {
        Ballot ballot = getCurrentBallot();
        for (String choice : ballot.getChoices()) {
            Object[] options = ballot.getOptionsForChoice(choice).toArray();
            if (options.length == 0) continue;
            
            String selectedOption = (String) options[RANDOM.nextInt(options.length)];

            if (ballot instanceof BinaryBallot) {
                ((BinaryBallot) ballot).setSelectionForOption(choice, selectedOption, true);
            } else if (ballot instanceof RankedBallot) {
                ((RankedBallot) ballot).setOptionSelection(choice, selectedOption, 1);
            } else {
                throw new UnsupportedOperationException(
                    "Random voter does not support ballot type: " + ballot.getClass().getName());
            }
        }
    }
}

