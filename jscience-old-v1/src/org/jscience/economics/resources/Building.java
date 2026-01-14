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
import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Collections;
import java.util.Date;
import java.util.Set;


/**
 * A class representing a Building or a monument.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//any wood, mud, stone, steel, concrete, bricks human place
//generally covered with a roof
//may be a scryscrapper, a factory, blockhaus, house
//caves like in Turkia or indians Anassasi are not buildings as they are only artificial caves and holes in ground

//for an architectural monument, you may prefer to use org.jscience.arts.Artwork
public class Building extends Artifact implements Store {
    /** DOCUMENT ME! */
    private Set contents;

    /** DOCUMENT ME! */
    private String purpose;

/**
     * Creates a new Building object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     * @param identification  DOCUMENT ME!
     * @param value           DOCUMENT ME!
     */
    public Building(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
        this.contents = Collections.EMPTY_SET;
        this.purpose = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getContents() {
        return contents;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //may return null
    public String getPurpose() {
        return purpose;
    }

    /**
     * DOCUMENT ME!
     *
     * @param purpose DOCUMENT ME!
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    //do nothing
    /**
     * DOCUMENT ME!
     */
    public void getIn() {
    }

    //do nothing
    /**
     * DOCUMENT ME!
     */
    public void getOut() {
    }
}
