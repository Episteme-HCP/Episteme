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

package org.jscience.economics.money;

import org.jscience.biology.human.Human;

import org.jscience.economics.Organization;
import org.jscience.economics.Property;

import org.jscience.measure.Amount;

import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a share in a company, a value, a title of
 * ownership.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//the share is the idealized view on a product
//you normally should convert back and forth shares and quotes
//see:
//http://en.wikipedia.org/wiki/Asset
//http://en.wikipedia.org/wiki/Share
//http://en.wikipedia.org/wiki/Stock_option
//final modifier used to secure the access
//shares should be moved as subclass of Resource
//as Money itself should
public final class Share extends Object implements Property {
    /** DOCUMENT ME! */
    private Set owners;

    /** DOCUMENT ME! */
    private String symbol;

    /** DOCUMENT ME! */
    private Organization company;

    /** DOCUMENT ME! */
    private Amount<Money> currentValue;

    //this is one share
    /**
     * Creates a new Share object.
     *
     * @param owners DOCUMENT ME!
     * @param symbol DOCUMENT ME!
     * @param company DOCUMENT ME!
     * @param currentValue DOCUMENT ME!
     */
    public Share(Set owners, String symbol, Organization company,
        Amount<Money> currentValue) {
        Iterator iterator;
        boolean valid;

        if ((owners != null) && (owners.size() > 0) && (symbol != null) &&
                (symbol.length() > 0) && (company != null) &&
                (currentValue != null)) {
            iterator = owners.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.owners = owners;
                this.symbol = symbol;
                this.company = company;
                this.currentValue = currentValue;
            } else {
                throw new IllegalArgumentException(
                    "The owners Set must contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "Share doesn't accept null arguments and owners and symbol can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Set getOwners() {
        return owners;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String getSymbol() {
        return symbol;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Organization getOrganization() {
        return company;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Amount<Money> getValue() {
        return currentValue;
    }
}
