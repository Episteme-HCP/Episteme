package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * The people against whom the trial is done, may be a human, a group, a
 * country... (a Set of HumanGroups)
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Defendant extends Role {
    /** DOCUMENT ME! */
    private Set charges;

/**
     * Creates a new Defendant object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     */
    public Defendant(Individual individual, LawSuitSituation lawSuitSituation) {
        super(individual, "Defendant", lawSuitSituation, Role.CLIENT);
        charges = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCharges() {
        return charges;
    }

    /**
     * DOCUMENT ME!
     *
     * @param charges DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setCharges(Set charges) {
        Iterator iterator;
        boolean valid;

        if (charges != null) {
            iterator = charges.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.charges = charges;
            } else {
                throw new IllegalArgumentException(
                    "The Set of charges should contain only Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of charges shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param charge DOCUMENT ME!
     */
    public void addCharge(String charge) {
        charges.add(charge);
    }

    /**
     * DOCUMENT ME!
     *
     * @param charge DOCUMENT ME!
     */
    public void removeCharge(String charge) {
        charges.remove(charge);
    }
}
