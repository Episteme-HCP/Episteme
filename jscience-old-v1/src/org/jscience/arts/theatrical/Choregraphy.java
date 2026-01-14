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

package org.jscience.arts.theatrical;

import org.jscience.economics.Community;
import org.jscience.economics.money.Currency;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import org.jscience.arts.ArtsConstants;
import org.jscience.arts.Artwork;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing the ordered poses to be adopted by the actors of a
 * play.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Choregraphy extends Artwork {
    /** DOCUMENT ME! */
    private Vector steps;

    //a vector of String
    /**
     * Creates a new Choregraphy object.
     *
     * @param name DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param producer DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param authors DOCUMENT ME!
     * @param steps DOCUMENT ME!
     */
    public Choregraphy(String name, String description, Community producer,
        Date productionDate, Identification identification, Set authors,
        Vector steps) {
        super(name, description, Amount.ZERO, producer, producer.getPosition(),
            productionDate, identification, Amount.valueOf(0, Currency.USD),
            authors, ArtsConstants.DANCE);

        Iterator iterator;
        boolean valid;

        if ((steps != null) && (steps.size() > 0)) {
            iterator = steps.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.steps = steps;
            } else {
                throw new IllegalArgumentException(
                    "The Vector can consist only of Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of steps must be not null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getSteps() {
        return steps;
    }
}
