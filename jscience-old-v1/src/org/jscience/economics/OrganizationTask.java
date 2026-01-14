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

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;

import org.jscience.measure.Amount;

import java.util.Set;


/**
 * A class representing the transformation of some materials and some human
 * ressources into a finished something that can be sold. A product (whether
 * primary or secondary, that is, already transformed) is a material thing. A
 * service is a kind of immaterial product (like having a hair cut). Work is
 * also known as task. Each task can in turn be divided further on into
 * subtasks to further describe each process.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class OrganizationTask extends Task {
    //usually lower than sum of energy and human costs thanks to the fact that big groups work usually faster
    //you may also count in that cost the price of using the machines, renting the building, etc....
    /** DOCUMENT ME! */
    private double humanCost; //the number of hours of man work

    /** DOCUMENT ME! */
    private Amount<Money> adjustedCost; //the adjusted cost depending on the price of the energy and the human work

    /** DOCUMENT ME! */
    private int kind;

    //this is the work to produce one unit of the products (there is usually only a single resulting product)
    /**
     * Creates a new OrganizationTask object.
     *
     * @param name DOCUMENT ME!
     * @param resources DOCUMENT ME!
     * @param products DOCUMENT ME!
     */
    public OrganizationTask(String name, Set resources, Set products) {
        super(name, resources, products);
        this.humanCost = 0;
        this.adjustedCost = Amount.valueOf(0, Currency.USD);
        this.kind = EconomicsConstants.UNKNOWN;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getHumanCost() {
        return humanCost;
    }

    //should always be positive or zero
    /**
     * DOCUMENT ME!
     *
     * @param cost DOCUMENT ME!
     */
    public void setHumanCost(double cost) {
        humanCost = cost;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getAdjustedCost() {
        return adjustedCost;
    }

    //this is the only cost actually used in the system to produce work
    /**
     * DOCUMENT ME!
     *
     * @param cost DOCUMENT ME!
     */
    public void setAdjustedCost(Amount<Money> cost) {
        if (cost != null) {
            this.adjustedCost = cost;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null adjusted cost.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     */

    // see EconomicsConstants processes
    public void setKind(int kind) {
        this.kind = kind;
    }
}
