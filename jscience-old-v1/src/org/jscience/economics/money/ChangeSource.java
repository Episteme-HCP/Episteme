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

import org.jscience.util.UnavailableDataException;

//import java.util.Currency
//should replace our currency class from org.jscience.economics.money.Currency

/**
 * A class representing a currency conveter.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//each bank or dealer will have its own rates
//rates change very often
//Foreign Exchange Rate Currency Sources:
//http://www.bankofcanada.ca/en/financial_markets/csv/exchange_eng.csv
//http://www.oanda.com/convert/fxdaily
//http://www.bmo.com/economic/regular/fxrates.html
//http://pacific.commerce.ubc.ca/xr/today.html
//http://www.ny.frb.org/pihome/statistics/forex12.shtml
//http://www.customhouse.com/cgi-bin/World.cgi
//http://moneycentral.msn.com/investor/market/rates.asp
//http://www.xe.com/cus
//or see for complete java solutions:
//http://www.mindprod.com/zips/java/currcon.html
//http://www.neuro-tech.net/archives/000032.html (recommended)
//this is the total price including every commission, tax...
public interface ChangeSource {
    /**
     * public Money convert(Money source, Currency target) {
     * <p/>
     * return new Money(getConverted(source.getAmount(), source.getCurrency(), target), target);
     * <p/>
     * }
     */

    //returns the converted amount
    public float getConverted(float amount, Currency source, Currency target)
            throws UnavailableDataException;
}
