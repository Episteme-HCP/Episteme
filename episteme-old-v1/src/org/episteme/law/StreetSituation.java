package org.episteme.law;

import org.episteme.biology.Individual;

import org.episteme.sociology.Situation;


/**
 * A class representing the interaction of people around justice.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class StreetSituation extends Situation {
/**
     * Creates a new StreetSituation object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public StreetSituation(String name, String comments) {
        super(name, comments);
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addResponsibleIndividual(Individual individual) {
        super.addRole(new ResponsibleIndividual(individual, this));
    }
}
