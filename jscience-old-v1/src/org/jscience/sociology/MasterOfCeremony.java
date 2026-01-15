package org.jscience.sociology;

import org.jscience.biology.Individual;

import org.jscience.economics.Organization;
import org.jscience.economics.Worker;


/**
 * The MasterOfCeremony class provides some useful information for people
 * whose job is to entertain.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class MasterOfCeremony extends Worker {
/**
     * Creates a new MasterOfCeremony object.
     *
     * @param individual   DOCUMENT ME!
     * @param celebration  DOCUMENT ME!
     * @param function     DOCUMENT ME!
     * @param organization DOCUMENT ME!
     */
    public MasterOfCeremony(Individual individual, Celebration celebration,
        String function, Organization organization) {
        super(individual, celebration, function, organization);
    }

/**
     * Creates a new MasterOfCeremony object.
     *
     * @param individual   DOCUMENT ME!
     * @param celebration  DOCUMENT ME!
     * @param organization DOCUMENT ME!
     */
    public MasterOfCeremony(Individual individual, Celebration celebration,
        Organization organization) {
        super(individual, celebration, "Master of ceremony", organization);
    }
}
