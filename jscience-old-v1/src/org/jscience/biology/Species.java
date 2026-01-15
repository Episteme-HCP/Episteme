package org.jscience.biology;

import org.jscience.biology.taxonomy.Taxon;

import org.jscience.psychology.Behavior;

import org.jscience.util.Named;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a specie.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//Species are known to last on average 10 million years.
//species is plural even for a single species

//perhaps we should have a class to store well known species.

//includes phylotype ("species" with asexual reproduction)
public abstract class Species extends Object implements Named {
    /** DOCUMENT ME! */
    private String name; //the common name

    /** DOCUMENT ME! */
    private Taxon taxon; //the classification

    /** DOCUMENT ME! */
    private int sexuality; // see BiologyConstants

    /** DOCUMENT ME! */
    private int predation; // see BiologyConstants

    /** DOCUMENT ME! */
    private Set behaviors; //the behaviors common to all members of the species

    //uses the name from the taxon
    /**
     * Creates a new Species object.
     *
     * @param taxon DOCUMENT ME!
     */
    public Species(Taxon taxon) {
        if (taxon != null) {
            this.name = taxon.getCommonName();
            this.taxon = taxon;
            sexuality = BiologyConstants.UNKNOWN;
            predation = BiologyConstants.UNKNOWN;
            behaviors = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Species constructor can't have null arguments.");
        }
    }

/**
     * Creates a new Species object.
     *
     * @param name  DOCUMENT ME!
     * @param taxon DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Species(String name, Taxon taxon) {
        if ((name != null) && (name.length() > 0) && (taxon != null)) {
            this.name = name;
            this.taxon = taxon;
            sexuality = BiologyConstants.UNKNOWN;
            predation = BiologyConstants.UNKNOWN;
            behaviors = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Species constructor can't have null arguments (or empty name).");
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
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getReproductionMode() {
        return sexuality;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mode DOCUMENT ME!
     */
    public void setReproductionMode(int mode) {
        this.sexuality = mode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPredationMode() {
        return predation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mode DOCUMENT ME!
     */
    public void setPredationMode(int mode) {
        this.predation = mode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getBehaviors() {
        return behaviors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param behaviors DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setBehaviors(Set behaviors) {
        Iterator iterator;
        boolean valid;

        if (behaviors != null) {
            iterator = behaviors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Behavior;
            }

            if (valid) {
                this.behaviors = behaviors;
            } else {
                throw new IllegalArgumentException(
                    "The Set of behaviors should contain only Behaviors.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of behaviors shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param behavior DOCUMENT ME!
     */
    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
    }

    /**
     * DOCUMENT ME!
     *
     * @param behavior DOCUMENT ME!
     */
    public void removeBehavior(Behavior behavior) {
        behaviors.remove(behavior);
    }

    //two species are equal if they bind to the same name and taxon
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        Species species;

        if ((o != null) && (o instanceof Species)) {
            species = (Species) o;

            return this.getName().equals(species.getName()) &&
            this.getTaxon().equals(species.getTaxon());
        } else {
            return false;
        }
    }

    //a Set of Individuals resulting from this one with related genome
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param individuals DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Set reproduce(Individual individual, Set individuals);
}
