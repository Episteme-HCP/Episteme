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

import org.jscience.economics.Community;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;

import java.util.Date;


/**
 * A class representing Fossils.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also see org.jscience.biology.Individual  
//natural object (dead animal remains)
//please note that not all animals have bones (but all remains of animals are considered as fossil)
//make an effort to provide the specie for the fossil as there is no further chance to guess by the system
//natural object (dead plant remains)
//please note that not all plants leave remains (but all remains of plant are considered as fossil)
//make an effort to provide the species for the fossil as there is no further chance to guess by the system
public class Fossil extends Mineral {
    /** DOCUMENT ME! */
    private String classification;

/**
     * Creates a new Fossil object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     */
    public Fossil(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate) {
        super(name, description, amount, producer, productionPlace,
            productionDate);
        this.classification = null;
    }

    //the complete name and classification for the animal this bones comes from
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClassification() {
        return classification;
    }

    //the complete name and classification for the animal this bones comes from
    /**
     * DOCUMENT ME!
     *
     * @param classification DOCUMENT ME!
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }
}
