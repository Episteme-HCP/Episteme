package org.jscience.sociology;

import org.jscience.biology.Individual;

import org.jscience.util.Commented;
import org.jscience.util.Named;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing the interaction of people around a common activity
 * or conflict. Situations happen usually at dedicated places.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//subclass this class and provide extra fields for the role and the situation: for example room number, ordered dishes, table number, served dishes, reservation, payment form   
public class Situation extends Object implements Named, Commented {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String comments;

    /** DOCUMENT ME! */
    private Set roles; //the roles in this situation

/**
     * Creates a new Situation object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public Situation(String name, String comments) {
        if ((name != null) && (name.length() > 0) && (comments != null) &&
                (comments.length() > 0)) {
            this.name = name;
            this.comments = comments;
            roles = new HashSet();
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
    public String getComments() {
        return comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getIndividuals() {
        Iterator iterator;
        Set result;

        iterator = roles.iterator();
        result = new HashSet();

        while (iterator.hasNext()) {
            result.add(((Role) iterator.next()).getIndividual());
        }

        return result;
    }

    //the situation of the role is set accordingly
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param kind DOCUMENT ME!
     */
    public void addRole(Individual individual, String name, int kind) {
        if (individual != null) {
            roles.add(new Role(individual, name, this, kind));
        } else {
            throw new IllegalArgumentException("You can't add a null role.");
        }
    }

    //the situation of the role is set accordingly
    /**
     * DOCUMENT ME!
     *
     * @param role DOCUMENT ME!
     */
    public void addRole(Role role) {
        if (role != null) {
            role.setSituation(this);
            roles.add(role);
        } else {
            throw new IllegalArgumentException("You can't add a null role.");
        }
    }

    //the role is also removed from the individual automatically
    /**
     * DOCUMENT ME!
     *
     * @param role DOCUMENT ME!
     */
    public void removeRole(Role role) {
        if (role != null) {
            roles.remove(role);
            role.getIndividual().removeRole(role);
        } else {
            throw new IllegalArgumentException("You can't remove a null role.");
        }
    }

    //actually changes the situation of each role
    /**
     * DOCUMENT ME!
     *
     * @param roles DOCUMENT ME!
     */
    public void setRoles(Set roles) {
        Iterator iterator;
        boolean valid;
        Role role;

        if (roles != null) {
            iterator = roles.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Role;
            }

            if (valid) {
                iterator = this.roles.iterator();

                while (iterator.hasNext()) {
                    role = ((Role) iterator.next());
                    role.getIndividual().removeRole(role);
                }

                iterator = roles.iterator();

                while (iterator.hasNext()) {
                    ((Role) iterator.next()).setSituation(this);
                }

                this.roles = roles;
            } else {
                throw new IllegalArgumentException(
                    "The Role Set should contain only Roles.");
            }
        } else {
            throw new IllegalArgumentException("The Set shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getRoles() {
        return roles;
    }
}
