package org.jscience.politics;

import org.jscience.biology.Individual;

import org.jscience.geography.Place;

import org.jscience.psychology.social.Group;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a primitive settlement. This is to be used mostly
 * for Tribes (human groups), or primate groups, or also Ants groups (in which
 * case this class is the colony).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
// perhaps should be called chiefdom
//or extend Community and implement Named, Positioned
public class Settlement extends Place {
    /** DOCUMENT ME! */
    private Group group;

    /** DOCUMENT ME! */
    private Set leaders; //the current leader

/**
     * Creates a new Settlement object.
     *
     * @param name  DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Settlement(String name, Group group) {
        super(name, group.getFormalTerritory().getBoundary());

        if (group != null) {
            this.group = group;
            this.leaders = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Settlement constructor doesn't allow null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Group getGroup() {
        return group;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getLeaders() {
        return leaders;
    }

    /**
     * DOCUMENT ME!
     *
     * @param leaders DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //a good policy is to set the leaders to getGovernement().getOrganigram().getWorkers()
    public void setLeaders(Set leaders) {
        Iterator iterator;
        boolean valid;

        if (leaders != null) {
            iterator = leaders.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Individual;
            }

            if (valid) {
                this.leaders = leaders;
            } else {
                throw new IllegalArgumentException(
                    "The leaders Set must contain only Individuals.");
            }
        } else {
            throw new IllegalArgumentException("You can't set null leaders.");
        }
    }
}
