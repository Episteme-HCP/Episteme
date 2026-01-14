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

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import java.util.Date;


//import java.util.Currency
//should replace our currency class from org.jscience.economics.money.Currency
/**
 * A class representing a metal or paper form of payment from one person to
 * another.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you have to provide yourself with actual values
//for example 1 euroCent, 2 euroCents, 5 euroCents, 10 euroCents, 20 euroCents, 50 euroCents, 1 euros, 2 euros

//perhaps we should extend Amount<Money> or something
public final class Coin implements Identified {
    /** DOCUMENT ME! */
    private Identification identification; //unique for each coin

    /** DOCUMENT ME! */
    private Date emission;

    /** DOCUMENT ME! */
    private Amount<Money> value;

    //value is the facial value
    /**
     * Creates a new Coin object.
     *
     * @param value DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param emission DOCUMENT ME!
     * @param currency DOCUMENT ME!
     */
    public Coin(double value, Identification identification, Date emission,
        Currency currency) {
        if ((identification != null) && (emission != null)) {
            this.identification = identification;
            this.emission = emission;
            this.value = Amount.valueOf(value, currency);
        } else {
            throw new IllegalArgumentException(
                "Check doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Date getEmission() {
        return emission;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Amount<Money> getAmount() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String toString() {
        return "Coin " + identification + " emitted on " + emission +
        " is worth " + getAmount() + " " + getAmount().getUnit().toString();
    }
}
