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

import org.jscience.economics.Organization;

import org.jscience.geography.BusinessPlace;

import org.jscience.measure.Identification;
import org.jscience.measure.StringIdentificationFactory;

import java.util.Set;


/**
 * A class representing a group of people organized in a hierarchy and
 * loyal subjects to a state, they represent the active force. Armies, Police,
 * may be politicians, and prosecutors
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//stateforce require no identification as they are part of the state and are all unique: there is one army, one police, etc.
//for the same reason there no owners as the government or normally the whole country owns the stateforce
//also called bureaucracy
public class Administration extends Organization {
    /** DOCUMENT ME! */
    private Country country;

/**
     * Creates a new Administration object.
     *
     * @param name     DOCUMENT ME!
     * @param country  DOCUMENT ME!
     * @param place    DOCUMENT ME!
     * @param accounts DOCUMENT ME!
     */
    public Administration(String name, Country country, BusinessPlace place,
        Set accounts) {
        super(name,
            new StringIdentificationFactory().generateIdentification(name),
            country.getNation().getIndividuals(), place, accounts);
        this.country = country;
    }

/**
     * Creates a new Administration object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param country        DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @param accounts       DOCUMENT ME!
     */
    public Administration(String name, Identification identification,
        Country country, BusinessPlace place, Set accounts) {
        super(name, identification, country.getNation().getIndividuals(),
            place, accounts);
        this.country = country;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Country getCountry() {
        return country;
    }
}
