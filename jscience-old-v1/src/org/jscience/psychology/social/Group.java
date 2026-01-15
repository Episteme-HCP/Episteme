package org.jscience.psychology.social;

import org.jscience.biology.Individual;
import org.jscience.biology.Population;
import org.jscience.biology.Species;

import org.jscience.geography.Place;

import java.util.*;


/**
 * A class representing a group, that is a population seen from a
 * psychological point of view. Includes a sociogram, that is a representation
 * of the relations among a the individuals along time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a group is a set of individuals from the same species who normally share space and/or resources
//this class does account for hierarchical relations between the members of the group (using a positive/negative system which the highest positive number means you are strong over someone).
//the relation system herein can be used also for a pure sociogram with the number describing attraction/repulsion towards an individual (this is the prefered mechanism as dominance hierarchies may also be defined using economics, see below).
//One can also use the Organigram class under economics for task specific hierarchy: the group efficiency is based on an economic and hierarchic way of grouping individuals.
//but also, hierarchical relations can be implemented as Roles/Situations using org.jscience.sociology classes or better as economic entities
//it is expected that the number of individuals and relations considered is very low
//in other words it is designed to grasp the relations of a small group in a small to very big population
//may be we should call this class Sociogram or have a specific class
//internal representation is based upon a map but could be based on a sparse matrix or a graph
//some subclasses for high order species should consider implementing "home range" concept
//some groups have no leader
//as a general rule in the animal kingdom, the leader (or leaders) are individuals who control the resources
//the goup is therefore rather seen as a community, with some members higher in the organigram than others
//on the opposite, humans have formal leaders, ants (and the like) have individuals that are physically different from the others (queens)
//you should therefore in most cases see the getLeaders() method as a convenince method to get some information that should be in one form or another in the underlying hierarchy
public class Group extends Population {
    /** DOCUMENT ME! */
    private Place formalTerritory;

    //all these individuals are normally contained in the underlying population
    /** DOCUMENT ME! */
    private Set leaders; //the head, the ruler, see http://en.wikipedia.org/wiki/Monarch for common titles and names

    /** DOCUMENT ME! */
    private Map relations;

/**
     * Creates a new Group object.
     *
     * @param species DOCUMENT ME!
     */
    public Group(Species species) {
        super(species);
        this.formalTerritory = getTerritory();
        this.leaders = Collections.EMPTY_SET;
        this.relations = Collections.EMPTY_MAP;
    }

/**
     * Creates a new Group object.
     *
     * @param species         DOCUMENT ME!
     * @param formalTerritory DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Group(Species species, Place formalTerritory) {
        super(species);

        if (formalTerritory != null) {
            this.formalTerritory = formalTerritory;
            this.leaders = Collections.EMPTY_SET;
            this.relations = Collections.EMPTY_MAP;
        } else {
            throw new IllegalArgumentException(
                "The Group constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getFormalTerritory() {
        return formalTerritory;
    }

    /**
     * DOCUMENT ME!
     *
     * @param territory DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setFormalTerritory(Place territory) {
        if ((territory != null)) {
            this.formalTerritory = territory;
        } else {
            throw new IllegalArgumentException(
                "The territory must be non null.");
        }
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

    //all leaders must be from the population
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
                if (getIndividuals().containsAll(leaders)) {
                    this.leaders = leaders;
                } else {
                    throw new IllegalArgumentException(
                        "The leaders must also be part of the population.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The leaders Set must contain only Individuals.");
            }
        } else {
            throw new IllegalArgumentException("You can't set null leaders.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual1 DOCUMENT ME!
     * @param individual2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double getRelation(Individual individual1, Individual individual2) {
        Object object;
        Hashtable hashtable;
        double result;
        Object object2;

        if ((getIndividuals().contains(individual1)) &&
                (getIndividuals().contains(individual2))) {
            object = relations.get(individual1);

            if (object != null) {
                hashtable = (Hashtable) object;
                object2 = hashtable.get(individual2);

                if (object2 != null) {
                    result = ((Double) object).doubleValue();
                } else {
                    result = 0;
                }
            } else {
                result = 0;
            }
        } else {
            throw new IllegalArgumentException(
                "Can't get the relation of individuals not in the Population.");
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual1 DOCUMENT ME!
     * @param individual2 DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setRelation(Individual individual1, Individual individual2,
        double value) {
        Object object;
        Hashtable hashtable;

        if ((getIndividuals().contains(individual1)) &&
                (getIndividuals().contains(individual2))) {
            object = relations.get(individual1);

            if (object != null) {
                hashtable = (Hashtable) object;
            } else {
                hashtable = new Hashtable();
            }

            hashtable.put(individual2, new Double(value));
            relations.put(individual1, hashtable);
        } else {
            throw new IllegalArgumentException(
                "Can't get the relation of individuals not in the Population.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param individuals DOCUMENT ME!
     */
    public void setIndividuals(Set individuals) {
        super.setIndividuals(individuals);
        relations = Collections.EMPTY_MAP;
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addIndividual(Individual individual) {
        super.addIndividual(individual);
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void removeIndividual(Individual individual) {
        Iterator iterator;
        Hashtable currentHashtable;

        super.removeIndividual(individual);
        relations.remove(individual);

        //even if saving memory, the next part may be too time consuming compared to the memory benefits
        //simply remove if unsuitable as it will not change the class behavior
        iterator = relations.keySet().iterator();

        while (iterator.hasNext()) {
            currentHashtable = (Hashtable) iterator.next();
            currentHashtable.remove(individual);
        }
    }
}
