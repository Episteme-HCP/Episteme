/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * Utility class for financial mathematics.
 * <p>
 * Includes Net Present Value (NPV), Future Value (FV), and Amortization
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FinancialMath {

    private FinancialMath() {
        // Utility class
    }

    /**
     * Calculates the Future Value (FV) of an investment.
     * FV = PV * (1 + r)^n
     *
     * @param presentValue The current value (PV).
     * @param rate         The interest rate per period (r) as a decimal (e.g. 0.05
     *                     for 5%).
     * @param periods      The number of periods (n).
     * @return The future value.
     */
    public static Money calculateFutureValue(Money presentValue, Real rate, int periods) {
        Real multiplier = Real.ONE.add(rate).pow(periods);
        return presentValue.multiply(multiplier);
    }

    /**
     * Calculates the Net Present Value (NPV) of a series of cash flows.
     * NPV = Sum(CF_t / (1 + r)^t)
     *
     * @param rate      The discount rate (r).
     * @param cashFlows The series of cash flows (Money objects).
     * @return The Net Present Value.
     */
    public static Money calculateNPV(Real rate, Money... cashFlows) {
        if (cashFlows.length == 0) {
            return Money.usd(Real.ZERO); // Default
        }

        Currency currency = cashFlows[0].getCurrency();
        Real npv = Real.ZERO;

        for (int t = 0; t < cashFlows.length; t++) {
            Real cf = cashFlows[t].getAmount();
            Real denominator = Real.ONE.add(rate).pow(t); // t=0 is initial investment usually
            npv = npv.add(cf.divide(denominator));
        }

        return new Money(npv, currency);
    }

    /**
     * Calculates monthly mortgage payment (Amortization).
     * M = P [ i(1 + i)^n ] / [ (1 + i)^n Ã¢â‚¬â€œ 1]
     *
     * @param principal  The loan amount (P).
     * @param annualRate The annual interest rate.
     * @param years      The loan term in years.
     * @return The monthly payment.
     */
    public static Money calculateMonthlyPayment(Money principal, Real annualRate, int years) {
        Real monthlyRate = annualRate.divide(Real.of(12));
        int months = years * 12;

        Real p = principal.getAmount();
        Real r = monthlyRate;
        Real onePlusR = r.add(Real.ONE).pow(months);

        Real numerator = p.multiply(r).multiply(onePlusR);
        Real denominator = onePlusR.subtract(Real.ONE);

        if (denominator.equals(Real.ZERO)) {
            // Should theoretically check almostEquals(Real.ZERO) if precision issues arise,
            // but Real should handle exact zero for 0 rate.
            // If rate is 0, payment is just P / months
            return principal.divide(Real.of(months));
        }

        Real monthlyPayment = numerator.divide(denominator);
        return new Money(monthlyPayment, principal.getCurrency());
    }
}


