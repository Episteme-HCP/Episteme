package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.economics.Worker;

import org.jscience.politics.Administration;


/**
 * The Judge class provides some useful information for people whose job is
 * to take care of the trial process completion.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Judge extends Worker {
/**
     * Creates a new Judge object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     * @param function         DOCUMENT ME!
     * @param administration   DOCUMENT ME!
     */
    public Judge(Individual individual, LawSuitSituation lawSuitSituation,
        String function, Administration administration) {
        super(individual, lawSuitSituation, function, administration);
    }

/**
     * Creates a new Judge object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     * @param administration   DOCUMENT ME!
     */
    public Judge(Individual individual, LawSuitSituation lawSuitSituation,
        Administration administration) {
        super(individual, lawSuitSituation, "Judge", administration);
    }
}
