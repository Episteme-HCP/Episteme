package org.jscience.sociology;

import org.jscience.biology.Individual;

import org.jscience.util.Named;


/**
 * A class representing the current behavior/relation a Person has with its
 * environment. Usually in pairs, for example client (student, customer,
 * patient...) and server (teacher, waiter, doctor...) or triplets client,
 * server and supervisor (administrator, manager...). There is sometimes
 * another role which is not part of the process itself: the observer
 * (narrator, passive audience...). Several other roles may be considered
 * although you should try to fit them in the above categories when possible:
 * mediator, slave... Think for example about all the roles of the individual
 * that are in a theater: producer, director, actors, technicians, public...
 * You may want not to assign the default roles but reproduce a more complex
 * environment although you may not apply some models on them.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
// a role defines some tasks, activities and behaviors, current and expected

//even in the street there are policemen: humans have roles everywhere in there life, for example at home there are parents and children, friends and neighbors.
public class Role extends Object implements Named {
    /** DOCUMENT ME! */
    public final static int CLIENT = 1; //mutually exclusive

    /** DOCUMENT ME! */
    public final static int SERVER = 2;

    /** DOCUMENT ME! */
    public final static int SUPERVISOR = 4;

    /** DOCUMENT ME! */
    public final static int OBSERVER = 8;

    /** DOCUMENT ME! */
    private Individual individual; //the individual callback

    /** DOCUMENT ME! */
    private String name; //the given name for this kind of role

    /** DOCUMENT ME! */
    private Situation situation; //the situation callback

    /** DOCUMENT ME! */
    private int kind; //the archetypal role

/**
     * Creates a new Role object.
     *
     * @param individual DOCUMENT ME!
     * @param name       DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     * @param kind       DOCUMENT ME!
     */
    public Role(Individual individual, String name, Situation situation,
        int kind) {
        if ((individual != null) && (name != null) && (name.length() > 0) &&
                (situation != null)) {
            this.individual = individual;
            individual.addRole(this);
            this.name = name;
            this.situation = situation;
            this.kind = kind;
        } else {
            throw new IllegalArgumentException(
                "The Role constructor cannot have null or empty arguments.");
        }
    }

/**
     * Creates a new Role object.
     *
     * @param name DOCUMENT ME!
     * @param kind DOCUMENT ME!
     */
    public Role(String name, int kind) {
        if ((name != null) && (name.length() > 0)) {
            this.individual = null;
            this.name = name;
            this.situation = null;
            this.kind = kind;
        } else {
            throw new IllegalArgumentException(
                "The Role constructor cannot have null or empty name.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Individual getIndividual() {
        return individual;
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
    public Situation getSituation() {
        return situation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param situation DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    protected void setSituation(Situation situation) {
        if (situation != null) {
            this.situation = situation;
        } else {
            throw new IllegalArgumentException("Situation can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }
}
