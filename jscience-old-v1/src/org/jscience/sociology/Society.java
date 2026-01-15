package org.jscience.sociology;

import org.jscience.psychology.social.Tribe;

import org.jscience.util.Named;

import java.util.HashSet;
import java.util.Set;


/**
 * A class representing the common elements of a group of individuals, what
 * we usually also call civilization.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a society is basically a group of countries sharing the same culture though perhaps not federated
//for example the occidental society
public class Society extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Set tribes;

/**
     * Creates a new Society object.
     *
     * @param name  DOCUMENT ME!
     * @param tribe DOCUMENT ME!
     */
    public Society(String name, Tribe tribe) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            tribes = new HashSet();
            tribes.add(tribe);
        } else {
            throw new IllegalArgumentException(
                "The Society constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTribes() {
        return tribes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tribe DOCUMENT ME!
     */
    public void addTribe(Tribe tribe) {
        tribes.add(tribe);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tribe DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeTribe(Tribe tribe) {
        if ((tribe != null) && tribes.contains(tribe)) {
            if (tribes.size() > 1) {
                tribes.remove(tribe);
            } else {
                throw new IllegalArgumentException(
                    "You can't remove the last tribe of a society.");
            }
        }
    }
}
