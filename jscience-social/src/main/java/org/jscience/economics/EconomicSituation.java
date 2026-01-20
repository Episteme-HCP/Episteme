package org.jscience.economics;

import org.jscience.biology.Individual;

import org.jscience.sociology.Situation;


/**
 * A class representing the interaction of people around resources.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you may prefer this class to org.jscience.sociology.Situations.WORKING
public class EconomicSituation extends Situation {
    //use the organization name as the name, or a part of it if your organization is big
    /**
     * Creates a new EconomicSituation object.
     *
     * @param name DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public EconomicSituation(String name, String comments) {
        super(name, comments);
    }

    //builds out a worker
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addEconomicAgent(Individual individual) {
        super.addRole(new EconomicAgent(individual, this));
    }
}
