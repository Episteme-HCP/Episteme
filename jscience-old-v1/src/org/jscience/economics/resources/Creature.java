package org.jscience.economics.resources;

import org.jscience.biology.Individual;

import org.jscience.economics.Community;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;

import java.util.Date;


/**
 * A class representing animals, plants or fungi.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this should be used for macroscopic organisms
//also see org.jscience.biology.Individual
public class Creature extends Natural {
    /** DOCUMENT ME! */
    private String classification;

    /** DOCUMENT ME! */
    private Individual individual;

/**
     * Creates a new Creature object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     */
    public Creature(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate) {
        super(name, description, amount, producer, productionPlace,
            productionDate);
        this.classification = null;
    }

    //the complete name and classification for the living
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClassification() {
        return classification;
    }

    //the complete name and classification for the living
    /**
     * DOCUMENT ME!
     *
     * @param classification DOCUMENT ME!
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    //may be null
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
     * @param individual DOCUMENT ME!
     */
    public void setIndividual(Individual individual) {
        this.individual = individual;
    }
}
