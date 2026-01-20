package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.economics.Worker;

import org.jscience.politics.Administration;


/**
 * The Lawyer class provides some useful information for people whose job
 * is to decide if the defandant is guilty or not.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//jury members are normally paid by the court for being here
public class JuryMember extends Worker {
/**
     * Creates a new JuryMember object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     * @param function         DOCUMENT ME!
     * @param administration   DOCUMENT ME!
     */
    public JuryMember(Individual individual, LawSuitSituation lawSuitSituation,
        String function, Administration administration) {
        super(individual, lawSuitSituation, function, administration);
    }

/**
     * Creates a new JuryMember object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     * @param administration   DOCUMENT ME!
     */
    public JuryMember(Individual individual, LawSuitSituation lawSuitSituation,
        Administration administration) {
        super(individual, lawSuitSituation, "Jury member", administration);
    }
}
