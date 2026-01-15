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
