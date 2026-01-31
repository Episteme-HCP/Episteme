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

package org.jscience.social.economics.money;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Utility class for financial calculations using high-precision Real numbers.
 * <p>
 * Provides methods for computing interest rates, loan payments,
 * and other financial formulas.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="http://www.math.com/tables/general/interest.htm">Interest Formulas</a>
 */
public final class MoneyUtils {

    private MoneyUtils() {
        // Utility class - no instantiation
    }

    // ===== Simple Interest =====

    /**
     * Calculates the future value with simple interest.
     *
     * @param principal       initial deposit
     * @param interestRate    annual interest rate (as decimal, e.g., 0.05 for 5%)
     * @param numberOfPeriods number of periods
     * @return the future value
     */
    public static Real simpleInterestFutureValue(Real principal, 
            Real interestRate, int numberOfPeriods) {
        // P * (1 + r * n)
        return principal.multiply(Real.ONE.add(interestRate.multiply(Real.of(numberOfPeriods))));
    }

    /**
     * Calculates the present value for simple interest.
     *
     * @param futureValue     the target future value
     * @param interestRate    annual interest rate
     * @param numberOfPeriods number of periods
     * @return the present value (initial deposit needed)
     */
    public static Real simpleInterestPresentValue(Real futureValue, 
            Real interestRate, int numberOfPeriods) {
        // FV / (1 + r * n)
        Real divisor = Real.ONE.add(interestRate.multiply(Real.of(numberOfPeriods)));
        return futureValue.divide(divisor);
    }

    /**
     * Calculates the interest rate from initial and final values.
     *
     * @param principal       initial deposit
     * @param futureValue     final value
     * @param numberOfPeriods number of periods
     * @return the annual interest rate
     */
    public static Real simpleInterestRate(Real principal, Real futureValue, 
            int numberOfPeriods) {
        // (FV / P - 1) / n
        return futureValue.divide(principal).subtract(Real.ONE).divide(Real.of(numberOfPeriods));
    }

    /**
     * Calculates the number of periods needed to reach a target.
     *
     * @param principal    initial deposit
     * @param futureValue  target value
     * @param interestRate annual interest rate
     * @return the number of periods
     */
    public static Real simpleInterestPeriods(Real principal, Real futureValue, 
            Real interestRate) {
        // (FV / P - 1) / r
        return futureValue.divide(principal).subtract(Real.ONE).divide(interestRate);
    }

    // ===== Compound Interest =====

    /**
     * Calculates the future value with compound interest.
     *
     * @param principal        initial deposit
     * @param interestRate     annual interest rate
     * @param compoundsPerYear number of compounding periods per year
     * @param numberOfYears    number of years
     * @return the future value
     */
    public static Real compoundInterestFutureValue(Real principal, 
            Real interestRate, int compoundsPerYear, int numberOfYears) {
        // P * (1 + r/n)^(nt)
        Real n = Real.of(compoundsPerYear);
        Real ratePerPeriod = interestRate.divide(n);
        int totalPeriods = compoundsPerYear * numberOfYears;
        
        Real factor = Real.ONE.add(ratePerPeriod).pow(totalPeriods);
        return principal.multiply(factor);
    }

    /**
     * Calculates future value with continuous compounding.
     *
     * @param principal     initial deposit
     * @param interestRate  annual interest rate
     * @param numberOfYears number of years
     * @return the future value
     */
    public static Real continuousInterestFutureValue(Real principal, 
            Real interestRate, int numberOfYears) {
        // P * e^(rt)
        Real exponent = interestRate.multiply(Real.of(numberOfYears));
        return principal.multiply(exponent.exp());
    }

    /**
     * Calculates the effective annual rate (EAR).
     *
     * @param nominalRate      the nominal annual rate
     * @param compoundsPerYear number of compounding periods
     * @return the effective annual rate
     */
    public static Real effectiveAnnualRate(Real nominalRate, int compoundsPerYear) {
        // (1 + r/n)^n - 1
        Real n = Real.of(compoundsPerYear);
        return Real.ONE.add(nominalRate.divide(n)).pow(compoundsPerYear).subtract(Real.ONE);
    }

    // ===== Loan Calculations =====

    /**
     * Calculates the maximum loan amount for given payment.
     *
     * @param paymentPerPeriod the periodic payment
     * @param interestRate     periodic interest rate
     * @param numberOfPeriods  total number of payments
     * @return the maximum loan amount
     */
    public static Real loanPrincipal(Real paymentPerPeriod, 
            Real interestRate, int numberOfPeriods) {
        // P = PMT * (1 - (1+r)^-n) / r
        if (interestRate.equals(Real.ZERO)) {
            return paymentPerPeriod.multiply(Real.of(numberOfPeriods));
        }
        Real onePlusR = Real.ONE.add(interestRate);
        Real factor = Real.ONE.subtract(onePlusR.pow(-numberOfPeriods));
        return paymentPerPeriod.multiply(factor).divide(interestRate);
    }

    /**
     * Calculates the periodic payment for a loan.
     *
     * @param principal       loan amount
     * @param interestRate    periodic interest rate
     * @param numberOfPeriods total number of payments
     * @return the periodic payment
     */
    public static Real loanPayment(Real principal, Real interestRate, 
            int numberOfPeriods) {
        // PMT = P * r / (1 - (1+r)^-n)
        if (interestRate.equals(Real.ZERO)) {
            return principal.divide(Real.of(numberOfPeriods));
        }
        Real onePlusR = Real.ONE.add(interestRate);
        Real denominator = Real.ONE.subtract(onePlusR.pow(-numberOfPeriods));
        return principal.multiply(interestRate).divide(denominator);
    }

    /**
     * Calculates the remaining balance on a loan.
     *
     * @param principal           original loan amount
     * @param annualRate          annual percentage rate
     * @param paymentsPerYear     payments per year
     * @param yearsPaid           years of payments made
     * @param paymentAmount       amount of each payment
     * @return the remaining balance
     */
    public static Real loanBalance(Real principal, Real annualRate, 
            int paymentsPerYear, int yearsPaid, Real paymentAmount) {
        Real n = Real.of(paymentsPerYear);
        Real periodRate = annualRate.divide(n);
        int periodsPaid = paymentsPerYear * yearsPaid;
        
        Real compoundFactor = Real.ONE.add(periodRate).pow(periodsPaid);
        Real paymentsFactor = compoundFactor.subtract(Real.ONE).divide(periodRate);
        
        return principal.multiply(compoundFactor).subtract(paymentAmount.multiply(paymentsFactor));
    }

    /**
     * Calculates total interest paid over the life of a loan.
     *
     * @param principal       loan amount
     * @param payment         periodic payment
     * @param numberOfPeriods total number of payments
     * @return the total interest paid
     */
    public static Real totalInterestPaid(Real principal, Real payment, 
            int numberOfPeriods) {
        return payment.multiply(Real.of(numberOfPeriods)).subtract(principal);
    }

    // ===== Money-based methods =====

    /**
     * Applies compound interest to a money amount.
     *
     * @param principal        the initial amount
     * @param interestRate     annual interest rate
     * @param compoundsPerYear compounding frequency
     * @param years            number of years
     * @return the future value
     */
    public static Money compoundInterest(Money principal, Real interestRate, 
            int compoundsPerYear, int years) {
        Real futureValue = compoundInterestFutureValue(
            principal.getValue(), interestRate, compoundsPerYear, years);
        return Money.valueOf(futureValue, principal.getCurrency());
    }

    /**
     * Calculates monthly mortgage payment.
     *
     * @param principal   loan amount
     * @param annualRate  annual interest rate
     * @param years       loan term in years
     * @return the monthly payment
     */
    public static Money monthlyMortgagePayment(Money principal, Real annualRate, int years) {
        Real monthlyRate = annualRate.divide(Real.of(12));
        int totalPayments = years * 12;
        Real payment = loanPayment(principal.getValue(), monthlyRate, totalPayments);
        return Money.valueOf(payment, principal.getCurrency());
    }
}

