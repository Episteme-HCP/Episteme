/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
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
    public static Real valuateByCapRate(double netOperatingIncome, double capRate) {
        return Real.of(netOperatingIncome / capRate);
    }
}
