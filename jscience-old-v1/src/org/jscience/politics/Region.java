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
 * A class representing a common country subdivision.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//we could also define State, County... also these subdivisions are very dependent about the country you live in
//Countries using administrative regions
//The following countries use the term "region" (or its cognate) as the name of a type of subnational entity:
//Belgium (in French, région; in German, Region; the term gewest is used in Dutch)
//Chile (región)
//Congo (région)
//Côte d'Ivoire (région)
//France (région)
//Ghana
//Hungary (régió)
//Italy (regione)
//Mali (région)
//Namibia
//New Zealand
//Peru (región)
//Tanzania
//Togo (région)
//The Canadian province of Québec also uses the "administrative region" (région administrative).
//Prior to 1996, Scotland was also divided into regions.
//The government of the Philippines uses the region (in Filipino, rehiyon) when it's necessary to group provinces, the primary administrative subdivision of the country.
//The government of Singapore makes use of regions for its own administrative purposes. Similarly, the British government also makes limited use of regions for England.
//The following countries use an administrative subdivision conventionally referred to as a region in English:
//Russia, which uses the обла�?ть (oblast').
//Ukraine, which uses the обла�?ть (oblast').
//China has five 自治区 (zìzhìqū) and two 特別行政�?� (or 特别行政区; tèbiéxíngzhèngqū) which are conventionally translated as "autonomous region" and "special administrative region", respectively.
public class Region extends Place {
    /** DOCUMENT ME! */
    private Country country;

    /** DOCUMENT ME! */
    private Set leaders; //the current responsible persons in the region

/**
     * Creates a new Region object.
     *
     * @param name     DOCUMENT ME!
     * @param boundary DOCUMENT ME!
     * @param country  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //region is automatically added to country
    public Region(String name, Boundary boundary, Country country) {
        super(name, boundary);

        if ((country != null) && (leaders != null)) {
            this.country = country;
            this.country.addRegion(this);
            this.leaders = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Region constructor can't have null arguments.");
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
}
