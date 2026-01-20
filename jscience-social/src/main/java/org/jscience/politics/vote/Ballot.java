package org.jscience.politics.vote;

import java.util.Set;


/**
 * This class represents the different choices available to someone in a
 * specific vote session.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//inspired by http://www.cs.unc.edu/~wluebke/comp204/liberty/
public abstract class Ballot implements Cloneable {
    // Adds a choice to the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     */
    public abstract void addChoice(String title);

    //  Adds an option to a choice on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     */
    public abstract void addOptionToChoice(String title, String option);

    // Returns the choices on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Set getChoices();

    //  Returns a set of options for a choice on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Set getOptionsForChoice(String title);

    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isOptionSelected(String title, String option);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Object clone();
}
