package org.jscience.economics;

import org.jscience.biology.Individual;


/**
 * A class representing the interaction of people around a common activity.
 * Situations happen usually at dedicated places.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you may prefer this class to org.jscience.sociology.Situations.WORKING
public class WorkSituation extends EconomicSituation {
    //use the organization name as the name, or a part of it if your organization is big
    /**
     * Creates a new WorkSituation object.
     *
     * @param name DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public WorkSituation(String name, String comments) {
        super(name, comments);
    }

    //builds out a worker
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param function DOCUMENT ME!
     * @param organization DOCUMENT ME!
     */
    public void addWorker(Individual individual, String function,
        Organization organization) {
        super.addRole(new Worker(individual, this, function, organization));
    }
}
