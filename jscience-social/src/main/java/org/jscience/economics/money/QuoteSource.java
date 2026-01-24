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

/**
 * Interface for retrieving market quotes from data sources.
 * <p>
 * Implementations connect to various financial data providers to
 * fetch real-time or delayed stock, bond, and commodity quotes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface QuoteSource {

    /**
     * Fetches updated quote data from this source.
     * <p>
     * The quote object is updated in place with current pricing data.
     *
     * @param quote the quote to update with current data
     * @return true if the fetch was successful
     * @throws UnavailableDataException if data cannot be retrieved
     * @throws IllegalArgumentException if quote is null
     */
    boolean fetch(Quote quote) throws UnavailableDataException;

    /**
     * Fetches a quote for the given symbol.
     *
     * @param symbol the ticker symbol
     * @return the quote with current data
     * @throws UnavailableDataException if data cannot be retrieved
     */
    default Quote fetchBySymbol(String symbol) throws UnavailableDataException {
        Quote quote = new Quote(symbol, symbol, "UNKNOWN");
        if (fetch(quote)) {
            return quote;
        }
        throw new UnavailableDataException("Could not fetch quote for " + symbol);
    }

    /**
     * Returns the name of this quote source.
     * @return the source name
     */
    default String getSourceName() {
        return getClass().getSimpleName();
    }

    /**
     * Returns true if this source provides real-time data.
     * @return true if real-time, false if delayed
     */
    default boolean isRealTime() {
        return false;
    }
}
