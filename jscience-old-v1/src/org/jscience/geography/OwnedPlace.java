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

package org.jscience.geography;

import org.jscience.biology.human.Human;

import org.jscience.economics.Property;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;

import org.jscience.measure.Amount;

import java.util.Iterator;
import java.util.Set;


/**
 * A class representing ahuman owned place.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class OwnedPlace extends Place implements Property {
    /** DOCUMENT ME! */
    private Set owners; //humans

    /** DOCUMENT ME! */
    private Amount<Money> value;

    //there also should be a field for the property contract but we are not sure it really exist (it depends on the country) and this would really be unconvenient for the developer
    /**
     * Creates a new OwnedPlace object.
     *
     * @param name DOCUMENT ME!
     * @param boundary DOCUMENT ME!
     * @param owners DOCUMENT ME!
     */
    public OwnedPlace(String name, Boundary boundary, Set owners) {
        super(name, boundary);

        Iterator iterator;
        boolean valid;

        if ((owners != null) && (owners.size() > 0)) {
            iterator = owners.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.owners = owners;
                this.value = Amount.valueOf(0, Currency.USD);
            } else {
                throw new IllegalArgumentException(
                    "The owners Set must contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "The OwnedPlace constructor can't have null or empty owners.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOwners() {
        return owners;
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addOwner(Human owner) {
        if (owner != null) {
            owners.add(owner);
        } else {
            throw new IllegalArgumentException("You can't add a null owner.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeOwner(Human owner) {
        if ((owners.size() > 1)) {
            owners.remove(owner);
        } else {
            throw new IllegalArgumentException("You can't remove last owner.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owners DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setOwners(Set owners) {
        Iterator iterator;
        boolean valid;

        if ((owners != null) && (owners.size() > 0)) {
            iterator = owners.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.owners = owners;
            } else {
                throw new IllegalArgumentException(
                    "The owners Set must contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null or empty owners set.");
        }
    }

    //this is the price if sold by owners
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
}
