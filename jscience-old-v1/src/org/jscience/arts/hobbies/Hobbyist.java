package org.jscience.arts.hobbies;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;
import org.jscience.sociology.Situation;
import org.jscience.arts.Collection;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing a person who has hobbies (may be collecting objects
 * or stuff).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//as multiple inheritance is not possible we have choosen to extend Person although you may only need the superclass data
public final class Hobbyist extends Role {
    /** DOCUMENT ME! */
    private Set hobbies;

    /** DOCUMENT ME! */
    private Set collections;

/**
     * Creates a new Hobbyist object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public Hobbyist(Individual individual, Situation situation) {
        super(individual, "Hobbyist", situation, Role.OBSERVER);
        this.hobbies = Collections.EMPTY_SET;
        this.collections = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getHobbies() {
        return hobbies;
    }

    //there is no setHobbies() but I feel like it
    /**
     * DOCUMENT ME!
     *
     * @param hobby DOCUMENT ME!
     */
    public void addHobby(Hobby hobby) {
        hobbies.add(hobby);
    }

    /**
     * DOCUMENT ME!
     *
     * @param hobby DOCUMENT ME!
     */
    public void removeHobby(Hobby hobby) {
        hobbies.remove(hobby);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCollections() {
        return collections;
    }

    //there is no setCollections() but I feel like it
    /**
     * DOCUMENT ME!
     *
     * @param collection DOCUMENT ME!
     */
    public void addCollection(Collection collection) {
        collections.add(collection);
    }

    /**
     * DOCUMENT ME!
     *
     * @param collection DOCUMENT ME!
     */
    public void removeCollection(Collection collection) {
        collections.remove(collection);
    }
}
