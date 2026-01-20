package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.economics.Worker;

import org.jscience.politics.Administration;


/**
 * The Doctor class provides some useful information for people whose job
 * is to cure individuals.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Prosecutor extends Worker {
/**
     * Creates a new Prosecutor object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     * @param function         DOCUMENT ME!
     * @param administration   DOCUMENT ME!
     */
    public Prosecutor(Individual individual, LawSuitSituation lawSuitSituation,
        String function, Administration administration) {
        super(individual, lawSuitSituation, function, administration);
    }

/**
     * Creates a new Prosecutor object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     * @param administration   DOCUMENT ME!
     */
    public Prosecutor(Individual individual, LawSuitSituation lawSuitSituation,
        Administration administration) {
        super(individual, lawSuitSituation, "Prosecutor", administration);
    }
}
