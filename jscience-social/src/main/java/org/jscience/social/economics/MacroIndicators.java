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

package org.jscience.social.economics;


import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.social.economics.money.Money;

/**
 * Standard macroeconomic formulas for GDP, inflation, trade, and debt analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class MacroIndicators {

    private MacroIndicators() {}

    /** GDP (Expenditure Approach): C + I + G + (NX). */
    public static Money calculateGDP(Money consumption, Money investment, 
            Money governmentSpending, Money netExports) {
        return consumption.add(investment).add(governmentSpending).add(netExports);
    }

    /** Real GDP adjusted by deflator. */
    public static Money realGDP(Money nominalGDP, Real deflator) {
        return nominalGDP.divide(deflator).multiply(Real.of(100));
    }

    /** Annual inflation rate based on Consumer Price Index (CPI). */
    public static Real inflationRate(Real cpiCurrent, Real cpiPrevious) {
        if (cpiPrevious.isZero()) return Real.ZERO;
        return cpiCurrent.subtract(cpiPrevious).divide(cpiPrevious).multiply(Real.of(100));
    }

    /** Debt-to-GDP ratio as a percentage. */
    public static Real debtToGDP(Money totalDebt, Money gdp) {
        if (gdp.getAmount().isZero()) return Real.ZERO;
        return totalDebt.getAmount().divide(gdp.getAmount()).multiply(Real.of(100));
    }

    /** Taylor Rule for central bank interest rate settings. */
    public static Real taylorRule(Real neutralRate, Real currentInflation, Real targetInflation, Real outputGap) {
        Real inflationGap = currentInflation.subtract(targetInflation);
        return neutralRate.add(currentInflation)
            .add(inflationGap.multiply(Real.of(0.5)))
            .add(outputGap.multiply(Real.of(0.5)));
    }

    /** Gini coefficient for reaching income distribution analysis. */
    public static Real giniCoefficient(List<Real> incomes) {
        return org.jscience.social.economics.EconomicMetrics.giniCoefficientReal(incomes);
    }
}

