package org.jscience.politics;

import org.jscience.biology.Individual;

import org.jscience.sociology.Situation;


/**
 * A class representing the interaction of people around cities. See also,
 * LegalSituation
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class CivilSituation extends Situation {
/**
     * Creates a new CivilSituation object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public CivilSituation(String name, String comments) {
        super(name, comments);
    }

    //builds out a worker
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addCitizen(Individual individual) {
        super.addRole(new Citizen(individual, this));
    }
}
