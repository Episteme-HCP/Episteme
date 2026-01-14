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

import java.util.Date;


/**
 * A class representing an installation.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a structure build by humans, that modifies the landscape and that usually serve for a purpose
//a dam in concrete (not only a ground hill), a bridge, tunnel (not only the hole but the concrete)
//this also account for art (sculptures...)
public class Installation extends Artifact {
    //objects are meant to be sold and therefore have a price
    //yes, installations also in some way have a cost as they are not built by nature itself
    //but this cost is supported by the community, not by individuals, and is meaningless in terms of sale
    //as each one is art in some way
/**
     * Creates a new Installation object.
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
    public Installation(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
    }
}
