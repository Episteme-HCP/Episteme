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

import org.jscience.economics.Bank;

import org.jscience.measure.Amount;

import java.util.Iterator;
import java.util.Vector;


//import java.util.Currency
//should replace our currency class from org.jscience.economics.money.Currency
/**
 * A class representing a wallet (a store of money). It has no value by
 * itself. This can be an electronic wallet. Yet this is only very basic here.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//final modifier used to secure the access
//but perhaps we should allow developers to provide better implementations by subclassing
//perhaps we should get rid of this class and bundle it into EconomicAgent
public final class Wallet extends Object {
    /** DOCUMENT ME! */
    private Vector contents;

/**
     * Creates a new Wallet object.
     */
    public Wallet() {
        contents = new Vector();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Vector getContents() {
        return contents;
    }

    //the change is made in the bank as argument
    /**
     * DOCUMENT ME!
     *
     * @param bank DOCUMENT ME!
     * @param resultCurrency DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Amount<Money> getValue(Bank bank, Currency resultCurrency) {
        Iterator iterator;
        Amount<Money> currentAmount;
        Amount<Money> result;

        if ((bank != null) && (resultCurrency != null)) {
            result = Amount.valueOf(0, resultCurrency);
            iterator = contents.iterator();

            while (iterator.hasNext()) {
                currentAmount = (Amount<Money>) iterator.next();
                result.plus(currentAmount);
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can't get the value for a null Bank or null Currency.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //the current good idea is to add coin after coin here
    public final void addValue(Amount<Money> amount) {
        if (amount != null) {
            contents.add(amount);
        } else {
            throw new IllegalArgumentException("You can't add a null Money.");
        }
    }

    //you can only remove stored values AS they were added
    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public final void removeValue(Amount<Money> amount) {
        contents.remove(amount);
    }

    //perhaps we should provide a method to compact (and transform) contents
    //and another method to remove the exact amount if available
}
