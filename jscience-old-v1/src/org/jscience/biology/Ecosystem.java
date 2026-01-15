package org.jscience.biology;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing an ecosystem (interacting populations from
 * different species).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//interacting species (usually of predators/preys)
public class Ecosystem extends Object {
    /** DOCUMENT ME! */
    private Set populations;

/**
     * Creates a new Ecosystem object.
     */
    public Ecosystem() {
        populations = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumPopulations() {
        return populations.size();
    }

    //iterates through all populations and returns the species
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSpecies() {
        Iterator iterator;
        Set result;

        iterator = populations.iterator();
        result = Collections.EMPTY_SET;

        while (iterator.hasNext()) {
            result.add(((Population) iterator.next()).getSpecies());
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getPopulations() {
        return populations;
    }

    //all elements of the Set should be populations
    /**
     * DOCUMENT ME!
     *
     * @param populations DOCUMENT ME!
     */
    public void setPopulations(Set populations) {
        Iterator iterator;
        boolean valid;

        if (populations != null) {
            iterator = populations.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Population;
            }

            if (valid) {
                this.populations = populations;
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null populations.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param population DOCUMENT ME!
     */
    public void addPopulation(Population population) {
        populations.add(population);
    }

    /**
     * DOCUMENT ME!
     *
     * @param population DOCUMENT ME!
     */
    public void removePopulation(Population population) {
        populations.remove(population);
    }
}
