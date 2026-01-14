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

package org.jscience.economics;

import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;

import java.io.Serializable;

import java.util.Date;


/**
 * A class representing the modern human work as an economical resource.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also see http://en.wikipedia.org/wiki/Material
public class HumanResource extends Resource implements Property, Serializable {
    //a barcode system would be nice
    //it is however not public any it is therefore difficult to know what to put here
    //http://www.uc-council.org/ http://www.upcdatabase.com/
    /** DOCUMENT ME! */
    private Amount<Money> value; //the price for this quantity

/**
     * Creates a new HumanResource object.
     *
     * @param name        DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param amount      DOCUMENT ME!
     * @param producer    DOCUMENT ME!
     * @param value       DOCUMENT ME!
     */
    public HumanResource(String name, String description, Amount amount,
        Community producer, Amount<Money> value) {
        this(name, description, amount, producer, producer.getPosition(),
            new Date(), value);
    }

    //should always be strictly positive
    /**
     * Creates a new HumanResource object.
     *
     * @param name DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param amount DOCUMENT ME!
     * @param producer DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public HumanResource(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate);

        if (value != null) {
            this.value = value;
        } else {
            throw new IllegalArgumentException(
                "The Resource constructor can't have null arguments and name and description can't be empty.");
        }
    }

    //the cost is the negative price for your company getCost()=-getPrice()
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setValue(Amount<Money> value) {
        if (value != null) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("You can't set a null value.");
        }
    }

    //equality on all but identification (which should never be the same anyway)
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        MaterialResource resource;

        if ((o != null) && (o instanceof MaterialResource)) {
            resource = (MaterialResource) o;

            return super.equals(o) &&
            this.getValue().equals(resource.getValue());
        } else {
            return false;
        }
    }
}
