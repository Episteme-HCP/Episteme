package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * The people who have seen and come to testimony.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Witness extends Role {
    /** DOCUMENT ME! */
    private Set parties; //who you are helping

/**
     * Creates a new Witness object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     */
    public Witness(Individual individual, LawSuitSituation lawSuitSituation) {
        super(individual, "Witness", lawSuitSituation, Role.SERVER);
        parties = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getClients() {
        return parties;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parties DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setClients(Set parties) {
        Iterator iterator;
        boolean valid;
        Object value;

        if (parties != null) {
            iterator = parties.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                value = iterator.next();
                valid = (value instanceof Plaintiff) ||
                    (value instanceof Defendant);
            }

            if (valid) {
                this.parties = parties;
            } else {
                throw new IllegalArgumentException(
                    "The Set of parties should contain only Plaintiffs and Defendants.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of parties shouldn't be null.");
        }
    }
}
