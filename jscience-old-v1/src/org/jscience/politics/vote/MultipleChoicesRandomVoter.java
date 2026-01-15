package org.jscience.politics.vote;

import org.jscience.biology.Individual;

import java.util.Iterator;
import java.util.Vector;


/**
 * This class represent a person in a voting situation. The voter is
 * selecting a random number of options from EVERY choice in the ballot for
 * BinaryBallots and ranks ALL the options for EVERY choice in the ballot for
 * RankedBallots.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class MultipleChoicesRandomVoter extends Voter {
/**
     * Creates a new MultipleChoicesRandomVoter object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public MultipleChoicesRandomVoter(Individual individual,
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
        int[] optionsIndices;
        int checkedOptions;

        iterator = getCurrentBallot().getChoices().iterator();

        while (iterator.hasNext()) {
            choice = (String) iterator.next();
            values = getCurrentBallot().getOptionsForChoice(choice).toArray();
            optionsIndices = shuffleAscendingIntArray(values.length);

            if (getCurrentBallot() instanceof BinaryBallot) {
                checkedOptions = 1 +
                    new Double(Math.floor(Math.random() * values.length)).intValue();

                for (int i = 0; i < checkedOptions; i++) {
                    option = (String) values[optionsIndices[i]];
                    ((BinaryBallot) getCurrentBallot()).setSelectionForOption(choice,
                        option, true);
                }
            } else if (getCurrentBallot() instanceof RankedBallot) {
                for (int i = 0; i < values.length; i++) {
                    option = (String) values[optionsIndices[i]];
                    ((RankedBallot) getCurrentBallot()).setOptionSelection(choice,
                        option, i + 1);
                }
            } else {
                throw new IllegalArgumentException(
                    "The random voter only knows how to vote with BinaryBallots and RankedBallots.");
            }
        }
    }

    //method 1, unused
    /**
     * DOCUMENT ME!
     *
     * @param length DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int[] createAscendingIntArray(int length) {
        int[] result;
        result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = i;
        }

        return result;
    }

    //method 1, unused
    /**
     * DOCUMENT ME!
     *
     * @param array DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int[] shuffleArray(int[] array) {
        int[] result;
        int[] tempArray;
        result = new int[array.length];
        result[0] = new Double(Math.floor(Math.random() * array.length)).intValue();
        tempArray = new int[array.length - 1];
        System.arraycopy(array, 1, tempArray, 0, array.length - 1);
        tempArray = shuffleArray(tempArray);
        System.arraycopy(tempArray, 0, result, 1, tempArray.length);

        return result;
    }

    //method 2
    /**
     * DOCUMENT ME!
     *
     * @param length DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int[] shuffleAscendingIntArray(int length) {
        int[] result;
        Vector vector; //a linkedlist or arraylist may prove to be faster
        vector = new Vector();

        for (int i = 0; i < length; i++) {
            vector.add(new Integer(i));
        }

        result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = ((Integer) vector.get(new Double(Math.floor(
                            Math.random() * (length - i))).intValue())).intValue();
            vector.remove(result[i]);
        }

        return result;
    }
}
