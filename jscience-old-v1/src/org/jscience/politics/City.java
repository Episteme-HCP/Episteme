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

package org.jscience.politics;

import org.jscience.biology.Individual;

import org.jscience.geography.Boundary;
import org.jscience.geography.Place;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a human modern settlement.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//we could also have a field for the town hall coordinates
//includes villages
public class City extends Place {
    /** DOCUMENT ME! */
    private String zipCode;

    /** DOCUMENT ME! */
    private Country country;

    /** DOCUMENT ME! */
    private Set leaders; //the current responsible persons in the City

/**
     * Creates a new City object.
     *
     * @param name     DOCUMENT ME!
     * @param boundary DOCUMENT ME!
     * @param country  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //City is automatically added to country
    public City(String name, Boundary boundary, Country country) {
        super(name, boundary);

        if ((country != null) && (leaders != null)) {
            this.country = country;
            this.country.addCity(this);
            this.zipCode = null;
            this.leaders = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The City constructor can't have null arguments.");
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

    //a good policy is to set the leaders to getGovernement().getOrganigram().getWorkers()
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
                this.leaders = leaders;
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
     * @return DOCUMENT ME!
     */

    //may return null if unset by removing from the regions of a country
    public Country getCountry() {
        return country;
    }

    /**
     * DOCUMENT ME!
     *
     * @param country DOCUMENT ME!
     */
    protected void setCountry(Country country) {
        this.country = country;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //may return null;
    public String getZipCode() {
        return zipCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param zipCode DOCUMENT ME!
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
