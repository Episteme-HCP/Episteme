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

package org.jscience.social.economics;

import org.jscience.social.economics.money.Currency;
import org.jscience.social.economics.money.Money;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

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
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class OrganizationTask extends Task {
    //usually lower than sum of energy and human costs thanks to the fact that big groups work usually faster
    //you may also count in that cost the price of using the machines, renting the building, etc....
    /** The human work cost in hours. */
    @Attribute
    private Real humanCost; //the number of hours of man work

    /** The adjusted cost including energy and resource expenses. */
    @Attribute
    private Money adjustedCost; //the adjusted cost depending on the price of the energy and the human work

    /** The kind of task (see EconomicsConstants). */
    @Attribute
    private int kind;

    //this is the work to produce one unit of the products (there is usually only a single resulting product)
    /**
     * Creates a new OrganizationTask object.
     *
     * @param name the name of the task
     * @param resources the set of required resources
     * @param products the set of resulting products
     */
    public OrganizationTask(String name, Set<Resource> resources, Set<Resource> products) {
        super(name, resources, products);
        this.humanCost = Real.ZERO;
        this.adjustedCost = Money.valueOf(Real.ZERO, Currency.USD);
        this.kind = EconomicsConstants.UNKNOWN;
    }

    /**
     * Gets the human cost.
     *
     * @return the human cost
     */
    public Real getHumanCost() {
        return humanCost;
    }

    //should always be positive or zero
    /**
     * Sets the human cost.
     *
     * @param cost the human cost
     */
    public void setHumanCost(Real cost) {
        humanCost = cost;
    }

    /**
     * Gets the adjusted cost.
     *
     * @return the adjusted cost
     */
    public Money getAdjustedCost() {
        return adjustedCost;
    }

    //this is the only cost actually used in the system to produce work
    /**
     * Sets the adjusted cost.
     *
     * @param cost the adjusted cost
     */
    public void setAdjustedCost(Money cost) {
        if (cost != null) {
            this.adjustedCost = cost;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null adjusted cost.");
        }
    }

    /**
     * Gets the task kind.
     *
     * @return the kind
     */
    public int getKind() {
        return kind;
    }

    /**
     * Sets the task kind.
     *
     * @param kind the kind
     */
    public void setKind(int kind) {
        this.kind = kind;
    }
}

