package org.jscience.politics.vote;

import java.util.Set;


/**
 * This class represents a way by which you compute results.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//You have first to call validateBallots() THEN ONLY getResults() which returns the appropriate results
//and tell if you should launch one more round (shouldProceedToNextRound()) because some winners are still not known
public interface BallotsProcessor {
    //you have to call this method first
    /**
     * DOCUMENT ME!
     *
     * @param voters DOCUMENT ME!
     * @param round DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set validateBallots(Set voters, int round);

    //fills a new pseudo ballot with the summed results telling who has won for every choice
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Ballot getResults();

    //optional: implementations should also provide one of the two following depending on if there is one or multiple winners for every choice
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean shouldProceedToNextRound();
}
