/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
