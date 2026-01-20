package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;


/**
 * The (alleged) victim, may be a human, a group, a country... (a Set of
 * HumanGroups)s but represented by an individual.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Plaintiff extends Role {
/**
     * Creates a new Plaintiff object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     */
    public Plaintiff(Individual individual, LawSuitSituation lawSuitSituation) {
        super(individual, "Plaintiff", lawSuitSituation, Role.CLIENT);
    }
}
