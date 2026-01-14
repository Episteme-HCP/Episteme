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

package org.jscience.economics.money.providers;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Quote;
import org.jscience.economics.money.QuoteSource;

import org.jscience.measure.Amount;

import org.jscience.util.UnavailableDataException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

import java.util.StringTokenizer;


//this code is based on Java Financial Library:
//http://www.neuro-tech.net/
//Luke Reeves <lreeves@member.fsf.org>
//and was originally released under the LGPL

// we assume the value is returned ALWAYS in USD
//todo check what happens for quotes given  with other curreny
/**
 * A QuoteSource for the Yahoo! Finance site in CSV format.
 */
public class YahooQuoteSource implements QuoteSource {
    /** DOCUMENT ME! */
    private static final boolean DEBUG = false;

    /** DOCUMENT ME! */
    private Quote quote;

    /** DOCUMENT ME! */
    private String symbol;

    /**
     * DOCUMENT ME!
     *
     * @param quote DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnavailableDataException DOCUMENT ME!
     */
    public boolean fetch(Quote quote) throws UnavailableDataException {
        this.quote = quote;
        symbol = quote.getSymbol();

        String content;

        String u = "http://finance.yahoo.com/d/quotes.csv?s=" + symbol +
            "&f=snl1d1t1c1ohgv&e=.c";

        try {
            URL url = new URL(u);

            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

            content = in.readLine();
            in.close();

            if (DEBUG) {
                System.out.println(content);
            }
        } catch (Exception e) {
            throw new UnavailableDataException("Couldn't retrieve quote - " +
                e);
        }

        StringTokenizer tk = new StringTokenizer(content, ",");
        tk.nextToken(); // symbol
        quote.setCompany(stripQuotes(tk.nextToken())); // name
        quote.setValue(Amount.valueOf(Float.parseFloat(tk.nextToken()),
                Currency.USD)); // value
        tk.nextToken(); // date
        tk.nextToken(); // time
        tk.nextToken(); // net

        try {
            quote.setOpenPrice(Amount.valueOf(Float.parseFloat(tk.nextToken()),
                    Currency.USD)); // open price
        } catch (NumberFormatException nfe) {
            quote.setOpenPrice(Amount.valueOf(0, Currency.USD));
        }

        tk.nextToken(); // Daily High
        tk.nextToken(); // Daily Low

        try {
            quote.setVolume(Long.parseLong(tk.nextToken()));
        } catch (NumberFormatException nfe) {
            quote.setVolume(0);
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param q DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static final String stripQuotes(String q) {
        String s;

        s = q.substring(1, q.length() - 1);

        return s;
    }
}
