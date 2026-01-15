package org.jscience.biology;

import org.jscience.geography.Boundary;
import org.jscience.geography.Place;

import org.jscience.util.Positioned;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a population.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a group of individuals from the same specie that live and mate together
//this is a loose group, see subclass for groups with higher order relations
//we could add life expectancy, birth rate, etc
//use Ecosystem for multi species groups or subclass yourself or complain to us

//perhaps we should provide direct support from within the Individual class and double link with this class
public class Population extends Object implements Positioned {
    /** DOCUMENT ME! */
    private Species species;

    /** DOCUMENT ME! */
    private Set individuals;

/**
     * Creates a new Population object.
     *
     * @param species DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Population(Species species) {
        if (species != null) {
            this.species = species;
            individuals = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Population constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return individuals.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getIndividuals() {
        return individuals;
    }

    //all elements of the Set should be individuals from the same species given by getSpecie()
    /**
     * DOCUMENT ME!
     *
     * @param individuals DOCUMENT ME!
     */
    public void setIndividuals(Set individuals) {
        Iterator iterator;
        boolean valid;
        Object currentElement;

        if (individuals != null) {
            iterator = individuals.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                currentElement = iterator.next();
                valid = (currentElement instanceof Individual) &&
                    (((Individual) currentElement).getSpecies().equals(species));
            }

            if (valid) {
                this.individuals = individuals;
            } else {
                throw new IllegalArgumentException(
                    "Contents of the Set should all be Individuals.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set null individuals.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addIndividual(Individual individual) {
        individuals.add(individual);
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void removeIndividual(Individual individual) {
        individuals.remove(individual);
    }

    /**
     * DOCUMENT ME!
     *
     * @param population DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isContained(Population population) {
        return population.getIndividuals().containsAll(this.getIndividuals());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        Iterator iterator;
        Boundary boundary;

        iterator = individuals.iterator();
        boundary = new Boundary();

        while (iterator.hasNext()) {
            boundary.union(((Individual) iterator.next()).getPosition()
                            .getBoundary());
        }

        return new Place("Position", boundary);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getTerritory() {
        Iterator iterator;
        Boundary boundary;

        iterator = individuals.iterator();
        boundary = new Boundary();

        while (iterator.hasNext()) {
            boundary.union(((Individual) iterator.next()).getTerritory()
                            .getBoundary());
        }

        return new Place("Territory", boundary);
    }
}
