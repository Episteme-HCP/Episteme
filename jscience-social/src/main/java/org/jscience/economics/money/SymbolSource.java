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

import java.util.Set;

/**
 * Interface for searching financial instrument symbols.
 * <p>
 * Implementations query various data sources to find ticker symbols
 * matching a search expression.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface SymbolSource {

    /**
     * Searches for symbols matching the given expression.
     *
     * @param expression the search string (company name or partial symbol)
     * @return set of matching symbols
     * @throws UnavailableDataException if the search cannot be performed
     * @throws IllegalArgumentException if expression is null or empty
     */
    Set<String> search(String expression) throws UnavailableDataException;

    /**
     * Returns detailed information about a specific symbol.
     *
     * @param symbol the ticker symbol
     * @return information about the symbol, or null if not found
     * @throws UnavailableDataException if data cannot be retrieved
     */
    default SymbolInfo getSymbolInfo(String symbol) throws UnavailableDataException {
        Set<String> results = search(symbol);
        if (results.contains(symbol)) {
            return new SymbolInfo(symbol, symbol, "UNKNOWN");
        }
        return null;
    }

    /**
     * Returns the name of this symbol source.
     * @return the source name
     */
    default String getSourceName() {
        return getClass().getSimpleName();
    }

    /**
     * Basic symbol information.
     */
    record SymbolInfo(String symbol, String name, String exchange) {}
}
