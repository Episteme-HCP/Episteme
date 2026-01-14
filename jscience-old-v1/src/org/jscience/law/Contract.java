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

package org.jscience.law;

import org.jscience.economics.Organization;

import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;


/**
 * A class representing a piece of paper usually used when setting a deal,
 * describing it.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this could be an insurance contract for example
//or anything you rent or buy...
//there is no set method because this is meant to be a paper that you can't fill or modify
//contracts usually refer to some properties of people but they are usually not a property themselves
//computer licences, for example GPL should be found in org.jscience.sociology.License, not in org.jscience.law.Contract
//also you can have a different idea on this
public class Contract extends Object implements Identified {
    //many other fields could be used:
    //the generic name
    //the place where it was signed
    //the period (beginning, end) when it is usable
    //the extra clauses, that may also be added at a later time
    //there is also sometimes an authority that makes sure both parties respect the contract
    /** DOCUMENT ME! */
    private Organization firstParty;

    /** DOCUMENT ME! */
    private Organization secondParty;

    /** DOCUMENT ME! */
    private Identification identification; //big contracts usually don't have any identification (:from hand to hand)

    /** DOCUMENT ME! */
    private Date date; //when it is signed by both parties

    /** DOCUMENT ME! */
    private Vector contents;

/**
     * Creates a new Contract object.
     *
     * @param firstParty     DOCUMENT ME!
     * @param secondParty    DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param date           DOCUMENT ME!
     * @param contents       DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Contract(Organization firstParty, Organization secondParty,
        Identification identification, Date date, Vector contents) {
        Iterator iterator;
        boolean valid;

        if ((firstParty != null) && (secondParty != null) &&
                (identification != null) && (date != null) &&
                (contents != null) && (contents.size() > 0)) {
            iterator = contents.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.firstParty = firstParty;
                this.secondParty = secondParty;
                this.identification = identification;
                this.date = date;
                this.contents = contents;
            } else {
                throw new IllegalArgumentException(
                    "The contents Vector must contain only Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Contract constructor can't have null arguments or empty contents.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Organization getFirstParty() {
        return firstParty;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Organization getSecondParty() {
        return secondParty;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getContents() {
        return contents;
    }
}
