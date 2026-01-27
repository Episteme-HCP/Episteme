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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.UnavailableDataException;

/**
 * Interface for currency exchange rate providers.
 * <p>
 * Implementations retrieve current exchange rates from various sources
 * such as banks, financial exchanges, or online services.
 * Modernized to use Real for exchange rates to ensure high precision.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ChangeSource {

    /**
     * Converts an amount from one currency to another.
     *
     * @param amount the amount to convert
     * @param target the target currency
     * @return the converted amount in the target currency
     * @throws UnavailableDataException if exchange rate data cannot be retrieved
     */
    Money getConverted(Money amount, Currency target) throws UnavailableDataException;

    /**
     * Returns the current exchange rate between two currencies.
     * Use Real for internal and API representation.
     *
     * @param source the source currency
     * @param target the target currency
     * @return the exchange rate as Real (units of target per unit of source)
     * @throws UnavailableDataException if exchange rate data cannot be retrieved
     */
    default Real getExchangeRate(Currency source, Currency target) throws UnavailableDataException {
        Money oneUnit = Money.valueOf(Real.ONE, source);
        Money converted = getConverted(oneUnit, target);
        return converted.getValue();
    }

    /**
     * Returns the current exchange rate between two currencies as double.
     * Convenience method for display or export.
     */
    default double getExchangeRateValue(Currency source, Currency target) throws UnavailableDataException {
        return getExchangeRate(source, target).doubleValue();
    }
}
