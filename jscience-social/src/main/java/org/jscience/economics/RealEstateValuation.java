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

package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Utility class for real estate valuation using common financial methods
 * such as capitalization rate (Cap Rate) and discounted cash flow (DCF).
 * 
 * <p>This class provides static methods for property valuation calculations
 * commonly used in real estate investment analysis.</p>
 * 
 * @author <a href="mailto:jacob.dixon@jscience.org">Jacob Dixon</a>
 * @version 6.0, July 21, 2014
 */
public final class RealEstateValuation {

    /** Private constructor to prevent instantiation. */
    private RealEstateValuation() {}

    /**
     * Calculates property value using the Capitalization Rate formula.
     * 
     * <p>The formula used is: {@code Value = NOI / CapRate}</p>
     * 
     * @param netOperatingIncome the annual net operating income (NOI) of the property.
     * @param capRate the capitalization rate (as a decimal, e.g., 0.08 for 8%).
     * @return the estimated property value as a {@link Real} number.
     * @throws ArithmeticException if {@code capRate} is zero.
     */
    public static Real valuateByCapRate(Real netOperatingIncome, Real capRate) {
        return netOperatingIncome.divide(capRate);
    }
}
