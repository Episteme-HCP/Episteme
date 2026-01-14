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

import java.util.Iterator;


/**
 * This class represent a person in a voting situation. The voter is
 * selecting exactly ONE random option from EVERY choice in the ballot for
 * BinaryBallots and ranks ONE at random for EVERY choice in the ballot for
 * RankedBallots.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class SingleChoiceRandomVoter extends Voter {
/**
     * Creates a new SingleChoiceRandomVoter object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public SingleChoiceRandomVoter(Individual individual,
        VoteSituation situation) {
        super(individual, situation);
    }

    //using the getBallotForRoundI(currentRoundForVoter())
    /**
     * DOCUMENT ME!
     */
    public void select() {
        Iterator iterator;
        Object[] values;
        String choice;
        String option;

        iterator = getCurrentBallot().getChoices().iterator();

        while (iterator.hasNext()) {
            choice = (String) iterator.next();
            values = getCurrentBallot().getOptionsForChoice(choice).toArray();
            option = (String) values[new Double(Math.floor(
                        values.length * Math.random())).intValue()];

            if (getCurrentBallot() instanceof BinaryBallot) {
                ((BinaryBallot) getCurrentBallot()).setSelectionForOption(choice,
                    option, true);
            } else if (getCurrentBallot() instanceof RankedBallot) {
                ((RankedBallot) getCurrentBallot()).setOptionSelection(choice,
                    option, 1);
            } else {
                throw new IllegalArgumentException(
                    "The random voter only knows how to vote with BinaryBallots and RankedBallots.");
            }
        }
    }
}
